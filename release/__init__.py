import constants
from menu import menu
from version import update_current_version
import os
from javadoc import generate_javadoc

itemSelected = menu(constants.MENU_ITEMS, 'Welcome,\nSelect a menu item to start:')
plugin = menu(constants.PLUGINS, 'Choose a plugin:')
index = constants.PLUGINS.index(plugin)
plugin = constants.PLUGIN_FILE_PATH[index]
os.system('clear')
version_name = raw_input("What is the version number? ")

if itemSelected == constants.MENU_ITEMS[1]:
    generate_javadoc(version_name, plugin)