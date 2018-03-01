

MENU_ITEMS = ['1. Release a module', '2. Generate Javadoc', '3. Exit']
PLUGINS = ['1. building', '2. cluster', '3. geojson', '4. localization', '5. location layer', '6. offline', '7. places', '8. traffic']
PLUGIN_FILE_PATH = ['building', 'cluster', 'geojson', 'localization', 'locationlayer', 'offline', 'places', 'traffic']

# Three stages, from less stable to more stable
ALLOWED_STAGES = ['snapshot', 'beta', 'final']

# Get the version from GRADLE_PROPERTIES_PATH below
CURRENT_VERSION_TAG = 'current'

# You can find the API token in https://www.bitrise.io/app/82d6356fb9d86849#/code -> API token
BITRISE_API_TOKEN_ENV_VAR = 'BITRISE_API_TOKEN_NAVIGATION'

# In the future we might want to consider alpha, or rc.
ALLOWED_PRE_RELEASE = ['beta']

# We get the default version from here
ANDROID_DOCS_ROOT_PATH = '../../android-docs'
PLUGIN_ROOT_PATH = '../'
GRADLE_TOKEN = 'VERSION_NAME='

# Bitrise
URL_BITRISE = 'https://www.bitrise.io/app/82d6356fb9d86849/build/start.json'