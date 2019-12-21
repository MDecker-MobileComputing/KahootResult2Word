
@REM Script to tell Maven to create a fat jar.
@REM A fat jar contains all dependencies needed for execution of the program.
@REM Result file will be written into folder target, e.g. target\result2word-1.0-SNAPSHOT.jar
@REM Start it with java -jar result2word-1.0-SNAPSHOT.jar

mvn package
