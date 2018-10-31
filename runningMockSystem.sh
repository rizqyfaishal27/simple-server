#!/bin/bash

sudo kill -9 `ps -ef | grep java | awk '{ printf $2; printf " "}'`
export JAVA_ENV=production

gradle build
gradle installDist


for i in 1 2 3 4
do
	touch db$i.sqlite
	nohup ./build/install/simple-server/bin/simple-server 1200$i ./served-directory 10 db$i.sqlite localhost:1200$i http://localhost:12001/list.json 1>log$i.log 2>loh-error$i.log &
done

