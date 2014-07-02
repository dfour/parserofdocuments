package parser;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionListener;

public class ParserView extends JFrame{
	
	private static final long serialVersionUID = 1L;
	
	//CORE
	private ParserModel model;
		
	//GUI Items
	private JMenuItem searchTerm;
	private JMenuItem closeFilefileMenuItem;
	private JMenuItem exitAppMenuItem;
	private JMenuItem searchNewMenuItem;
	private JMenuItem searchTermForget;
	private JMenuItem openFile;
	private JMenuItem openFolder;
	private JList list;
	
	public ParserView(ParserModel mod){
		super("Results for "+mod.searchTerm+" in "+mod.fileName+"        No:0 of 0");
		model = mod;
		initialize();
	}

	private void initialize(){
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(100, 100, 1024, 720);

		//MENUS
			//FILE Menu
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);

		searchNewMenuItem = new JMenuItem("Search Files");
		searchNewMenuItem.setMnemonic(KeyEvent.VK_S);
		closeFilefileMenuItem = new JMenuItem("Close File");
		closeFilefileMenuItem.setMnemonic(KeyEvent.VK_C);
		exitAppMenuItem = new JMenuItem("Exit");
		exitAppMenuItem.setMnemonic(KeyEvent.VK_X);
		
			// Search Menu
		JMenu searchMenu = new JMenu("Search");
		searchMenu.setMnemonic(KeyEvent.VK_S);

		searchTerm = new JMenuItem("Set Search Term");
		searchTerm.setMnemonic(KeyEvent.VK_S);
		searchTermForget = new JMenuItem("Forget Search Term");
		searchTermForget.setMnemonic(KeyEvent.VK_F);
		

			//DATA Menu
		JMenu dataMenu = new JMenu("Data");
		dataMenu.setMnemonic(KeyEvent.VK_D);

		openFile = new JMenuItem("Open File");
		openFile.setMnemonic(KeyEvent.VK_O);
		openFolder = new JMenuItem("Open Folder");
		openFolder.setMnemonic(KeyEvent.VK_F);
		


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
		this.setJMenuBar(menuBar);
	}
	
	//create a file chooser for choosing files to search
	public File[] chooseFiles(){
		JFileChooser jfc = new JFileChooser("C:/eposhareddata/");
		jfc.setMultiSelectionEnabled(true);
		jfc.showOpenDialog(this);
		return jfc.getSelectedFiles();
	}
	
	// gets the term to search for
	public String getSearchTerm(){
		if(!model.isSearchTermSet){
			model.searchTerm = (String)JOptionPane.showInputDialog(null,"Enter Search Term:","Search.",JOptionPane.PLAIN_MESSAGE,null,null,"");
		}
		System.out.println(model.searchTerm);
		return model.searchTerm;
	}
	
	
	/*
	 * update listbox data
	 */
	public void displayResults(String results){
		String[] data = results.split(System.lineSeparator());
		System.out.println(data[1]);
		this.getContentPane().removeAll();
		list = new JList(data);
		JScrollPane jsp = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.add(jsp);
		this.revalidate();
		this.repaint();
	}
	
	//clears screen ready for another file
	public void clearScreen(){
		this.getContentPane().removeAll();
		this.revalidate();
		this.repaint();
	}
	
	public void updateTitle(){
		this.setTitle(model.fileName+" "+model.countMatch+" of "+model.count);
	}

	

	public void addSearchTermListener(ActionListener al){
		searchTerm.addActionListener(al);
	}
	public void addCloseFilefileMenuItem(ActionListener al){
		closeFilefileMenuItem.addActionListener(al);
	}
	public void addExitAppMenuItem(ActionListener al){
		exitAppMenuItem.addActionListener(al);
	}
	public void addSearchNewMenuItem(ActionListener al){
		searchNewMenuItem.addActionListener(al);
	}
	public void addSearchTermForget(ActionListener al){
		searchTermForget.addActionListener(al);
	}
	public void addOpenFile(ActionListener al){
		searchTerm.addActionListener(al);
	}
	public void addopenFolder(ActionListener al){
		openFolder.addActionListener(al);
	}
	
	public void setListSelectionListener(ListSelectionListener listener){
		list.addListSelectionListener(listener);
	}
	
	
}
