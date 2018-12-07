import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class PanelNorth extends JPanel {
	final private int ITEM_COUNT = 3;
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenuItem[] items;
	
	/**
	 * This is a constructor
	 * that takes care of menu
	 * items.
	 */
	public PanelNorth() {
		createComponent();
		wireComponent();
	}
	
	/**
	 * This function creates all
	 * the menu items.
	 */
	private void createComponent() {
		menuBar = new JMenuBar();
		items = new JMenuItem[ITEM_COUNT];
		fileMenu = new JMenu("File");
		items[0] = new JMenuItem("New");
		items[1] = new JMenuItem("Save");
		items[2] = new JMenuItem("Open");
	}
	
    /**
     * This function wires the menu
     * items.
     */
	private void wireComponent() {
		this.setLayout(new BorderLayout());
		this.setBackground(getBackground());
		for (JMenuItem item : items) {
			fileMenu.add(item);
		}
	    menuBar.add(fileMenu);
	    this.add(menuBar,BorderLayout.NORTH);
	}
	
	/**
	 * This function sets the listener for 
	 * save menu item.
	 * @param listener - save listener
	 */
	public void addFileListeners(ActionListener listener) {
		for (JMenuItem item : items) {
			item.addActionListener(listener);
		}
	}

}
