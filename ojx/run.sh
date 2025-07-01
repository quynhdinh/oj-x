#!/bin/bash
# This script is used to run the oj-x project.
mvn clean compile exec:java -Dexec.mainClass="org.ojx.Main"
