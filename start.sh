#!/usr/bin/env bash

BASEDIR=`dirname $0`

if [ ! -f $BASEDIR/libs/esper-3.3.0.jar ]; then
  echo "Downloading Esper..."
  if [ ! `which wget` ]; then
    echo "Please install wget in order to download libraries"
    exit 1
  fi
  wget -O $BASEDIR/libs/esper-3.3.0.jar http://repository.codehaus.org/com/espertech/esper/3.3.0/esper-3.3.0.jar
fi

if [ ! -f $BASEDIR/libs/esper-3.3.0.jar ]; then
  echo "ERROR: Missing Esper library(3.3.0), please download from http://esper.codehaus.org/esper/download/download.html"
  exit 1
fi

# Run this under extract path of rocksteady
java -Xms32m -Xmx128m -cp libs/*:classes/ -Dlog4j.configuration=META-INF/log4j.properties com.admob.rocksteady.Main
