package views;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.PriorityQueue;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerListModel;
import javax.swing.UIManager;

import decoders.CribDetector;

import machine.CA_Rotor;
import machine.Encryptor;
import misc.Logger;
import nlp.CharacterParser;
import nlp.Corpus;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

// Testing GUI interface for word demonstration.
// Used GWT Designer in Eclipse to build GUI.
// This was designed on Ubuntu, Windows builds may look off,
// but this was intended for proof-of-concept and to get an idea
// of GUI requirements.

public class TestPanel extends JFrame {
	private JTextField fileTextField;
	
	private Corpus database;
	private JTextField outputTextField;
	private JTextField leftRotorTextField;
	private JTextField middleRotorTextField;
	private JTextField rightRotorTextField;
	private JSpinner leftRotorSpinner;
	private JSpinner middleRotorSpinner;
	private JSpinner rightRotorSpinner;
	private JTextArea inputTextArea;
	private JTextArea encryptedTextArea;
	private JTextArea decryptedTextArea;
	private JTextField leftRingTextField;
	private JTextField middleRingTextField;
	private JTextField rightRingTextField;
	private JTextField plugboardPairsTextField;
	private JSpinner leftRingSpinner;
	private JSpinner middleRingSpinner;
	private JSpinner rightRingSpinner;
	
	private String[] alphabet = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
	
