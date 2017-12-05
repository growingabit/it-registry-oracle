#!/bin/bash

cd /opt/spring-boot-app

./mvnw package -DexcludeDevtools=true

java -Xmx2048M -Dfile.encoding=UTF8 -jar /opt/spring-boot-app/target/it-registry-oracle-0.0.1.jar &

sleep 10

echo -e "\n"
curl -fsS --retry 10 http://localhost:8080/_ah/health

echo -e "\n"
curl -X POST -H "Accept: application/json" http://localhost:8080/_ah/envs