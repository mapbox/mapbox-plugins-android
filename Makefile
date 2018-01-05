MBGL_ANDROID_PLUGINS  = geojson;plugin-geojson
MBGL_ANDROID_PLUGINS += traffic;plugin-traffic
MBGL_ANDROID_PLUGINS += locationlayer;plugin-locationlayer
MBGL_ANDROID_PLUGINS += building;plugin-building
MBGL_ANDROID_PLUGINS += cluster;plugin-cluster
MBGL_ANDROID_PLUGINS += offline;plugin-offline
MBGL_ANDROID_PLUGINS += places;plugin-places

sonarqube:
	./gradlew test
	./gradlew lint
	./gradlew jacocoTestReport
	./gradlew sonarqube

checkstyle:
	./gradlew checkstyle

test:
	./gradlew test

build-release:
	./gradlew assembleRelease

javadoc:
	# Android modules
	# Output is ./mapbox/*/build/docs/javadoc/release
	./gradlew javadocrelease

publish:
	export IS_LOCAL_DEVELOPMENT=false; ./gradlew uploadArchives

publish-local:
	# This publishes to ~/.m2/repository/com/mapbox/mapboxsdk
	export IS_LOCAL_DEVELOPMENT=true; ./gradlew uploadArchives

#
# individual Make commands
#

define ANDROID_RULES

test-$1:
	./gradlew :$2:test

build-release-$1:
	./gradlew :$2:assembleRelease

javadoc-$1:
	# Android modules
	# Output is ./mapbox/*/build/docs/javadoc/release
	./gradlew :$2:javadocrelease

publish-$1:
	export IS_LOCAL_DEVELOPMENT=false; ./gradlew :$2:uploadArchives

publish-local-$1:
	# This publishes to ~/.m2/repository/com/mapbox/mapboxsdk
	export IS_LOCAL_DEVELOPMENT=true; ./gradlew :$2:uploadArchives

endef

# Explodes the arguments into individual variables
define ANDROID_RULES_INVOKER
$(call ANDROID_RULES,$(word 1,$1),$(word 2,$1))
endef

$(foreach plugin,$(MBGL_ANDROID_PLUGINS),$(eval $(call ANDROID_RULES_INVOKER,$(subst ;, ,$(plugin)))))
