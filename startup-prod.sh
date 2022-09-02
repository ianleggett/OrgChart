#!/bin/sh
cd ~/p
echo "Running Site...(ctrl-c to stop)"
mvn spring-boot:run -Dspring-boot.run.profiles=prod -DskipTest=true -Drun.jvmArguments="-Xmx512m" >> ~/logs/prod-run-$(date +"%Y%m%d-%H%M%S").log