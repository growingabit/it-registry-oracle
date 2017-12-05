#!/bin/bash

cd /opt/spring-boot-app

./mvnw clean package -DexcludeDevtools=false

chown 1000:1000 -R /opt/spring-boot-app

# https://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#using-boot-devtools-remote
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5005 \
     -Xmx2048M \
     -Dfile.encoding=UTF8 \
     -jar /opt/spring-boot-app/target/it-registry-oracle-0.0.1.jar \
     --debug
# command little bit different then
# https://medium.com/@lhartikk/development-environment-in-spring-boot-with-docker-734ad6c50b34

cat /opt/spring-boot-app/logs/result.json | jq -c .