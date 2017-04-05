# Mapbox Plugins for Android

Experimental plugins to supercharge your maps ⚡️

### About plugins

Plugins are single-purpose libraries built on top of the Mapbox Android SDK that you can include in your apps like any other Android library.

These are the plugins currently available:

* **Traffic.** Adds a traffic layer to any Mapbox basemap.

### Contributing

We welcome contributions to this plugin repository! If you're interested in building and sharing your own, please follow these steps:

1. [Open a ticket](https://github.com/mapbox/mapbox-plugins-android/issues) to kick off a conversation and feel free to tag the `@mapbox/android` team. It's a good idea to explain your plans before you push any code to make sure noone else is working on something similar and to discuss the best approaches to tackle your particular idea.

1. Create a new branch that will contain the code for your plugin.

1. Create a new Android library module inside the `plugins` project with Android Studio. Each plugin is a separate library that depends on the Mapbox Android SDK, and potentially on other third-party libraries. Besides the code for the plugin, make sure you include:

  * Tests.
  * Javadoc that documents the plugin usage and purpose in detail.

1. Create a new activity inside the demo app that showcases how to use your plugin. As important as having a working plugin is to  show how to use it so that anyone can include it in their projects easily.

1. Finally, once you're ready to share your code, list your plugin in this file and then open a PR for the `@mapbox/android` team to review.

Please note that a plugin is simply a library module built on top of the Mapbox Android SDK. For now, we are not requiring plugins to register themselves or to implement any specific interfaces. This might change in the future as we build more plugins and learn how developers use them. We'd love to [hear your ideas](https://github.com/mapbox/mapbox-plugins-android/issues).
