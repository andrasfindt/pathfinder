#!/usr/bin/env bash
mvn clean install && cd pathfinder-ui/ && mvn exec:java