	public TestPanel() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				Logger.makeEntry("TestPanel closed.", true);
				Logger.closeFile();
			}
		});
		
		database = new Corpus();
		Logger.createFile();
		
		// Frame init. setup.
		setTitle("Word Database Generator");
		setSize(700, 700);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel controlPanel = new JPanel();
		getContentPane().add(controlPanel, BorderLayout.NORTH);
		controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
		
		JPanel instructionPanel = new JPanel();
		controlPanel.add(instructionPanel);
		
		JTextArea instructionsTextArea = new JTextArea();
		instructionsTextArea.setFont(new Font("Tahoma", Font.PLAIN, 11));
		instructionsTextArea.setLineWrap(true);
		instructionsTextArea.setBackground(UIManager.getColor("Panel.background"));
		instructionsTextArea.setEditable(false);
		instructionsTextArea.setColumns(40);
		instructionsTextArea.setRows(3);
		instructionsTextArea.setWrapStyleWord(true);
		instructionsTextArea.setText("Instructions: Click Browse or type in a file path to select a text file for parsing, and select Parse  to break the text into 1, 2, and 3 word groups (unigrams, bigrams, and trigrams). Adding subsequent files will add to the databases. Note: Text may take some time to parse.");
		instructionPanel.add(instructionsTextArea);
		
		JPanel buttonPanel = new JPanel();
		controlPanel.add(buttonPanel);
		
		fileTextField = new JTextField(20);
		buttonPanel.add(fileTextField);
		
		JButton browseButton = new JButton("Browse...");
		buttonPanel.add(browseButton);
		
		JButton parseButton = new JButton("Parse");
		buttonPanel.add(parseButton);
		
		// Parse a text file and display results..
		parseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File file = new File(fileTextField.getText());
				Logger.makeEntry("Started parsing file " + file.getPath() + " ...", true);
				
				long startTime = System.currentTimeMillis();
				
				CharacterParser parser = new CharacterParser(database);
				parser.parseFile(file);
				
				long endTime = System.currentTimeMillis();
				
				Logger.makeEntry("Parsing completed in " + (endTime - startTime) + " milliseconds.", true);
				Logger.makeEntry("Corpus now has " + database.getUnigramCount() + " unigrams.", true);
				Logger.makeEntry("Corpus now has " + database.getBigramCount() + " bigrams.", true);
				Logger.makeEntry("Corpus now has " + database.getTrigramCount() + " trigrams.", true);
				Logger.makeEntry("Corpus now has " + database.getQuadgramCount() + " quadgrams.", true);
			}
		});
		browseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();

				if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					fileTextField.setText(fileChooser.getSelectedFile().getAbsolutePath());
				}
			}
		});
		
		JPanel statisticsPanel = new JPanel();
		getContentPane().add(statisticsPanel, BorderLayout.EAST);
		
		JPanel centerPanel = new JPanel();
		getContentPane().add(centerPanel, BorderLayout.CENTER);
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
		
		JPanel textEntryPanel = new JPanel();
		centerPanel.add(textEntryPanel);
		
		JLabel lblPlainTextEntry = new JLabel("Plain Text Entry:");
		textEntryPanel.add(lblPlainTextEntry);
		
		inputTextArea = new JTextArea();
		inputTextArea.setLineWrap(true);
		inputTextArea.setWrapStyleWord(true);
		inputTextArea.setRows(5);
		inputTextArea.setColumns(20);
		textEntryPanel.add(inputTextArea);
		
		JPanel rotorSettingPanel = new JPanel();
		centerPanel.add(rotorSettingPanel);
		
		JLabel lblNewLabel_4 = new JLabel("Rotor Settings:");
		rotorSettingPanel.add(lblNewLabel_4);
		
		leftRotorSpinner = new JSpinner();
		leftRotorSpinner.setModel(new SpinnerListModel(alphabet));
		rotorSettingPanel.add(leftRotorSpinner);
		
		middleRotorSpinner = new JSpinner();
		middleRotorSpinner.setModel(new SpinnerListModel(alphabet));
		rotorSettingPanel.add(middleRotorSpinner);
		
		rightRotorSpinner = new JSpinner();
		rightRotorSpinner.setModel(new SpinnerListModel(alphabet));
		rotorSettingPanel.add(rightRotorSpinner);
		
		JButton encryptButton = new JButton("Encrypt");
		
		// Encrypt a text string.
		encryptButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				char leftRotorSetting = ((String)(leftRotorSpinner.getValue())).toCharArray()[0];
				char middleRotorSetting = ((String)(middleRotorSpinner.getValue())).toCharArray()[0];
				char rightRotorSetting = ((String)(rightRotorSpinner.getValue())).toCharArray()[0];
				char leftRingSetting = ((String)(leftRingSpinner.getValue())).toCharArray()[0];
				char middleRingSetting = ((String)(middleRingSpinner.getValue())).toCharArray()[0];
				char rightRingSetting = ((String)(rightRingSpinner.getValue())).toCharArray()[0];
				
				Logger.makeEntry("Starting encryption...", true);
				Logger.makeEntry("Rotors - 123", true);
				Logger.makeEntry("Reflector - B", true);
				Logger.makeEntry("Ring Settings - " + leftRingSetting  + middleRingSetting + rightRingSetting, true);
				Logger.makeEntry("Rotor Settings - " + leftRotorSetting  + middleRotorSetting + rightRotorSetting, true);
				
				String text = inputTextArea.getText();
				
				// Init. rotors.
				CA_Rotor rotorI = CA_Rotor.getNewRotor('1');
				CA_Rotor rotorII = CA_Rotor.getNewRotor('2');
				CA_Rotor rotorIII = CA_Rotor.getNewRotor('3');
				CA_Rotor reflectorB = CA_Rotor.getNewRotor('B');
				
				// Set ring settings.
				rotorI.setRingPosition(leftRingSetting);
				rotorII.setRingPosition(middleRingSetting);
				rotorIII.setRingPosition(rightRingSetting);
				
				Encryptor machine = new Encryptor(
						rotorI,
						rotorII,
						rotorIII,
						reflectorB);
				
				machine.setRotorPositions(leftRotorSetting, middleRotorSetting, rightRotorSetting);
				
				String cipher = machine.encrypt(text);
				Logger.makeEntry("Encryption Complete.\r\n", true);
		
				encryptedTextArea.setText(cipher);
				
				machine.setRotorPositions(leftRotorSetting, middleRotorSetting, rightRotorSetting);
				decryptedTextArea.setText(machine.encrypt(cipher));
				Logger.makeEntry("Decryption Complete.\r\n", true);
			}
		});
		
		JPanel ringSettingPanel = new JPanel();
		centerPanel.add(ringSettingPanel);
		
		JLabel lblNewLabel = new JLabel("Ring Settings:");
		ringSettingPanel.add(lblNewLabel);
		
		leftRingSpinner = new JSpinner();
		leftRingSpinner.setModel(new SpinnerListModel(alphabet));
		ringSettingPanel.add(leftRingSpinner);
		
		middleRingSpinner = new JSpinner();
		middleRingSpinner.setModel(new SpinnerListModel(alphabet));
		ringSettingPanel.add(middleRingSpinner);
		
		rightRingSpinner = new JSpinner();
		rightRingSpinner.setModel(new SpinnerListModel(alphabet));
		ringSettingPanel.add(rightRingSpinner);
		
		centerPanel.add(encryptButton);
		
		JPanel enceryptedPanel = new JPanel();
		centerPanel.add(enceryptedPanel);
		
		JLabel lblNewLabel_3 = new JLabel("Encrypted Output:");
		enceryptedPanel.add(lblNewLabel_3);
		
		encryptedTextArea = new JTextArea();
		encryptedTextArea.setWrapStyleWord(true);
		encryptedTextArea.setLineWrap(true);
		encryptedTextArea.setColumns(20);
		encryptedTextArea.setRows(5);
		enceryptedPanel.add(encryptedTextArea);
		
		JLabel lblNewLabel_5 = new JLabel("Decrypted Output:");
		enceryptedPanel.add(lblNewLabel_5);
		
		decryptedTextArea = new JTextArea();
		decryptedTextArea.setLineWrap(true);
		decryptedTextArea.setWrapStyleWord(true);
		decryptedTextArea.setRows(5);
		decryptedTextArea.setColumns(20);
		enceryptedPanel.add(decryptedTextArea);
		
		JButton breakCodeButton = new JButton("Decrypt...");
		
		// Decrypt an encrypted message by brute force using cribs.
		breakCodeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PriorityQueue<String> unigramQueue = database.getUnigramTestQueue();
				
				while (!unigramQueue.isEmpty()) {
					CribDetector detector = new CribDetector(encryptedTextArea.getText());
					
					String settings = detector.testCrib(unigramQueue.remove());
					
					// Init. rotors.
					CA_Rotor rotorI = CA_Rotor.getNewRotor('1');
					CA_Rotor rotorII = CA_Rotor.getNewRotor('2');
					CA_Rotor rotorIII = CA_Rotor.getNewRotor('3');
					CA_Rotor reflectorB = CA_Rotor.getNewRotor('B');
					
					// Set ring settings.
					rotorI.setRingPosition((char)leftRingSpinner.getValue());
					rotorII.setRingPosition((char)leftRingSpinner.getValue());
					rotorIII.setRingPosition((char)leftRingSpinner.getValue());
					
					if (settings != "") {
						Encryptor machine = new Encryptor(
								rotorI,
								rotorII,
								rotorIII,
								reflectorB);
						
						machine.setRotorPositions(settings.charAt(0), settings.charAt(1), settings.charAt(2));
						
						String result = machine.encrypt(encryptedTextArea.getText());
						
						if (database.testString(result)) {
							outputTextField.setText(result);
							
							leftRotorTextField.setText("" + settings.charAt(0));
							middleRotorTextField.setText("" + settings.charAt(1));
							rightRotorTextField.setText("" + settings.charAt(2));
							
							break;
						}
					}
				}
			}
		});
		
		centerPanel.add(breakCodeButton);
		
		JPanel outputPanel = new JPanel();
		centerPanel.add(outputPanel);
		
		outputTextField = new JTextField(40);
		outputPanel.add(outputTextField);
		
		JPanel settingsPanel = new JPanel();
		outputPanel.add(settingsPanel);
		
		leftRotorTextField = new JTextField(2);
		settingsPanel.add(leftRotorTextField);
		
		middleRotorTextField = new JTextField(2);
		settingsPanel.add(middleRotorTextField);
		
		rightRotorTextField = new JTextField(2);
		settingsPanel.add(rightRotorTextField);
		
		leftRingTextField = new JTextField();
		settingsPanel.add(leftRingTextField);
		leftRingTextField.setColumns(2);
		
		middleRingTextField = new JTextField();
		settingsPanel.add(middleRingTextField);
		middleRingTextField.setColumns(2);
		
		rightRingTextField = new JTextField();
		settingsPanel.add(rightRingTextField);
		rightRingTextField.setColumns(2);
		
		plugboardPairsTextField = new JTextField();
		settingsPanel.add(plugboardPairsTextField);
		plugboardPairsTextField.setColumns(30);
		
		Logger.makeEntry("TestPanel initialized.", true);
	}
	
}
