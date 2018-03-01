#
# Utils
#

import click
import subprocess

def abort_with_message(message):
    click.echo(message)
    click.get_current_context().abort()

def execute_call(command):
    click.echo('Executing: %s' % command)
    result = subprocess.call(command, shell=True)
    if result != 0:
        abort_with_message('Command failed: %s' % command)