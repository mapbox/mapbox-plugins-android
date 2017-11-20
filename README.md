[![Build Status](https://www.bitrise.io/app/a3a5f64a6d4a78c3.svg?token=RJY5I160EZSKZr1e0KgLrw&branch=master)](https://www.bitrise.io/app/a3a5f64a6d4a78c3)

# Mapbox Plugins for Android

Plugins are single-purpose libraries built on top of the [Mapbox Maps SDK for Android](https://www.mapbox.com/android-docs/) that you can include in your apps like any other Android dependency. You'll [find documentation](https://www.mapbox.com/android-docs/overview/) for each plugin on our website. A full list of the current plugins is available below.

## Available Plugins

* [**Traffic plugin:** Adds a real-time traffic layer to any Mapbox base map.](https://github.com/mapbox/mapbox-plugins-android/tree/master/plugin-traffic)

* [**Location layer:** Add a location marker on your map indicating the user's location.](https://github.com/mapbox/mapbox-plugins-android/tree/master/plugin-locationlayer)

* [**Building:** Add extruded buildings in your map style.](https://github.com/mapbox/mapbox-plugins-android/tree/master/plugin-building)

* [**GeoJSON:** Load GeoJSON data from a URL, an asset file, or path.](https://github.com/mapbox/mapbox-plugins-android/tree/master/plugin-geojson)

## Install a plugin

By using a plugin, you also include the Android Map SDK which means that you'll need to setup your project to use the Map SDK if you haven't already. Head over to the [Map SDK for Android Overview page](https://www.mapbox.com/android-docs/map-sdk/overview/) to learn more.

Note that depending on the plugin you add, there might be required permissions and additional setup steps. You'll find more information on whether or not more configuration steps are involved when looking at the specific plugin documentation.

1. Start Android Studio
2. Open up your application's `build.gradle`
3. Make sure that your project's `minSdkVersion` is at API 15 or higher
4. Under dependencies, add a new build rule for the latest plugin version you are trying to use.
5. Click the Sync Project with Gradle Files near the toolbar in Studio.

```gradle
repositories {
  mavenCentral()
}

dependencies {
  compile 'com.mapbox.mapboxsdk:PLUGIN_NAME:PLUGIN_VERSION_NUMBER'
}
```

## Help and Usage

This repository includes an app that shows how to use each plugins in this repository. [Check out its code](https://github.com/mapbox/mapbox-plugins-android/tree/master/app/src/main/java/com/mapbox/mapboxsdk/plugins/testapp) for ready-to-use snippets.

Plugins are easy to use. A plugin is simply a library module built on top of the Mapbox Maps SDK for Android. Currently, we are not requiring plugins to register themselves or to implement any specific interfaces so that they're simple to consume.

This might change in the future as we build more plugins and learn how you use them. We'd love to [hear your feedback](https://github.com/mapbox/mapbox-plugins-android/issues).

## Why Plugins

Splitting specific functionality into plugins makes our Maps SDK lighter and nimble for you to use, and it also lets us iterate faster. We can release plugins more often than the SDK, which requires a slower pace due to its larger codebase.

The Mapbox Android team creates plugins but this plugins repository is an open-source project similar to the various Mapbox SDKs for Android.
Plugins' lightweight nature makes them much easier for you and anyone else to contribute rather than trying to add the same feature to the more robust Maps SDK. The Mapbox team can also more easily accept contributed plugins and keep the plugin list growing.

## Contributing

We welcome contributions to this plugin repository!

If you're interested in building and sharing your own plugin, please read [the contribution guide](https://github.com/mapbox/mapbox-plugins-android/blob/master/CONTRIBUTING.md) to learn how to get started.
