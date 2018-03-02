#
# Utility which assist with the release process.
#

import constants
from menu import menu
import os
from javadoc import generate_javadoc
from release_final import release
import sys

itemSelected = menu(constants.MENU_ITEMS, 'Welcome,\nSelect a menu item to start:')
# Exit early if user selected exit.
if itemSelected == constants.MENU_ITEMS[2]:
    sys.exit()
plugin = menu(constants.PLUGINS, 'Choose a plugin:')
index = constants.PLUGINS.index(plugin)
plugin = constants.PLUGIN_FILE_PATH[index]
os.system('clear')
version_name = raw_input("What is the version number? ")

if itemSelected == constants.MENU_ITEMS[1]:
    generate_javadoc(version_name, plugin)
elif itemSelected == constants.MENU_ITEMS[0]:
    release(version_name, plugin)
