package views;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.BorderLayout;

import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import nlp.Corpus;
import nlp.TextParser;
import java.awt.GridLayout;
import javax.swing.JLabel;
import java.awt.SystemColor;
import javax.swing.UIManager;
import javax.swing.BoxLayout;

public class TestPanel extends JFrame {
	private JTextField fileTextField;
	
	private Corpus database;
	private JTextArea resultsTextArea;
	private JLabel unigramCountLabel;
	private JLabel bigramCountLabel;
	private JLabel trigramCountLabel;
	
	public TestPanel() {
		database = new Corpus();
		
		// Frame init. setup.
		setTitle("Word Database Generator");
		setSize(600, 400);
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
				
				resultsTextArea.setText(database.printTestStatistics());
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
		
		resultsTextArea = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(resultsTextArea);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		
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
	}
	
}
