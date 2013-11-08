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
import nlp.Corpus;
import nlp.TextParser;

public class TestPanel extends JFrame {
	private JTextField fileTextField;
	
	private Corpus database;
	private JLabel unigramCountLabel;
	private JLabel bigramCountLabel;
	private JLabel trigramCountLabel;
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
	
	public TestPanel() {
		database = new Corpus();
		
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
		parseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File file = new File(fileTextField.getText());
				
				TextParser parser = new TextParser(database);
				parser.parseFile(file);
				
				unigramCountLabel.setText(database.getUnigramCount() + "");
				bigramCountLabel.setText(database.getBigramCount() + "");
				trigramCountLabel.setText(database.getTrigramCount() + "");
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
		
		JPanel gridPanel = new JPanel();
		statisticsPanel.add(gridPanel);
		gridPanel.setLayout(new GridLayout(0, 2, 2, 2));
		
		JLabel lblNewLabel = new JLabel("Unigram Count:");
		gridPanel.add(lblNewLabel);
		
		unigramCountLabel = new JLabel("0");
		gridPanel.add(unigramCountLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Bigram Count:");
		gridPanel.add(lblNewLabel_1);
		
		bigramCountLabel = new JLabel("0");
		gridPanel.add(bigramCountLabel);
		
		JLabel lblNewLabel_2 = new JLabel("Trigram Count:");
		gridPanel.add(lblNewLabel_2);
		
		trigramCountLabel = new JLabel("0");
		gridPanel.add(trigramCountLabel);
		
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
		leftRotorSpinner.setModel(new SpinnerListModel(new String[] {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"}));
		rotorSettingPanel.add(leftRotorSpinner);
		
		middleRotorSpinner = new JSpinner();
		middleRotorSpinner.setModel(new SpinnerListModel(new String[] {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"}));
		rotorSettingPanel.add(middleRotorSpinner);
		
		rightRotorSpinner = new JSpinner();
		rightRotorSpinner.setModel(new SpinnerListModel(new String[] {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"}));
		rotorSettingPanel.add(rightRotorSpinner);
		
		JButton encryptButton = new JButton("Encrypt");
		encryptButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				char leftRotorSetting = ((String)(leftRotorSpinner.getValue())).toCharArray()[0];
				char middleRotorSetting = ((String)(middleRotorSpinner.getValue())).toCharArray()[0];
				char rightRotorSetting = ((String)(rightRotorSpinner.getValue())).toCharArray()[0];
				
				String text = inputTextArea.getText();
				
				Encryptor machine = new Encryptor(
						CA_Rotor.ROTOR1,
						CA_Rotor.ROTOR2,
						CA_Rotor.ROTOR3,
						CA_Rotor.REFLECTORB);
				
				machine.setPositions(leftRotorSetting, middleRotorSetting, rightRotorSetting);
				
				String cipher = machine.encrypt(text);
		
				encryptedTextArea.setText(cipher);
				
				machine.setPositions(leftRotorSetting, middleRotorSetting, rightRotorSetting);
				decryptedTextArea.setText(machine.encrypt(cipher));
			}
		});
		
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
		breakCodeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PriorityQueue<String> unigramQueue = database.getUnigramTestQueue();
				
				while (!unigramQueue.isEmpty()) {
					CribDetector detector = new CribDetector(encryptedTextArea.getText());
					
					String settings = detector.testCrib(unigramQueue.remove());
					
					if (settings != "") {
						Encryptor machine = new Encryptor(
								CA_Rotor.ROTOR1,
								CA_Rotor.ROTOR2,
								CA_Rotor.ROTOR3,
								CA_Rotor.REFLECTORB);
						
						machine.setPositions(settings.charAt(0), settings.charAt(1), settings.charAt(2));
						
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
		
		outputTextField = new JTextField(20);
		outputPanel.add(outputTextField);
		
		leftRotorTextField = new JTextField(2);
		outputPanel.add(leftRotorTextField);
		
		middleRotorTextField = new JTextField(2);
		outputPanel.add(middleRotorTextField);
		
		rightRotorTextField = new JTextField(2);
		outputPanel.add(rightRotorTextField);
	}
	
}
