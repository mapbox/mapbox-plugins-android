# Changelog for the Mapbox location layer plugin

Mapbox welcomes participation and contributions from everyone.

### 0.5.4 - TBD
- Remove `allowobfuscation` option from ProGuard rule file in order to support built-in shrinker of Android Gradle Plugin [#549](https://github.com/mapbox/mapbox-plugins-android/pull/549)

### 0.5.3 - May 23, 2018
- Adds a constructor which doesn't take in a locationEngine and sets up an internal engine instead [#527](https://github.com/mapbox/mapbox-plugins-android/pull/527)
- Remove and readd onMapChange in onStart/onStop [#403](https://github.com/mapbox/mapbox-plugins-android/pull/403)
- Sets initial value to infinity to prevent puck showing at nullisland [#516](https://github.com/mapbox/mapbox-plugins-android/pull/516)
- Use enable/disable methods in onStop and onStart to toggle `isEnabled` boolean [#528](https://github.com/mapbox/mapbox-plugins-android/pull/528)
- Don’t update stale state if it’s not enabled in options [#525](https://github.com/mapbox/mapbox-plugins-android/pull/525)
- Fixes accuracy circle layer not setting color correctly [#526](https://github.com/mapbox/mapbox-plugins-android/pull/526)
- Updated Map SDK to 6.1.3 [#531](https://github.com/mapbox/mapbox-plugins-android/pull/531)

### 0.5.2 - May 10, 2018
- Fixes missing files from Maven Central

### 0.5.1 - May 9, 2018
- Updates Maps SDK to 6.1.1 [#488](https://github.com/mapbox/mapbox-plugins-android/pull/488)
- Lowered min SDK to API level 14 to match Map SDK [#472](https://github.com/mapbox/mapbox-plugins-android/pull/472)
- Fix initialization not setting default modes [#458](https://github.com/mapbox/mapbox-plugins-android/pull/458)
- Fix source not being re-added when the user switches map styles [#483](https://github.com/mapbox/mapbox-plugins-android/pull/483)
- Max animation duration added when in tracking mode [#430](https://github.com/mapbox/mapbox-plugins-android/pull/430)
- Remove invalid `Location` update check [#431](https://github.com/mapbox/mapbox-plugins-android/pull/431)

### 0.5.0 - April 19, 2018
- Maps SDK Bumped to 6.0.1 [#432](https://github.com/mapbox/mapbox-plugins-android/pull/432)
- Remove invalid Location update check [#431](https://github.com/mapbox/mapbox-plugins-android/pull/431)
- Max animation duration when tracking [#430](https://github.com/mapbox/mapbox-plugins-android/pull/430)
- Initialize in a stale state [#427](https://github.com/mapbox/mapbox-plugins-android/pull/427)
- Use json features instead of layer properties setters [#426](https://github.com/mapbox/mapbox-plugins-android/pull/426)
- GeoJson lib to 3.0.1 [#425](https://github.com/mapbox/mapbox-plugins-android/pull/425)
- Fix Proguard issues [#422](https://github.com/mapbox/mapbox-plugins-android/pull/422)
- Fix LocationLayerAnimator ignored updates for first second [#420](https://github.com/mapbox/mapbox-plugins-android/pull/420)
- Remove support library and use just livedata dependency [#419](https://github.com/mapbox/mapbox-plugins-android/pull/419)

### 0.5.0-beta.2 - April 5, 2018
- Update Maps SDK to 6.0.0-beta.6 [#414](https://github.com/mapbox/mapbox-plugins-android/pull/414)
- Filter location updates and remove unused animator code [#393](https://github.com/mapbox/mapbox-plugins-android/pull/393)
- Fix order of interpolator expression [#388](https://github.com/mapbox/mapbox-plugins-android/pull/388)
- Remove duplicate map camera option APIs [#402](https://github.com/mapbox/mapbox-plugins-android/pull/402)
- Fix `LocationLayerOption` class typed array trying to get demension as integer [#399](https://github.com/mapbox/mapbox-plugins-android/pull/399)
- Invalidate onCameraMove as part of tracking animation [#395](https://github.com/mapbox/mapbox-plugins-android/pull/395)
- Added missing Location Layer style attributes [#392](https://github.com/mapbox/mapbox-plugins-android/pull/392)

### 0.5.0-beta.1 - March 29, 2018
- Update Maps SDK to 6.0.0-beta.4 [#384](https://github.com/mapbox/mapbox-plugins-android/pull/384)
- Added Camera and tracking modes [#294](https://github.com/mapbox/mapbox-plugins-android/pull/294)
- Added Location layer `onLongClickListener` [#313](https://github.com/mapbox/mapbox-plugins-android/pull/313)
- Add padding APIs to LocationLayerOptions [#313](https://github.com/mapbox/mapbox-plugins-android/pull/313)
- Improved enabling/disabling layers [#308](https://github.com/mapbox/mapbox-plugins-android/pull/308)
- LocationEngine listens to updates after resetting [#307](https://github.com/mapbox/mapbox-plugins-android/pull/307)
- Add ProGuard consumer rules file [#373](https://github.com/mapbox/mapbox-plugins-android/pull/373)
- Fixed icon elevation still showing even when set to zero in some cases [#356](https://github.com/mapbox/mapbox-plugins-android/pull/356)
- Location layer accuracy visibility issue fix [#306](https://github.com/mapbox/mapbox-plugins-android/pull/306)

### 0.4.0 - February 26, 2018
- LocationLayerOptions class added allowing dynamic styling of layer [#267](https://github.com/mapbox/mapbox-plugins-android/pull/267)
- Stale location mode added [#264](https://github.com/mapbox/mapbox-plugins-android/pull/264)
- Assets have be updated and added a 3D effect [#245](https://github.com/mapbox/mapbox-plugins-android/pull/245)
- All assets and attributes are now prefixed [#263](https://github.com/mapbox/mapbox-plugins-android/pull/263)
- Fixed issue with accuracy ring being initialized with value greater than zero [#245](https://github.com/mapbox/mapbox-plugins-android/pull/245)
- `lastLocation()` now returns the location variable directly [#245](https://github.com/mapbox/mapbox-plugins-android/pull/245)
- Replacing LocationEngine now updates the listener to use the new engine [#292](https://github.com/mapbox/mapbox-plugins-android/pull/292)
- Cancel animation as part of stopping the animation [#247](https://github.com/mapbox/mapbox-plugins-android/pull/247)


### 0.3.0 - December 22, 2017
- Change accuracy ring from fill layer to a circle layer [#186](https://github.com/mapbox/mapbox-plugins-android/pull/186)
- Adds last known location API [#199](https://github.com/mapbox/mapbox-plugins-android/pull/199)
- Adds location layer icon click listener [#198](https://github.com/mapbox/mapbox-plugins-android/pull/198)
- Rework location layer abstraction [#196](https://github.com/mapbox/mapbox-plugins-android/pull/196)

### 0.2.0 - August 18, 2017
- Updated Maps and Mapbox Java Dependencies [#84](https://github.com/mapbox/mapbox-plugins-android/pull/84)
- Navigation icon now uses runtime styling to scale at lower zoom levels [#84](https://github.com/mapbox/mapbox-plugins-android/pull/84)
- Added listener for compass heading and accuracy changes [#84](https://github.com/mapbox/mapbox-plugins-android/pull/84)

### 0.1.0 - July 17, 2017
- Initial release as a standalone package.
