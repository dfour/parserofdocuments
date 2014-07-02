package parser;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ParserController implements ListSelectionListener,ActionListener {

	private ParserModel model;
	private ParserView view;
		
	public ParserController(ParserModel mod, ParserView vw){
		this.view = vw;
		this.model = mod;
		setActionListeners();
	}
	
	public void setActionListeners(){
		view.addSearchTermListener(this);
		view.addCloseFilefileMenuItem(this);
		view.addExitAppMenuItem(this);
		view.addSearchNewMenuItem(this);
		view.addSearchTermForget(this);
		view.addOpenFile(this);
		view.addopenFolder(this);
	}
	
	public void setValueChangedListeners(){
		view.setListSelectionListener(this);
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
					model.resultFile = "";
					view.clearScreen();
					if(view.getSearchTerm() != null){
						model.searchNewFiles(view.chooseFiles());
						view.displayResults(model.resultFile);
						setValueChangedListeners();
					}
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
