#!/usr/bin/env bash

ZKDASH_VERSION=1.0.2

if [[ -d ${JAVA_HOME} ]];then
        ${JAVA_HOME}/bin/java -Djava.security.egd=file:/dev/./urandom -jar ./zkdash-${ZKDASH_VERSION}.jar > ./output.log 2>&1 &
else
        echo "You need to set JAVA_HOME and then run ./zkdash.sh"
fi