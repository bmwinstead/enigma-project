package main.java.GUI;


import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

/**
 * @author Rosana Montanez
 * @author Team Enigma
 * @version 0.1
 * @date - Nov 27, 2013
 */


@SuppressWarnings("serial")
public class PlugBoardGUI extends JFrame
{
	private static ComponentLinker linker;
	
	final static int max_pairs = 10;  
    private static String[][] plugPairs = new String [2][max_pairs];
	private static int filled =0;
	
	final int letterTotal =26; 	
	PlugButton plugs[] = new PlugButton[letterTotal];
	
	PlugBoardGUI(){

        linker = new ComponentLinker ();
        setGlassPane ( linker );
        linker.setVisible ( true );

        JPanel panel = new JPanel ();
        panel.setLayout (new GridLayout(3,1));
        add (panel);

        for (int i=0; i < letterTotal; i++){
			int ASCIIvalue = 65 +i;
			char letter = (char) ASCIIvalue;
			
			final PlugButton plug;
			
			if(i < letterTotal/2){
			plug = new PlugButton(String.valueOf(letter), JRadioButton.TOP);
			
	    }
		
		else{
			plug = new PlugButton(String.valueOf(letter), JRadioButton.BOTTOM);
			
		}
			panel.add(plug);
			
			//add actionListener
			plug.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					
					if(filled < 10){
						//disable button, so no more changes can be made
						plug.setEnabled(false);
					
						//store plug pairs
						plugPairs(e.getActionCommand());
					
						//draw a line between plugs
						link(plug);
					}					
				}
			});
			
			plugs[i] = plug;
        }
        
        setDefaultCloseOperation ( JFrame.EXIT_ON_CLOSE );
        pack();
        setLocationRelativeTo ( null );
        setVisible ( true );
	}
    
    //Once two plugs have select it, save them as pairs
    private static String selected = "";
    private static void plugPairs(String button){
    	  	
    	if (selected.equals("")){	
			selected = button;
		}
		else{
			plugPairs[0][filled] = selected;
			plugPairs[1][filled] = button;
			selected = "";
			filled++;
			System.out.println(Arrays.deepToString(plugPairs));
		}//end else		
    }

    //This methods draw the line between components
    private JRadioButton last = null;

    private void link ( JRadioButton button )
    {
        if ( last == null )
        {
            last = button;
        }
        else
        {
            linker.link ( last, button );
            last = null;
        }
    }
    
    @SuppressWarnings("unused") //I believe we want to use this later
	private void resetPlugBoard(){
    
    	//deselect all button and enable them	
    	for (int i = 0; i < letterTotal; i++){
    		plugs[i].setSelected(false);
    		plugs[i].setEnabled(true);
    	}
    	
    	//clear plug pairs
    	for (int i = 0; i < max_pairs; i++){
    		plugPairs[0][i] = " ";
    		plugPairs[1][i] = " ";
    	}
    	
    	filled =0;
    	linker.clear();
    	
    }

    public static void main ( String[] args )
    {
    	new PlugBoardGUI();
    }

}
