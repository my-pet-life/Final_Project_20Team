#!/usr/bin/env bash

REPOSITORY=/home/ubuntu
PROJECT_NAME=my-pet-life
CURRENT_PID=$(ps -ef | grep my-pet-life.*.jar | awk '{print $2}')

echo "> Build 파일 복사"
cd $REPOSITORY
cp $REPOSITORY/$PROJECT_NAME/build/libs/*.jar $REPOSITORY/

echo "> 현재 구동 중인 애플리케이션 pid: $CURRENT_PID"
if [ -z $CURRENT_PID ]
then
  echo "> 현재 구동 중인 애플리케이션 없음"
else
  echo "> kill -9 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

echo "> 새 애플리케이션 배포"
JAR_NAME=$(ls -tr $REPOSITORY/ | grep jar | tail -n 1)

echo "> Jar Name: $JAR_NAME"
echo "> $JAR_NAME에 실행권한 추가"
chmod +x $JAR_NAME

nohup java -jar $REPOSITORY/$JAR_NAME 1>/home/ubuntu/log/output.log 2>/home/ubuntu/log/error.log &