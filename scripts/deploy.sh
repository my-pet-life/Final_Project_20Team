#!/usr/bin/env bash

REPOSITORY=/home/ubuntu/my-pet-life
PROJECT_NAME=my-pet-life

cd $REPOSITORY
cp $REPOSITORY/$PROJECT_NAME/build/libs/*.jar $REPOSITORY/

#APP_NAME=my-pet-life
#JAR_NAME=$(ls $REPOSITORY/build/libs/ | grep 'SNAPSHOT.jar' | tail -n 1)
#JAR_PATH=$REPOSITORY/build/libs/$JAR_NAME

#CURRENT_PID=$(pgrep -f $APP_NAME)
CURRENT_PID=$(pgrep -f ${PROJECT_NAME}.*.jar)

if [ -z $CURRENT_PID ]
then
  echo "> 종료할것 없음."
else
  echo "> kill -9 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

echo "> $JAR_PATH 배포"
nohup java -jar $JAR_PATH > /dev/null 2> /dev/null < /dev/null &