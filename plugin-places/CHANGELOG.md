# Changelog for the Mapbox Places Plugin

Mapbox welcomes participation and contributions from everyone.

### mapbox-android-plugin-places-v9:0.11.0 - March 31, 2020
####Bugs

- Setting reverse geocoding BottomSheetBehavior hideable to true [#1107](https://github.com/mapbox/mapbox-plugins-android/pull/1107)

### mapbox-android-plugin-places-v9:0.10.0 - March 5, 2020
#### Features
- Switching all plugins to AndroidX [#1100](https://github.com/mapbox/mapbox-plugins-android/pull/1100)
- Added user location button to Places Plugin's PlacePickerActivity [#994](https://github.com/mapbox/mapbox-plugins-android/pull/994)

### mapbox-android-plugin-places-v8:0.9.0 - June 11, 2019
#### Features
- Adding visual limit option to the number of recently seached places [#922](https://github.com/mapbox/mapbox-plugins-android/pull/922)
- Adding ability to set status bar color in places plugin search UI [#962](https://github.com/mapbox/mapbox-plugins-android/pull/962)
- Bump java services version to v4.6.0 [#923](https://github.com/mapbox/mapbox-plugins-android/pull/923)
- Added check for null latLng in PlacePickerActivity makeReverseGeocodingSearch()[#964](https://github.com/mapbox/mapbox-plugins-android/pull/964)
- Adding includeReverseGeocoding call method to PlacePickerOptions [#965](https://github.com/mapbox/mapbox-plugins-android/pull/965)

### mapbox-android-plugin-places-v7:0.8.0 - April 5, 2019
#### Features
- Maps SDK bump to 7.3.0 [#895](https://github.com/mapbox/mapbox-plugins-android/pull/895)
#### Bugs
- Include missing Timber dependency [#820](https://github.com/mapbox/mapbox-plugins-android/pull/820/commits/12083e8964fd81b4cd0818bfcc2d433ba361b6fa)

### mapbox-android-plugin-places-v7:0.7.0 - January 8, 2019
 - Update Offline plugin with the Maps SDK v7.0.0 [#789](https://github.com/mapbox/mapbox-plugins-android/pull/789)
 - Places plugin NPEs fixes [#806](https://github.com/mapbox/mapbox-plugins-android/pull/806)

### 0.6.0 - September 12, 2018
 - Problem with option 'country' [#554](https://github.com/mapbox/mapbox-plugins-android/pull/554)
 - Update ProGuard to support built-in shrinker of Gradle Plugin [#582](https://github.com/mapbox/mapbox-plugins-android/pull/582)
 - Fix for disappearing places picker marker [#584](https://github.com/mapbox/mapbox-plugins-android/pull/584)
 - Maps SDK bump to 6.5.0 [#634](https://github.com/mapbox/mapbox-plugins-android/pull/634)

### 0.5.0 - May 23, 2018
- Updates the Map SDK to 6.1.3 [#531](https://github.com/mapbox/mapbox-plugins-android/pull/531)

### 0.4.0 - May 7, 2018
- Updates Map SDK to 6.1.1 [#488](https://github.com/mapbox/mapbox-plugins-android/pull/488)
- Lowered min SDK to API level 14 to match Map SDK [#472](https://github.com/mapbox/mapbox-plugins-android/pull/472)

### 0.3.0 - April 18, 2018
- Updates Map SDK to 6.0.1
- Adds consumer ProGuard rules [#373](https://github.com/mapbox/mapbox-plugins-android/pull/373)
- Resolves Places Plugin Offline crash [#377](https://github.com/mapbox/mapbox-plugins-android/pull/377)

### 0.2.2 - March 7, 2018
- Package namespacing is now corrected to match other plugins [#336](https://github.com/mapbox/mapbox-plugins-android/pull/336)
- Back button in Place Picker activity now calls `finish()` [#337](https://github.com/mapbox/mapbox-plugins-android/pull/337)
- Adds missing Javadoc information for `PlaceOptions` class [#339](https://github.com/mapbox/mapbox-plugins-android/pull/339)

### 0.2.1 - February 27, 2018
- Remove location layer dep [#319](https://github.com/mapbox/mapbox-plugins-android/pull/319)

### 0.2.0 - February 26, 2018
- Place Picker added [#262](https://github.com/mapbox/mapbox-plugins-android/pull/262)
- Updated geocoding dependency [#261](https://github.com/mapbox/mapbox-plugins-android/pull/261)
- Increased the search view button sizes to help capture when a user clicks the button. [#240](https://github.com/mapbox/mapbox-plugins-android/pull/240)

### 0.1.0 - December 22, 2017
- Initial release as a standalone package.