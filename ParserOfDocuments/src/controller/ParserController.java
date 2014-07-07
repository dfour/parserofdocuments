package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import model.ParserModel;
import view.ParserView;

public class ParserController implements ListSelectionListener,ActionListener {

	private ParserModel model;
	private ParserView view;
	private SwingWorker<Integer, String> jsw;
		
	public ParserController(ParserModel mod, ParserView vw){
		this.view = vw;
		this.model = mod;
		setActionListeners();
	}
	
	/*
	 * Adds listeners to all the buttons in the view.
	 */
	public void setActionListeners(){
		view.addSearchTermListener(this);
		view.addCloseFilefileMenuItem(this);
		view.addExitAppMenuItem(this);
		view.addSearchNewMenuItem(this);
		view.addSearchTermForget(this);
		view.addOpenFile(this);
		view.addOpenFolder(this);
		view.addSearchTermRegex(this);
	}
	
	/*
	 * Adds a listener to the listbox in the view.
	 */
	public void setValueChangedListeners(){
		view.setListSelectionListener(this);
	}
	
	private void keepTitleUpdated(){
		jsw = new SwingWorker<Integer, String>(){
			private int currentCount = 0;
			@Override
		       public Integer doInBackground() {
				while(true){
					if(model.count != currentCount){
						view.updateTitle();
					}
				}
			}
		};
		jsw.execute();
	}
	
	// starts searching files for matches in the background.
	private void searchForFiles(){
		keepTitleUpdated();
		model.resultFile = "";
		view.clearScreen();
		if(view.getSearchTerm() != null){
			SwingWorker<Integer, String> jsw = new SwingWorker<Integer, String>(){
				@Override
			       public Integer doInBackground() {
			           model.searchNewFiles(view.chooseFiles());
			           return model.count;
			       }

			       @Override
			       protected void done() {
			    	   view.displayResults(model.result);
			    	   setValueChangedListeners();
			       }
			};
			jsw.execute();
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 * 
	 * Listenes to actions from the view.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() instanceof JMenuItem){
			JMenuItem actionSource = (JMenuItem) e.getSource();
			switch(actionSource.getText()){
				case "Exit": 
					System.exit(0); 
					break;
				case "Search Files": 
					searchForFiles();
					break;
				case "Close File": 
					view.clearScreen();
					break;
				case "Set Search Term":
					if(view.getSearchTerm() != null){
						model.isSearchTermSet = true;
					}
					break;
				case "Forget Search Term":
					model.isSearchTermSet = false;
					model.isSearchTermRegex = false;
					break;
				case "Open File":
					if(model.currentSelectedFile != null){
						model.openFile(model.currentSelectedFile);
					}
					break;
				case "Open Folder":
					if(model.currentSelectedFile != null){
						model.openFolder(model.currentSelectedFile);
					}
					break;
				case "Set Regex Term":
					if(view.getSearchTerm() != null){
						model.isSearchTermRegex = true;
					}
					break;
			}
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if(!e.getValueIsAdjusting()){
			JList jlist = (JList) e.getSource();
			model.currentSelectedFile = model.splitResultFile[jlist.getSelectedIndex()];
			System.out.println(model.currentSelectedFile);
		}
	}

}
