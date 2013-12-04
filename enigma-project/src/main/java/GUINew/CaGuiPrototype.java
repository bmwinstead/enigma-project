/**
 * 
 */
package main.java.GUINew;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import java.awt.Color;
import java.awt.SystemColor;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JSpinner;

import java.awt.Dimension;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JProgressBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpinnerListModel;
import javax.swing.border.TitledBorder;

import java.awt.FlowLayout;

public class CaGuiPrototype extends JPanel {
	private JTextField plugboardTextField;
	public CaGuiPrototype() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JTextArea instructionTextArea = new JTextArea();
		add(instructionTextArea);
		instructionTextArea.setWrapStyleWord(true);
		instructionTextArea.setText("Instructions: Click Browse or type in a file path to select a text file for parsing, and select Parse  to break the text into 1, 2, and 3 word groups (unigrams, bigrams, and trigrams). Adding subsequent files will add to the databases. Note: Text may take some time to parse.");
		instructionTextArea.setRows(1);
		instructionTextArea.setLineWrap(true);
		instructionTextArea.setFont(new Font("Tahoma", Font.PLAIN, 11));
		instructionTextArea.setEditable(false);
		instructionTextArea.setColumns(40);
		instructionTextArea.setBackground(SystemColor.menu);
		instructionTextArea.setBackground(Color.black);
		instructionTextArea.setForeground(Color.white);
		
