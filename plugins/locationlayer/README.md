[![Build Status](https://www.bitrise.io/app/a3a5f64a6d4a78c3.svg?token=RJY5I160EZSKZr1e0KgLrw&branch=master)](https://www.bitrise.io/app/a3a5f64a6d4a78c3)

# Mapbox location layer plugin

![location-plugin](https://user-images.githubusercontent.com/4394910/28844322-1c52a672-76b9-11e7-904a-fcf6f51c1481.gif)

## Getting Started

To use the location layer plugin, you include it in your `build.gradle` file.

```
// In the root build.gradle file
repositories {
    mavenCentral()
}

...

// In the app build.gradle file
dependencies {
    compile 'com.mapbox.mapboxsdk:mapbox-android-plugin-locationlayer:0.1.0'
}
```

All plugins are published to Maven Central, and nightly SNAPSHOTs are available on Sonatype:

```
// In the root build.gradle file
repositories {
    mavenCentral()
    maven { url "http://oss.sonatype.org/content/repositories/snapshots/" }
}

...

// In the app build.gradle file
dependencies {
    compile 'com.mapbox.mapboxsdk:mapbox-android-plugin-locationlayer:0.1.0-SNAPSHOT'
}
```

## Examples

- [This repo's test app]()

- [The Mapbox Android demo app](https://github.com/mapbox/mapbox-android-demo/blob/a411fa95cd71c1b90a30895060b319310444aebb/MapboxAndroidDemo/src/main/java/com/mapbox/mapboxandroiddemo/examples/plugins/TrafficPluginActivity.java) – [Download from Google Play](https://play.google.com/store/apps/details?id=com.mapbox.mapboxandroiddemo)


## Help and Usage

This repository includes an app that shows how to use each plugins in this repository. [Check out its code](https://github.com/mapbox/mapbox-plugins-android/tree/master/plugins/app/src/main/java/com/mapbox/mapboxsdk/plugins/testapp) for ready-to-use snippets.

Plugins are easy to use. A plugin is simply a library module built on top of the Mapbox Android SDK. Currently, we are not requiring plugins to register themselves or to implement any specific interfaces so that they're simple to consume.

This might change in the future as we build more plugins and learn how you use them. We'd love to [hear your feedback](https://github.com/mapbox/mapbox-plugins-android/issues).

## Why Plugins

Splitting specific functionality into plugins makes our Map SDK lighter and nimble for you to use, and it also lets us iterate faster. We can release plugins more often than the SDK, which requires a slower pace due to its larger codebase.

Also, because it's easier to build a plugin than a SDK feature, it's easier to [contribute plugins](https://github.com/mapbox/mapbox-plugins-android#contributing).

## Contributing

We welcome contributions to this plugin repository!

If you're interested in building and sharing your own plugin, please read [the contribution guide](https://github.com/mapbox/mapbox-plugins-android/blob/master/CONTRIBUTING.md) to learn how to get started.
