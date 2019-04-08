# Mapbox annotation plugin

![annotations-plugin](https://user-images.githubusercontent.com/2151639/44861322-945d5e00-ac78-11e8-8ccb-883a4a743bcf.gif)

## Getting Started

[More documentation about the plugin can be found here](https://www.mapbox.com/android-docs/plugins/overview/annotation/)

To use the annotation plugin you include it in your `build.gradle` file.

In the root `build.gradle` file:

```groovy
repositories {
    mavenCentral()
}

```

In the app-level `build.gradle` file:

```groovy
android {

	// The Annotation Plugin requires Java 8 usage
	compileOptions {
	    sourceCompatibility JavaVersion.VERSION_1_8
	    targetCompatibility JavaVersion.VERSION_1_8
	}

}

dependencies {
    implementation 'com.mapbox.mapboxsdk:mapbox-android-plugin-annotation-v7:0.6.0'
}
```

The annotation plugin is published to Maven Central and nightly SNAPSHOTs are available on Sonatype:

```
// In the root build.gradle file
repositories {
    mavenCentral()
    maven { url "http://oss.sonatype.org/content/repositories/snapshots/" }
}

...

// In the app build.gradle file
dependencies {
    implementation 'com.mapbox.mapboxsdk:mapbox-android-plugin-annotation-v7:0.7.0-SNAPSHOT'
}
```

## Annotation plugin examples

- [In this repo's test app](https://github.com/mapbox/mapbox-plugins-android/tree/master/app/src/main/java/com/mapbox/mapboxsdk/plugins/testapp/activity/annotation)

## Help and Usage

This repository includes an app that shows how to use each plugin in this repository. [Check out its code](https://github.com/mapbox/mapbox-plugins-android/tree/master/app/src/main/java/com/mapbox/mapboxsdk/plugins/testapp/activity) for ready-to-use snippets.

We'd love to [hear your feedback](https://github.com/mapbox/mapbox-plugins-android/issues) as we build more plugins and learn how you use them.

## Why Plugins

Splitting specific functionality into plugins makes our Map SDK lighter and nimble for you to use, and it also lets us iterate faster. We can release plugins more often than the SDK, which requires a slower pace due to its larger codebase.

The Mapbox Android team creates plugins but this plugins repository is an open-source project similar to the various Mapbox SDKs for Android.
Plugins' lightweight nature makes them much easier for you and anyone else to contribute rather than trying to add the same feature to the more robust Map SDK. The Mapbox team can also more easily accept contributed plugins and keep the plugin list growing.

## Contributing

We welcome contributions to this plugin repository!

If you're interested in building and sharing your own plugin, please read [the contribution guide](https://github.com/mapbox/mapbox-plugins-android/blob/master/CONTRIBUTING.md) to learn how to get started.
