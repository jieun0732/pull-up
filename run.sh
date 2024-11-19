#!/bin/sh

# kill java
kill -9 `ps -ef | grep java | awk '{print $2}'`

# restart java
cd /home/ubuntu/pull-up
nohup java -Dspring.profiles.active=prod -jar pull-up-backend.jar > ./log/prod/log.out 2> ./log/prod/log.err &
nohup java -Dspring.profiles.active=dev -jar pull-up-backend.jar > ./log/dev/log.out 2> ./log/dev/log.out &

# exit
exit
