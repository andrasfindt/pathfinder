#!/usr/bin/env bash
mvn clean install && cd mazesolver-ui/ && mvn exec:java
