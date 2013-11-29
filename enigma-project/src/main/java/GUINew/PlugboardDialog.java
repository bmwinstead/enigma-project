package main.java.GUINew;

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
public class PlugboardDialog extends JDialog implements ActionListener {
	private static ComponentLinker linker;
	private JButton closeButton;
	private static int maxPairs = 13;
	private static String[][] plugPairs = new String[2][maxPairs];
	private static int filled = 0;

	final int letterTotal = 26;
	PlugButton plugs[] = new PlugButton[letterTotal];

	public PlugboardDialog() {
		setTitle("Plugboard");
		linker = new ComponentLinker();
		setGlassPane(linker);
		linker.setVisible(true);
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(3, 1));
		add(panel);

		for (int i = 0; i < letterTotal; i++) {
			int ASCIIvalue = 65 + i;
			char letter = (char) ASCIIvalue;

			final PlugButton plug;

			if (i < letterTotal / 2) {
				plug = new PlugButton(String.valueOf(letter), JRadioButton.TOP);
			}
			else {
				plug = new PlugButton(String.valueOf(letter),
						JRadioButton.BOTTOM);
			}
			panel.add(plug);

			plug.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					if (filled < maxPairs) {
						// disable button, so no more changes can be made
						plug.setEnabled(false);

						// store plug pairs
						plugPairs(e.getActionCommand());

						// draw a line between plugs
						link(plug);
					}
				}
			});

			plugs[i] = plug;
		}
		closeButton = new JButton("X");
		closeButton.setBackground(Color.DARK_GRAY);
		closeButton.setForeground(Color.white);
		closeButton.addActionListener(this);
		panel.add(closeButton);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setSize(600, 300);
	}

	// Once two plugs have select it, save them as pairs
	private static String selected = "";

	private static void plugPairs(String button) {

		if (selected.equals("")) {
			selected = button;
		} else {
			plugPairs[0][filled] = selected;
			plugPairs[1][filled] = button;
			selected = "";
			filled++;
		}// end else
	}

	// This methods draw the line between components
	private JRadioButton last = null;

	private void link(JRadioButton button) {
		if (last == null) {
			last = button;
		} else {
			linker.link(last, button);
			last = null;
		}
	}

	public void resetPlugBoard() {
		// deselect all button and enable them
		for (int i = 0; i < letterTotal; i++) {
			plugs[i].setSelected(false);
			plugs[i].setEnabled(true);
		}
		// clear plug pairs
		for (int i = 0; i < maxPairs; i++) {
			plugPairs[0][i] = " ";
			plugPairs[1][i] = " ";
		}
		filled = 0;
		linker.clear();
	}
	
	public String displayDialog(){
		setVisible(true);
		return getPBMap();
	}
	
	public void actionPerformed(ActionEvent arg0) {
		setVisible(false);
		dispose();
	}
	
	private String getPBMap(){
		String pbMap = "";
		for(int i = 0; i < maxPairs; i++){
			if(plugPairs[0][1] == null)
				break;
			pbMap += plugPairs[0][i];
			pbMap += plugPairs[1][i];
		}
		return pbMap;
	}
	private class PlugButton extends JRadioButton {
		
		PlugButton(String letter, int vposition){
			super (letter);
			
			setForeground(Color.WHITE);
			setBackground(Color.BLACK);
			setVerticalTextPosition(vposition);
			setHorizontalTextPosition(JRadioButton.CENTER);
			setFocusable(false);
		}
	}
}
