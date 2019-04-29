package com.mapbox.mapboxsdk.plugins.annotation;

import android.annotation.SuppressLint;
import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.view.MotionEvent;
import android.view.View;

import com.mapbox.android.gestures.AndroidGesturesManager;
import com.mapbox.android.gestures.MoveDistancesObject;
import com.mapbox.android.gestures.MoveGestureDetector;
import com.mapbox.geojson.Geometry;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;

final class DraggableAnnotationController<T extends Annotation, D extends OnAnnotationDragListener<T>> {
  private final MapboxMap mapboxMap;
  private AnnotationManager<?, T, ?, D, ?, ?> annotationManager;

  private final int touchAreaShiftX;
  private final int touchAreaShiftY;
  private final int touchAreaMaxX;
  private final int touchAreaMaxY;

  @Nullable private T draggedAnnotation;

  @SuppressLint("ClickableViewAccessibility")
  DraggableAnnotationController(MapView mapView, MapboxMap mapboxMap) {
    this(mapView, mapboxMap, new AndroidGesturesManager(mapView.getContext(), false),
      mapView.getScrollX(), mapView.getScrollY(), mapView.getMeasuredWidth(), mapView.getMeasuredHeight());
  }

  @VisibleForTesting
  public DraggableAnnotationController(MapView mapView, MapboxMap mapboxMap,
                                       final AndroidGesturesManager androidGesturesManager,
                                       int touchAreaShiftX, int touchAreaShiftY,
                                       int touchAreaMaxX, int touchAreaMaxY) {
    this.mapboxMap = mapboxMap;
    this.touchAreaShiftX = touchAreaShiftX;
    this.touchAreaShiftY = touchAreaShiftY;
    this.touchAreaMaxX = touchAreaMaxX;
    this.touchAreaMaxY = touchAreaMaxY;

    androidGesturesManager.setMoveGestureListener(new AnnotationMoveGestureListener());

    mapView.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        androidGesturesManager.onTouchEvent(event);
        // if drag is started, don't pass motion events further
        return draggedAnnotation != null;
      }
    });
  }

  void injectAnnotationManager(AnnotationManager<?, T, ?, D, ?, ?> annotationManager) {
    this.annotationManager = annotationManager;
  }

  void onSourceUpdated() {
    stopDragging(draggedAnnotation);
  }

  boolean onMoveBegin(MoveGestureDetector detector) {
    if (detector.getPointersCount() == 1) {
      T annotation = annotationManager.queryMapForFeatures(detector.getFocalPoint());
      if (annotation != null) {
        return startDragging(annotation);
      }
    }
    return false;
  }

  boolean onMove(MoveGestureDetector detector) {
    if (draggedAnnotation != null && (detector.getPointersCount() > 1 || !draggedAnnotation.isDraggable())) {
      // Stopping the drag when we don't work with a simple, on-pointer move anymore
      stopDragging(draggedAnnotation);
      return true;
    }

    // Updating symbol's position
    if (draggedAnnotation != null) {
      MoveDistancesObject moveObject = detector.getMoveObject(0);

      float x = moveObject.getCurrentX() - touchAreaShiftX;
      float y = moveObject.getCurrentY() - touchAreaShiftY;

      PointF pointF = new PointF(x, y);

      if (pointF.x < 0 || pointF.y < 0 || pointF.x > touchAreaMaxX || pointF.y > touchAreaMaxY) {
        stopDragging(draggedAnnotation);
        return true;
      }

      Geometry shiftedGeometry = draggedAnnotation.getOffsetGeometry(
        mapboxMap.getProjection(), moveObject, touchAreaShiftX, touchAreaShiftY
      );

      if (shiftedGeometry != null) {
        draggedAnnotation.setGeometry(
          shiftedGeometry
        );
        annotationManager.internalUpdateSource();
        if (!annotationManager.getDragListeners().isEmpty()) {
          for (D d : annotationManager.getDragListeners()) {
            d.onAnnotationDrag(draggedAnnotation);
          }
        }
        return true;
      }
    }

    return false;
  }

  void onMoveEnd() {
    // Stopping the drag when move ends
    stopDragging(draggedAnnotation);
  }

  boolean startDragging(@NonNull T annotation) {
    if (annotation.isDraggable()) {
      if (!annotationManager.getDragListeners().isEmpty()) {
        for (D d : annotationManager.getDragListeners()) {
          d.onAnnotationDragStarted(annotation);
        }
      }
      draggedAnnotation = annotation;
      return true;
    }
    return false;
  }

  void stopDragging(@Nullable T annotation) {
    if (annotation != null) {
      if (!annotationManager.getDragListeners().isEmpty()) {
        for (D d : annotationManager.getDragListeners()) {
          d.onAnnotationDragFinished(annotation);
        }
      }
    }
    draggedAnnotation = null;
  }

  private class AnnotationMoveGestureListener implements MoveGestureDetector.OnMoveGestureListener {

    @Override
    public boolean onMoveBegin(MoveGestureDetector detector) {
      return DraggableAnnotationController.this.onMoveBegin(detector);
    }

    @Override
    public boolean onMove(MoveGestureDetector detector, float distanceX, float distanceY) {
      return DraggableAnnotationController.this.onMove(detector);
    }

    @Override
    public void onMoveEnd(MoveGestureDetector detector, float velocityX, float velocityY) {
      DraggableAnnotationController.this.onMoveEnd();
    }
  }
}
