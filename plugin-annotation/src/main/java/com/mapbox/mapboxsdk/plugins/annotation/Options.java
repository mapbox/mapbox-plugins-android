package com.mapbox.mapboxsdk.plugins.annotation;

abstract class Options<T extends Annotation> {

  abstract T build(long id);
}
