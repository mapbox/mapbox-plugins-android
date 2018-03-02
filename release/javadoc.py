from version import update_current_version
import constants
import subprocess


# Generate Javadoc
def generate_javadoc(version_name, plugin):
    print 'Version being used for Javadoc: %s' % version_name
    update_current_version('../plugin-%s/gradle.properties' % plugin,
                           constants.GRADLE_TOKEN, version_name)
    subprocess.Popen(['make', 'javadoc-%s' % plugin], constants.PLUGIN_ROOT_PATH).wait()
    subprocess.Popen(['mv', 'release', version_name],
                     '../plugin-%s/build/docs/javadoc/' % plugin).wait()
    subprocess.Popen(['git', 'checkout', 'mb-pages'], constants.ANDROID_DOCS_ROOT_PATH).wait()
    BRANCH_NAME = version_name + '-javadoc'
    print 'Creating android-docs branch: %s' % BRANCH_NAME
    subprocess.Popen(['git', 'checkout', '-b', BRANCH_NAME], '../../android-docs').wait()
    subprocess.Popen(['mv', version_name, '../../../../../android-docs/api/plugins/%s' % plugin],
                     '../plugin-%s/build/docs/javadoc/' % plugin).wait()
    subprocess.Popen(['git', 'add', 'api/plugins'], constants.ANDROID_DOCS_ROOT_PATH).wait()
    COMMIT_MESSAGE = version_name + "-javadoc-added"
    print 'Committing with message: %s' % COMMIT_MESSAGE
    subprocess.Popen(['git', 'commit', '-m', COMMIT_MESSAGE],
                     constants.ANDROID_DOCS_ROOT_PATH).wait()
    subprocess.Popen(['git', 'push', '-u', 'origin', BRANCH_NAME],
                     constants.ANDROID_DOCS_ROOT_PATH).wait()
    print 'Commit pushed, open a PR now'
