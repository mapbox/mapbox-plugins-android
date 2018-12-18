# Mapbox location layer plugin

![location-plugin](https://user-images.githubusercontent.com/4394910/28844322-1c52a672-76b9-11e7-904a-fcf6f51c1481.gif)

## Deprecation
The location layer plugin is no longer maintained and the whole functionality as well as the future development has been moved to the Mapbox Maps SDK for Android and usage doesn't require any additional dependencies.

Follow [`LocationComponent` documentation](https://www.mapbox.com/android-docs/maps/overview/location-component/) for more information.

## Getting Started

To use the location layer plugin, you include it in your `build.gradle` file.

```gradle
// In the root build.gradle file
repositories {
    mavenCentral()
    google()
}

...

// In the app build.gradle file
dependencies {
    implementation 'com.mapbox.mapboxsdk:mapbox-android-plugin-locationlayer:0.11.0'
}
```

The location layer plugin is published to Maven Central and nightly SNAPSHOTs are available on Sonatype:

```gradle
// In the root build.gradle file
repositories {
    mavenCentral()
    google()
    maven { url "http://oss.sonatype.org/content/repositories/snapshots/" }
}

...

// In the app build.gradle file
dependencies {
    implementation 'com.mapbox.mapboxsdk:mapbox-android-plugin-locationlayer:0.12.0-SNAPSHOT'
}
```

## Location layer examples

- In this repo's test app:
  - [beginner example](https://github.com/mapbox/mapbox-plugins-android/blob/master/app/src/main/java/com/mapbox/mapboxsdk/plugins/testapp/activity/location/LocationLayerMapChangeActivity.kt)
  - [advanced example](https://github.com/mapbox/mapbox-plugins-android/blob/master/app/src/main/java/com/mapbox/mapboxsdk/plugins/testapp/activity/location/kt)
  - [all examples](https://github.com/mapbox/mapbox-plugins-android/tree/master/app/src/main/java/com/mapbox/mapboxsdk/plugins/testapp/activity/location)
- [In the Mapbox Android Demo app](https://github.com/mapbox/mapbox-android-demo/blob/master/MapboxAndroidDemo/src/main/java/com/mapbox/mapboxandroiddemo/examples/plugins/LocationPluginActivity.java) – (Don't have the app? [Download it on Google Play](https://play.google.com/store/apps/details?id=com.mapbox.mapboxandroiddemo).)

## Help and Usage

This repository includes an app that shows how to use each plugins in this repository. [Check out its code](https://github.com/mapbox/mapbox-plugins-android/tree/master/app/src/main/java/com/mapbox/mapboxsdk/plugins/testapp/activity) for ready-to-use snippets.

We'd love to [hear your feedback](https://github.com/mapbox/mapbox-plugins-android/issues) as we build more plugins and learn how you use them.

## Why plugins

Splitting specific functionality into plugins makes our Map SDK lighter and nimble for you to use, and it also lets us iterate faster. We can release plugins more often than the SDK, which requires a slower pace due to its larger codebase.

The Mapbox Android team creates plugins but this plugins repository is an open-source project similar to the various Mapbox SDKs for Android.
Plugins' lightweight nature makes them much easier for you and anyone else to contribute rather than trying to add the same feature to the more robust Map SDK. The Mapbox team can also more easily accept contributed plugins and keep the plugin list growing.

## Contributing

We welcome contributions to this plugin repository!

If you're interested in building and sharing your own plugin, please read [the contribution guide](https://github.com/mapbox/mapbox-plugins-android/blob/master/CONTRIBUTING.md) to learn how to get started.
