#!/bin/sh

export GOOGLE_MAPS_API_KEY=XYZ
source ~/.env
mvn package && java -jar target/vrp-0.0.1-SNAPSHOT.jar
