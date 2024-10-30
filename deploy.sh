#!/bin/bash

cd ./backend

./gradlew build

# build condition
if [ $? -eq 1 ]; then
    echo "gradle build failed, exiting."
    exit 1
fi

# move jar file
mv ./build/libs/pull-up-backend.jar ~/.ssh/pull-up-backend.jar

# scp jar file
cd ~/.ssh
scp -i pull-up.pem pull-up-backend.jar ubuntu@pullup-api.shop:/home/ubuntu/pull-up/

# run server script
ssh -tt -i pull-up.pem ubuntu@pullup-api.shop < ~/pull-up/git/run.sh
