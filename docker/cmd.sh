#!/bin/bash

java -Xmx2048M -Dfile.encoding=UTF8 -jar /opt/srping-boot-app/target/it-registry-oracle-0.0.1.jar &

curl -fsS --retry 10 http://localhost:8080/_ah/health

curl -X POST -H "Accept: application/json" http://localhost:8080/_ah/health/dump-envs


