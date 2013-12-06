/**
 * 
 */
package main.java.GUINew;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;

import main.java.cryptanalysis.nlp.Corpus;
import main.java.cryptanalysis.quadbomb.QuadbombManager;
import main.java.enigma.EnigmaSettings;

public class CaGuiPrototype extends JPanel {
	private Corpus database;
	
	
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
	
	public CaGuiPrototype() {
		// Load the corpus from the default project location.
		FileInputStream fileStream;
		try {
			fileStream = new FileInputStream("training.corpus");
			ObjectInputStream objectStream = new ObjectInputStream(fileStream);
			database = (Corpus) objectStream.readObject();
			objectStream.close();
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Automatically generated code.
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
		
		cipherTextInputTextArea = new JTextArea();
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
		
		// Decrypts an encrypted messaging using QuadBomb.
		decryptButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (database.getTotalQuadgramCount() > 0) {
					//String[] ciphers = cipherTextInputTextArea.getText().split("\\s");	// Split on whitespace.
					//String cipher = "";
					String cipher = cipherTextInputTextArea.getText().toUpperCase().replace(" ", "");
					// Remove whitespace.
					//for (String word: ciphers) {
					//	cipher += word;
					//}

					int threadLimit = (int)(threadCountSpinner.getValue());
					int candidateSize = (int)(candidateSizeSpinner.getValue());
					
					// TODO: Set static statistic tests in QuadbombManager (pending testing).
					int statTest = 3;	// Default to Sinkov's Quadgrams.
					QuadbombManager analyzer = new QuadbombManager(database, cipher, statTest, threadLimit, candidateSize, getConstraints(), resultsPanel);
					
					analyzer.addPropertyChangeListener(new PropertyChangeListener() {
						public void propertyChange(PropertyChangeEvent event) {
							if (event.getPropertyName().equals("progress")) {
								decryptProgressBar.setValue((Integer)event.getNewValue());
							}
						}
					});
					
					analyzer.execute();
				}
			}
		});
		
		inputControlPanel.add(decryptButton);
		
		JLabel label_4 = new JLabel("Thread Limit:");
		inputControlPanel.add(label_4);
		label_4.setBackground(Color.black);
		label_4.setForeground(Color.white);
		
		threadCountSpinner = new JSpinner();
		threadCountSpinner.setModel(new SpinnerNumberModel(2, 1, 16, 1));
		inputControlPanel.add(threadCountSpinner);
		
		JLabel label_5 = new JLabel("Candidate Size:");
		inputControlPanel.add(label_5);
		label_5.setBackground(Color.black);
		label_5.setForeground(Color.white);
		
		candidateSizeSpinner = new JSpinner();
		candidateSizeSpinner.setModel(new SpinnerNumberModel(100, 100, 5000, 100));
		inputControlPanel.add(candidateSizeSpinner);
		
		JLabel label_7 = new JLabel("Progress:");
		inputControlPanel.add(label_7);
		label_7.setBackground(Color.black);
		label_7.setForeground(Color.white);
		
		decryptProgressBar = new JProgressBar();
		inputControlPanel.add(decryptProgressBar);
		
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
	
	private EnigmaSettings getConstraints() {
		int[] rotors = new int[4];
		int reflector;
		char[] ringSettings = new char[4];
		char[] indicatorSettings = new char[4];
		String plugboardMap;
		
		// Three-rotor test.
		boolean isThreeRotor = fourthRotorComboBox.getSelectedIndex() == 0 || (reflectorComboBox.getSelectedIndex() == 1 && reflectorComboBox.getSelectedIndex() == 2);
		
		if (isThreeRotor) {	
			rotors[3] = -2;	// Flag to indicate not to test for fourth rotor configurations.
			ringSettings[3] = '!';
			indicatorSettings[3] = '!';
		}
		else { // Is possibly four-rotor.
			if (fourthRotorComboBox.getSelectedIndex() == 1) {
				rotors[3] = -1;	// Flag to test for any combination.
			}
			else {
				rotors[3] = fourthRotorComboBox.getSelectedIndex() + 6;	// Index adjust.
			}
			
			if (fourthRingComboBox.getSelectedIndex() == 0) {
				ringSettings[3] = '!';	// Flag to test for any combination.
			}
			else {
				ringSettings[3] = (char) (fourthRotorComboBox.getSelectedIndex() - 1 + 'A');	// Index adjust.
			}
			
			if (fourthIndicatorComboBox.getSelectedIndex() == 0) {
				indicatorSettings[3] = '!';	// Flag to test for any combination.
			}
			else {
				indicatorSettings[3] = (char) (fourthIndicatorComboBox.getSelectedIndex() - 1 + 'A');	// Index adjust.
			}
		} // End four-rotor test.
		
		// Get settings for other three rotors.
		if (leftRingComboBox.getSelectedIndex() == 0) {
			ringSettings[0] = '!';	// Flag to test for any combination.
		}
		else {
			ringSettings[0] = (char) (leftRingComboBox.getSelectedIndex() - 1 + 'A');	// Index adjust.
		}
		
		if (middleRingComboBox.getSelectedIndex() == 0) {
			ringSettings[1] = '!';	// Flag to test for any combination.
		}
		else {
			ringSettings[1] = (char) (middleRingComboBox.getSelectedIndex() - 1 + 'A');	// Index adjust.
		}
		
		if (rightRingComboBox.getSelectedIndex() == 0) {
			ringSettings[2] = '!';	// Flag to test for any combination.
		}
		else {
			ringSettings[2] = (char) (rightRingComboBox.getSelectedIndex() - 1 + 'A');	// Index adjust.
		}
		
		if (leftIndicatorComboBox.getSelectedIndex() == 0) {
			indicatorSettings[0] = '!';	// Flag to test for any combination.
		}
		else {
			indicatorSettings[0] = (char) (leftIndicatorComboBox.getSelectedIndex() - 1 + 'A');	// Index adjust.
		}
		
		if (middleIndicatorComboBox.getSelectedIndex() == 0) {
			indicatorSettings[1] = '!';	// Flag to test for any combination.
		}
		else {
			indicatorSettings[1] = (char) (middleIndicatorComboBox.getSelectedIndex() - 1 + 'A');	// Index adjust.
		}
		
		if (rightIndicatorComboBox.getSelectedIndex() == 0) {
			indicatorSettings[2] = '!';	// Flag to test for any combination.
		}
		else {
			indicatorSettings[2] = (char) (rightIndicatorComboBox.getSelectedIndex() - 1 + 'A');	// Index adjust.
		}
		// End settings check.
		
		// Get other settings.
		reflector = reflectorComboBox.getSelectedIndex() - 1;
		
		rotors[0] = leftRotorComboBox.getSelectedIndex() - 1;	// Index adjust.
		rotors[1] = middleRotorComboBox.getSelectedIndex() - 1;	// Index adjust.
		rotors[2] = rightRotorComboBox.getSelectedIndex() - 1;	// Index adjust.
		plugboardMap = plugboardTextField.getText().toUpperCase().replace(" ", "");	// Strip whitespace.
		
		return new EnigmaSettings(rotors, ringSettings, indicatorSettings, reflector, plugboardMap);
	}
}
