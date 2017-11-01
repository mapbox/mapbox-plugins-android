checkstyle:
	./gradlew checkstyle

test:
	./gradlew :geojson:test
	./gradlew :traffic:test
	./gradlew :locationlayer:test
	./gradlew :building:test
	./gradlew :cluster:test

build-release:
	./gradlew :geojson:assembleRelease
	./gradlew :traffic:assembleRelease
	./gradlew :locationlayer:assembleRelease
	./gradlew :building:assembleRelease
	./gradlew :cluster:assembleRelease

javadoc:
	# Android modules
	# Output is ./mapbox/*/build/docs/javadoc/release
	./gradlew :geojson:javadocrelease
	./gradlew :traffic:javadocrelease
	./gradlew :locationlayer:javadocrelease
	./gradlew :building:javadocrelease
	./gradlew :cluster:javadocrelease

publish:
	export IS_LOCAL_DEVELOPMENT=false; ./gradlew :geojson:uploadArchives
	export IS_LOCAL_DEVELOPMENT=false; ./gradlew :traffic:uploadArchives
	export IS_LOCAL_DEVELOPMENT=false; ./gradlew :locationlayer:uploadArchives
	export IS_LOCAL_DEVELOPMENT=false; ./gradlew :building:uploadArchives
	export IS_LOCAL_DEVELOPMENT=false; ./gradlew :cluster:uploadArchives

publish-local:
	# This publishes to ~/.m2/repository/com/mapbox/mapboxsdk
	export IS_LOCAL_DEVELOPMENT=true; ./gradlew :geojson:uploadArchives
	export IS_LOCAL_DEVELOPMENT=true; ./gradlew :traffic:uploadArchives
	export IS_LOCAL_DEVELOPMENT=true; ./gradlew :locationlayer:uploadArchives
	export IS_LOCAL_DEVELOPMENT=true; ./gradlew :building:uploadArchives
	export IS_LOCAL_DEVELOPMENT=true; ./gradlew :cluster:uploadArchives
