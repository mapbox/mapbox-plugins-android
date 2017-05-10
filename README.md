[![Build Status](https://www.bitrise.io/app/a3a5f64a6d4a78c3.svg?token=RJY5I160EZSKZr1e0KgLrw&branch=master)](https://www.bitrise.io/app/a3a5f64a6d4a78c3)

# Mapbox Plugins for Android

Plugins are single-purpose libraries built on top of the [Mapbox Android SDK](https://www.mapbox.com/android-sdk/) that you can include in your apps like any other Android dependency.

## Getting Started

To use a plugin you include it in your `build.gradle` file. For example, to add the traffic plugin to your app include the following:

```
// In the root build.gradle file
repositories {
    mavenCentral()
}

...

// In the app build.gradle file
dependencies {
    compile 'com.mapbox.mapboxsdk:mapbox-android-plugin-traffic:0.1.0'
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
    compile 'com.mapbox.mapboxsdk:mapbox-android-plugin-traffic:0.1.0-SNAPSHOT'
}
```

## Available Plugins

These plugins are currently available:

* **Traffic plugin:** Adds a real-time traffic layer to any Mapbox basemap.

![ezgif com-crop](https://cloud.githubusercontent.com/assets/4394910/24972279/bf88b170-1f6f-11e7-8638-6afe08369d9d.gif)

## Help and Usage

This repository includes an app that shows how to use each plugins in this repository. [Check out its code](https://github.com/mapbox/mapbox-plugins-android/tree/master/plugins/app/src/main/java/com/mapbox/mapboxsdk/plugins/testapp) for ready-to-use snippets.

Plugins are easy to use. A plugin is simply a library module built on top of the Mapbox Android SDK. Currently, we are not requiring plugins to register themselves or to implement any specific interfaces so that they're simple to consume.

This might change in the future as we build more plugins and learn how you use them. We'd love to [hear your feedback](https://github.com/mapbox/mapbox-plugins-android/issues).

## Why Plugins

Splitting specific functionality into plugins makes our Map SDK lighter and nimble for you to use, and it also lets us iterate faster. We can release plugins more often than the SDK, which requires a slower pace due to its larger codebase.

Also, because it's easier to build a plugin than a SDK feature, it's easier to [contribute plugins](https://github.com/mapbox/mapbox-plugins-android#contributing).

## Contributing

We welcome contributions to this plugin repository. If you're interested in building and sharing your own, please follow these steps:

- [Open a ticket](https://github.com/mapbox/mapbox-plugins-android/issues/new) to kick off a conversation, feel free to tag the `@mapbox/android` team. It's a good idea to explain your plans before you push any code to make sure noone else is working on something similar and to discuss the best approaches to tackle your particular idea.

- Create a new branch that will contain the code for your plugin.

- Create a new Android library module inside the `plugins` project with Android Studio. Each plugin is a separate library that depends on the Mapbox Android SDK, and potentially on other third-party libraries. Besides the code for the plugin, make sure you include:

  - Tests.
  - Javadoc that documents the plugin usage and purpose in detail.

- Create a new activity inside the demo app that showcases how to use your plugin. As important as having a working plugin is to show how to use it so that anyone can include it in their projects easily.

- Finally, once you're ready to share your code, list your plugin in this file and then open a PR for the `@mapbox/android` team to review.
