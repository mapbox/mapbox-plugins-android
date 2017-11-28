checkstyle:
	./gradlew checkstyle

test:
	./gradlew :plugin-geojson:test
	./gradlew :plugin-traffic:test
	./gradlew :plugin-locationlayer:test
	./gradlew :plugin-building:test
	./gradlew :plugin-cluster:test
	./gradlew :plugin-places:test

build-release:
	./gradlew :plugin-geojson:assembleRelease
	./gradlew :plugin-traffic:assembleRelease
	./gradlew :plugin-locationlayer:assembleRelease
	./gradlew :plugin-building:assembleRelease
	./gradlew :plugin-cluster:assembleRelease
	./gradlew :plugin-places:assembleRelease

javadoc:
	# Android modules
	# Output is ./mapbox/*/build/docs/javadoc/release
	./gradlew :plugin-geojson:javadocrelease
	./gradlew :plugin-traffic:javadocrelease
	./gradlew :plugin-locationlayer:javadocrelease
	./gradlew :plugin-building:javadocrelease
	./gradlew :plugin-cluster:javadocrelease
	./gradlew :plugin-places:javadocrelease

publish:
	export IS_LOCAL_DEVELOPMENT=false; ./gradlew :plugin-geojson:uploadArchives
	export IS_LOCAL_DEVELOPMENT=false; ./gradlew :plugin-traffic:uploadArchives
	export IS_LOCAL_DEVELOPMENT=false; ./gradlew :plugin-locationlayer:uploadArchives
	export IS_LOCAL_DEVELOPMENT=false; ./gradlew :plugin-building:uploadArchives
	export IS_LOCAL_DEVELOPMENT=false; ./gradlew :plugin-cluster:uploadArchives
	export IS_LOCAL_DEVELOPMENT=false; ./gradlew :plugin-places:uploadArchives

publish-local:
	# This publishes to ~/.m2/repository/com/mapbox/mapboxsdk
	export IS_LOCAL_DEVELOPMENT=true; ./gradlew :plugin-geojson:uploadArchives
	export IS_LOCAL_DEVELOPMENT=true; ./gradlew :plugin-traffic:uploadArchives
	export IS_LOCAL_DEVELOPMENT=true; ./gradlew :plugin-locationlayer:uploadArchives
	export IS_LOCAL_DEVELOPMENT=true; ./gradlew :plugin-building:uploadArchives
	export IS_LOCAL_DEVELOPMENT=true; ./gradlew :plugin-cluster:uploadArchives
	export IS_LOCAL_DEVELOPMENT=true; ./gradlew :plugin-places:uploadArchives
