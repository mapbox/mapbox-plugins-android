MENU_ITEMS = ['1. Release a module', '2. Generate Javadoc', '3. Exit']
PLUGINS = ['1. building', '2. cluster', '3. geojson', '4. localization', '5. location layer',
           '6. offline', '7. places', '8. traffic']
PLUGIN_FILE_PATH = ['building', 'cluster', 'geojson', 'localization', 'locationlayer', 'offline',
                    'places', 'traffic']

# You can add your API token onhttps://circleci.com/account/api
CIRCLECI_API_TOKEN_ENV_VAR = 'CIRCLECI_API_TOKEN'

# We get the default version from here
ANDROID_DOCS_ROOT_PATH = '../../android-docs'
PLUGIN_ROOT_PATH = '../'
GRADLE_TOKEN = 'VERSION_NAME='

# Circle CI
# Triggers a new build, returns a summary of the build
URL_CIRCLECI = 'https://circleci.com/api/v1.1/project/github/mapbox/mapbox-plugins-android/tree/'  # + :branch
