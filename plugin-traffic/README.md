# Mapbox traffic plugin

The traffic plugin adds a real-time traffic layer to any Mapbox basemap. 

![ezgif com-crop](https://cloud.githubusercontent.com/assets/4394910/24972279/bf88b170-1f6f-11e7-8638-6afe08369d9d.gif)

## Getting Started

[More documentation about the plugin can be found here](https://www.mapbox.com/android-docs/plugins/overview/traffic/)

To use the traffic plugin, you include it in your `build.gradle` file.

```
// In the root build.gradle file
repositories {
    mavenCentral()
}

...

// In the app build.gradle file
dependencies {
    implementation 'com.mapbox.mapboxsdk:mapbox-android-plugin-traffic:0.6.0'
}
```

The traffic plugin is published to Maven Central and nightly SNAPSHOTs are available on Sonatype:

```
// In the root build.gradle file
repositories {
    mavenCentral()
    maven { url "http://oss.sonatype.org/content/repositories/snapshots/" }
}

...

// In the app build.gradle file
dependencies {
    implementation 'com.mapbox.mapboxsdk:mapbox-android-plugin-traffic:0.7.0-SNAPSHOT'
}
```

## Traffic plugin examples

- [In this repo's test app](https://github.com/mapbox/mapbox-plugins-android/blob/master/app/src/main/java/com/mapbox/mapboxsdk/plugins/testapp/activity/traffic/TrafficActivity.kt)
- [In the Mapbox Android demo app](https://github.com/mapbox/mapbox-android-demo/blob/a411fa95cd71c1b90a30895060b319310444aebb/MapboxAndroidDemo/src/main/java/com/mapbox/mapboxandroiddemo/examples/plugins/TrafficPluginActivity.java) – (Don't have the app? [Download it on Google Play](https://play.google.com/store/apps/details?id=com.mapbox.mapboxandroiddemo).)

## Help and Usage

This repository includes an app that shows how to use each plugins in this repository. [Check out its code](https://github.com/mapbox/mapbox-plugins-android/tree/master/app/src/main/java/com/mapbox/mapboxsdk/plugins/testapp/activity) for ready-to-use snippets.

Plugins are easy to use. A plugin is simply a library module built on top of the Mapbox Maps SDK for Android. Currently, we are not requiring plugins to register themselves or to implement any specific interfaces so that they're simple to consume.

We'd love to [hear your feedback](https://github.com/mapbox/mapbox-plugins-android/issues) as we build more plugins and learn how you use them.

## Why Plugins

Splitting specific functionality into plugins makes our Maps SDK lighter and nimble for you to use, and it also lets us iterate faster. We can release plugins more often than the SDK, which requires a slower pace due to its larger codebase.

The Mapbox Android team creates plugins but this plugins repository is an open-source project similar to the various Mapbox SDKs for Android.
Plugins' lightweight nature makes them much easier for you and anyone else to contribute rather than trying to add the same feature to the more robust Maps SDK. The Mapbox team can also more easily accept contributed plugins and keep the plugin list growing.

## Contributing

We welcome contributions to this plugin repository!

If you're interested in building and sharing your own plugin, please read [the contribution guide](https://github.com/mapbox/mapbox-plugins-android/blob/master/CONTRIBUTING.md) to learn how to get started.
