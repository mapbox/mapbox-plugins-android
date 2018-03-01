#
# Git
#

import subprocess

def git_get_current_branch():
    return subprocess.check_output('git symbolic-ref --short HEAD'.split(' ')).strip()

def git_add(path):
    execute_call(command='git add %s' % path)

def git_commit_and_push(branch, version):
    message = '[android] [auto] Update properties to version %s in preparation for build.' % version
    commands = [
        'git commit -m "%s"' % message,
        'git push -u origin %s' % branch]
    for command in commands:
        execute_call(command=command)