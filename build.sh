#!/bin/bash

# override the service name for the builder
SERVICE_NAME=${SERVICE_NAME:-$(basename $(pwd))}

# set the docker helper image name
HELPER_IMAGE=${HELPER_IMAGE:-docker.tivo.com/inception-k8s-docker-helper}

# boilerplate below
export BUILDER_IMAGE=${SERVICE_NAME}-builder
mkdir -p build/helper
docker pull $HELPER_IMAGE 1>/dev/null
docker run --rm $HELPER_IMAGE get-build-script > build/helper/build.sh
sh build/helper/build.sh "$*"
