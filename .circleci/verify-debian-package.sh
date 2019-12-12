#!/bin/bash

java_version="$1"

if [ "$java_version" != "8" ] && [ "$java_version" != "11" ] ; then
  echo Unsupported Java version
  exit 1
fi

timestamp=$(date)

cat << EOF > Dockerfile
FROM ubuntu:18.04

# Force a new image every time we run this script
RUN echo $timestamp

RUN apt-get update -q
RUN apt-get install -q -y openjdk-${java_version}-jre

ADD target/deb/*deb /tmp
EOF

# Build an intermediate container
docker build -t packagechecker .

docker run packagechecker /bin/bash -c "dpkg -i /tmp/*deb"

