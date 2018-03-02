#
# Utils
#

import sys
import subprocess

def abort_with_message(message):
    print message
    sys.exit()

def execute_call(command):
    print 'Executing: %s' % command
    result = subprocess.call(command, True)
    if result != 0:
        abort_with_message('Command failed: %s' % command)