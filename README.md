# MS3_CodingChallenge
## Installation
There must be three files in the specified directory:
1. MS3_CodingChallenge.java
2. sqlite-jdbc-3.20.0.jar
3. ms3Interview.csv

Once all three files are downloaded open the system's command prompt and compile the program with:
* javac MS3_CodingChallenge.java

On the command prompt, run the program with:
* java -classpath ".:sqlite-jdbc-3.20.0.jar" MS3_CodingChallenge

It is important that when running the program the sqlite-jdbc library is compiled along with the program classpath with the proper syntax as demonstrated above. The sqlite-jdbc library is a core feature in this program and it will not run without the specified classpath. It is also vital that all files mentioned above are in the same directory when compiling the program to avoid unecessary classpath errors.

## Input
There is only one input for the program and it must be in the specified directory:
1. ms3Interview.csv

Once the program is compiled and executed, it will prompt for a filename to execute the required functionalities. Make sure the filename entered includes the <.csv> extension. If not, the program will prompt the user to do so.

## Output
There will be three additional files created in the specified directory:
1. ms3Interview.db
2. ms3Interview.log OR ms3Interview.txt (the program outputs the file as ms3Interview.log but some kernels will save the file as ms3Interview.txt which is an alternative way to read a file with a <.log> extension)
3. ms3Interview-bad.csv

ms3Interview.db stores the valid entries from the original CSV file, ms3Interview.log/ms3Interview.txt stores the statistics of the entries and finally ms3Interview-bad.csv stores the invalid entries from the original CSV file.

## Challenges
1. Finding an efficient way to parse the large CSV file. It would be optimal to store the entries in an array however it is not recommended to create an array without knowing how many entries there are. To solve this problem, the entries were first stored in a list and then transferred to a 2D-array.
2. Parsing the CSV file was not as easy as I thought it would be. This is because one of the columns always had a comma within the text. This was a problem because I used a comma delimiter to parse the file. In order to overcome this issue I used a regex command that basically meant: "Parse the contents if and only if there is a comma which is not followed by "iVBOR". It took me awhile to figure out the correct syntax of the regex expression as I have not used regex expressions in awhile but eventually I figured it out.
3. Finding an efficient way to store valid entries into the SQLite  database. Initially the entires were written to the database all at once in a for loop, however this method took about ~30 seconds to complete. To solve this problem I stored the entries to the database in segments through the 'addBatch()' method which significantly decreased the runtime.

##Additional Notes
1. I did not include the output files in this directory to avoid conflicts when running the program. Some system processors will not allow Java while another file with the same filename in the same directory to be created or overwritten. Therefore only the three files needed to execute the program completely are included in this directory.
2. I have written comments in my code to explain my thought processes and to simplify code assessment.
