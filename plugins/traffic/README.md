[![Build Status](https://www.bitrise.io/app/a3a5f64a6d4a78c3.svg?token=RJY5I160EZSKZr1e0KgLrw&branch=master)](https://www.bitrise.io/app/a3a5f64a6d4a78c3)

# Mapbox traffic plugin

The traffic plugin adds a real-time traffic layer to any Mapbox basemap. 

![ezgif com-crop](https://cloud.githubusercontent.com/assets/4394910/24972279/bf88b170-1f6f-11e7-8638-6afe08369d9d.gif)

## Getting Started

To use the traffic plugin, you include it in your `build.gradle` file.

```
// In the root build.gradle file
repositories {
    mavenCentral()
}

...

// In the app build.gradle file
dependencies {
    compile 'com.mapbox.mapboxsdk:mapbox-android-plugin-traffic:0.3.0'
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
    compile 'com.mapbox.mapboxsdk:mapbox-android-plugin-traffic:0.4.0-SNAPSHOT'
}
```

## Traffic plugin examples

- [In this repo's test app](https://github.com/mapbox/mapbox-plugins-android/blob/master/plugins/app/src/main/java/com/mapbox/mapboxsdk/plugins/testapp/activity/TrafficActivity.java)

- [In the Mapbox Android demo app](https://github.com/mapbox/mapbox-android-demo/blob/a411fa95cd71c1b90a30895060b319310444aebb/MapboxAndroidDemo/src/main/java/com/mapbox/mapboxandroiddemo/examples/plugins/TrafficPluginActivity.java) – (Don't have the app? [Download it on Google Play](https://play.google.com/store/apps/details?id=com.mapbox.mapboxandroiddemo).)


## Help and Usage

This repository includes an app that shows how to use each plugins in this repository. [Check out its code](https://github.com/mapbox/mapbox-plugins-android/tree/master/plugins/app/src/main/java/com/mapbox/mapboxsdk/plugins/testapp) for ready-to-use snippets.

Plugins are easy to use. A plugin is simply a library module built on top of the Mapbox Android SDK. Currently, we are not requiring plugins to register themselves or to implement any specific interfaces so that they're simple to consume.

We'd love to [hear your feedback](https://github.com/mapbox/mapbox-plugins-android/issues) as we build more plugins and learn how you use them.

## Why Plugins

Splitting specific functionality into plugins makes our Map SDK lighter and nimble for you to use, and it also lets us iterate faster. We can release plugins more often than the SDK, which requires a slower pace due to its larger codebase.

The Mapbox Android team creates plugins but this plugins repository is an open-source project similar to the various Mapbox Android SDKs.
Plugins' lightweight nature makes them much easier for you and anyone else to contribute rather than trying to add the same feature to the more robust Map SDK. The Mapbox team can also more easily accept contributed plugins and keep the plugin list growing.

## Contributing

We welcome contributions to this plugin repository!

If you're interested in building and sharing your own plugin, please read [the contribution guide](https://github.com/mapbox/mapbox-plugins-android/blob/master/CONTRIBUTING.md) to learn how to get started.
