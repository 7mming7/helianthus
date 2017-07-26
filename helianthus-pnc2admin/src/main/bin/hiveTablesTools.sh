#!/bin/bash

THIS="$0"

while [ -h "$THIS" ]; do
ls=`ls -ld "$THIS"`
link=`expr "$ls" : '.*-> \(.*\)$'`
if expr "$link" : '.*/.*' > /dev/null; then
THIS="$link"
else
THIS=`dirname "$THIS"`/"$link"
fi
done

THIS_DIR=`dirname "$THIS"`
BIGDATA_HOME=`cd "$THIS_DIR/.." ; pwd`
cd $BIGDATA_HOME
source $BIGDATA_HOME/bin/env.sh

GENERATE_HIVE_SHELL="$BIGDATA_HOME/bin/command.sh generateHiveScript"

$GENERATE_HIVE_SHELL $@