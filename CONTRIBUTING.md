# Contributing

We welcome contributions to this plugin repository. If you're interested in building and sharing your own, please follow these steps:

- [Open a ticket](https://github.com/mapbox/mapbox-plugins-android/issues/new) to kick off a conversation, feel free to tag the `@mapbox/android` team. It's a good idea to explain your plans before you push any code to make sure noone else is working on something similar and to discuss the best approaches to tackle your particular idea.

- Create a new branch that will contain the code for your plugin.

- Create a new Android library module inside the `plugins` project with Android Studio. Each plugin is a separate library that depends on the Mapbox Android SDK, and potentially on other third-party libraries. Besides the code for the plugin, make sure you include:

  - Tests.
  - Javadoc that documents the plugin usage and purpose in detail.

- Create a new activity inside the demo app that showcases how to use your plugin. As important as having a working plugin is to show how to use it so that anyone can include it in their projects easily.

- Finally, once you're ready to share your code, list your plugin in this file and then open a PR for the `@mapbox/android` team to review.

## Adding or updating a localization

The Mapbox Plugins SDK for Android features several translations contributed through [Transifex](https://www.transifex.com/mapbox/mapbox-plugins-android/). If your language already has a translation, feel free to complete or proofread it. Otherwise, please [request your language](https://www.transifex.com/mapbox/mapbox-plugins-android/) so you can start translating. Note that we’re primarily interested in languages that Android supports as system languages.

While you’re there, please consider also translating the following related projects:

* [OSRM Text Instructions](https://www.transifex.com/project-osrm/osrm-text-instructions/), which the Mapbox Directions API uses to generate textual and verbal turn instructions ([instructions](https://github.com/Project-OSRM/osrm-text-instructions/blob/master/CONTRIBUTING.md#adding-or-updating-a-localization)).
* [Mapbox Navigation SDK for iOS](https://www.transifex.com/mapbox/mapbox-navigation-ios/), the analogous library for iOS applications ([instructions](https://github.com/mapbox/mapbox-navigation-ios/blob/master/CONTRIBUTING.md#adding-or-updating-a-localization)).
* [Mapbox Maps SDK for Android](https://www.transifex.com/mapbox/mapbox-gl-native/), which is responsible for the map view and minor UI elements such as the Mapbox Telemetry permissions dialog

Once you’ve finished translating the Android Plugins SDK into a new language in Transifex, open an issue in this repository asking to pull in your translations. You can also pull in the translations yourself:

1. Create a new branch that will contain the new translations.
1. _(First time only.)_ Download the [`tx` command line tool](https://docs.transifex.com/client/installing-the-client) and [configure your .transifexrc](https://docs.transifex.com/client/client-configuration).
1. Run `tx pull -a` to fetch translations from Transifex. You can restrict the operation to only pull in the new language using `tx pull -l xyz`, where _xyz_ is the language code.
1. Commit any new files that were added and open a pull request with your changes.

# Code of conduct
Everyone is invited to participate in Mapbox’s open source projects and public discussions: we want to create a welcoming and friendly environment. Harassment of participants or other unethical and unprofessional behavior will not be tolerated in our spaces. The [Contributor Covenant](http://contributor-covenant.org) applies to all projects under the Mapbox organization and we ask that you please read [the full text](http://contributor-covenant.org/version/1/2/0/).

You can learn more about our open source philosophy on [mapbox.com](https://www.mapbox.com/about/open/).
