# Mapbox marker cluster plugin

The marker cluster plugin automatically handles the display of a large number of markers so that your map doesn't have many markers overlapping one another. The plugin "clusters" markers on a Mapbox map for a cleaner and lighter display of data. The number within the cluster circle represents the number of markers that are in that area at a higher zoom level.

![marker-cluster-plugin](https://user-images.githubusercontent.com/4394910/32691924-213993c0-c6c4-11e7-8f89-819c639f5ed4.gif)

## Getting Started

To use the marker cluster plugin you include it in your `build.gradle` file.

```
// In the root build.gradle file
repositories {
    mavenCentral()
}

...

// In the app build.gradle file
dependencies {
    compile 'com.mapbox.mapboxsdk:mapbox-android-plugin-cluster-utils:0.1.0'
}
```

The marker cluster plugin is published to Maven Central and nightly SNAPSHOTs are available on Sonatype:

```
// In the root build.gradle file
repositories {
    mavenCentral()
    maven { url "http://oss.sonatype.org/content/repositories/snapshots/" }
}

...

// In the app build.gradle file
dependencies {
	compile 'com.mapbox.mapboxsdk:mapbox-android-plugin-cluster-utils:0.2.0-SNAPSHOT'
}
```

## Marker cluster plugin examples

- [In this repo's test app](
https://github.com/mapbox/mapbox-plugins-android/blob/master/plugins/app/src/main/java/com/mapbox/mapboxsdk/plugins/testapp/activity/MarkerClusterActivity.java)

- [In the Mapbox Android demo app](https://github.com/mapbox/mapbox-android-demo/blob/master/MapboxAndroidDemo/src/main/java/com/mapbox/mapboxandroiddemo/examples/plugins/MarkerClustersPluginActivity.java) – (Don't have the app? [Download it on Google Play](https://play.google.com/store/apps/details?id=com.mapbox.mapboxandroiddemo).)

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
