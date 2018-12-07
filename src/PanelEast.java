import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeListener;

class ColorWheel extends JPanel implements MouseListener, MouseMotionListener{
	final private int RADIUS = 200;
	private BufferedImage img;
	private JPanel colorsPanel;
	
	/**
	 * This function is a constructor
	 * that creates the color wheel
	 * and wires its mouse drag event.
	 */
	public ColorWheel() {
		createComponent();
		wireComponent();
	}
	
	/**
	 * This function takes care of drawing
	 * the color wheel.
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D graphics2d = (Graphics2D)g;
		graphics2d.drawImage(img, null, 0, 0);
	}
	
	/**
	 * This function creates and 
	 * initializes the components.
	 */
	private void createComponent() {
		this.setPreferredSize(new Dimension(RADIUS, RADIUS));
		img = new BufferedImage(RADIUS, RADIUS, BufferedImage.TYPE_INT_RGB);
		drawCircle();
	}
	
	/**
	 * This function wires components.
	 */
	private void wireComponent() {
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	/**
	 * This function draws the 
	 * color wheel and its pixel 
	 * value.
	 */
	private void drawCircle() {
        //Center of circle
        int centerX = img.getWidth() / 2;
        int centerY = img.getHeight() / 2;
        int radius = (img.getWidth() / 2) * (img.getWidth() / 2);

        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                int a = i - centerX;
                int b = j - centerY;
                int distance = a * a + b * b;
                if (distance < radius) {
                    Color c = getColorXY(i, j);
                    float hsbVals[] = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
                    Color drawColor = Color.getHSBColor(hsbVals[0], hsbVals[1], 1);
                    img.setRGB(i, j, RGBtoHEX(drawColor));
                } else {
                    img.setRGB(i, j, getBackground().getRGB());
                }
            }
        }
	}
	
	/**
	 * This function takes care of deciding
	 * the pixel color at a given x and y 
	 * within the circle radius.
	 * @param i - x position
	 * @param j - y position
	 * @return color at that x and y
	 */
	private Color getColorXY(int i,int j) {
		//RED
        int redX = img.getWidth();
        int redY = img.getHeight() / 2;
        int redRad = img.getWidth() * img.getWidth();
        //Green
        int greenX = 0;
        int greenY = img.getHeight() / 2;
        int greenRad = img.getWidth() * img.getWidth();

        //Blue
        int blueX = img.getWidth() / 2;
        int blueY = img.getHeight();
        int blueRad = img.getWidth() * img.getWidth();
        
        int rdx = i - redX;
        int rdy = j - redY;
        int redDist = (rdx * rdx + rdy * rdy);
        int redVal = (int) (255 - ((redDist / (float) redRad) * 256));

        int gdx = i - greenX;
        int gdy = j - greenY;
        int greenDist = (gdx * gdx + gdy * gdy);
        int greenVal = (int) (255 - ((greenDist / (float) greenRad) * 256));

        int bdx = i - blueX;
        int bdy = j - blueY;
        int blueDist = (bdx * bdx + bdy * bdy);
        int blueVal = (int) (255 - ((blueDist / (float) blueRad) * 256));
        return new Color(redVal, greenVal, blueVal);
	}
	
	/**
	 * This function converts a given color
	 * in RGB to hex.
	 * @param color - color in RGB
	 * @return color in HEX
	 */
	private int RGBtoHEX(Color color) {
        String hex = Integer.toHexString(color.getRGB() & 0xffffff);
        if (hex.length() < 6) {
            if (hex.length() == 5)
                hex = "0" + hex;
            if (hex.length() == 4)
                hex = "00" + hex;
            if (hex.length() == 3)
                hex = "000" + hex;
        }
        hex = "#" + hex;
        return Integer.decode(hex);
    }
	
	/**
	 * This function checks if the user
	 * mouse dragged is inside the 
	 * color wheel
	 * @param x - mouse x
	 * @param y - mouse y
	 * @return true if the drag is inside
	 * the circle
	 */
	public boolean isMouseInside(int x,int y) {
		int centerX = img.getWidth()/2;
		int centerY = img.getHeight()/2;
		int radius = centerX*centerX;
		int a = x - centerX;
        int b = y - centerY;
        int distance = a * a + b * b;
        if(distance<radius) {
        	return true;
        }
        else {
        	return false;
        }
	}
	
	/**
	 * This function sets the panel
	 * mouse dragged listener.
	 * @param listener - mouse listener
	 */
	public void setListener(MouseMotionListener listener) {
		this.addMouseMotionListener(listener);
	}
	
	/**
	 * This function gets the panel
	 * color at x and y.
	 * @param x
	 * @param y
	 * @return
	 */
    public Color getColor(int x,int y) {
    	return new Color(img.getRGB(x, y));
    }
    
    /**
     * This function changes the color picker
     * history color.
     * @param colorPanel - color history panel
     */
    public void setColorsPanel(JPanel colorPanel) {
    	this.colorsPanel = colorPanel;
    }
    
    /**
     * This function returns the color
     * history panel.
     * @return color panel
     */
    public JPanel colorPanel(){
    	return colorsPanel;
    }
    
	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent event) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseDragged(MouseEvent event) {}

	@Override
	public void mouseMoved(MouseEvent e) {}
}

public class PanelEast extends JPanel {
	private JSlider slider;
	private JPanel brushPanel;
	private ColorWheel colorWheel;
	
	/**
	 * This function is a constructor
	 * that creates the color wheel
	 * and the brush size.
	 */
	public PanelEast() {
		createComponent();
		wireComponent();
	}
	
	/**
	 * This function creates and 
	 * initializes the components.
	 */
	private void createComponent() {
		colorWheel = new ColorWheel();
		slider = new JSlider(SwingConstants.HORIZONTAL, 20, 70, 20);
		brushPanel = new JPanel(new BorderLayout());
	}
	
	/**
	 * This function wires components.
	 */
	private void wireComponent() {
		this.setPreferredSize(new Dimension(200,0));
		brushPanel.add(new JLabel("Brush Size:"),BorderLayout.NORTH);
		brushPanel.add(slider,BorderLayout.CENTER);
		this.add(colorWheel);
		this.add(brushPanel);
	}
	
	/**
	 * This function sets the active
	 * color history panel
	 * @param colorPanel - color panel
	 */
    public void setColorsPanel(JPanel colorPanel) {
    	colorWheel.setColorsPanel(colorPanel);
    }
    
    /**
     * This function sets color wheel
     * listener
     * @param listener - mouse event
     */
    public void setColorWheelListener(MouseMotionListener listener) {
    	colorWheel.setListener(listener);
    }
    
    /**
     * This function sets the brush size
     * slider listener
     * @param listener - slider change
     */
	public void setSliderListener(ChangeListener listener) {
		slider.addChangeListener(listener);
	}
}
