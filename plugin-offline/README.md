# Mapbox offline plugin

The offline plugin automatically does the downloading and managment of map tiles for offline Android device use. _Especially_ if you're building a project for outdoors use, a user's device won't always have a strong enough internet connection to download and view map tiles. Once given the desired coordinates, zoom levels, and map style, this plugin handles the rest of the work that's required for fetching and storing map tiles for any region of the world that you want.

## Getting Started

[More documentation about the plugin can be found here](https://www.mapbox.com/android-docs/plugins/overview/offline)

To use the offline plugin you include it in your `build.gradle` file.

```
// In the root build.gradle file
repositories {
    mavenCentral()
}

...

// In the app build.gradle file
dependencies {
    implementation 'com.mapbox.mapboxsdk:mapbox-android-plugin-offline-v7:0.5.0'
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
	implementation 'com.mapbox.mapboxsdk:mapbox-android-plugin-offline-v7:0.6.0-SNAPSHOT'
}
```

## Offline plugin examples

- [In this repo's test app](https://github.com/mapbox/mapbox-plugins-android/tree/master/app/src/main/java/com/mapbox/mapboxsdk/plugins/testapp/activity/offline)

## Help and Usage

This repository includes an app that shows how to use each plugins in this repository. [Check out its code](https://github.com/mapbox/mapbox-plugins-android/tree/master/app/src/main/java/com/mapbox/mapboxsdk/plugins/testapp/activity) for ready-to-use snippets.

We'd love to [hear your feedback](https://github.com/mapbox/mapbox-plugins-android/issues) as we build more plugins and learn how you use them.

## Why Plugins

Splitting specific functionality into plugins makes our Map SDK lighter and nimble for you to use, and it also lets us iterate faster. We can release plugins more often than the SDK, which requires a slower pace due to its larger codebase.

The Mapbox Android team creates plugins but this plugins repository is an open-source project similar to the various Mapbox Android SDKs.
Plugins' lightweight nature makes them much easier for you and anyone else to contribute rather than trying to add the same feature to the more robust Map SDK. The Mapbox team can also more easily accept contributed plugins and keep the plugin list growing.

## Contributing

We welcome contributions to this plugin repository!

If you're interested in geojson and sharing your own plugin, please read [the contribution guide](https://github.com/mapbox/mapbox-plugins-android/blob/master/CONTRIBUTING.md) to learn how to get started.
