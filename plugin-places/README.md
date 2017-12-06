# Mapbox places plugin

The places plugin is the easiest and most powerful way to take advantage of [Mapbox's location search ("geocoding") capabilities](https://www.mapbox.com/geocoding/). The plugin automatically makes geocoding requests, has built-in saved locations, includes location picker functionality, and adds beautiful UI into your Android project.

![ezgif com-crop](https://user-images.githubusercontent.com/5652865/32994007-045b6230-cd2f-11e7-8902-d7ee6da3ab47.gif)

## Getting Started

To use the places plugin, you include it in your `build.gradle` file.

```
// In the root build.gradle file
repositories {
    mavenCentral()
}

...

// In the app build.gradle file
dependencies {
    compile 'com.mapbox.mapboxsdk:mapbox-android-plugin-places:0.1.0'
}
```

The places plugin is published to Maven Central and nightly SNAPSHOTs are available on Sonatype:

```
// In the root build.gradle file
repositories {
    mavenCentral()
    maven { url "http://oss.sonatype.org/content/repositories/snapshots/" }
}

...

// In the app build.gradle file
dependencies {
    compile 'com.mapbox.mapboxsdk:mapbox-android-plugin-places:0.2.0-SNAPSHOT'
}
```

## Places plugin examples

- [In this repo's test app](https://github.com/mapbox/mapbox-plugins-android/blob/master/app/src/main/java/com/mapbox/mapboxsdk/plugins/testapp/activity/AutocompleteLauncherActivity.java)

## Help and Usage

This repository includes an app that shows how to use each plugins in this repository. [Check out its code](https://github.com/mapbox/mapbox-plugins-android/tree/master/plugins/app/src/main/java/com/mapbox/mapboxsdk/plugins/testapp) for ready-to-use snippets.

Plugins are easy to use. A plugin is simply a library module built on top of the Mapbox Maps SDK for Android. Currently, we are not requiring plugins to register themselves or to implement any specific interfaces so that they're simple to consume.

We'd love to [hear your feedback](https://github.com/mapbox/mapbox-plugins-android/issues) as we build more plugins and learn how you use them.

## Why Plugins

Splitting specific functionality into plugins makes our Maps SDK lighter and nimble for you to use, and it also lets us iterate faster. We can release plugins more often than the SDK, which requires a slower pace due to its larger codebase.

The Mapbox Android team creates plugins but this plugins repository is an open-source project similar to the various Mapbox SDKs for Android.
Plugins' lightweight nature makes them much easier for you and anyone else to contribute rather than trying to add the same feature to the more robust Maps SDK. The Mapbox team can also more easily accept contributed plugins and keep the plugin list growing.

## Contributing

We welcome contributions to this plugin repository!

If you're interested in building and sharing your own plugin, please read [the contribution guide](https://github.com/mapbox/mapbox-plugins-android/blob/master/CONTRIBUTING.md) to learn how to get started.
