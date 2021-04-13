@echo off
set home=%~dp0

set conf_dir=%home%..\conf
set lib_dir=%home%..\lib\*
set log_dir=%home%..\logs


java -Dlogging.file=%log_dir%\kafkaUI-lite.log -classpath %conf_dir%;%lib_dir% com.jq.kafkaui.KafkaUIApplication
pause