#!/bin/sh

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

  nohup java -Dlogging.file=$LOG_DIR/kafkaUI-lite.log -classpath $CONF_DIR:$LIB_JARS com.jq.kafkaui.KafkaUIApplication >/dev/null 2>&1 &
  echo $! >$PID

elif [ $1 = "stop" ]; then
  TARGET_PID=$(cat $PID)
  kill $TARGET_PID

else
  echo "parameter invalid"
fi
