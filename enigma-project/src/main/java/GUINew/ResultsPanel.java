/**
 * ResultsPanel.java
 * @author - Walter Adolph
 * @author - Team Enigma
 * @version - 0.9
 * @date - Nov 26, 2013
 */
package main.java.GUINew;

import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import main.java.enigma.EnigmaSettings;

public class ResultsPanel extends JPanel {
	private JTextField leftRotorTextField;
	private JTextField middleRotorTextField;
	private JTextField rightRotorTextField;
	private JTextField reflectorTextField;
	private JTextField leftRingTextField;
	private JTextField middleRingTextField;
	private JTextField rightRingTextField;
	private JTextField leftIndicatorTextField;
	private JTextField middleIndicatorTextField;
	private JTextField rightIndicatorTextField;
	private JTextField plugboardTextField;
	private JTextArea outputTextArea;
	private JTextField fourthRotorTextField;
	private JTextField fourthRingTextField;
	private JTextField fourthIndicatorTextField;
	
	public ResultsPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		outputTextArea = new JTextArea();
		outputTextArea.setLineWrap(true);
		outputTextArea.setWrapStyleWord(true);
		outputTextArea.setColumns(50);
		outputTextArea.setRows(10);
		add(outputTextArea);
		
		JPanel resultsPanel = new JPanel();
		add(resultsPanel);
		resultsPanel.setBackground(Color.black);
		resultsPanel.setForeground(Color.white);
		
		JLabel label = new JLabel("rotors:");
		resultsPanel.add(label);
		label.setBackground(Color.black);
		label.setForeground(Color.white);
		
		fourthRotorTextField = new JTextField();
		resultsPanel.add(fourthRotorTextField);
		fourthRotorTextField.setColumns(2);
		
		leftRotorTextField = new JTextField();
		leftRotorTextField.setColumns(2);
		resultsPanel.add(leftRotorTextField);
		
		middleRotorTextField = new JTextField();
		middleRotorTextField.setColumns(2);
		resultsPanel.add(middleRotorTextField);
		
		rightRotorTextField = new JTextField();
		rightRotorTextField.setColumns(2);
		resultsPanel.add(rightRotorTextField);
		
		JLabel label_1 = new JLabel("reflector:");
		resultsPanel.add(label_1);
		label_1.setBackground(Color.black);
		label_1.setForeground(Color.white);
		
		reflectorTextField = new JTextField();
		reflectorTextField.setColumns(2);
		resultsPanel.add(reflectorTextField);
		
		JLabel label_2 = new JLabel("rings:");
		resultsPanel.add(label_2);
		label_2.setBackground(Color.black);
		label_2.setForeground(Color.white);
		
		fourthRingTextField = new JTextField();
		resultsPanel.add(fourthRingTextField);
		fourthRingTextField.setColumns(2);
		
		leftRingTextField = new JTextField();
		leftRingTextField.setColumns(2);
		resultsPanel.add(leftRingTextField);
		
		middleRingTextField = new JTextField();
		middleRingTextField.setColumns(2);
		resultsPanel.add(middleRingTextField);
		
		rightRingTextField = new JTextField();
		rightRingTextField.setColumns(2);
		resultsPanel.add(rightRingTextField);
		
		JLabel label_3 = new JLabel("indicators:");
		resultsPanel.add(label_3);
		label_3.setBackground(Color.black);
		label_3.setForeground(Color.white);
		
		fourthIndicatorTextField = new JTextField();
		resultsPanel.add(fourthIndicatorTextField);
		fourthIndicatorTextField.setColumns(2);
		
		leftIndicatorTextField = new JTextField(2);
		resultsPanel.add(leftIndicatorTextField);
		
		middleIndicatorTextField = new JTextField(2);
		resultsPanel.add(middleIndicatorTextField);
		
		rightIndicatorTextField = new JTextField(2);
		resultsPanel.add(rightIndicatorTextField);
		
		JLabel label_4 = new JLabel("plugboard:");
		resultsPanel.add(label_4);
		label_4.setBackground(Color.black);
		label_4.setForeground(Color.white);
		
		plugboardTextField = new JTextField();
		plugboardTextField.setColumns(30);
		resultsPanel.add(plugboardTextField);
	}

	// Outputs the decrypted text and discovered settings to the panel.
	public void printSolution(EnigmaSettings settings, String message) {
		int[] wheel = settings.getRotors();
		char[] ring = settings.getRingSettings();
		char[] indicator = settings.getIndicatorSettings();
		
		if (wheel.length == 3) {
			leftRotorTextField.setText("" + (wheel[0] + 1));
			middleRotorTextField.setText("" + (wheel[1] + 1));
			rightRotorTextField.setText("" + (wheel[2] + 1));
			leftRingTextField.setText("" + ring[0]);
			middleRingTextField.setText("" + ring[1]);
			rightRingTextField.setText("" + ring[2]);
			leftIndicatorTextField.setText("" + indicator[0]);
			middleIndicatorTextField.setText("" + indicator[1]);
			rightIndicatorTextField.setText("" + indicator[2]);
		}
		else {
			fourthRotorTextField.setText("" + (wheel[0] + 1));
			leftRotorTextField.setText("" + (wheel[1] + 1));
			middleRotorTextField.setText("" + (wheel[2] + 1));
			rightRotorTextField.setText("" + (wheel[3] + 1));
			
			fourthRingTextField.setText("" + (ring[0]));
			leftRingTextField.setText("" + ring[1]);
			middleRingTextField.setText("" + ring[2]);
			rightRingTextField.setText("" + ring[3]);
			
			fourthIndicatorTextField.setText("" + (indicator[0]));
			leftIndicatorTextField.setText("" + indicator[1]);
			middleIndicatorTextField.setText("" + indicator[2]);
			rightIndicatorTextField.setText("" + indicator[3]);
		}
		
		reflectorTextField.setText("" + settings.printReflector());
		plugboardTextField.setText(settings.printPlugboard());
		
		outputTextArea.setText(message);
	}
}
