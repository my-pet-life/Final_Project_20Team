#!/usr/bin/env bash

REPOSITORY=/home/ubuntu/my-pet-life
PROJECT_NAME=my-pet-life

cd $REPOSITORY
cp $REPOSITORY/$PROJECT_NAME/build/libs/*.jar $REPOSITORY/

CURRENT_PID=$(pgrep -f ${PROJECT_NAME}.*.jar)

if [ -z $CURRENT_PID ]
then
  echo "> 종료할것 없음."
else
  echo "> kill -9 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

echo "> 새 애플리케이션 배포"
JAR_NAME=$(ls -tr $REPOSITORY/ | grep jar | tail -n 1)

echo "> Jar Name: $JAR_NAME"
nohup java -jar $REPOSITORY/$JAR_NAME 2>&1 &