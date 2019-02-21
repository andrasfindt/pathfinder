#!/usr/bin/env bash

DEBUG=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000
source_home=/Users/andrasfindt/Development/zyx.andrast/genetic-algorithm-tutorial/mazesolver-ui
mvn_exec=/usr/local/Cellar/maven@3.3/3.3.9/libexec
mvn_conf=/usr/local/Cellar/maven@3.3/3.3.9/libexec/bin/m2.conf
mvn_classpath="/usr/local/Cellar/maven@3.3/3.3.9/libexec/boot/plexus-classworlds-2.5.2.jar org.codehaus.classworlds.Launcher"
java -Dmaven.multiModuleProjectDirectory=${source_home} ${DEBUG} -Dmaven.home=${mvn_exec} -Dclassworlds.conf=${mvn_conf} -Dfile.encoding=UTF-8 -classpath ${mvn_classpath} clean compile exec:java > rand0_0.05_1000.txt