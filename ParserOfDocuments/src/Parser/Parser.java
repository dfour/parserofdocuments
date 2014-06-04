package Parser;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

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
 */
public class Parser implements ListSelectionListener,ActionListener  {
	
	private String fileName;
	private String searchTerm;
	private String result;
	private String resultFile = "";
	private String[] splitResultFile;
	private String currentSelectedFile = "";
	private int count = 0;
	private int countMatch =0;
	private boolean gui;
	private boolean isSeachTermSet = false;
	
	// GUI items
	private JFrame frame;
	private JList list;

	
	/*
	 * Constructor
	 */
	public Parser(){
		initialize();
	}
	
	// set up JFrame and menu etc
	private void initialize(){
		//Frame
		frame = new JFrame("Results for "+searchTerm+" in "+fileName+"        No:"+countMatch+" of "+count);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(100, 100, 1024, 720);
		
		//MENUS
			//FILE Menu
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		
		JMenuItem searchNewMenuItem = new JMenuItem("Search Files");
		searchNewMenuItem.setMnemonic(KeyEvent.VK_S);
		searchNewMenuItem.addActionListener(this);
		
		JMenuItem closeFilefileMenuItem = new JMenuItem("Close File");
		closeFilefileMenuItem.setMnemonic(KeyEvent.VK_C);
		closeFilefileMenuItem.addActionListener(this);
		
		JMenuItem exitAppMenuItem = new JMenuItem("Exit");
		exitAppMenuItem.setMnemonic(KeyEvent.VK_X);
		exitAppMenuItem.addActionListener(this);
		
			// Search Menu
		JMenu searchMenu = new JMenu("Search");
		searchMenu.setMnemonic(KeyEvent.VK_S);
		
		JMenuItem searchTerm = new JMenuItem("Set Search Term");
		searchTerm.setMnemonic(KeyEvent.VK_S);
		searchTerm.addActionListener(this);
		
		JMenuItem searchTermForget = new JMenuItem("Forget Search Term");
		searchTermForget.setMnemonic(KeyEvent.VK_F);
		searchTermForget.addActionListener(this);
		
			//DATA Menu
		JMenu dataMenu = new JMenu("Data");
		dataMenu.setMnemonic(KeyEvent.VK_D);
		
		JMenuItem openFile = new JMenuItem("Open File");
		openFile.setMnemonic(KeyEvent.VK_O);
		openFile.addActionListener(this);
		
		JMenuItem openFolder = new JMenuItem("Open Folder");
		openFolder.setMnemonic(KeyEvent.VK_F);
		openFolder.addActionListener(this);

	
		//set menu items
		menuBar.add(fileMenu);
		menuBar.add(searchMenu);
		menuBar.add(dataMenu);
		
		dataMenu.add(openFile);
		dataMenu.add(openFolder);
		
		searchMenu.add(searchTerm);
		searchMenu.add(searchTermForget);
		
		fileMenu.add(searchNewMenuItem);
		fileMenu.add(closeFilefileMenuItem);
		fileMenu.add(exitAppMenuItem);
		
		// add items to frame
		frame.setJMenuBar(menuBar);
		frame.setVisible(true);
	}
		
	/*
	 * Main Method
	 */
	public static void main (String[] args){
		
		if(args.length == 1 && args != null){ 
			System.out.println("Usage: parser.jar filelocation searchterm [gui:Yes/no]");
			System.exit(1);
		}else if(args.length == 2 && args != null){	
			new Parser(args[0],args[1],false);
		}else if(args.length == 3 && args != null){	
			if(args[2].equalsIgnoreCase("GUI") || args[2].equalsIgnoreCase( "Y") || args[2].equalsIgnoreCase( "YES")){
				new Parser(args[0],args[1],true);
			}else{
				new Parser(args[0],args[1],false);
			}
		}else{											// No args (Maybe executed from desktop so GUI it up
			new Parser();
		}
	}
	
	//create a file chooser for choosing files to search
	private File[] chooseFiles(){
		JFileChooser jfc = new JFileChooser("C:/eposhareddata/");
		jfc.setMultiSelectionEnabled(true);
		jfc.showOpenDialog(frame);
		return jfc.getSelectedFiles();
	}
	
	// gets the term to search for
	private String getSearchTerm(){
		if(!isSeachTermSet){
			searchTerm = (String)JOptionPane.showInputDialog(null,"Enter Search Term:","Search.",JOptionPane.PLAIN_MESSAGE,null,null,"");
		}
		return searchTerm;
	}
	
	// gets the data from the files and displays the results
	private void searchNewFiles(){
		File[] allFiles = chooseFiles();
		
		fileName = allFiles[0].getAbsolutePath();
		searchTerm = getSearchTerm();
		
		displayResults(parseMultiplefiles(allFiles));
		splitResultFile = resultFile.split(System.lineSeparator());
		
		frame.setTitle(fileName+" "+countMatch+" of "+count);
	}
	/*
	 * loops through files and returns the string of data
	 */
	private String parseMultiplefiles(File[] file){
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
	
	
	/*
	 * update listbox data
	 */
	private void displayResults(String results){
		String[] data = results.split(System.lineSeparator());
		System.out.println(data[1]);
		frame.getContentPane().removeAll();
		list = new JList(data);
		list.addListSelectionListener(this);
		JScrollPane jsp = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		frame.add(jsp);
		frame.revalidate();
		frame.repaint();
	}
	
	//clears screen ready for another file
	private void clearScreen(){
		frame.getContentPane().removeAll();
		frame.revalidate();
		frame.repaint();
	}
	
	// opens the file with the default program
	private void openFile(String fileLocation){
		try {
			Desktop.getDesktop().open(new File(fileLocation));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// Opens folder location
	private void openFolder(String fileLocation){
		String[] fileLocationParts = fileLocation.replace("\\", "/").split("/");
		String folderLocation = "";
		for(int i = 0; i < fileLocationParts.length -1; i++){
			folderLocation+= fileLocationParts[i]+"\\";
		}
		try {
			Desktop.getDesktop().open(new File(folderLocation));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if(!e.getValueIsAdjusting()){
			JList jlist = (JList) e.getSource();
			currentSelectedFile = splitResultFile[jlist.getSelectedIndex()];
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() instanceof JMenuItem){
			JMenuItem actionSource = (JMenuItem) e.getSource();
			switch(actionSource.getText()){
				case "Exit": 
					System.exit(0); 
					break;
				case "Search Files": 
					resultFile = "";
					clearScreen();
					searchNewFiles();
					break;
				case "Close File": 
					clearScreen();
					break;
				case "Set Search Term":
					getSearchTerm();
					this.isSeachTermSet = true;
					break;
				case "Forget Search Term":
					this.isSeachTermSet = false;
					break;
				case "Open File":
					openFile(currentSelectedFile);
					break;
				case "Open Folder":
					openFolder(currentSelectedFile);
					break;
			}
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
	public Parser(String filename,String searchterm, boolean gooey){
		fileName = filename;
		searchTerm = searchterm;
		gui = gooey;
		File[] files = new File[1];
		files[0] = new File(filename);
		if(gui){
			displayResults(parseMultiplefiles(files));
		}else{
			parseSingleFile(fileName);
		}
	}
}
