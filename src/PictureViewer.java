import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PictureViewer extends JPanel implements MouseListener, MouseMotionListener {
	private PenType currentPen = PenType.BRUSH;
	private final int DEFUALT_HEIGHT = 500;
	private final int DEFUALT_WIDTH = 800;
	private boolean doDrawString = false;
	private boolean doDrawEraser = false;
	private boolean doDrawBrush = false;
	private boolean doDrawBox = false;
	private boolean doMagnify = false;
	private BufferedImage eraserCanvas;
    private BufferedImage canvas;
    private int mouseXStart;
    private int mouseYStart;
    private int mouseXEnd;
    private int mouseYEnd;
    private int brushSize = 20;
    private int fontSize;
    private Color drawColor;
    private Color textColor;
    private JPanel colorsPanel;
    private String response;
    private File imageFile;
    private JTextField textInput;
    private JTextField textSizeInput;
    private JPanel inputPanel;
    private Graphics2D canvasGraphics;
    
    /***
     * This is constructor that
     * takes in a given file or
     * a null and creates the 
     * canvas out of the image.
     * @param file - image file
     */
    public PictureViewer(File file) {
    	imageFile = file;
    	createComponent();
    	wireComponents();
    }
    
    /**
     * This function creates and initializes
     * all the components.
     */
    private void createComponent() {
    	if(imageFile == null) {
    		canvas = createBlankCanvas();
    		eraserCanvas = createBlankCanvas();
    	}
    	else {
    		canvas = createCanvas();
    		eraserCanvas = createCanvas();
    	}
    	canvasGraphics = canvas.createGraphics();
    	textColor = Color.BLACK;
    	textInput = new JTextField(5);
    	textSizeInput = new JTextField(5);
    	inputPanel = new JPanel();
    	
    	inputPanel.add(new JLabel("String:"));
    	inputPanel.add(textInput);
    	inputPanel.add(Box.createHorizontalStrut(15)); 
    	inputPanel.add(new JLabel("Size:"));
    	inputPanel.add(textSizeInput);
    }

    /**
     * This function wires the picture
     * viewer with the mouse events.
     */
    private void wireComponents() {
        addMouseMotionListener(this);
        addMouseListener(this);
    }

    /**
     * This function draws the given image
     * file or a blank canvas on the
     * viewer.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics2d = (Graphics2D) g;
        if(doMagnify) {
    		int boxStartX = mouseXStart;
    		int boxStartY = mouseYStart;
    		int boxEndX = boxStartX+mouseXEnd;
    		int boxEndY = boxStartY+mouseYEnd;
    		graphics2d.drawImage(canvas, 0, 0, 
        				getParent().getWidth(), getParent().getHeight(), boxStartX, 
        				boxStartY, boxEndX, boxEndY, null);
    		doDrawBox = false;
    		mouseXStart = 0;
    		mouseYStart = 0;
        }
        else {
        	graphics2d.drawImage(canvas,0,0,null);
        }
        if(doDrawBox) {
        	graphics2d.setColor(Color.RED);
        	graphics2d.drawRect(mouseXStart, mouseYStart, 
        						mouseXEnd, mouseYEnd);
        }
    }
    
    /**
     * This function takes care or
     * all the other drawings
     * that happen on the panel. 
     * Such has the brush and the
     * eraser.
     */
    private void draw() {
    	if(doDrawString) {
    		canvasGraphics.setColor(textColor);
    		canvasGraphics.setFont(new Font("TimesRoman", Font.PLAIN, fontSize));
    		canvasGraphics.drawString(response, mouseXStart, mouseYStart);
    		doDrawString = false;
    	}
    	mouseXStart -= (brushSize/2);
    	mouseYStart -= (brushSize/2);
    	if(doDrawEraser) {
    		int boxStartX = mouseXStart;
    		int boxStartY = mouseYStart;
    		int boxEndX = boxStartX+brushSize;
    		int boxEndY = boxStartY+brushSize;
        	canvasGraphics.drawImage(eraserCanvas, boxStartX, boxStartY, 
        				boxEndX, boxEndY, boxStartX, 
        				boxStartY, boxEndX, boxEndY, null);
        }

    	if(doDrawBrush) {
        	canvasGraphics.setColor(drawColor);
    		canvasGraphics.fillOval(mouseXStart, mouseYStart, brushSize,brushSize);
    	}
    	repaint();
    }
    
    /**
     * This function decides what happens
     * when the mouse is dragged, based
     * upon the selected brush type.
     */
    @Override
    public void mouseDragged(MouseEvent event) {
		if(currentPen == PenType.BOX_SELECT) {
    		doDrawBox = true;
    		mouseXEnd = event.getX();
    		mouseYEnd = event.getY();
    		mouseXEnd -= mouseXStart;
    		mouseYEnd -= mouseYStart;
    		repaint();
    	}
    	else if(currentPen == PenType.BRUSH) {
    		doDrawBrush = true;
    		mouseXStart = event.getX();
    		mouseYStart = event.getY();
    		draw();
    	}
    	else if(currentPen == PenType.ERASER) {
    		doDrawEraser = true;
    		mouseXStart = event.getX();
    		mouseYStart = event.getY();
    		draw();
    	}
    }

    @Override
    public void mouseMoved(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
    
    /**
     * This function resets the drawings
     * and the brush type, if the user
     * simply clicks.
     */
    @Override
    public void mouseClicked(MouseEvent e) {
    	if(currentPen == PenType.BOX_SELECT) {
    		if(doDrawBox) {doDrawBox=false;}
    	}
    	else if(currentPen == PenType.BRUSH) {
    		if(doDrawEraser) {doDrawEraser=false;}
    	}
    }

    /**
     * This function takes care of drawing
     * when the user presses the mouse button
     * and draws depending on brush type.
     */
    @Override
    public void mousePressed(MouseEvent event) {
    	if(currentPen == PenType.BOX_SELECT) {
    		doDrawBox = true;
    		mouseXStart = event.getX();
    		mouseYStart = event.getY();
    		mouseXEnd = 0;
    		mouseYEnd = 0;
    		repaint();
    	}
    	else if(currentPen == PenType.MAGNIFIER) {
    		if(isMouseInside(event, mouseXStart, 
    						mouseYStart, mouseXEnd, 
    						mouseYEnd)) {
    			doMagnify = true;
    			repaint();
    		}
    		else {
    			doMagnify = false;
    			doDrawBox = false;
    			repaint();
    		}
    	}
    	else if(currentPen == PenType.BRUSH) {
    		doDrawBox = false;
    		doDrawBrush = true;
    		mouseXStart = event.getX();
    		mouseYStart = event.getY();
    		draw();
    	}
    	else if(currentPen == PenType.EYE_DROP) {
    		mouseXStart = event.getX();
    		mouseYStart = event.getY();
    		Color color = getColor(mouseXStart, mouseYStart);
    		colorsPanel.setBackground(color);
    		drawColor = color;
    	}
    	else if(currentPen == PenType.TEXT) {
    		mouseXStart = event.getX();
    		mouseYStart = event.getY();
    		int result = JOptionPane.showConfirmDialog(null, 
    				inputPanel, "Enter string and font size",
    				JOptionPane.OK_CANCEL_OPTION);
    		if (result == 0) {
    			response = textInput.getText();
    			fontSize = Integer.valueOf(textSizeInput.getText());
    			doDrawString = true;
    			draw();
    		}
    	}
    	else if(currentPen == PenType.ERASER) {
    		doDrawEraser = false;
    		mouseXStart = event.getX();
    		mouseYStart = event.getY();
    		draw();
    	}
    }

    /**
     * This function gets called when the user
     * releases the mouse event, and resets
     * all the drawing.
     */
    @Override
    public void mouseReleased(MouseEvent event) {
    	if(currentPen == PenType.BOX_SELECT) {
    		mouseXEnd = event.getX();
    		mouseYEnd = event.getY();
    		mouseXEnd -= mouseXStart;
    		mouseYEnd -= mouseYStart;
    		repaint();
    	}
    	else if(currentPen == PenType.MAGNIFIER) {
    		doMagnify = false;
    	}
    	else if(currentPen == PenType.BRUSH) {
    		doDrawBrush = false;
    	}
    	else if(currentPen == PenType.ERASER) {
    		doDrawEraser = false;
    	}
    }
    
    /**
     * This function checks if a given
     * mouse x and y is inside a given
     * rectangle.
     * @param e - mouse event
     * @param minX - min of box x
     * @param minY - min of box y
     * @param maxX - max of box x
     * @param maxY - max of box y
     * @return
     */
    private boolean isMouseInside(MouseEvent e,int minX,int minY,int maxX,int maxY) {
    	int mouseX = e.getX();
    	int mouseY = e.getY();
    	maxX += minX;
    	maxY += minY;
    	if((mouseX>=minX && mouseY>=minY)
    		&& (mouseX<=maxX &&mouseY<=maxY)) {
    		return true;
    	}
    	else {
    		return false;
    	}
    }
    
    /**
     * This function returns a color
     * given an x and y.
     * @param x - mouse x position
     * @param y - mouse y position
     * @return color at x and y
     */
    private Color getColor(int x,int y) {
    	return new Color(canvas.getRGB(x, y));
    }
    
    /**
     * This function sets the active
     * color panel from the panel west.
     * @param colorPanel - active color panel
     */
    public void setColorsPanel(JPanel colorPanel) {
    	this.colorsPanel = colorPanel;
    }
    
    /**
     * This function returns the current
     * canvas width
     * @return canvas width
     */
    public int getImageWidth() {
    	return canvas.getWidth();
    }
    
    /**
     * This function returns the current
     * canvas height.
     * @return canvas height
     */
    public int getImageHeight() {
    	return canvas.getHeight();
    }

    /**
     * This function sets the current
     * brush color.
     * @param color - brush color.
     */
    public void setBrushColor(Color color) {
    	drawColor = color;
    	textColor = color;
    }
    
    /**
     * This function sets the current
     * brush size.
     * @param size - brush size
     */
    public void setBrushSize(int size) {
    	this.brushSize = size;
    }
    
    /**
     * This function sets the current
     * pen type.
     * @param type - current pen type.
     */
    public void setPenType(PenType type) {
    	this.currentPen = type;
    }
    
    /**
     * This function sets the frame
     * name, "Canvas" if no image
     * is provided.
     * @return a string of the frame name.
     */
    public String getPictureName() {
    	if(imageFile == null) 
    		return "Canvas";
    	return imageFile.getName();
    }
    
    /**
     * This function returns the 
     * current canvas.
     * @return current canvas.
     */
    public BufferedImage getCanvas() {
    	return canvas;
    }
    
    /**
     * This function changes the 
     * current canvas with a new canvas.
     * @param img - other canvas.
     */
    public void setCanvas(BufferedImage img) {
    	this.canvas = img;
    	canvasGraphics = (Graphics2D) canvas.getGraphics();
    }
    
    /**
     * This function creates an empty white
     * canvas, if the user did not provide
     * any image file.
     * @return a white buffered image
     */
    private BufferedImage createBlankCanvas() {
    	BufferedImage temp = new BufferedImage(DEFUALT_WIDTH, 
				DEFUALT_HEIGHT, 
				BufferedImage.TYPE_INT_RGB);
    	Graphics2D graphics2d = (Graphics2D)temp.createGraphics();
    	graphics2d.setColor(Color.WHITE);
    	graphics2d.fillRect(0, 0, DEFUALT_WIDTH, DEFUALT_HEIGHT);
    	return temp;
    }
    
    /**
     * This function creates a canvas of
     * the given image file.
     * @return a canvas that has the image file.
     */
    private BufferedImage createCanvas() {
    	BufferedImage temp = null;
		try {
			temp =  ImageIO.read(imageFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return temp;
    }
}