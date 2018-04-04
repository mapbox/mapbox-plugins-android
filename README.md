<h1 align="center">
  <br>
  <a href="https://www.mapbox.com/android-docs/mapbox-services/overview/"><img src="https://github.com/mapbox/mapbox-plugins-android/blob/master/.github/mbx-plugins-logo.png" alt="Mapbox Plugins" width="500"></a>
</h1>

<h4 align="center">Plugins are single-purpose libraries built on top of the <a href="https://www.mapbox.com/android-docs/">Mapbox Maps SDK for Android</a> that you can include in your apps like any other Android dependency</h4>

<p align="center">
  <a href="https://circleci.com/gh/mapbox/mapbox-plugins-android">
    <img src="https://circleci.com/gh/mapbox/mapbox-plugins-android.svg?style=shield&circle-token=:circle-token">
  </a>
  <!--<a href="https://sonarcloud.io/dashboard?id=mapbox-java-services"><img src="https://sonarcloud.io/api/badges/gate?key=mapbox-java-services"/></a>-->
</p>
<br>

## Documentation

You'll [find documentation](https://www.mapbox.com/android-docs/overview/) for each plugin on our website. A full list of the current plugins is available below.
-->
# Mapbox Plugins for Android

Plugins are single-purpose libraries built on top of the [Mapbox Maps SDK for Android](https://www.mapbox.com/android-docs/) that you can include in your apps like any other Android dependency. You'll [find documentation](https://www.mapbox.com/android-docs/overview/) for each plugin on our website. A full list of the current plugins is available below.

## Available Plugins
 
* [**Traffic:** Adds a real-time traffic layer to any Mapbox base map.](https://github.com/mapbox/mapbox-plugins-android/tree/master/plugin-traffic)

* [**Location layer:** Add a location marker on your map indicating the user's location.](https://github.com/mapbox/mapbox-plugins-android/tree/master/plugin-locationlayer)

* [**Building:** Add extruded "3D" buildings in your map style.](https://github.com/mapbox/mapbox-plugins-android/tree/master/plugin-building)

* [**GeoJSON:** Load GeoJSON data from a URL, an asset file, or path.](https://github.com/mapbox/mapbox-plugins-android/tree/master/plugin-geojson)

* [**Marker cluster:** Add marker/markerview clusters to display a large number of points on a map.](https://github.com/mapbox/mapbox-plugins-android/tree/master/plugin-cluster)

* [**Offline:** Download maps tiles and manage downloaded regions for situations when a user's device doesn't have an internet connection.](https://github.com/mapbox/mapbox-plugins-android/tree/master/plugin-offline)

* [**Places:** Add location search to your app with beautiful UI.](https://github.com/mapbox/mapbox-plugins-android/tree/master/plugin-places)

* [**Localization:** Have your map's text automatically match the device's default language setting.](https://github.com/mapbox/mapbox-plugins-android/tree/master/plugin-localization)

## Installing a plugin

By using a plugin, you also have to include the Mapbox Maps SDK for Android which means that you'll need to setup your project to use the Maps SDK if you haven't already. Head over to the [overview page for the Maps SDK](https://www.mapbox.com/android-docs/map-sdk/overview/) to learn more.

Note that depending on the plugin you add, there might be required permissions and additional setup steps. You'll find more information on whether or not more configuration steps are involved when looking at the specific plugin documentation.

1. Start Android Studio
2. Open up your application's `build.gradle`
3. Make sure that your project's `minSdkVersion` is at API 14 or higher
4. Under dependencies, add a new build rule for the latest plugin version you are trying to use.
```gradle
repositories {
  mavenCentral()
}

dependencies {
  implementation 'com.mapbox.mapboxsdk:PLUGIN_NAME:PLUGIN_VERSION_NUMBER'
}
```
5. Click the Sync Project with Gradle Files near the toolbar in Studio.

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
