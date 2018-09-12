# Changelog for the Mapbox location layer plugin

Mapbox welcomes participation and contributions from everyone.

### 0.8.2 - September 12, 2018
 - Maps SDK bump to 6.5.0 [#634](https://github.com/mapbox/mapbox-plugins-android/pull/634)

### 0.8.1 - September 4, 2018
- Prevent from showing the location layer on every location update [#641](https://github.com/mapbox/mapbox-plugins-android/pull/641)

### 0.8.0 - August 31, 2018
- Remove auto value [#633](https://github.com/mapbox/mapbox-plugins-android/pull/633)
- Fix LocationLayerPlugin's leaks when used in a fragment [#636](https://github.com/mapbox/mapbox-plugins-android/pull/636)
- Add getter and setter for CompassEngine interface [#626](https://github.com/mapbox/mapbox-plugins-android/pull/626)
- Add fallback compass sensors for devices without a gyroscope [#627](https://github.com/mapbox/mapbox-plugins-android/pull/627)
- Add LocationLayer unit tests [#631](https://github.com/mapbox/mapbox-plugins-android/pull/631)
- Adding new location layer plugin constructor for options and default engine [#618](https://github.com/mapbox/mapbox-plugins-android/pull/618)
- Remove unused LiveData dependency for LocationLayerPlugin [#632](https://github.com/mapbox/mapbox-plugins-android/pull/632)
- Allow adding location layer plugin below a certain layer [#628](https://github.com/mapbox/mapbox-plugins-android/pull/628)
- PluginAnimatorCoordinator unit tests [#630](https://github.com/mapbox/mapbox-plugins-android/pull/630)
- When camera is in a tracking mode, animate instead of snapping on the first location fix [#620](https://github.com/mapbox/mapbox-plugins-android/pull/620)
- Add unit tests for LocationLayerCamera [#629](https://github.com/mapbox/mapbox-plugins-android/pull/629)
- Expose bearing compass accuracy [#625](https://github.com/mapbox/mapbox-plugins-android/pull/625)

### 0.7.2 - August 22, 2018
- Maps SDK bump to 6.4.0 [#615](https://github.com/mapbox/mapbox-plugins-android/pull/615)

### 0.7.1 - July 26, 2018
- Request location updates for AndroidLocationEngine [#602](https://github.com/mapbox/mapbox-plugins-android/pull/602)

### 0.7.0 - July 19, 2018
- Additional methods to tilt and zoom in/out the camera while camera tracking is engaged [#583](https://github.com/mapbox/mapbox-plugins-android/pull/583)
- Fixed wrong focal point persisting when camera modes are changed quickly [#583](https://github.com/mapbox/mapbox-plugins-android/pull/583)
- Update ProGuard to support built-in shrinker of Gradle Plugin [#582](https://github.com/mapbox/mapbox-plugins-android/pull/582)
- Break camera tracking on fling [#587](https://github.com/mapbox/mapbox-plugins-android/pull/587)
- Maps SDK bump to 6.3.0 [#585](https://github.com/mapbox/mapbox-plugins-android/pull/585)
- Fixed memory leak when using the mapbox-plugins [#570](https://github.com/mapbox/mapbox-plugins-android/issues/570)
- Always make plugin visible in location start when enabled [#592](https://github.com/mapbox/mapbox-plugins-android/pull/592)
- Check if the layer is hidden when setting render mode [#594](https://github.com/mapbox/mapbox-plugins-android/pull/594)
- Adjust camera based on the camera mode when plugin enabled [#596](https://github.com/mapbox/mapbox-plugins-android/pull/596)

### 0.6.0 - July 9, 2018
- Animate accuracy ring when new value is provided [#577](https://github.com/mapbox/mapbox-plugins-android/pull/577)
- Allow pre-loaded MapboxMap images / maki icons in LocationLayerOptions [#574](https://github.com/mapbox/mapbox-plugins-android/pull/574)
- Showing accuracy for forced location [#572](https://github.com/mapbox/mapbox-plugins-android/pull/572)
- Update LocationLayerPlugin's readme examples [#571](https://github.com/mapbox/mapbox-plugins-android/pull/571)
- Harden style changes [#568](https://github.com/mapbox/mapbox-plugins-android/pull/568)
- Map zoom range docs [#565](https://github.com/mapbox/mapbox-plugins-android/pull/565)
- 6.2.1 maps sdk bump [#562](https://github.com/mapbox/mapbox-plugins-android/pull/562)
- Maps sdk bump to 6.2.0 [#559](https://github.com/mapbox/mapbox-plugins-android/pull/559)
- Harden state and style changes [#545](https://github.com/mapbox/mapbox-plugins-android/pull/545)
- Expose icon scale option [#543](https://github.com/mapbox/mapbox-plugins-android/pull/543)

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
