

@REM Execute goal "java" of plugin "exec", see also https://www.mojohaus.org/exec-maven-plugin/java-mojo.html
mvn exec:java -Dexec.mainClass=de.mide.kahoot.result2word.Main -Dexec.args="ExampleFiles/input_result1.xlsx"
