from version import update_current_version
import sys
import os
import constants
from git import git_get_current_branch
from git import git_add
from git import git_commit_and_push
from ci import do_circleci_request


# Begin release process
def release(version_name, plugin):
    branch = git_get_current_branch()

    os.system('clear')
    # Get user confirmation
    print '\n===== Build information ====='
    print '- Branch: %s' % branch
    print '- Plugin: %s' % plugin
    print '- Version: %s' % version_name
    continue_release = raw_input("\nDoes it look right (Y/n)? ").lower()
    if not continue_release.startswith('y'):
        print 'Aborting release'
        sys.exit()
    releaseFinal(branch, plugin, version_name)


def releaseFinal(branch, plugin, version_name):
    print 'Publishing %s final release from branch: %s (version: %s).' % (
    plugin, branch, version_name)
    file_path = '../plugin-%s/gradle.properties' % plugin
    dirty_gradle = update_current_version(file_path, constants.GRADLE_TOKEN, version_name)
    if dirty_gradle:
        git_add(file_path)
        git_commit_and_push(branch, version_name)
    do_circleci_request(branch)
