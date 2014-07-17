parserofdocuments
=================

Just a quick document scanner. Scans text documents and outputs each line that contains a match.

The parserofdocuments was originally created to search through a set of large CSV files. 
These files would usually contains between 10,000 and 500,000 lines of data.

This program has now been upgraded to be able to search through whole folders of text based documents and
display a list of each match found.

Basic list of features:
Search document for lines that match a set search term.
Search through multiple files within on folder.
Match strings based on either literal or regex matches.
Open files via the GUI.
Open folders via the GUI.
Run in command window with no GUI.


commandline examples:

Open GUI from commandline
    java -jar parser.jar
    
search file
    java -jar parser.jar "C:\csvfilename.CSV" searchTerm
    