		JPanel inputPanel = new JPanel();
		inputPanel.setBorder(new TitledBorder(null, "Input Settings", TitledBorder.LEADING, TitledBorder.TOP, null, Color.white));
		add(inputPanel);
		inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS));
		inputPanel.setBackground(Color.black);
		inputPanel.setForeground(Color.white);
		
		JPanel inputLeftPanel = new JPanel();
		inputPanel.add(inputLeftPanel);
		inputLeftPanel.setLayout(new BoxLayout(inputLeftPanel, BoxLayout.Y_AXIS));
		inputLeftPanel.setBackground(Color.black);
		inputLeftPanel.setForeground(Color.white);
		
		JTextArea cipherTextInputTextArea = new JTextArea();
		inputLeftPanel.add(cipherTextInputTextArea);
		cipherTextInputTextArea.setWrapStyleWord(true);
		cipherTextInputTextArea.setRows(5);
		cipherTextInputTextArea.setLineWrap(true);
		cipherTextInputTextArea.setColumns(50);
		
		JPanel inputControlPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) inputControlPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		inputLeftPanel.add(inputControlPanel);
		inputControlPanel.setBackground(Color.black);
		inputControlPanel.setForeground(Color.white);
		
		JButton decryptButton = new JButton("Decrypt...");
		inputControlPanel.add(decryptButton);
		
		JLabel label_4 = new JLabel("Thread Limit:");
		inputControlPanel.add(label_4);
		label_4.setBackground(Color.black);
		label_4.setForeground(Color.white);
		
		JSpinner threadCountSpinner = new JSpinner();
		threadCountSpinner.setModel(new SpinnerNumberModel(2, 1, 16, 1));
		inputControlPanel.add(threadCountSpinner);
		
		JLabel label_5 = new JLabel("Candidate Size:");
		inputControlPanel.add(label_5);
		label_5.setBackground(Color.black);
		label_5.setForeground(Color.white);
		
		JSpinner candidateSizeSpinner = new JSpinner();
		candidateSizeSpinner.setModel(new SpinnerNumberModel(100, 100, 5000, 100));
		inputControlPanel.add(candidateSizeSpinner);
		
		JLabel label_7 = new JLabel("Progress:");
		inputControlPanel.add(label_7);
		label_7.setBackground(Color.black);
		label_7.setForeground(Color.white);
		
		JProgressBar progressBar = new JProgressBar();
		inputControlPanel.add(progressBar);
		
		JPanel inputSettingsPanel = new JPanel();
		inputPanel.add(inputSettingsPanel);
		inputSettingsPanel.setLayout(new BoxLayout(inputSettingsPanel, BoxLayout.Y_AXIS));
		inputSettingsPanel.setBackground(Color.black);
		inputSettingsPanel.setForeground(Color.white);
		
		JPanel rotorOrderPanel = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) rotorOrderPanel.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		inputSettingsPanel.add(rotorOrderPanel);
		rotorOrderPanel.setBackground(Color.black);
		rotorOrderPanel.setForeground(Color.white);
		
		JLabel label = new JLabel("Rotor Selection:");
		rotorOrderPanel.add(label);
		label.setBackground(Color.black);
		label.setForeground(Color.white);
		
		JSpinner fourthRotorSpinner = new JSpinner();
		fourthRotorSpinner.setModel(new SpinnerNumberModel(1, 1, 8, 1));
		fourthRotorSpinner.setPreferredSize(new Dimension(35, 20));
		rotorOrderPanel.add(fourthRotorSpinner);
		
		JSpinner leftRotorSpinner = new JSpinner();
		leftRotorSpinner.setModel(new SpinnerNumberModel(1, 1, 8, 1));
		leftRotorSpinner.setPreferredSize(new Dimension(35, 20));
		rotorOrderPanel.add(leftRotorSpinner);
		
		JSpinner middleRotorSpinner = new JSpinner();
		middleRotorSpinner.setModel(new SpinnerNumberModel(1, 1, 8, 1));
		middleRotorSpinner.setPreferredSize(new Dimension(35, 20));
		rotorOrderPanel.add(middleRotorSpinner);
		
		JSpinner rightRotorSpinner = new JSpinner();
		rightRotorSpinner.setModel(new SpinnerNumberModel(1, 1, 8, 1));
		rightRotorSpinner.setPreferredSize(new Dimension(35, 20));
		rotorOrderPanel.add(rightRotorSpinner);
		
		JPanel reflectorPanel = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) reflectorPanel.getLayout();
		flowLayout_2.setAlignment(FlowLayout.LEFT);
		inputSettingsPanel.add(reflectorPanel);
		reflectorPanel.setBackground(Color.black);
		reflectorPanel.setForeground(Color.white);
		
		JLabel label_1 = new JLabel("Reflector:");
		reflectorPanel.add(label_1);
		label_1.setBackground(Color.black);
		label_1.setForeground(Color.white);
		
		JSpinner reflectorSpinner = new JSpinner();
		reflectorPanel.add(reflectorSpinner);
		reflectorSpinner.setModel(new SpinnerListModel(new String[] {"B", "C", "B Thin", "C Thin"}));
		reflectorSpinner.setPreferredSize(new Dimension(60, 20));
		
		JPanel indicatorPanel = new JPanel();
		FlowLayout flowLayout_3 = (FlowLayout) indicatorPanel.getLayout();
		flowLayout_3.setAlignment(FlowLayout.LEFT);
		inputSettingsPanel.add(indicatorPanel);
		indicatorPanel.setBackground(Color.black);
		indicatorPanel.setForeground(Color.white);
		
		JLabel lblIndicatorSettings = new JLabel("Indicator Settings:");
		indicatorPanel.add(lblIndicatorSettings);
		lblIndicatorSettings.setBackground(Color.black);
		lblIndicatorSettings.setForeground(Color.white);
		
		JSpinner leftIndicatorPanel = new JSpinner();
		leftIndicatorPanel.setModel(new SpinnerListModel(new String[] {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"}));
		leftIndicatorPanel.setPreferredSize(new Dimension(35, 20));
		indicatorPanel.add(leftIndicatorPanel);
		
		JSpinner middleIndicatorPanel = new JSpinner();
		middleIndicatorPanel.setModel(new SpinnerListModel(new String[] {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"}));
		middleIndicatorPanel.setPreferredSize(new Dimension(35, 20));
		indicatorPanel.add(middleIndicatorPanel);
		
		JSpinner rightIndicatorPanel = new JSpinner();
		rightIndicatorPanel.setModel(new SpinnerListModel(new String[] {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"}));
		rightIndicatorPanel.setPreferredSize(new Dimension(35, 20));
		indicatorPanel.add(rightIndicatorPanel);
		
		JPanel ringPanel = new JPanel();
		FlowLayout flowLayout_4 = (FlowLayout) ringPanel.getLayout();
		flowLayout_4.setAlignment(FlowLayout.LEFT);
		inputSettingsPanel.add(ringPanel);
		ringPanel.setBackground(Color.black);
		ringPanel.setForeground(Color.white);
		
		JLabel label_2 = new JLabel("Ring Settings:");
		ringPanel.add(label_2);
		label_2.setBackground(Color.black);
		label_2.setForeground(Color.white);
		
		JSpinner leftRingSpinner = new JSpinner();
		leftRingSpinner.setModel(new SpinnerListModel(new String[] {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"}));
		leftRingSpinner.setPreferredSize(new Dimension(35, 20));
		ringPanel.add(leftRingSpinner);
		
		JSpinner middleRingSpinner = new JSpinner();
		middleRingSpinner.setModel(new SpinnerListModel(new String[] {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"}));
		middleRingSpinner.setPreferredSize(new Dimension(35, 20));
		ringPanel.add(middleRingSpinner);
		
		JSpinner rightRingSpinner = new JSpinner();
		rightRingSpinner.setModel(new SpinnerListModel(new String[] {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"}));
		rightRingSpinner.setPreferredSize(new Dimension(35, 20));
		ringPanel.add(rightRingSpinner);
		
		JPanel plugboardPanel = new JPanel();
		FlowLayout flowLayout_5 = (FlowLayout) plugboardPanel.getLayout();
		flowLayout_5.setAlignment(FlowLayout.LEFT);
		inputSettingsPanel.add(plugboardPanel);
		plugboardPanel.setBackground(Color.black);
		plugboardPanel.setForeground(Color.white);
		
		JLabel label_3 = new JLabel("Plugboard Settings:");
		plugboardPanel.add(label_3);
		label_3.setBackground(Color.black);
		label_3.setForeground(Color.white);
		
		plugboardTextField = new JTextField();
		plugboardTextField.setColumns(20);
		plugboardPanel.add(plugboardTextField);
		
		ResultsPanel resultsPanel = new ResultsPanel();
		add(resultsPanel);
		
		JPanel cribTestPanel = new JPanel();
		add(cribTestPanel);
		cribTestPanel.setBackground(Color.black);
		cribTestPanel.setForeground(Color.white);
		
		JTextArea cribTestTextArea = new JTextArea();
		cribTestTextArea.setColumns(50);
		cribTestTextArea.setRows(10);
		cribTestPanel.add(cribTestTextArea);
		
		JPanel cribControlPanel = new JPanel();
		cribTestPanel.add(cribControlPanel);
		cribControlPanel.setBackground(Color.black);
		cribControlPanel.setForeground(Color.white);
		
		JButton cribCheckButton = new JButton("Check Cribs...");
		cribControlPanel.add(cribCheckButton);
	}

}
