#!/bin/bash

# set these variables with appropriate defaults for your service
export SERVICE_NAME=${SERVICE_NAME:-mychannel}
export TIVO_APP_VERSION=${TIVO_APP_VERSION:-0.0.1}
export INSTANCE_NAME=${INSTANCE_NAME:-$USER}
export TIVO_OWNER=${TIVO_OWNER:-${CORP_USER:-$USER}@xperi.com}
export TIVO_DATACENTER=${TIVO_DATACENTER:-tek2}
export SLACK_ALERT_CHANNEL=${SLACK_ALERT_CHANNEL:-${TIVO_DATACENTER}-${SERVICE_NAME}-${USER}-alerts}
export AWS_REGION=${AWS_REGION:-us-east-1}

# set the docker helper image name
export HELPER_IMAGE=${HELPER_IMAGE:-docker.tivo.com/inception-k8s-docker-helper}

# boilerplate below
mkdir -p build/helper
docker pull $HELPER_IMAGE 1>/dev/null
docker run --rm $HELPER_IMAGE get-run-script > build/helper/run.sh
sh build/helper/run.sh "$*"
