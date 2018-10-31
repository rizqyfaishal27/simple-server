#!/bin/bash
export JAVA_ENV=production
gradle buildDist

for i in 1 2 3 4 5 6
do
	touch db$i.sqlite
	nohup ./build/scripts/simple-server 1200$i ./served-directory 10 db$i.sqlite localhost:1200$i localhost:12001/list.json
done
