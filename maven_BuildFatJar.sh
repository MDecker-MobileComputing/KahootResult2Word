#!/bin/bash 

# Script to tell Maven to create a fat jar.
# A fat jar contains all dependencies needed for execution of the program.
# Result file will be written into folder target, e.g. target\result2word-1.0-SNAPSHOT.jar
# Start it with java -jar result2word-1.0-SNAPSHOT.jar

mvn package
