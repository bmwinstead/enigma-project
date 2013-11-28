package main.java.GUI;

import java.awt.*;

//import java.awt.geom.*;
import javax.swing.*;


/**
 * @author Rosana Montanez
 * @author Team Enigma
 * @version 0.1
 * @date - Nov 27, 2013
 */

@SuppressWarnings("serial")
public class PlugButton extends JRadioButton {
	
	PlugButton(String letter, int vposition){
		super (letter);
		
		setForeground(Color.WHITE);
		setBackground(Color.DARK_GRAY);
		setVerticalTextPosition(vposition);
		setHorizontalTextPosition(JRadioButton.CENTER);
		setFocusable(false);
	}
/*public class PlugButton extends JButton {
  public PlugButton (String label) {
    super(label);

 //make a circle and color it
    Dimension size = new Dimension(45,45);
    setPreferredSize(size);
    setContentAreaFilled(false);
    setForeground(Color.white);
    setFocusable(false);
  }

// Paint the round background and label.
  protected void paintComponent(Graphics g) {
	//the button has two color, light-gray when pressed and dark grey when not pressed  
    if (getModel().isArmed()) {
      g.setColor(Color.lightGray);
    } else {
      g.setColor(Color.darkGray);
    //	g.setColor(getBackground());
    }
    g.fillOval(0, 0, getSize().width-1, 
    getSize().height-1);
    
    //set the font
    Font font = new Font("DialogInput", Font.BOLD, 18);
    //Font font = new Font("Serif", Font.BOLD, 15);
    g.setFont(font);
    
    //paint the label and circle
    super.paintComponent(g);
  }

// Paint the border of the button
  protected void paintBorder(Graphics g) {
    g.setColor(Color.DARK_GRAY);
	//g.setColor(getForeground());
    g.drawOval(0, 0, getSize().width-1, 
      getSize().height-1);
  }
	  

  public static void main(String[] args) {
	  
	  JRadioButton button = new PlugButton("A", JRadioButton.TOP);
   // JButton button = new PlugButton("A");

// Create a frame in which to show the button.
    JFrame frame = new JFrame();
    frame.getContentPane().setBackground(Color.WHITE);
    frame.getContentPane().add(button);
    frame.getContentPane().setLayout(new FlowLayout());
    frame.setSize(150, 150);
    frame.setVisible(true);
  }*/
}
