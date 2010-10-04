#!/usr/bin/env bash

BASEDIR=`dirname $0`

# Run this under extract path of rocksteady
java -Xms32m -Xmx128m -cp libs/*:classes/ -Dlog4j.configuration=META-INF/log4j.properties com.admob.rocksteady.Main
