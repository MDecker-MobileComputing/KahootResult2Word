# Kahoot: Result Excel to Word #

This repository contains a simple Java program that processes Excel files with the results from a 
[Kahoot](https://kahoot.com/) game and writes them into a Word file.

See [here](https://support.kahoot.com/hc/en-us/articles/115002308028-Reports-and-the-Reports-page) on how to download the Excel file with the results from a Kahoot game.
For an example of such an Excel file you can look at the files in folder [ExampleFiles](ExampleFiles/),

The author of this application is *NOT* related in any way to [Kahoot](https://kahoot.com/company/).

The Java program in this repository is in the form of a [Maven](http://maven.apache.org/) project for the [Eclipse IDE](https://www.eclipse.org).

<br>

----
## Usage ##

Maven call to process one Excel file:
````
mvn exec:java -Dexec.mainClass=de.mide.kahoot.result2word.Main -Dexec.args="-infile path/to/file/result_downloaded_from_kahoot.xlsx"
````

<br>

----
## License ##

This project is licensed under the terms of the GNU GENERAL PUBLIC LICENSE version 3 (GPL v3), 
see also  the [LICENSE file](LICENSE.md).

<br>
