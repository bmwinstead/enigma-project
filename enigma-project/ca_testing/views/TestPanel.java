package views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;

import main.java.cryptanalysis.nlp.CharacterParser;
import main.java.cryptanalysis.nlp.Corpus;
import main.java.cryptanalysis.quadbomb.QuadbombManager;
import main.java.enigma.EnigmaMachine;
import misc.Logger;

// Testing GUI interface for word demonstration.
// Used GWT Designer in Eclipse to build GUI.
// This was designed on Ubuntu, Windows builds may look off,
// but this was intended for proof-of-concept and to get an idea
// of GUI requirements.

public class TestPanel extends JFrame {
	private JTextField fileTextField;
	
	private Corpus database;
	private JSpinner leftRotorSelectionSpinner;
	private JSpinner middleRotorSelectionSpinner;
	private JSpinner rightRotorSelectionSpinner;
	private JTextArea inputTextArea;
	private JTextArea encryptedTextArea;
	private JTextArea decryptedTextArea;
	private JSpinner leftRingSpinner;
	private JSpinner middleRingSpinner;
	private JSpinner rightRingSpinner;
	
	private String[] alphabet = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
	private JTextField plugboardTextField;
	private JSpinner leftRotorSpinner;
	private JSpinner middleRotorSpinner;
	private JSpinner rightRotorSpinner;
	private JSpinner reflectorSpinner;
	
	private Logger log;
	private JSpinner threadCountSpinner;
	private ResultsPanel resultsPanel;
	private JProgressBar decryptProgressBar;
	private JButton breakCodeButton;
	private JSpinner candidateSpinner;
	private JComboBox testComboBox;
	
