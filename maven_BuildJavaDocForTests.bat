
@REM Script to tell Maven to build the JavaDoc for the source code in folder src/test/java.
@REM JavaDoc content is written into folder target/site/testapidocs 

@REM Execute goal "test-javadoc" of plugin "javadoc", see also http://maven.apache.org/plugins/maven-javadoc-plugin/test-javadoc-mojo.html
mvn javadoc:test-javadoc
