package main.java.GUINew;


import javax.swing.*;

import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Rosana Montanez
 * @author Team Enigma
 * @version 0.1
 * @date - Nov 27, 2013
 * Most of this code comes from 
 * @see http://stackoverflow.com/a/12389479/909085
 * 
 * Draws the lines that illustrate which letters are linked together in the
 * plugboard. 
 */


@SuppressWarnings("serial")
public class ComponentLinker extends JComponent{
	private Map<JComponent, JComponent> linked;

	/**
	 * Constuctor. Initializes a new hashmap to contain the links.
	 */
    public ComponentLinker()
    {
        super();
        linked = new HashMap<JComponent, JComponent> ();
    } // end constructor

    /**
     * Adds a new link to the hashmap, indicting which letters are linked.
     * 
     * @param c1
     * 			First component to be linked.
     * @param c2
     * 			Second component to be linked. 
     */
    public void link ( JComponent c1, JComponent c2 )
    {
        linked.put ( c1, c2 );
        repaint();
    } // end link method
    
    /**
     * Clears the hashmap. Used when the plugboard is reset. 
     */
    public void clear(){
    	linked.clear();
    	repaint();
    } // end clear method

    protected void paintComponent ( Graphics g )
    {
        Graphics2D g2d = ( Graphics2D ) g;
        g2d.setRenderingHint ( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );

        g2d.setPaint ( Color.WHITE);
        g2d.setStroke(new BasicStroke(3));
        for ( JComponent c1 : linked.keySet () )
        {
            Point p1 = getRectCenter ( getBoundsInWindow ( c1 ) );
            Point p2 = getRectCenter ( getBoundsInWindow ( linked.get ( c1 ) ) );
            g2d.drawLine ( p1.x, p1.y, p2.x, p2.y );
        }
    }

    private Point getRectCenter ( Rectangle rect )
    {
        return new Point ( rect.x + rect.width / 2, rect.y + rect.height / 2 );
    }

    private Rectangle getBoundsInWindow ( Component component )
    {
        return getRelativeBounds ( component, getRootPaneAncestor ( component ) );
    }

    private Rectangle getRelativeBounds ( Component component, Component relativeTo )
    {
        return new Rectangle ( getRelativeLocation ( component, relativeTo ),
                component.getSize () );
    }

    private Point getRelativeLocation ( Component component, Component relativeTo )
    {
        Point los = component.getLocationOnScreen ();
        Point rt = relativeTo.getLocationOnScreen ();
        return new Point ( los.x - rt.x, los.y - rt.y );
    }

    private JRootPane getRootPaneAncestor ( Component c )
    {
        for ( Container p = c.getParent (); p != null; p = p.getParent () )
        {
            if ( p instanceof JRootPane )
            {
                return ( JRootPane ) p;
            }
        }
        return null;
    }

    public boolean contains ( int x, int y )
    {
        return false;
    }
	
} // end ComponentLinker class
