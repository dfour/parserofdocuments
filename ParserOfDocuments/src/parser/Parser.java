package parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import view.ParserView;

import model.ParserModel;

import controller.ParserController;

/*
 * 
 * The Parsomatic 1001
 * Parses a file and searches for a term set in the arguments.
 * To be run from command line.
 * 
 * v0.1 created prototype. read a file for matches and output to console
 * v0.2 added ability to run in command line with arguments
 * v0.3 added ability to run as stand alone app with GUI file chooser
 * v0.4 added optional GUI to command line output
 * v0.5 added GUI output to stand alone version
 * v0.6 tidied up code a bit
 * v0.7 added ability to select multiple files in GUI version
 * v0.8 added scroll bars to GUI output for large outputs
 * v0.9 fixed bug that exponentially replicated data
 * v1.0 added full GUI with menu system, added ability to view another file after finishing with one.
 * v1.1 added ability to save search term for use on multiple files.
 * v1.2 add ability to open file or folder of document match selected in list.
 * v1.2.1 fixed bug where close file wouldn't close file.
 * v2.0.0 Reworked whole project to MVC.
 */
public class Parser {

	private String fileName;
	private String searchTerm;
	private String result;
	/*
	 * Main Method
	 */
	public static void main (String[] args){

		if(args.length == 1 && args != null){ 
			System.out.println("Usage: parser.jar filelocation searchterm [gui:Yes/no]");
			System.exit(1);
		}else if(args.length == 2 && args != null){	
			new Parser(args[0],args[1]);
		}else if(args.length == 3 && args != null){	
			if(!args[2].equalsIgnoreCase("GUI") && !args[2].equalsIgnoreCase( "Y") && !args[2].equalsIgnoreCase( "YES")){
				new Parser(args[0],args[1]);
			}
		}
		//new Parser();
		ParserModel model = new ParserModel();
		ParserView view = new ParserView(model);
		new ParserController(model,view);
		view.setVisible(true);
	}

	// loops through files and returns the string of data
	private String parseSingleFile(String file){
		String data = "";
		try {
			data = parseFile(file,searchTerm);
			System.out.println(data);
		} catch(FileNotFoundException fnf){
			System.out.println("Unable to find file. Please check you have entered the correct location.");
		}catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}

	// Reads data from file and searches for match on each line
	private String parseFile(String filename, String term) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String filler = "========================================================================";
		result = filename+System.lineSeparator()+filler+System.lineSeparator();
		try {
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();
	        while (line != null) {
	        	if(line.contains(term)){
	        		sb.append(line);
	        		sb.append(System.lineSeparator());
	        	}
	           	line = br.readLine();
	        }
	        result+= sb.toString();
	    } finally {
	        br.close();
	    }
	    return result;
	}


	//========================================== Comman Line Section =======================================//

	/*
	 * Command Line Constructor
	 * 
	 * @param String filename : the name and location of teh file to be searched for
	 * @param String searchterm : the term being searched for
	 * @param boolean gooey : show gui 
	 */
	public Parser(String filename,String searchterm){
		fileName = filename;
		searchTerm = searchterm;
		File[] files = new File[1];
		files[0] = new File(filename);
		parseSingleFile(fileName);
	}
}