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

# if no args specified, show usage
if [ $# = 0 ]; then
  echo "Usage: spider COMMAND"
  echo "where COMMAND is one of:"
  echo "  CLASSNAME         run the class named CLASSNAME"
  echo "Most commands print help when invoked w/o parameters."
  exit 1
fi

# get arguments
COMMAND=$1
shift

if [ ! -d "$BIGDATA_HOME/logs" ];then
   mkdir -p $BIGDATA_HOME/logs
fi

export HADOOP_USER_CLASSPATH_FIRST=true
BIGDATA_CLASSPATH=""

if [ -d "${BIGDATA_HOME}/resources" ];then
   BIGDATA_CLASSPATH=${BIGDATA_CLASSPATH}:${BIGDATA_HOME}/resource
fi

export HADOOP_CLASSPATH="${BIGDATA_CLASSPATH}"

# figure out which class to run
if [ "$COMMAND" = "generateHiveScript" ] ; then
  CLASS="com.sky.table.main.HiveTableTools"
elif [ "$COMMAND" = "importBdtqBySplit" ] ; then
   CLASS="com.sky.cib.bdtq.main.SplitLineImportMain"
else
  CLASS=$COMMAND
fi

$JAVA_HOME/bin/java $JAVA_OPTS $LIBRARY_PATH -cp $BIGDATA_CLASSPATH $CLASS $@