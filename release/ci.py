#
# CircleCI
#

import click
import json
import os
import requests

def get_circleci_api_token():
    circleci_api_token = os.environ.get(CIRCLECI_API_TOKEN_ENV_VAR)
    if not circleci_api_token:
        abort_with_message('You need to set the CIRCLECI_API_TOKEN environment variable.')
    click.echo('Found CircleCI API token.')
    return circleci_api_token

def do_circleci_request(branch):
    url = URL_CIRCLECI + branch
    params = {'circle-token': get_circleci_api_token()}
    click.echo('CircleCI request to %s (params: %s)' % (url, json.dumps(params)))
    click.confirm('\nDo you want to start a build?', abort=True)

    r = requests.post(url, params=params)
    click.echo('- CircleCI response code: %s' % r.status_code)