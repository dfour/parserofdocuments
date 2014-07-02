package parser;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ParserModel {
	
	public boolean isSearchTermSet = false;
	public String searchTerm = "Find Me";
	public String fileName = "";
	public String[] splitResultFile;
	private String result;
	public String resultFile;
	public int count;
	public int countMatch;
	public String currentSelectedFile;
			
	
	public ParserModel(){
	}
	
	// gets the data from the files
	public void searchNewFiles(File[] allFiles){
		fileName = allFiles[0].getAbsolutePath();
		resultFile = parseMultiplefiles(allFiles);
		splitResultFile = resultFile.split(System.lineSeparator());
	}
	
	
	/*
	 * loops through files and returns the string of data
	 */
	public String parseMultiplefiles(File[] file){
		String data = "";
		try {
			for(int i =0;i < file.length; i++){
				data += parseFile(file[i].getPath(),searchTerm);
			}
		} catch(FileNotFoundException fnf){
			System.out.println("Unable to find file. Please check you have entered the correct location.");
		}catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(data);
		return data;
	}
	
	// Reads data from file and searches for match on each line
	private String parseFile(String filename, String term) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String filler = "========================================================================";
		result = filename+System.lineSeparator()+filler+System.lineSeparator();
		resultFile +=filename+System.lineSeparator();
		resultFile +=filename+System.lineSeparator();

		try {
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();
	        while (line != null) {
	        	count++;
	        	if(line.contains(term)){
	        		countMatch++;
	        		sb.append(line);
	        		sb.append(System.lineSeparator());
	        		resultFile += filename+System.lineSeparator();
	        	}
	           	line = br.readLine();
	        }
	        result+= sb.toString();

	    } finally {
	        br.close();
	    }
	    return result;
	}
		
	// opens the file with the default program
	public void openFile(String fileLocation){
		try {
			Desktop.getDesktop().open(new File(fileLocation));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Opens folder location
	public void openFolder(String fileLocation){
		System.out.println(fileLocation);
		String[] fileLocationParts = fileLocation.replace("\\", "/").split("/");
		String folderLocation = "";
		for(int i = 0; i < fileLocationParts.length -1; i++){
			folderLocation+= fileLocationParts[i]+"\\";
		}
		try {
			System.out.println(folderLocation);
			Desktop.getDesktop().open(new File(folderLocation));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
