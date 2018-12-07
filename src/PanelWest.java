import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

class ToolsOption extends JPanel{
	private final int NUM_OF_BUTTONS = 6;
	private final int ICON_SIZE = 35;
	private static Dimension buttonSize = new Dimension(45,45);
	private JButton[] toolsButton;
	private ImageIcon boxSelectIcon = new ImageIcon("box_select.png");
	private ImageIcon eyeDropIcon = new ImageIcon("eye_drop.png");
	private ImageIcon textIcon = new ImageIcon("text.png");
	private ImageIcon magnifierIcon = new ImageIcon("magnifier.png");
	private ImageIcon brushIcon = new ImageIcon("brush.png");
	private ImageIcon eraserIcon = new ImageIcon("eraser.png");
	
	/**
	 * This object takes care of
	 * all the button tools available 
	 * for the user. 
	 */
	public ToolsOption() {
		createComponent();
		wireComponent();
	}
	
	/**
	 * This function creates the buttons
	 * and assigns the buttons an icon
	 * for there type. 
	 */
	void createComponent() {
		toolsButton = new JButton[NUM_OF_BUTTONS];
		boxSelectIcon = createIcon(boxSelectIcon.getImage(),ICON_SIZE);
		eyeDropIcon = createIcon(eyeDropIcon.getImage(),ICON_SIZE);
		textIcon = createIcon(textIcon.getImage(),ICON_SIZE);
		magnifierIcon = createIcon(magnifierIcon.getImage(),ICON_SIZE);
		brushIcon = createIcon(brushIcon.getImage(),ICON_SIZE);
		eraserIcon = createIcon(eraserIcon.getImage(),ICON_SIZE);
		for(int i=0;i<NUM_OF_BUTTONS;i++) {
			toolsButton[i] = new JButton(); 
		}
	}
	
	/**
	 * This function wires and sets the 
	 * button action command for inputs.
	 */
	void wireComponent() {
		this.setMaximumSize( this.getPreferredSize() );
		this.setLayout(new GridLayout(NUM_OF_BUTTONS,1));
		for(int i=0;i<NUM_OF_BUTTONS;i++) {
			toolsButton[i].setPreferredSize(buttonSize);
			this.add(toolsButton[i]);
		}
		
		toolsButton[0].setActionCommand("MAGNIFIER");
		toolsButton[0].setIcon(magnifierIcon);
		
		toolsButton[1].setActionCommand("BOX_SELECT");
		toolsButton[1].setIcon(boxSelectIcon);
		
		toolsButton[2].setActionCommand("EYE_DROP");
		toolsButton[2].setIcon(eyeDropIcon);
		
		toolsButton[3].setActionCommand("TEXT");
		toolsButton[3].setIcon(textIcon);
		
		toolsButton[4].setActionCommand("BRUSH");
		toolsButton[4].setIcon(brushIcon);
		
		toolsButton[5].setActionCommand("ERASER");
		toolsButton[5].setIcon(eraserIcon);
	}
	
	/**
	 * This function scales down 
	 * the tool button icon and assigns
	 * the scaled down image
	 * @param srcImg - original image
	 * @param w - desired width
	 * @param h - desired height
	 * @return scaled down image.
	 */
	private Image getScaledImage(Image srcImg, int w, int h){
	    BufferedImage resizedImg = new BufferedImage(w, h, 
	    							BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2 = resizedImg.createGraphics();

	    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, 
	    					RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g2.drawImage(srcImg, 0, 0, w, h, null);
	    g2.dispose();

	    return resizedImg;
	}
	
	/**
	 * This function creates an the
	 * tool button icon.
	 * @param image - source image
	 * @param width - desired width
	 * @return an image icon
	 */
	private ImageIcon createIcon(Image image,int width) {
		return new ImageIcon(getScaledImage(image, width,width));
	}
	
	/**
	 * This function gets all the
	 * tool buttons.
	 * @return toolsButton
	 */
	public JButton[] getButtons() {
		return toolsButton;
	}
}

class ColorHistory extends JPanel{
    final private int WIDTH = 75;
    final private int HEIGHT= 90;
    private JLayeredPane layeredPane;
    private JPanel panelColor1;
    private JPanel panelColor2;
    
