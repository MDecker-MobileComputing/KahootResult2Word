
@REM Script to tell Maven to build the JavaDoc for the source code in folder src/main/java.
@REM JavaDoc content is written into folder target/site/apidocs 

@REM Execute goal "javadoc" of plugin "javadoc", see also http://maven.apache.org/plugins/maven-javadoc-plugin/javadoc-mojo.html
mvn javadoc:javadoc
