#!/bin/sh

export GOOGLE_MAP_API_KEY=XYZ
mvn package && java -jar target/vrp-0.0.1-SNAPSHOT.jar
