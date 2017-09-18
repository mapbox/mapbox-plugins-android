checkstyle:
	cd plugins; ./gradlew checkstyle

test:
	cd plugins; ./gradlew :geojson:test
	cd plugins; ./gradlew :traffic:test
	cd plugins; ./gradlew :locationlayer:test
	cd plugins; ./gradlew :building:test
	cd plugins; ./gradlew :offline:test		

build-release:
	cd plugins; ./gradlew :geojson:assembleRelease
	cd plugins; ./gradlew :traffic:assembleRelease
	cd plugins; ./gradlew :locationlayer:assembleRelease
	cd plugins; ./gradlew :building:assembleRelease
	cd plugins; ./gradlew :offline:assembleRelease

javadoc:
	# Android modules
	# Output is ./mapbox/*/build/docs/javadoc/release
	cd plugins; ./gradlew :geojson:javadocrelease
	cd plugins; ./gradlew :traffic:javadocrelease
	cd plugins; ./gradlew :locationlayer:javadocrelease
	cd plugins; ./gradlew :building:javadocrelease
	cd plugins; ./gradlew :offline:javadocrelease

publish:
	cd plugins; export IS_LOCAL_DEVELOPMENT=false; ./gradlew :geojson:uploadArchives
	cd plugins; export IS_LOCAL_DEVELOPMENT=false; ./gradlew :traffic:uploadArchives
	cd plugins; export IS_LOCAL_DEVELOPMENT=false; ./gradlew :locationlayer:uploadArchives
	cd plugins; export IS_LOCAL_DEVELOPMENT=false; ./gradlew :building:uploadArchives
	cd plugins; export IS_LOCAL_DEVELOPMENT=false; ./gradlew :offline:uploadArchives

publish-local:
	# This publishes to ~/.m2/repository/com/mapbox/mapboxsdk
	cd plugins; export IS_LOCAL_DEVELOPMENT=true; ./gradlew :geojson:uploadArchives
	cd plugins; export IS_LOCAL_DEVELOPMENT=true; ./gradlew :traffic:uploadArchives
	cd plugins; export IS_LOCAL_DEVELOPMENT=true; ./gradlew :locationlayer:uploadArchives
	cd plugins; export IS_LOCAL_DEVELOPMENT=true; ./gradlew :building:uploadArchives
	cd plugins: export IS_LOCAL_DEVELOPMENT=true; ./gradlew :offline:uploadArchives