	public TestPanel() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				log.makeEntry("TestPanel closed.", true);
				log.closeFile();
			}
		});
		
		database = new Corpus();
		log = new Logger("TestPanel log.txt");
		
		// Frame init. setup.
		setTitle("Word Database Generator");
		setSize(950, 757);
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
		
		JButton saveCorpusButton = new JButton("Save..");
		saveCorpusButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();

				if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
					try {
						log.makeEntry("Saving corpus....", true);
						ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(fileChooser.getSelectedFile() + ".corpus"));
						output.writeObject(database);
						output.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				log.makeEntry("Corpus saved.", true);
			}
		});
		buttonPanel.add(saveCorpusButton);
		
		JButton loadCorpusButton = new JButton("Load...");
		loadCorpusButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				
				if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					try {
						log.makeEntry("Loading corpus....", true);
						
						FileInputStream fileStream = new FileInputStream(fileChooser.getSelectedFile());
						ObjectInputStream objectStream = new ObjectInputStream(fileStream);
						database = (Corpus) objectStream.readObject();
						objectStream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				log.makeEntry("Corpus loaded.", true);
			}
		});
		buttonPanel.add(loadCorpusButton);
		
		// Parse a text file and display results..
		parseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File file = new File(fileTextField.getText());

				log.makeEntry("Parsing " + file.getAbsolutePath() + "...", true);
				
				long startTime = System.currentTimeMillis();
				
				CharacterParser parser = new CharacterParser(database);
				parser.parseFile(file);
				
				long endTime = System.currentTimeMillis();
				
				log.makeEntry("Parsing complete.", true);
				log.makeEntry("Parsing took " + (endTime - startTime) + " milliseconds.", true);
				log.makeEntry("Corpus now contains:", true);
				log.makeEntry(database.getTotalUnigramCount() + " unigrams.", true);
				log.makeEntry(database.getTotalBigramCount() + " bigrams.", true);
				log.makeEntry(database.getTotalTrigramCount() + " trigrams.", true);
				log.makeEntry(database.getTotalQuadgramCount() + " quadgrams.", true);
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
		inputTextArea.setColumns(50);
		textEntryPanel.add(inputTextArea);
		
		JPanel rotorSelectionPanel = new JPanel();
		centerPanel.add(rotorSelectionPanel);
		
		JLabel lblNewLabel_1 = new JLabel("Rotor Selection:");
		rotorSelectionPanel.add(lblNewLabel_1);
		
		leftRotorSpinner = new JSpinner();
		leftRotorSpinner.setPreferredSize(new Dimension(35, 20));
		leftRotorSpinner.setModel(new SpinnerNumberModel(1, 1, 5, 1));
		rotorSelectionPanel.add(leftRotorSpinner);
		
		middleRotorSpinner = new JSpinner();
		middleRotorSpinner.setPreferredSize(new Dimension(35, 20));
		middleRotorSpinner.setModel(new SpinnerNumberModel(1, 1, 5, 1));
		rotorSelectionPanel.add(middleRotorSpinner);
		
		rightRotorSpinner = new JSpinner();
		rightRotorSpinner.setPreferredSize(new Dimension(35, 20));
		rightRotorSpinner.setModel(new SpinnerNumberModel(1, 1, 5, 1));
		rotorSelectionPanel.add(rightRotorSpinner);
		
		JLabel reflectorLabel = new JLabel("Reflector:");
		rotorSelectionPanel.add(reflectorLabel);
		
		reflectorSpinner = new JSpinner();
		reflectorSpinner.setPreferredSize(new Dimension(60, 20));
		reflectorSpinner.setModel(new SpinnerListModel(new String[] {"B", "C", "B Thin", "C Thin"}));
		rotorSelectionPanel.add(reflectorSpinner);
		
		JPanel rotorSettingPanel = new JPanel();
		centerPanel.add(rotorSettingPanel);
		
		JLabel lblNewLabel_4 = new JLabel("Rotor Settings:");
		rotorSettingPanel.add(lblNewLabel_4);
		
		leftRotorSelectionSpinner = new JSpinner();
		leftRotorSelectionSpinner.setPreferredSize(new Dimension(35, 20));
		leftRotorSelectionSpinner.setModel(new SpinnerListModel(alphabet));
		rotorSettingPanel.add(leftRotorSelectionSpinner);
		
		middleRotorSelectionSpinner = new JSpinner();
		middleRotorSelectionSpinner.setPreferredSize(new Dimension(35, 20));
		middleRotorSelectionSpinner.setModel(new SpinnerListModel(alphabet));
		rotorSettingPanel.add(middleRotorSelectionSpinner);
		
		rightRotorSelectionSpinner = new JSpinner();
		rightRotorSelectionSpinner.setPreferredSize(new Dimension(35, 20));
		rightRotorSelectionSpinner.setModel(new SpinnerListModel(alphabet));
		rotorSettingPanel.add(rightRotorSelectionSpinner);
		
		JButton encryptButton = new JButton("Encrypt");
		
		// Encrypt a text string.
		encryptButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int leftRotorSelection = (int)(leftRotorSpinner.getValue()) - 1;
				int middleRotorSelection = (int)(middleRotorSpinner.getValue()) - 1;
				int rightRotorSelection = (int)(rightRotorSpinner.getValue()) - 1;
				char leftRotorSetting = ((String)(leftRotorSelectionSpinner.getValue())).toCharArray()[0];
				char middleRotorSetting = ((String)(middleRotorSelectionSpinner.getValue())).toCharArray()[0];
				char rightRotorSetting = ((String)(rightRotorSelectionSpinner.getValue())).toCharArray()[0];
				char leftRingSetting = ((String)(leftRingSpinner.getValue())).toCharArray()[0];
				char middleRingSetting = ((String)(middleRingSpinner.getValue())).toCharArray()[0];
				char rightRingSetting = ((String)(rightRingSpinner.getValue())).toCharArray()[0];
				
				log.makeEntry("Starting encryption...", true);
				log.makeEntry("Rotors - " + leftRotorSelection + middleRotorSelection + rightRotorSelection, true);
				log.makeEntry("Reflector - " + reflectorSpinner.getValue(), true);
				log.makeEntry("Ring Settings - " + leftRingSetting  + middleRingSetting + rightRingSetting, true);
				log.makeEntry("Rotor Settings - " + leftRotorSetting  + middleRotorSetting + rightRotorSetting, true);
				
				String text = inputTextArea.getText().toUpperCase();
				
				// Init. plugboard.
				String plugboardMap = plugboardTextField.getText();
				
				int[] rotors = {leftRotorSelection, middleRotorSelection, rightRotorSelection};
				int reflector = getReflectorIndex((String)(reflectorSpinner.getValue()));
				
				char[] ringSettings = {leftRingSetting, middleRingSetting, rightRingSetting};
				char[] rotorSettings = {leftRotorSetting, middleRotorSetting, rightRotorSetting};
				
				EnigmaMachine machine = new EnigmaMachine(rotors, reflector, ringSettings, rotorSettings, String.valueOf(plugboardMap));

				String cipher = machine.encryptString(text);
				log.makeEntry("Encryption Complete.\r\n", true);
		
				encryptedTextArea.setText(cipher);
				
				machine.reset();
				decryptedTextArea.setText(machine.encryptString(cipher));
				log.makeEntry("Decryption Complete.\r\n", true);
			}
		});
		
		JPanel ringSettingPanel = new JPanel();
		centerPanel.add(ringSettingPanel);
		
		JLabel lblNewLabel = new JLabel("Ring Settings:");
		ringSettingPanel.add(lblNewLabel);
		
		leftRingSpinner = new JSpinner();
		leftRingSpinner.setPreferredSize(new Dimension(35, 20));
		leftRingSpinner.setModel(new SpinnerListModel(alphabet));
		ringSettingPanel.add(leftRingSpinner);
		
		middleRingSpinner = new JSpinner();
		middleRingSpinner.setPreferredSize(new Dimension(35, 20));
		middleRingSpinner.setModel(new SpinnerListModel(alphabet));
		ringSettingPanel.add(middleRingSpinner);
		
		rightRingSpinner = new JSpinner();
		rightRingSpinner.setPreferredSize(new Dimension(35, 20));
		rightRingSpinner.setModel(new SpinnerListModel(alphabet));
		ringSettingPanel.add(rightRingSpinner);
		
		JPanel plugboardPanel = new JPanel();
		centerPanel.add(plugboardPanel);
		
		JLabel lblPlugboardSettings = new JLabel("Plugboard Settings:");
		plugboardPanel.add(lblPlugboardSettings);
		
		plugboardTextField = new JTextField();
		plugboardPanel.add(plugboardTextField);
		plugboardTextField.setColumns(20);
		
		centerPanel.add(encryptButton);
		
		JPanel encryptedPanel = new JPanel();
		centerPanel.add(encryptedPanel);
		encryptedPanel.setLayout(new BoxLayout(encryptedPanel, BoxLayout.X_AXIS));
		
		JLabel lblNewLabel_3 = new JLabel("Encrypted Output:");
		encryptedPanel.add(lblNewLabel_3);
		
		encryptedTextArea = new JTextArea();
		encryptedTextArea.setWrapStyleWord(true);
		encryptedTextArea.setLineWrap(true);
		encryptedTextArea.setColumns(40);
		encryptedTextArea.setRows(5);
		encryptedPanel.add(encryptedTextArea);
		
		JLabel lblNewLabel_5 = new JLabel("Decrypted Output:");
		encryptedPanel.add(lblNewLabel_5);
		
		decryptedTextArea = new JTextArea();
		decryptedTextArea.setLineWrap(true);
		decryptedTextArea.setWrapStyleWord(true);
		decryptedTextArea.setRows(5);
		decryptedTextArea.setColumns(40);
		encryptedPanel.add(decryptedTextArea);
		
		JPanel decryptPanel = new JPanel();
		centerPanel.add(decryptPanel);
		
		breakCodeButton = new JButton("Decrypt...");
		decryptPanel.add(breakCodeButton);
		
		JLabel lblNewLabel_10 = new JLabel("Thread Limit:");
		decryptPanel.add(lblNewLabel_10);
		
		threadCountSpinner = new JSpinner();
		threadCountSpinner.setModel(new SpinnerNumberModel(2, 1, 16, 1));
		decryptPanel.add(threadCountSpinner);
		
		JLabel lblCandidateSize = new JLabel("Candidate Size:");
		decryptPanel.add(lblCandidateSize);
		
		candidateSpinner = new JSpinner();
		candidateSpinner.setModel(new SpinnerNumberModel(100, 100, 5000, 100));
		decryptPanel.add(candidateSpinner);
		
		JLabel lblNewLabel_2 = new JLabel("Test Statistic:");
		decryptPanel.add(lblNewLabel_2);
		
		testComboBox = new JComboBox();
		testComboBox.setModel(new DefaultComboBoxModel(new String[] {"Sinkov's Unigram", "Sinkov's Bigram", "Sinkov's Trigram", "Sinkov's Quadgram", "I.O.C. Unigram", "I.O.C. Bigram", "I.O.C. Trigram", "I.O.C. Quadgram"}));
		decryptPanel.add(testComboBox);
		
		JLabel lblNewLabel_11 = new JLabel("Progress:");
		decryptPanel.add(lblNewLabel_11);
		
		decryptProgressBar = new JProgressBar();
		decryptPanel.add(decryptProgressBar);
		
		// Decrypt an encrypted message.
		breakCodeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (database.getTotalQuadgramCount() > 0) {
					String[] ciphers = encryptedTextArea.getText().split("\\s");	// Split on whitespace.
					String cipher = "";
					
					// Remove whitespace.
					for (String word: ciphers) {
						cipher += word;
					}
					
					// Init. log file for decryption attempt.
					log.makeEntry("Starting quadgram analyser...", true);

					int threadLimit = (int)(threadCountSpinner.getValue());
					int candidateSize = (int)(candidateSpinner.getValue());
					
					int statTest = testComboBox.getSelectedIndex();	// debugging to select stat test.
					QuadbombManager analyzer = new QuadbombManager(database, cipher, statTest, threadLimit, candidateSize, resultsPanel);
					
					analyzer.addPropertyChangeListener(new PropertyChangeListener() {
						public void propertyChange(PropertyChangeEvent event) {
							if (event.getPropertyName().equals("progress")) {
								decryptProgressBar.setValue((Integer)event.getNewValue());
							}
						}
					});
					
					analyzer.execute();
					
					log.makeEntry("Quadgram analyser finished.", true);
				}
			}
		});
		
		JPanel outputPanel = new JPanel();
		getContentPane().add(outputPanel, BorderLayout.SOUTH);
		outputPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		resultsPanel = new ResultsPanel();
		outputPanel.add(resultsPanel);
		
		log.makeEntry("TestPanel initialized.", true);
	}
	
	public int getReflectorIndex(String reflector) {
		switch(reflector) {
		case "B":
			return 0;
		case "C":
			return 1;
		case "B Thin":
			return 2;
		case "C Thin":
			return 3;
		default:
			return -1; // Error.
		}
	}
}
