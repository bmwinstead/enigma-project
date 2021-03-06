package main.java.GUINew;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;

import main.java.cryptanalysis.nlp.Corpus;
import main.java.cryptanalysis.quadbomb.QuadBombSettings;
import main.java.cryptanalysis.quadbomb.QuadbombManager;

/**
 * User Interface for Cryptanalysis components.
 * Contains all cryptanalysis fields and options.
 * The user can enter the message in the input field and set specific
 * constraints if part of the key is known (for example, some or all of
 * the rotors are known, but not the reflector). After processing is complete,
 * the GUI displays the QuadBomb results and what it believes to be the most
 * likely combination of settings and decryption. A four-rotor decryption
 * attempt using no constraints can potentially take a considerable amount of
 * time. The user is warned about this in the instructions, and an abort option
 * is provided. 
 * 
 * @author Walter Adolph
 * @author Team Enigma
 * @version 0.5
 * 12/2/2013
 */

@SuppressWarnings("serial")
public class CaGuiPrototype extends JPanel {
	private Corpus database;
	private QuadbombManager analyzer;
	
	private JTextField plugboardTextField;
	private JSpinner threadCountSpinner;
	private JSpinner candidateSizeSpinner;
	private ResultsPanel resultsPanel;
	private JProgressBar decryptProgressBar;
	private JTextArea cipherTextInputTextArea;
	private JComboBox<String> fourthRotorComboBox;
	private JComboBox<String> leftRotorComboBox;
	private JComboBox<String> middleRotorComboBox;
	private JComboBox<String> rightRotorComboBox;
	private JComboBox<String> reflectorComboBox;
	private JComboBox<String> fourthRingComboBox;
	private JComboBox<String> leftRingComboBox;
	private JComboBox<String> middleRingComboBox;
	private JComboBox<String> rightRingComboBox;
	private JComboBox<String> fourthIndicatorComboBox;
	private JComboBox<String> leftIndicatorComboBox;
	private JComboBox<String> middleIndicatorComboBox;
	private JComboBox<String> rightIndicatorComboBox;
	private JPanel buttonInputPanel;
	private JButton abortButton;
	private JPanel spinnerInputPanel;
	private JPanel statusInputPanel;
	private JPanel statusLabelPanel;
	private JLabel lblNewLabel;
	private JTextField statusTextField;
	private JPanel progressBarPanel;
	private JPanel inputFlowPanel;
	private JButton decryptButton;
	
