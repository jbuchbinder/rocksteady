#!/usr/bin/env bash
#
# *** NOT BEING USED ***
# *** LEFT HERE FOR REFERENCE ***
#
# Should be run within target directory

conf_dir=classes/META-INF

echo "Switching rocksteady.properties to production"
cp -v ${conf_dir}/rocksteady.production.properties ${conf_dir}/rocksteady.properties
echo "Switching epl.xml to production"
cp -v ${conf_dir}/spring/epl.production.xml ${conf_dir}/spring/epl.xml
echo "Switching log.properties to production"
cp -v ${conf_dir}/spring/log4j.production.properties ${conf_dir}/spring/log4j.properties

echo "Killing running rocksteady"
for i in `ps auxww | grep 'com.admob.rocksteady.Main' | grep -v grep | awk '{print $2}'`; do echo killing $i; kill $i; done

JMX="-Dcom.sun.management.jmxremote.port=5015 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false"

echo "Starting rocksteady"
sleep 3
nohup java -Xms1g -Xmx1g ${JMX} -cp libs/*:classes/ -Dlog4j.configuration=META-INF/spring/log4j.properties com.admob.rocksteady.Main >/dev/null 2>&1 &
