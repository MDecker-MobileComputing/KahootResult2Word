@REM Check if there are newer versions available for any of the dependencies defined in the pom.xml.
@REM Docu: https://www.mojohaus.org/versions-maven-plugin/examples/display-dependency-updates.html

@REM Execute goal "display-dependency-updates" of plugin "versions"
@REM https://www.mojohaus.org/versions-maven-plugin/display-dependency-updates-mojo.html
mvn versions:display-dependency-updates
