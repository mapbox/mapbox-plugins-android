[![Build Status](https://www.bitrise.io/app/a3a5f64a6d4a78c3.svg?token=RJY5I160EZSKZr1e0KgLrw&branch=master)](https://www.bitrise.io/app/a3a5f64a6d4a78c3)

# Mapbox Plugins for Android

Plugins are single-purpose libraries built on top of the [Mapbox Android SDK](https://www.mapbox.com/android-docs/) that you can include in your apps like any other Android dependency. You'll [find documentation](https://www.mapbox.com/android-docs/plugins/overview/) for each plugin on our website. A full list of the current plugins is available below.

## Available Plugins

* [**Traffic plugin:** Adds a real-time traffic layer to any Mapbox basemap.](https://github.com/mapbox/mapbox-plugins-android/plugins/traffic/)

* [**Location layer:** Add a location marker on your map indicating the users location.](https://github.com/mapbox/mapbox-plugins-android/plugins/locationlayer/)

* [**Building:** Add extruded buildings in your map style.](https://github.com/mapbox/mapbox-plugins-android/plugins/building/)

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
