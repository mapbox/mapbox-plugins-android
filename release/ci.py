#
# CircleCI
#

from utils import abort_with_message
import constants
import json
import os
import requests
import sys


def get_circleci_api_token():
    circleci_api_token = os.environ.get(constants.CIRCLECI_API_TOKEN_ENV_VAR)
    if not circleci_api_token:
        abort_with_message('You need to set the CIRCLECI_API_TOKEN environment variable.')
    print 'Found CircleCI API token.'
    return circleci_api_token


def do_circleci_request(branch):
    url = constants.URL_CIRCLECI + branch
    params = {'circle-token': get_circleci_api_token()}
    print 'CircleCI request to %s (params: %s)' % (url, json.dumps(params))

    continue_release = raw_input("\nDo you want to start a build? ").lower()
    if not continue_release.startswith('y'):
        print 'Aborting release'
        sys.exit()

    r = requests.post(url, params)
    print '- CircleCI response code: %s' % r.status_code
