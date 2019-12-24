#!/bin/bash

# Check if there are newer versions available for any of the dependencies defined in the pom.xml

# Execute goal "display-dependency-updates" of plugin "versions"
mvn versions:display-dependency-updates


