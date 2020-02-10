# Kahoot: Result Excel to Word #

This repository contains a simple Java program that processes an *Excel file (xlsx)* with the results from a
[Kahoot](https://kahoot.com/) game and writes them into a *Word file (docx)*. Only the questions and answers will be 
included in the generated file (but not the number of students which gave the right answer), so it can be 
provided as "Sample Solution" to students/players afterwards.

See [here](https://support.kahoot.com/hc/en-us/articles/115002308028-Reports-and-the-Reports-page) on how to download the Excel file with the results from a Kahoot game.
For an example of such an Excel file you can look at the files in folder [ExampleFiles](ExampleFiles/).
To get such an result file it is sufficient to play the game with yourself using Kahoot's [Preview feature](https://support.kahoot.com/hc/en-us/articles/115003173007-How-can-I-preview-play-a-kahoot-alone-).

**The author of this application is *NOT* related in any way to [Kahoot](https://kahoot.com/company/).**

The Java program in this repository is in the form of a [Maven](http://maven.apache.org/) project for the [Eclipse IDE](https://www.eclipse.org).

<br>

----
## Building the *Fat Jar* ##

* Build the *Fat Jar* (which contains all dependencies) with the following execution of Maven: `mvn package`

* After this command you should find a file named `kahoot_result2word-<version>-SNAPSHOT-jar-with-dependencies.jar` in folder `target/`.

<br>

## Using the Fat Jar ##

When you have built the *Fat Jar*, then you can process one input file like shown in the following example command:

````
java -jar target/kahoot_result2word-1.0-SNAPSHOT-jar-with-dependencies.jar -f ExampleFiles/input_result_1.xlsx
````

<br>

To process all files in a particular folder:
````
java -jar target/kahoot_result2word-1.0-SNAPSHOT-jar-with-dependencies.jar -i ExampleFiles
````

<br>

Show all command line options:
````
java -jar target/kahoot_result2word-1.0-SNAPSHOT-jar-with-dependencies.jar -h
````

<br>

## Execution via Maven ##

It is also possible to execute the program via [Maven's Exec plugin](https://www.mojohaus.org/exec-maven-plugin/):

Process one input file:
````
mvn exec:java -Dexec.mainClass=de.mide.kahoot.result2word.Main -Dexec.args="-infile path/to/file/result_downloaded_from_kahoot.xlsx"
````

<br>

Process all files in a folder:
````
mvn exec:java -Dexec.mainClass=de.mide.kahoot.result2word.Main -Dexec.args="-infolder path/to/folder/"
````

<br>

----
## Command Line Options ##

````
 -f,--infile <file>        Single Excel file to be processed, not compatible with -i
                           
 -h,--help                 Show this help
 
 -i,--infolder <folder>    Folder from which input files (xlsx) are to be read; not compatible with -f
                           
 -l,--locale <locale>      Set language to be used for output files, e.g. "en" for English or "de" for German; default value is "en" for English                           
                           
 -o,--outfolder <folder>   Folder into which output files (docx) are to be written
                           
 -t,--topline <text>       Set text for topline (header) on each page of the generated docx file.
````

<br>

----
## License ##

This project is licensed under the terms of the GNU GENERAL PUBLIC LICENSE version 3 (GPL v3),
see also  the [LICENSE file](LICENSE.md).

<br>
