[![Build Status](https://www.bitrise.io/app/a3a5f64a6d4a78c3.svg?token=RJY5I160EZSKZr1e0KgLrw&branch=master)](https://www.bitrise.io/app/a3a5f64a6d4a78c3)

# Mapbox GeoJSON plugin

The GeoJSON plugin makes it simple to load GeoJSON data from a URL, an asset file, or path. It also has a `OnLoadingGeoJsonListener` for handling GeoJSON preload, loaded GeoJSON, and failed GeoJSON load events.

![geojson-plugin](https://user-images.githubusercontent.com/4394910/29901400-a1f37194-8dac-11e7-8362-a8ec562513b4.gif)

## Getting Started

To use the GeoJSON plugin you include it in your `build.gradle` file.

```
// In the root build.gradle file
repositories {
    mavenCentral()
}

...

// In the app build.gradle file
dependencies {
    compile 'com.mapbox.mapboxsdk:mapbox-android-plugin-geojson:0.1.0'
}
```

The GeoJSON plugin is published to Maven Central and nightly SNAPSHOTs are available on Sonatype:

```
// In the root build.gradle file
repositories {
    mavenCentral()
    maven { url "http://oss.sonatype.org/content/repositories/snapshots/" }
}

...

// In the app build.gradle file
dependencies {
    compile 'com.mapbox.mapboxsdk:mapbox-android-plugin-geojson:0.2.0-SNAPSHOT'
}
```

## GeoJSON plugin examples

- [In this repo's test app](
https://github.com/mapbox/mapbox-plugins-android/blob/master/plugins/app/src/main/java/com/mapbox/mapboxsdk/plugins/testapp/activity/GeoJsonActivity.java)

## Help and Usage

This repository includes an app that shows how to use each plugins in this repository. [Check out its code](https://github.com/mapbox/mapbox-plugins-android/tree/master/plugins/app/src/main/java/com/mapbox/mapboxsdk/plugins/testapp) for ready-to-use snippets.

We'd love to [hear your feedback](https://github.com/mapbox/mapbox-plugins-android/issues) as we build more plugins and learn how you use them.

## Why Plugins

Splitting specific functionality into plugins makes our Map SDK lighter and nimble for you to use, and it also lets us iterate faster. We can release plugins more often than the SDK, which requires a slower pace due to its larger codebase.

The Mapbox Android team creates plugins but this plugins repository is an open-source project similar to the various Mapbox Android SDKs.
Plugins' lightweight nature makes them much easier for you and anyone else to contribute rather than trying to add the same feature to the more robust Map SDK. The Mapbox team can also more easily accept contributed plugins and keep the plugin list growing.


## Contributing

We welcome contributions to this plugin repository!

If you're interested in geojson and sharing your own plugin, please read [the contribution guide](https://github.com/mapbox/mapbox-plugins-android/blob/master/CONTRIBUTING.md) to learn how to get started.