    /**
     * This object handles the color
     * history by storing two previous
     * colors.
     */
	public ColorHistory() {
		createComponent();
		wireComponent();
	}
	
	/**
	 * This function creates
	 * and sets the details of
	 * components.
	 */
	private void createComponent() {
		layeredPane = new JLayeredPane();
		panelColor1 = new JPanel();
		panelColor2 = new JPanel();
		
		panelColor1.setBackground(Color.BLACK);
		panelColor2.setBackground(Color.WHITE);
		layeredPane.setBounds(0, 0, WIDTH,HEIGHT);
		panelColor1.setBounds(0, 0, WIDTH-10, WIDTH-10);
		panelColor2.setBounds(10, 10, WIDTH, WIDTH-10);
		panelColor2.setOpaque(true);
		panelColor1.setOpaque(true);
	}
	
	/**
	 * This function wires
	 * the layered panel, and
	 * sets the color.
	 */
	private void wireComponent() {
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setMaximumSize( this.getPreferredSize() );	
		layeredPane.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		layeredPane.add(panelColor1, new Integer(1), 0);
		layeredPane.add(panelColor2, new Integer(0), 0);
		this.add(layeredPane);
	}
	
	/**
	 * This function gets
	 * the active color panel
	 * @return active color panel
	 */
	public JPanel activePanel() {
		if(layeredPane.getComponentZOrder(panelColor1)==0) {
			return panelColor1;
		}
		else {
			return panelColor2;
		}
	}
	
	/**
	 * This function switch's
	 * with the current
	 * active color panel
	 */
	public void switchColor() {
		layeredPane.setComponentZOrder(layeredPane.getComponents()[1], 0);
		repaint();
	}
	
}
public class PanelWest extends JPanel{
	private ToolsOption toolsOption;
	private ColorHistory colorHistory;
	private JButton switchColorButton;
	private JPanel colorHistoryPanel;
	private JPanel toolPanel;
	
	/**
	 * This object handles
	 * and assigns where
	 * to draw all other
	 * objects such has tool
	 * buttons and color history.
	 */
	public PanelWest() {
		createComponent();
		wireComponent();
	}
	
	/**
	 * This function creates the
	 * required components.
	 */
	private void createComponent() {
		toolsOption = new ToolsOption();
		colorHistory = new ColorHistory();
		switchColorButton = new JButton("Switch");
		toolPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15,0));
		colorHistoryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0,0));
	}
	
	/**
	 * This function wires
	 * and adds all the components
	 * in one panel.
	 */
	private void wireComponent() {
		this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 5));
		this.setPreferredSize(new Dimension(75, 0));
		
		toolPanel.add(toolsOption);
		colorHistoryPanel.add(colorHistory);
		this.add(toolPanel);
		this.add(colorHistoryPanel);
		this.add(switchColorButton);
	}
	
	/**
	 * This function adds the
	 * button listener for all
	 * the tool buttons.
	 * @param listener
	 */
	public void setButtonLisiter(ActionListener listener) {
		for(int i=0;i<toolsOption.getButtons().length;i++) {
			toolsOption.getButtons()[i].addActionListener(listener);
		}
	}
	
	/**
	 * This function adds the
	 * switch button listener
	 * for the color history.
	 * @param listener 
	 */
	public void setSwitchListener(ActionListener listener) {
		switchColorButton.addActionListener(listener);
	}
	
	/**
	 * This function gets the
	 * active color history panel
	 * @return JPanel active panel
	 */
	public JPanel getActiveColorPanel() {
		return colorHistory.activePanel();
	}
	
	/**
	 * This function switch's the 
	 * active color panel.
	 */
	public void switchColors() {
		colorHistory.switchColor();
	}
}
