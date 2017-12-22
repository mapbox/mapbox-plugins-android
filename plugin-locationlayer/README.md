# Mapbox location layer plugin

![location-plugin](https://user-images.githubusercontent.com/4394910/28844322-1c52a672-76b9-11e7-904a-fcf6f51c1481.gif)

## Getting Started

To use the location layer plugin, you include it in your `build.gradle` file.

```gradle
// In the root build.gradle file
repositories {
    mavenCentral()
    maven { url 'https://maven.google.com' }
}

...

// In the app build.gradle file
dependencies {
    compile 'com.mapbox.mapboxsdk:mapbox-android-plugin-locationlayer:0.3.0'
}
```

The location layer plugin is published to Maven Central and nightly SNAPSHOTs are available on Sonatype:

```gradle
// In the root build.gradle file
repositories {
    mavenCentral()
    maven { url 'https://maven.google.com' }
    maven { url "http://oss.sonatype.org/content/repositories/snapshots/" }
}

...

// In the app build.gradle file
dependencies {
    compile 'com.mapbox.mapboxsdk:mapbox-android-plugin-locationlayer:0.4.0-SNAPSHOT'
}
```

## Location layer examples

- [In this repo's test app](https://github.com/mapbox/mapbox-plugins-android/blob/master/app/src/main/java/com/mapbox/mapboxsdk/plugins/testapp/activity/location/LocationLayerModesActivity.java)

- [In the Mapbox Android demo app](https://github.com/mapbox/mapbox-android-demo/blob/a411fa95cd71c1b90a30895060b319310444aebb/MapboxAndroidDemo/src/main/java/com/mapbox/mapboxandroiddemo/examples/plugins/LocationPluginActivity.java) – (Don't have the app? [Download it on Google Play](https://play.google.com/store/apps/details?id=com.mapbox.mapboxandroiddemo).)

## Help and Usage

This repository includes an app that shows how to use each plugins in this repository. [Check out its code](https://github.com/mapbox/mapbox-plugins-android/tree/master/plugins/app/src/main/java/com/mapbox/mapboxsdk/plugins/testapp) for ready-to-use snippets.

We'd love to [hear your feedback](https://github.com/mapbox/mapbox-plugins-android/issues) as we build more plugins and learn how you use them.

## Why plugins

Splitting specific functionality into plugins makes our Map SDK lighter and nimble for you to use, and it also lets us iterate faster. We can release plugins more often than the SDK, which requires a slower pace due to its larger codebase.

The Mapbox Android team creates plugins but this plugins repository is an open-source project similar to the various Mapbox SDKs for Android.
Plugins' lightweight nature makes them much easier for you and anyone else to contribute rather than trying to add the same feature to the more robust Map SDK. The Mapbox team can also more easily accept contributed plugins and keep the plugin list growing.

## Contributing

We welcome contributions to this plugin repository!

If you're interested in building and sharing your own plugin, please read [the contribution guide](https://github.com/mapbox/mapbox-plugins-android/blob/master/CONTRIBUTING.md) to learn how to get started.
