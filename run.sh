#!/bin/sh

# kill java
kill -9 `ps -ef | grep java | awk '{print $2}'`

# restart java
cd /home/ubuntu/pull-up
nohup java -jar pull-up-backend.jar > ./log/log.out 2> ./log/log.err &

# exit
exit
