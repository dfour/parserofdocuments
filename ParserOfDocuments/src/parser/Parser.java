package parser;

import java.io.File;
import view.ParserView;

import model.ParserModel;

import controller.ParserController;

/*
 * 
 * The parsomatic 1001
 * Parses a file and searches for a term set in the arguments.
 * To be run from command line.
 * 
 * v0.1 created prototype. read a file for matches and output to console
 * v0.2 added ability to run in commandline with arguments
 * v0.3 added ability to run as stand alone app with GUI file chooser
 * v0.4 added optional GUI to command line output
 * v0.5 added gui output to stand alone version
 * v0.6 tidied up code a bit
 * v0.7 added ability to select multiple files in GUI version
 * v0.8 added scroll bars to GUI output for large outputs
 * v0.9 fixed bug that exponetially replicated data
 * v1.0 added full GUI with menu system, added ability to view another file after finshing with one.
 * v1.1 added ability to save search term for use on ultiple files.
 * v1.2 add ability to open file or folder of document match selected in list.
 * v1.2.1 fixed bug where close file wouldn't close file.
 * v2.0.0 Reworked whole project to MVC.
 * v2.0.1 Fixed bug where commadline search would turn up empty.
 * 
 */
public class Parser {

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
			}else{
				ParserModel m = new ParserModel();
				ParserView v = new ParserView(m);
				new ParserController(m,v);
				v.setVisible(true);
			}
		}else{
			ParserModel m = new ParserModel();
			ParserView v = new ParserView(m);
			new ParserController(m,v);
			v.setVisible(true);
		}
	}

	//========================================== Comman Line Section =======================================//

	/*
	 * Command Line Constructor
	 * 
	 * @param String filename : the name and location of teh file to be searched for
	 * @param String searchterm : the term being searched for
	 * @param boolean gooey : show gui 
	 */
	public Parser(String fn,String st){
		File[] files = new File[1];
		files[0] = new File(fn);
		ParserModel m = new ParserModel();
		m.parseSingleFile(fn,st);
	}
}