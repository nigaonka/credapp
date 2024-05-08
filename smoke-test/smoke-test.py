#! /usr/bin/env python3

import argparse
import json
import requests
import sys

class SmokeTestError(Exception):
    """Base class for Smoke test related exceptions"""
    pass
    
def main(argv):
    """
    Run mychannel smoke test.
    """
    parser = argparse.ArgumentParser()
    parser.add_argument('test', nargs='?', choices=['smoke-test'], help='compatibility with old containers')
    parser.add_argument('--terraform-output', help='Terraform output describing required state')
    parser.add_argument("-v", "--verbose", default=False, action='store_true', help="Verbose mode")
    args = parser.parse_args(argv)
    global verbose
    verbose = args.verbose

    terraform_output = json.loads(args.terraform_output)

    url = "http://{}/ping".format(terraform_output['k8s_service_endpoint']['value'])
    params = dict(type = 'ping')
    success = dict(type = 'success')
    params["response"] = success
    headers = {}
    headers["SchemaVersion"] = "2"
    if verbose:
        print("sending to {}".format(url))
    response = requests.post(url, headers = headers, json = params, timeout=10)

    if response.status_code != requests.codes.ok:
        raise SmokeTestError("Failure %s: %d, %s" % (url, response.status_code, response.text))

    responseType = response.json()["type"]
    if verbose:
        print("received response type: {}".format(responseType))
    if responseType != "success":
        raise SmokeTestError("Response type was not \"success\": %s." % responseType)
 
    return 0


if __name__ == '__main__':
    try:
        ret = main(sys.argv[1:])
        sys.exit(ret)
    except Exception as ex:
        print('Exception running smoke-test: ' + str(ex))
        raise
