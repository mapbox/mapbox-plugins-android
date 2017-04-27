## Getting Started

Plugins are single-purpose libraries built on top of the Mapbox Android SDK that you can include in your apps like any other Android library. Rather than automatically including these libraries into our SDK, plugins help keep our base Maps SDK light and nimble for your use.

These plugins are currently available:

[Traffic –– Adds a traffic layer to any Mapbox basemap.](https://github.com/mapbox/mapbox-plugins-android/issues/4)

![ezgif com-crop](https://cloud.githubusercontent.com/assets/4394910/24972279/bf88b170-1f6f-11e7-8638-6afe08369d9d.gif)


### Installing plugins with Maven & Gradle


Set `IS_LOCAL_DEVELOPMENT` gradle property to `true` (is `false` by default)
`$> gradlew uploadArchives`
`aar` or `jar` will be automatically installed in your Maven local repo folder (`.m2/M2_HOME`)
Remember to add `mavenLocal()` in `repositories{}` (`build.gradle`) in the project in which you'd like to use Maven local repository


Now if you want to install `aars` or `jars` in your Maven local repository you should define `IS_LOCAL_DEVELOPMENT` environment variable like this:

`$> export IS_LOCAL_DEVELOPMENT=true`

And `$> export IS_LOCAL_DEVELOPMENT=false` if you want same behavior as before. If `IS_LOCAL_DEVELOPMENT` is not exported also works as before.


## Getting Help

Please note that a plugin is simply a library module built on top of the Mapbox Android SDK. For now, we are not requiring plugins to register themselves or to implement any specific interfaces. This might change in the future as we build more plugins and learn how developers use them. We'd love to [hear your ideas](https://github.com/mapbox/mapbox-plugins-android/issues).


## Contributing

We welcome contributions to this plugin repository! If you're interested in building and sharing your own, please follow these steps:

1. [Open a ticket](https://github.com/mapbox/mapbox-plugins-android/issues/new) to kick off a conversation and feel free to tag the `@mapbox/android` team. It's a good idea to explain your plans before you push any code to make sure noone else is working on something similar and to discuss the best approaches to tackle your particular idea.

1. Create a new branch that will contain the code for your plugin.

1. Create a new Android library module inside the `plugins` project with Android Studio. Each plugin is a separate library that depends on the Mapbox Android SDK, and potentially on other third-party libraries. Besides the code for the plugin, make sure you include:

  * Tests.
  * Javadoc that documents the plugin usage and purpose in detail.

1. Create a new activity inside the demo app that showcases how to use your plugin. As important as having a working plugin is to  show how to use it so that anyone can include it in their projects easily.

1. Finally, once you're ready to share your code, list your plugin in this file and then open a PR for the `@mapbox/android` team to review.

