package com.mapbox.mapboxsdk.plugins.annotation;

/**
 * Generic interface definition of a callback to be invoked when an annotation has been clicked.
 *
 * @param <T> generic parameter extending from Annotation
 */
public interface OnAnnotationClickListener<T extends Annotation> {

  /**
   * Called when an annotation has been clicked
   *
   * @param t the annotation clicked.
   */
  void onAnnotationClick(T t);

}