	/**
	 * Creates panel to allow user interface with the Enigma Cryptanalysis
	 * functions. 
	 */
	public CaGuiPrototype() {
		String resourceLoc = "/main/resources/training.corpus";
		ObjectInputStream is = null;
		try{
			is = new ObjectInputStream(getClass().getResourceAsStream(resourceLoc));
			database = (Corpus)is.readObject();
		} catch(NullPointerException e){
			resourceLoc = "/training.corpus";
			try {
				is = new ObjectInputStream(getClass().getResourceAsStream(resourceLoc));
				database = (Corpus)is.readObject();
			} catch (IOException | ClassNotFoundException e1) {
				JOptionPane.showMessageDialog(null, "Corpus not found");
			}
		} catch (IOException | ClassNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Corpus corrupt");
		}
		
		// Automatically generated code.
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JTextArea instructionTextArea = new JTextArea();
		add(instructionTextArea);
		instructionTextArea.setWrapStyleWord(true);
		instructionTextArea.setText("Instructions: Enter a valid Enigma encrypted text below in the text box. Increasing the thread count will speed decryption attempts, but will consume more CPU resources. Increasing the candidate size will improve odds of decryption, but will take longer. Also, the longer the message, the longer it will take to decrypt, but the chances of a successful decryption will be higher. If some of the settings are known, they can be entered on the right. With more known settings, the faster the algorithm will run and the odds of decryption will be higher.\r\n\r\nNote: With most computers, decryption will take quite some time to run. Decryption times can vary from a few minutes on high-end computers to an hour or more on most machines.");
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
		
		cipherTextInputTextArea = new JTextArea();
		inputLeftPanel.add(cipherTextInputTextArea);
		cipherTextInputTextArea.setWrapStyleWord(true);
		cipherTextInputTextArea.setRows(5);
		cipherTextInputTextArea.setLineWrap(true);
		cipherTextInputTextArea.setColumns(50);
		
		JPanel inputControlPanel = new JPanel();
		inputLeftPanel.add(inputControlPanel);
		inputControlPanel.setBackground(Color.black);
		inputControlPanel.setForeground(Color.white);
		inputControlPanel.setLayout(new GridLayout(0, 1, 0, 0));
		
		inputFlowPanel = new JPanel();
		inputFlowPanel.setBackground(Color.BLACK);
		inputControlPanel.add(inputFlowPanel);
		inputFlowPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		buttonInputPanel = new JPanel();
		inputFlowPanel.add(buttonInputPanel);
		buttonInputPanel.setBackground(Color.BLACK);
		buttonInputPanel.setLayout(new GridLayout(2, 1, 2, 2));
		
		decryptButton = new JButton("Decrypt...");
		buttonInputPanel.add(decryptButton);
		
		abortButton = new JButton("Abort");
		
		abortButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (analyzer != null) {
					analyzer.abort();
					analyzer.cancel(true);
					
					decryptButton.setEnabled(true);
					statusTextField.setText("Aborted...");
				}
			}
		});
		
		buttonInputPanel.add(abortButton);
		
		spinnerInputPanel = new JPanel();
		inputFlowPanel.add(spinnerInputPanel);
		spinnerInputPanel.setBackground(Color.BLACK);
		spinnerInputPanel.setLayout(new GridLayout(0, 2, 2, 2));
		
		JLabel label_4 = new JLabel("Thread Limit:");
		spinnerInputPanel.add(label_4);
		label_4.setBackground(Color.black);
		label_4.setForeground(Color.white);
		
		threadCountSpinner = new JSpinner();
		spinnerInputPanel.add(threadCountSpinner);
		threadCountSpinner.setModel(new SpinnerNumberModel(2, 1, 16, 1));
		
		JLabel label_5 = new JLabel("Candidate Size:");
		spinnerInputPanel.add(label_5);
		label_5.setBackground(Color.black);
		label_5.setForeground(Color.white);
		
		candidateSizeSpinner = new JSpinner();
		spinnerInputPanel.add(candidateSizeSpinner);
		candidateSizeSpinner.setModel(new SpinnerNumberModel(100, 100, 5000, 100));
		
		statusInputPanel = new JPanel();
		statusInputPanel.setBackground(Color.BLACK);
		inputFlowPanel.add(statusInputPanel);
		statusInputPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		statusLabelPanel = new JPanel();
		statusLabelPanel.setBackground(Color.BLACK);
		statusInputPanel.add(statusLabelPanel);
		statusLabelPanel.setLayout(new GridLayout(2, 1, 2, 2));
		
		lblNewLabel = new JLabel("Status:");
		lblNewLabel.setForeground(Color.WHITE);
		statusLabelPanel.add(lblNewLabel);
		
		JLabel label_7 = new JLabel("Progress:");
		statusLabelPanel.add(label_7);
		label_7.setBackground(Color.black);
		label_7.setForeground(Color.white);
		
		progressBarPanel = new JPanel();
		progressBarPanel.setBackground(Color.BLACK);
		statusInputPanel.add(progressBarPanel);
		progressBarPanel.setLayout(new GridLayout(2, 1, 2, 2));
		
		statusTextField = new JTextField();
		progressBarPanel.add(statusTextField);
		statusTextField.setBorder(null);
		statusTextField.setEditable(false);
		statusTextField.setText("Ready...");
		statusTextField.setForeground(Color.WHITE);
		statusTextField.setBackground(Color.BLACK);
		statusTextField.setColumns(10);
		
		decryptProgressBar = new JProgressBar();
		decryptProgressBar.setPreferredSize(new Dimension(260, 15));
		progressBarPanel.add(decryptProgressBar);
		
		// Decrypts an encrypted messaging using QuadBomb.
		decryptButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				Pattern nonalphanumeric = Pattern.compile("[^a-zA-Z\\s]");
				Matcher invalidCharacterMatcher = nonalphanumeric.matcher(cipherTextInputTextArea.getText().replace(" ", ""));
				
				boolean validCipherText = !invalidCharacterMatcher.find() && cipherTextInputTextArea.getText().trim().length() > 0;
				
				invalidCharacterMatcher = invalidCharacterMatcher.reset(plugboardTextField.getText());
				boolean validPlugboardText = !invalidCharacterMatcher.find() && plugboardTextField.getText().replace(" ", "").length() % 2 == 0;
				
				if (database.getTotalQuadgramCount() > 0 && validCipherText && validPlugboardText) {
					String[] words = cipherTextInputTextArea.getText().toUpperCase().split("\\s");	// Split on whitespace.
					String cipher = "";
					
					// Remove whitespace.
					for (String word: words) {
						cipher += word;
					}

					decryptProgressBar.setValue(0);
					resultsPanel.clearSolution();
					statusTextField.setText("Starting...");
					
					analyzer = new QuadbombManager(database, cipher, getSettings(), statusTextField, decryptButton, resultsPanel);
					
					analyzer.addPropertyChangeListener(new PropertyChangeListener() {
						public void propertyChange(PropertyChangeEvent event) {
							if (event.getPropertyName().equals("progress")) {
								decryptProgressBar.setValue((Integer)event.getNewValue());
							}
						}
					});
					
					analyzer.execute();
					decryptButton.setEnabled(false);
				}
				else if (!validCipherText) {
					JOptionPane.showMessageDialog(null, "Error - Invalid encrypted text.", "Invalid Ciphertext",  JOptionPane.ERROR_MESSAGE);
				}
				else if (!validPlugboardText) {
					JOptionPane.showMessageDialog(null, "Error - Invalid plugboard settings.", "Invalid Plugboard",  JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
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
		
		fourthRotorComboBox = new JComboBox<String>();
		fourthRotorComboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"None", "Any", "Beta", "Gamma"}));
		rotorOrderPanel.add(fourthRotorComboBox);
		
		leftRotorComboBox = new JComboBox<String>();
		leftRotorComboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"Any", "I", "II", "III", "IV", "V", "VI", "VII", "VIII"}));
		rotorOrderPanel.add(leftRotorComboBox);
		
		middleRotorComboBox = new JComboBox<String>();
		middleRotorComboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"Any", "I", "II", "III", "IV", "V", "VI", "VII", "VIII"}));
		rotorOrderPanel.add(middleRotorComboBox);
		
		rightRotorComboBox = new JComboBox<String>();
		rightRotorComboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"Any", "I", "II", "III", "IV", "V", "VI", "VII", "VIII"}));
		rotorOrderPanel.add(rightRotorComboBox);
		
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
		
		reflectorComboBox = new JComboBox<String>();
		reflectorComboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"Any", "B", "C", "B Thin", "C Thin"}));
		reflectorPanel.add(reflectorComboBox);
		
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
		
		fourthRingComboBox = new JComboBox<String>();
		fourthRingComboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"Any", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"}));
		ringPanel.add(fourthRingComboBox);
		
		leftRingComboBox = new JComboBox<String>();
		leftRingComboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"Any", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"}));
		ringPanel.add(leftRingComboBox);
		
		middleRingComboBox = new JComboBox<String>();
		middleRingComboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"Any", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"}));
		ringPanel.add(middleRingComboBox);
		
		rightRingComboBox = new JComboBox<String>();
		rightRingComboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"Any", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"}));
		ringPanel.add(rightRingComboBox);
		
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
		
		fourthIndicatorComboBox = new JComboBox<String>();
		fourthIndicatorComboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"Any", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"}));
		indicatorPanel.add(fourthIndicatorComboBox);
		
		leftIndicatorComboBox = new JComboBox<String>();
		leftIndicatorComboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"Any", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"}));
		indicatorPanel.add(leftIndicatorComboBox);
		
		middleIndicatorComboBox = new JComboBox<String>();
		middleIndicatorComboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"Any", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"}));
		indicatorPanel.add(middleIndicatorComboBox);
		
		rightIndicatorComboBox = new JComboBox<String>();
		rightIndicatorComboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"Any", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"}));
		indicatorPanel.add(rightIndicatorComboBox);
		
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
		
		resultsPanel = new ResultsPanel();
		add(resultsPanel);
		
		JTextArea cribTestTextArea = new JTextArea();
		cribTestTextArea.setColumns(50);
		cribTestTextArea.setRows(10);
		
		JPanel cribControlPanel = new JPanel();
		cribControlPanel.setBackground(Color.black);
		cribControlPanel.setForeground(Color.white);
		
		JButton cribCheckButton = new JButton("Check Cribs...");
		cribControlPanel.add(cribCheckButton);
	}
	
	private QuadBombSettings getSettings() {
		int[] rotors = new int[4];
		int[] ringSettings = new int[4];
		int[] indicatorSettings = new int[4];
		
		rotors[0] = fourthRotorComboBox.getSelectedIndex();
		rotors[1] = leftRotorComboBox.getSelectedIndex();
		rotors[2] = middleRotorComboBox.getSelectedIndex();
		rotors[3] = rightRotorComboBox.getSelectedIndex();
		
		ringSettings[0] = fourthRingComboBox.getSelectedIndex();
		ringSettings[1] = leftRingComboBox.getSelectedIndex();
		ringSettings[2] = middleRingComboBox.getSelectedIndex();
		ringSettings[3] = rightRingComboBox.getSelectedIndex();
		
		indicatorSettings[0] = fourthIndicatorComboBox.getSelectedIndex();
		indicatorSettings[1] = leftIndicatorComboBox.getSelectedIndex();
		indicatorSettings[2] = middleIndicatorComboBox.getSelectedIndex();
		indicatorSettings[3] = rightIndicatorComboBox.getSelectedIndex();
		
		int threadLimit = (int)(threadCountSpinner.getValue());
		int candidateSize = (int)(candidateSizeSpinner.getValue());
		
		return new QuadBombSettings(rotors, reflectorComboBox.getSelectedIndex(), ringSettings, indicatorSettings, plugboardTextField.getText(), threadLimit, candidateSize);
	}
}
