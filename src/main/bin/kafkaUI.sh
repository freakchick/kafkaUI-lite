#!/bin/sh

bool=false

while getopts d opt;
do
case $opt in
  d) echo "running in daemon"
    bool=true
    shift
    ;;
  ?) echo "$opt is an invalid option"
    ;;
esac
done


BIN_DIR=$(dirname $0)
BIN_DIR=$(
  cd "$BIN_DIR"
  pwd
)
# 安装包部署的目录
HOME=$BIN_DIR/..
PID=$BIN_DIR/server.pid

export CONF_DIR=$HOME/conf
export LIB_JARS=$HOME/lib/*
export LOG_DIR=$HOME/logs



if [ $1 = "start" ]; then
  if [ "$bool" = "false" ]; then
    java -Dlogging.file=$LOG_DIR/kafkaUI-lite.log -classpath $CONF_DIR:$LIB_JARS com.jq.kafkaui.KafkaUIApplication
  else
    nohup java -Dlogging.file=$LOG_DIR/kafkaUI-lite.log -classpath $CONF_DIR:$LIB_JARS com.jq.kafkaui.KafkaUIApplication >/dev/null &
    echo $! >$PID
  fi
elif [ $1 = "stop" ]; then
  TARGET_PID=$(cat $PID)
  kill $TARGET_PID

else
  echo "parameter invalid"
fi
