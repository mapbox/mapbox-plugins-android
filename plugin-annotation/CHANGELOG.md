# Changelog for the Mapbox Annotation Plugin

Mapbox welcomes participation and contributions from everyone.

### mapbox-android-plugin-annotation-v7:0.6.0 - April 5, 2019
#### Features
- Maps SDK bump to 7.3.0 [#895](https://github.com/mapbox/mapbox-plugins-android/pull/895)
- Add toString, hashcode and equals [#858](https://github.com/mapbox/mapbox-plugins-android/pull/858)
- Delete all annotations method [#859](https://github.com/mapbox/mapbox-plugins-android/pull/859)
- Add style spec documentation generation [#879](https://github.com/mapbox/mapbox-plugins-android/pull/879)
- Move supported json properties docs to the public methods [#889](https://github.com/mapbox/mapbox-plugins-android/pull/889)
- Support string color for layout properties [#897](https://github.com/mapbox/mapbox-plugins-android/pull/897)
- Expose GeoJsonOptions [#909](https://github.com/mapbox/mapbox-plugins-android/pull/909)
#### Bugs
- Check if annotation is an active annotation when updating [#891](https://github.com/mapbox/mapbox-plugins-android/pull/891)
- Use with prefix for options [#898](https://github.com/mapbox/mapbox-plugins-android/pull/898)
- Notify gesture manager when drag is handled [#906](https://github.com/mapbox/mapbox-plugins-android/pull/906)
- Register only one style loaded listener [#905](https://github.com/mapbox/mapbox-plugins-android/pull/905)
- Use constants wherever property name strings are needed [#910](https://github.com/mapbox/mapbox-plugins-android/pull/910)

### mapbox-android-plugin-annotation-v7:0.5.0 - February 5, 2019
- Do not flag click events as handled [#822](https://github.com/mapbox/mapbox-plugins-android/pull/822)
- Update dependencies and javadoc source reference [#827](https://github.com/mapbox/mapbox-plugins-android/pull/827)
- Fixup remove drag listener naming [#833](https://github.com/mapbox/mapbox-plugins-android/pull/833)
- Maps SDK v7.1.1 [#820](https://github.com/mapbox/mapbox-plugins-android/pull/820)

### mapbox-android-plugin-annotation-v7:0.4.0 - January 8, 2019
- Mapbox maps SDK for Android v7.0.0 [#800](https://github.com/mapbox/mapbox-plugins-android/pull/800)
- Save plugin's state across style changes [#800](https://github.com/mapbox/mapbox-plugins-android/pull/800/commits/cba17474e6087faf94375570700d4edfc52b6dd6)
- Exposed filter API [#775](https://github.com/mapbox/mapbox-plugins-android/pull/775)
- Create annotations from FeatureCollection/GeoJSON [#772](https://github.com/mapbox/mapbox-plugins-android/pull/772)
- Invalid symbol ID during animation fix [#776](https://github.com/mapbox/mapbox-plugins-android/pull/776/commits/c2488010f27107693fd529a01afc2f51ecde03cf)
- Make package private abstract classes public [#769](https://github.com/mapbox/mapbox-plugins-android/pull/769)
- Draggable builder option [#768](https://github.com/mapbox/mapbox-plugins-android/pull/768)
- Generic geometry [#777](https://github.com/mapbox/mapbox-plugins-android/pull/777)

### 0.3.0 - November 9, 2018
- Mapbox maps SDK for Android v6.7.0 [#728](https://github.com/mapbox/mapbox-plugins-android/pull/728)
- Draggable annotations [#753](https://github.com/mapbox/mapbox-plugins-android/pull/753)
- Set layer properties expressions only on utilized properties [#762](https://github.com/mapbox/mapbox-plugins-android/pull/762)

### 0.2.0 - October 25, 2018
- Expose below layer ID configuration [#677](https://github.com/mapbox/mapbox-plugins-android/pull/677)
- Add long click listeners to annotation manager [#685](https://github.com/mapbox/mapbox-plugins-android/pull/685)
- Add bulk annotation addition API [#689](https://github.com/mapbox/mapbox-plugins-android/pull/689)
- Add builder classes [#700](https://github.com/mapbox/mapbox-plugins-android/pull/700)
- Remove self update mechanism, CRUD interface [#702](https://github.com/mapbox/mapbox-plugins-android/pull/702)
- Add GeoJSON compatible API [#704](https://github.com/mapbox/mapbox-plugins-android/pull/704)
- Style spec update, z-ordering symbols [#718](https://github.com/mapbox/mapbox-plugins-android/pull/718)
- Color API for android color ints, remove string API [#721](https://github.com/mapbox/mapbox-plugins-android/pull/721)
- Use PointF instead of Float[] [#722](https://github.com/mapbox/mapbox-plugins-android/pull/722)
- Integrate pattern for fills and lines [#726](https://github.com/mapbox/mapbox-plugins-android/pull/726)

### 0.1.0 - September 12, 2018
- Initial release