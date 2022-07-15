#!/bin/bash

echo

# Execute goal "java" of plugin "exec", see also https://www.mojohaus.org/exec-maven-plugin/java-mojo.html
mvn exec:java -Dexec.mainClass=de.mide.kahoot.result2word.Main -Dexec.args="-infile ExampleFiles/input_result_1.xlsx --newpage"

echo
