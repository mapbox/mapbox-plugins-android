sonarqube:
	./gradlew test
	./gradlew lint
	./gradlew jacocoTestReport
	./gradlew --info sonarqube

checkstyle:
	./gradlew checkstyle

test:
	./gradlew :plugin-geojson:test
	./gradlew :plugin-traffic:test
	./gradlew :plugin-locationlayer:test
	./gradlew :plugin-building:test
	./gradlew :plugin-cluster:test
	./gradlew :plugin-places:test
	./gradlew :plugin-offline:test

build-release:
	./gradlew :plugin-geojson:assembleRelease
	./gradlew :plugin-traffic:assembleRelease
	./gradlew :plugin-locationlayer:assembleRelease
	./gradlew :plugin-building:assembleRelease
	./gradlew :plugin-cluster:assembleRelease
	./gradlew :plugin-places:assembleRelease
	./gradlew :plugin-offline:assembleRelease

javadoc:
	# Android modules
	# Output is ./mapbox/*/build/docs/javadoc/release
	./gradlew :plugin-geojson:javadocrelease
	./gradlew :plugin-traffic:javadocrelease
	./gradlew :plugin-locationlayer:javadocrelease
	./gradlew :plugin-building:javadocrelease
	./gradlew :plugin-cluster:javadocrelease
	./gradlew :plugin-places:javadocrelease
	./gradlew :plugin-offline:javadocrelease

publish:
	export IS_LOCAL_DEVELOPMENT=false; ./gradlew :plugin-geojson:uploadArchives
	export IS_LOCAL_DEVELOPMENT=false; ./gradlew :plugin-traffic:uploadArchives
	export IS_LOCAL_DEVELOPMENT=false; ./gradlew :plugin-locationlayer:uploadArchives
	export IS_LOCAL_DEVELOPMENT=false; ./gradlew :plugin-building:uploadArchives
	export IS_LOCAL_DEVELOPMENT=false; ./gradlew :plugin-cluster:uploadArchives
	export IS_LOCAL_DEVELOPMENT=false; ./gradlew :plugin-places:uploadArchives
	export IS_LOCAL_DEVELOPMENT=false; ./gradlew :plugin-offline:uploadArchives

publish-local:
	# This publishes to ~/.m2/repository/com/mapbox/mapboxsdk
	export IS_LOCAL_DEVELOPMENT=true; ./gradlew :plugin-geojson:uploadArchives
	export IS_LOCAL_DEVELOPMENT=true; ./gradlew :plugin-traffic:uploadArchives
	export IS_LOCAL_DEVELOPMENT=true; ./gradlew :plugin-locationlayer:uploadArchives
	export IS_LOCAL_DEVELOPMENT=true; ./gradlew :plugin-building:uploadArchives
	export IS_LOCAL_DEVELOPMENT=true; ./gradlew :plugin-cluster:uploadArchives
	export IS_LOCAL_DEVELOPMENT=true; ./gradlew :plugin-places:uploadArchives
	export IS_LOCAL_DEVELOPMENT=true; ./gradlew :plugin-offline:uploadArchives
