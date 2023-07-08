#!/bin/bash

export AD_DB_HOST=127.0.0.1
export AD_DB_PORT=3306
export AD_DB_NAME=askdata_dev
export AD_DB_USER=askdata_user
export AD_DB_PASS=askdata624@MySQL

# mvn -DskipTests clean package
ps -ef | grep 'askdata-backend' | grep -v grep | awk '{print $2}' | xargs kill -9
rm -frv askdata.log
nohup java -jar target/askdata-backend-1.0-SNAPSHOT.jar >> askdata.log 2>&1 &
sleep 1
tail -fn 200 askdata.log