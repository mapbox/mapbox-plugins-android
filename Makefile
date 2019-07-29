MBGL_ANDROID_PLUGINS += traffic;plugin-traffic
MBGL_ANDROID_PLUGINS += locationlayer;plugin-locationlayer
MBGL_ANDROID_PLUGINS += building;plugin-building
MBGL_ANDROID_PLUGINS += offline;plugin-offline
MBGL_ANDROID_PLUGINS += places;plugin-places
MBGL_ANDROID_PLUGINS += localization;plugin-localization
MBGL_ANDROID_PLUGINS += scalebar;plugin-scalebar

checkstyle:
	./gradlew checkstyle && ./gradlew ktlintCheck

kotlin-lint:
	./gradlew ktlintCheck

test:
	./gradlew test --info

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

generate-sanity-test:
	npm install && node scripts/generate-activity-test.js

generate-annotation-code:
	npm install && node plugin-annotation/scripts/code-gen.js

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
