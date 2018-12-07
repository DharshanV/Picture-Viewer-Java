import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
//Hello there

import javax.imageio.ImageIO;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

enum PenType{
	BOX_SELECT,EYE_DROP,
	TEXT,MAGNIFIER,BRUSH,ERASER;
}

public class MainWindow extends JPanel {
	final static Dimension windowSize = new Dimension(800, 600);
	private PanelCenter panelCenter;
	private PanelWest panelWest;
	private PanelNorth panelNorth;
	private PanelEast panelEast;
	private JDesktopPane desktop;
	
	public MainWindow() {
		createComponent();
		wireComponent();
	}
	
	private void createComponent() {
		desktop = new JDesktopPane();
		panelCenter = new PanelCenter(desktop);
		panelWest = new PanelWest();
		panelEast = new PanelEast();
		panelNorth = new PanelNorth();
	}
	
	private class ButtonListiner implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			PenType temp = PenType.BOX_SELECT;
			if("EYE_DROP".equals(e.getActionCommand())) {
				temp = PenType.EYE_DROP;
				updateEditorColorPanel(panelWest.getActiveColorPanel());
			}
			else if("TEXT".equals(e.getActionCommand())) {
				temp = PenType.TEXT;
			}
			else if("MAGNIFIER".equals(e.getActionCommand())) {
				temp = PenType.MAGNIFIER;
			}
			else if("BRUSH".equals(e.getActionCommand())) {
				temp = PenType.BRUSH;
			}
			else if("ERASER".equals(e.getActionCommand())) {
				temp = PenType.ERASER;
			}
			updateEditorPen(temp);
		}
	}
	
	private class BrushSizeListiner implements ChangeListener{
		@Override
		public void stateChanged(ChangeEvent e) {
			JSlider slider = (JSlider)e.getSource();
			updateEditorBrushSize(slider.getValue());
		}
	}
	
	private class FileListiner implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			JMenuItem item = (JMenuItem)e.getSource();
			switch (item.getText()) {
			case "New":
				openPicture(false);
				break;
			case "Save":
				savePicture();
				break;
			case "Open":
				openPicture(true);
				break;
			default:
				break;
			}
		}
	}
	
	private class ColorSwitchListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			panelWest.switchColors();
			Color brushColor = panelWest.getActiveColorPanel().getBackground();
			panelEast.setColorsPanel(panelWest.getActiveColorPanel());
			updateEditorBrushColor(brushColor);
		}
		
	}
	
	private class ColorWheelListener implements MouseMotionListener{
		@Override
		public void mouseDragged(MouseEvent event) {
			ColorWheel colorWheel = (ColorWheel)event.getSource();
			if(colorWheel.isMouseInside(event.getX(),event.getY())) {
				Color color = colorWheel.getColor(event.getX(), event.getY());
				colorWheel.colorPanel().setBackground(color);
				updateEditorBrushColor(color);
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {}
	}

	private void updateEditorPen(PenType type) {
		JInternalFrame[] frames = desktop.getAllFrames();
		for(int i=0;i<frames.length;i++) {
			getPictureViewer(frames, i).setPenType(type);
		}
	}
	
	private void updateEditorBrushSize(int size) {
		JInternalFrame[] frames = desktop.getAllFrames();
		for(int i=0;i<frames.length;i++) {
			getPictureViewer(frames, i).setBrushSize(size);
		}
	}
	
	private void updateEditorBrushColor(Color color) {
		JInternalFrame[] frames = desktop.getAllFrames();
		for(int i=0;i<frames.length;i++) {
			getPictureViewer(frames, i).setBrushColor(color);
		}
	}
	
	private void updateEditorColorPanel(JPanel panel) {
		JInternalFrame[] frames = desktop.getAllFrames();
		for(int i=0;i<frames.length;i++) {
			getPictureViewer(frames, i).setColorsPanel(panel);
		}
	}
	
	private PictureViewer getPictureViewer(JInternalFrame[] frames,int i) {
		Container container = frames[i].getContentPane();
		JScrollPane pane = (JScrollPane)container.getComponents()[0];
		return (PictureViewer)pane.getViewport().getComponent(0);
	}
	
	private PictureViewer getActiveViewer() {
		Container container = desktop.getSelectedFrame().getContentPane();
		JScrollPane pane = (JScrollPane)container.getComponents()[0];
		return (PictureViewer)pane.getViewport().getComponent(0);
	}
	
	private void wireComponent() {
		this.setLayout(new BorderLayout());
		this.add(panelCenter, BorderLayout.CENTER);
		this.add(panelWest, BorderLayout.WEST);
		this.add(panelEast, BorderLayout.EAST);
		this.add(panelNorth, BorderLayout.NORTH);
		
		
		panelNorth.addFileListeners(new FileListiner());
		panelEast.setSliderListener(new BrushSizeListiner());
		panelEast.setColorWheelListener(new ColorWheelListener());
		panelWest.setSwitchListener(new ColorSwitchListener());
		panelWest.setButtonLisiter(new ButtonListiner());
		panelEast.setColorsPanel(panelWest.getActiveColorPanel());
	}
	
	private void savePicture() {
		JFileChooser fileChooser = new JFileChooser();
		File file = null;
		if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
		  file = fileChooser.getSelectedFile();
		}
		else {return;}
		PictureViewer viewer = getActiveViewer();
		BufferedImage viewerCanvas = viewer.getCanvas();
		BufferedImage bImg = new BufferedImage(viewerCanvas.getWidth(),
				viewer.getHeight(),BufferedImage.TYPE_INT_RGB);
		for(int i=0;i<viewerCanvas.getWidth();i++) {
			for(int j=0;j<viewerCanvas.getHeight();j++) {
				bImg.setRGB(i, j, viewerCanvas.getRGB(i, j));
			}
		}
	    try {
	    	ImageIO.write(bImg, "png", file);
	    } catch (IOException e1) {
	            e1.printStackTrace();
	    }
	}
	
	private void openPicture(boolean hasPic) {
		File file = null;
		if(hasPic == true) {
			JFileChooser fileChooser = new JFileChooser();
			if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
				  file = fileChooser.getSelectedFile();
			else {return;}
		}
		PictureViewer tempViewer = new PictureViewer(file);
		tempViewer.setBrushColor(panelWest.getActiveColorPanel().getBackground());
		panelCenter.addWindow(tempViewer);
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Photo Editor");
		frame.add(new MainWindow());
		frame.setPreferredSize(windowSize);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
}
