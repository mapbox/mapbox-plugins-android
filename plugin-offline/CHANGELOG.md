# Changelog for the Mapbox Offline Plugin

Mapbox welcomes participation and contributions from everyone.

### mapbox-android-plugin-offline-v9:0.8.0 - XXX
#### Bugs
- Cancel download should remove region definition [#1145](https://github.com/mapbox/mapbox-plugins-android/pull/1145)

### mapbox-android-plugin-offline-v9:0.7.0 - March 5, 2020
#### Features
- Switching all plugins to AndroidX [#1100](https://github.com/mapbox/mapbox-plugins-android/pull/1100)

### mapbox-android-plugin-offline-v8:0.6.0 - June 11, 2019

No changes since last release. Release happened to update the module to `-v8`. 

### mapbox-android-plugin-offline-v7:0.5.0 - April 5, 2019
#### Features
- Maps SDK bump to 7.3.0 [#895](https://github.com/mapbox/mapbox-plugins-android/pull/895)
- Use a generic OfflineRegionDefinition [#913](https://github.com/mapbox/mapbox-plugins-android/pull/913)
#### Bugs
- Include missing Timber dependency [#820](https://github.com/mapbox/mapbox-plugins-android/pull/820/commits/12083e8964fd81b4cd0818bfcc2d433ba361b6fa)
- Disable vector drawable in download notification on pre lollipop devices [#866](https://github.com/mapbox/mapbox-plugins-android/pull/866)
- Check for null intent in case of a service restart [#912](https://github.com/mapbox/mapbox-plugins-android/pull/912)

### mapbox-android-plugin-offline-v7:0.4.0 - January 8, 2019
- Update Offline plugin with the Maps SDK v7.0.0 [#789](https://github.com/mapbox/mapbox-plugins-android/pull/789)
- Cancelling download reworked [#779](https://github.com/mapbox/mapbox-plugins-android/pull/779)
- Add cancel text and enable/disable map snapshots notification options [#805](https://github.com/mapbox/mapbox-plugins-android/pull/805)

### 0.3.0 - May 23, 2018
- Updates the Map SDK to 6.1.3 [#531](https://github.com/mapbox/mapbox-plugins-android/pull/531)

### 0.2.0 - May 7, 2018
- Updates Map SDK to 6.1.1 [#488](https://github.com/mapbox/mapbox-plugins-android/pull/488)
- Lowered min SDK to API level 14 to match Map SDK [#472](https://github.com/mapbox/mapbox-plugins-android/pull/472)
- Adds offline UI Options [#468](https://github.com/mapbox/mapbox-plugins-android/pull/468)
- Adds additional documentation to offline classes [#466](https://github.com/mapbox/mapbox-plugins-android/pull/466)

### 0.1.0 - April 18, 2018
- Initial release as a standalone package.