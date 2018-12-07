import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class PanelCenter extends JPanel {
	private JDesktopPane desktop;
	
	/**
	 * This object handles
	 * creating multiple 
	 * picture viewer windows.
	 * @param desktopPane - desktop for
	 * other fames.
	 */
	public PanelCenter(JDesktopPane desktopPane) {
		desktop = desktopPane;
		createComponent();
		wireComponent();
	}
	
	/**
	 * This function creates
	 * the desktop and
	 * assigns it background color.
	 */
	private void createComponent() {
		desktop.setBackground(getBackground());
	}
	
	/**
	 * This function wires
	 * the panel and adds
	 * the desktop pane.
	 */
	private void wireComponent() {
		this.setLayout(new BorderLayout());
		this.add(desktop,BorderLayout.CENTER);
	}
	
	/**
	 * This function adds a new frame
	 * with the picture viewer
	 * in the desktop.
	 * @param pViewer - a picture viewer
	 */
	public void addWindow(PictureViewer pViewer) {
	    JInternalFrame internalFrame = new JInternalFrame(pViewer.getPictureName(),
	    								true, true, true, true);
	    JScrollPane pane = new JScrollPane(pViewer);
	    pane.getVerticalScrollBar().setUnitIncrement(16);
	    pViewer.setPreferredSize(new Dimension(pViewer.getImageWidth(), 
	    							pViewer.getImageHeight()));
	    internalFrame.setVisible(true);
	    internalFrame.setSize(300,300);
	    internalFrame.add(pane);
	    desktop.add(internalFrame);
	}
}
