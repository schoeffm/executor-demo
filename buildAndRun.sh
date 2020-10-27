#!/bin/sh
mvn clean package && docker build -t org.example/payaraexecutordemo .
docker rm -f payaraexecutordemo || true && docker run -d -p 8080:8080 -p 4848:4848 --name payaraexecutordemo org.example/payaraexecutordemo 
