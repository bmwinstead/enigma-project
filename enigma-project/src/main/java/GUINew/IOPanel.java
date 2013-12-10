package main.java.GUINew;

import java.awt.Color;

/**
 * 
 * @author Team Enigma
 * @version 0.9
 * @date Nov 30, 2013
 * 
 * Panel for file input/output and encrypt button
 * 
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import main.java.enigma.EnigmaSettings;

/**
 * Combines all the IO functionality of the Enigma, providing
 * ways to encrypt characters, long strings, and entire files.
 * When encrypting characters it provides a graphical representation
 * of the "lightboard", lighting up the encrypted character, reminiscient
 * of how the original machines operated.
 * @author Bryan Winstead
 *
 */
@SuppressWarnings("serial")
public class IOPanel extends JPanel implements Observer {
	private final int tapeLength = 50;
	private JTextField outputTape;
	private JTextField manualInput;
	private JTextArea bulkInput;
	private JTextArea bulkOutput;
	private JTextField fileTextField;
	private JButton browseButton;
	private JButton bulkEncryptButton;
	private JLabel tapeLabel;
	private JLabel manualInputLabel;
	private EnigmaSingleton machine = EnigmaSingleton.INSTANCE;
	private Lightboard lightboard;
	
	/**
	 * 
	 */
	public IOPanel()  {
		machine.addObserver(this);
		GroupLayout mainLayout = new GroupLayout(this);
		setLayout(mainLayout);
		setBackground(Color.black);
		JPanel topPanel = setUpTopPanel();
		JPanel bottomPanel = setUpBottomPanel();
		lightboard = buildLightboardPanel();
		mainLayout.setHorizontalGroup(mainLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addComponent(lightboard)
				.addComponent(topPanel)							
				.addComponent(bottomPanel));
		mainLayout.setVerticalGroup(mainLayout.createSequentialGroup()
				.addComponent(lightboard)
				.addComponent(topPanel)
				.addComponent(bottomPanel));
	}
	
	private JPanel setUpTopPanel(){
		JPanel topPanel = new JPanel();
		topPanel.setBackground(Color.black);
		GroupLayout panel1Layout = new GroupLayout(topPanel);
		topPanel.setLayout(panel1Layout);
		panel1Layout.setAutoCreateGaps(true);
		panel1Layout.setAutoCreateContainerGaps(true);
		
		tapeLabel = new JLabel("Encrypted Output");
		tapeLabel.setForeground(Color.white);
		outputTape = new JTextField(tapeLength);
		outputTape.setEditable(false);
		outputTape.setBackground(Color.lightGray);
		manualInputLabel = new JLabel("Manual Input");
		manualInputLabel.setForeground(Color.white);
		manualInput = new JTextField(tapeLength);
		manualInput.getDocument().addDocumentListener(new FieldListener());
		int tapeMin = 350;
		int tapePref = 400;
		int tapeMax = 500;
		panel1Layout.setHorizontalGroup(panel1Layout.createSequentialGroup()
				.addGroup(
						panel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(tapeLabel)
								.addComponent(manualInputLabel))
				.addGroup(
						panel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(outputTape,tapeMin,tapePref,tapeMax)
								.addComponent(manualInput,tapeMin,tapePref,tapeMax)));
		panel1Layout.setVerticalGroup(panel1Layout.createSequentialGroup()
				.addGroup(
						panel1Layout.createParallelGroup()
								.addComponent(tapeLabel)
								.addComponent(outputTape))
				.addGroup(
						panel1Layout.createParallelGroup()
								.addComponent(manualInputLabel)
								.addComponent(manualInput)));
		panel1Layout.linkSize(SwingConstants.VERTICAL, tapeLabel,manualInputLabel);
		panel1Layout.linkSize(SwingConstants.VERTICAL, outputTape, manualInput);
		return topPanel;
	}
	
	private JPanel setUpBottomPanel(){
		JPanel bottomPanel = new JPanel();
		bottomPanel.setBackground(Color.black);
		GroupLayout panel2Layout = new GroupLayout(bottomPanel);
		bottomPanel.setLayout(panel2Layout);
		panel2Layout.setAutoCreateGaps(true);
		panel2Layout.setAutoCreateContainerGaps(true);
		
		bulkInput = new JTextArea(5, 20);
		JScrollPane inputScrollPane = new JScrollPane(bulkInput);
		bulkOutput = new JTextArea(5, 20);
		bulkOutput.setEditable(false);
		JScrollPane outputScrollPane = new JScrollPane(bulkOutput);
		fileTextField = new JTextField(20);
		browseButton = new JButton("Browse");
        browseButton.addActionListener(new BrowseButtonListener());
		bulkEncryptButton = new JButton("Encrypt");
		bulkEncryptButton.addActionListener(new EncryptButtonListener());
		
		int textAreaMin = 155;
		int textAreaPref = 255;
		int textAreaMax = 305;
		panel2Layout.setHorizontalGroup(panel2Layout.createSequentialGroup()
				.addGroup(
						panel2Layout.createParallelGroup()
								.addComponent(inputScrollPane,textAreaMin,textAreaPref,textAreaMax)
								.addComponent(fileTextField,textAreaMin,textAreaPref,textAreaMax))
				.addGroup(
						panel2Layout.createParallelGroup(GroupLayout.Alignment.CENTER)
								.addComponent(outputScrollPane,textAreaMin,textAreaPref,textAreaMax)
								.addGroup(panel2Layout.createSequentialGroup()
												.addComponent(browseButton)
												.addComponent(bulkEncryptButton))));
		panel2Layout.setVerticalGroup(panel2Layout.createSequentialGroup()
				.addGroup(
						panel2Layout.createParallelGroup()
								.addComponent(inputScrollPane)
								.addComponent(outputScrollPane))
				.addGroup(
						panel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(fileTextField)
								.addComponent(browseButton)
								.addComponent(bulkEncryptButton)));
		panel2Layout.linkSize(SwingConstants.VERTICAL,browseButton,bulkEncryptButton);
		panel2Layout.linkSize(SwingConstants.VERTICAL, inputScrollPane, outputScrollPane);
		return bottomPanel;
	}
	// Lightboard panel
	private Lightboard buildLightboardPanel() {
		Lightboard lightboardPanel = new Lightboard();
		lightboardPanel.setBackground(Color.black);
		return lightboardPanel;
	}
	
	public void clearFields() {
		outputTape.setText("");
		manualInput.setText("");
		bulkInput.setText("");
		bulkOutput.setText("");
		fileTextField.setText("");
		lightboard.resetLights();
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
//		String s = (String) arg1;
		EnigmaSettings settings = (EnigmaSettings) arg1;
		
		if (settings.getUpdateType() == EnigmaSingleton.FULLRESET
				|| settings.getUpdateType() == EnigmaSingleton.CLEARTEXT) {
			clearFields();
			System.out.println("Resetting text fields.");
		}
	}
	
	private class BrowseButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();

            if (fileChooser.showOpenDialog(null) == 
                    JFileChooser.APPROVE_OPTION) {
                fileTextField.setText
                        (fileChooser.getSelectedFile().getAbsolutePath());
            }
        }
	}
	
	private class EncryptButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			String possibleFile = fileTextField.getText();
			JFrame tempFrame = new JFrame(); //Error Message Frame
			// Set the machine's "initial" positions. Useful for encrypting
			// multiple messages using the same key and starting indicators. 
			machine.setInitPositions();
			try{
				Path file = Paths.get(possibleFile);
				List<String> ls = Files.readAllLines(file, Charset.defaultCharset());
				String encrypted = "";
				for(String s : ls){
					s = s.replace("e&", "");//This is a hack. See GitHub issue #39
					s = s.replaceAll("[^\\w\\s]", ""); //Catching crazy characters
					encrypted += machine.encryptString(s);
					encrypted = encrypted.replace("\n", "").replace("\r", "");
					encrypted += "\n"; 
				}
				System.out.println("Text Error Checking and Conversion");
				encrypted = addSpaces(encrypted);
				if (encrypted.equals("") || encrypted.equals(null) || encrypted.equals("\n")){
					bulkOutput.setText("Input text must contain at least one letter or number. "
							+ "\n" + "Input file must be a text file.");
					JOptionPane.showMessageDialog(tempFrame,"Error 100: "
							+ "No valid data in input text.");
				}
				else{
					bulkOutput.setText(encrypted);
				}
			} catch(InvalidPathException e){
				String s = bulkInput.getText();
				s = s.replaceAll("[^\\w\\s]", ""); //catching crazy characters
				String encrypted = machine.encryptString(s);
				System.out.println("Text Error Checking and Conversion");
				JOptionPane.showMessageDialog(tempFrame,"Error 100: "
						+ "No valid data in input text.");
				encrypted = addSpaces(encrypted);
				bulkOutput.setText(encrypted);
			} catch (IOException e) {
				String s = bulkInput.getText();
				s = s.replaceAll("[^\\w\\s]", ""); //catching crazy characters
				s = machine.encryptString(s);
				lightboard.turnOnLight(s); //Activate Lightboard
				s = addSpaces(s);
				bulkOutput.setText(s);
				if(s.equals("") || s.equals(null)){
					bulkOutput.setText("Input text must contain at least one letter or number. "
							+ "\n" + "Input file must be a text file.");
					JOptionPane.showMessageDialog(tempFrame,"Error 100: "
							+ "No valid data in input text.");
				}
			} 
		} // end actionPerformed method
		
		private String addSpaces(String input) {
			String result = "";
			char[] inArray = input.toCharArray();
			
			for(int i = 0; i < input.length(); i++) {
				if (((machine.getSpacesOption() == EnigmaSingleton.FOURSPACES)
						&& (i % 4 == 0)) 
						|| ((machine.getSpacesOption() == EnigmaSingleton.FIVESPACES)
								&& (i % 5 == 0))){
					result += " ";
				}
				result += inArray[i];
			}
			
			return result;
		}
	}
	private class FieldListener implements  DocumentListener{
		@Override
		public void changedUpdate(DocumentEvent arg0) {
			//Do nothing
		}

		@Override
		public void insertUpdate(DocumentEvent arg0) {
			Document doc = arg0.getDocument();
			char c = '!';
			try {
				c = doc.getText((doc.getLength() - 1), 1).charAt(0); //get latest character.
			} catch (BadLocationException e) {
			}
			// If this is the first character typed, set the init Rotor Positions
			if (doc.getLength() == 1) {
				machine.setInitPositions();
			}
			char encrypted = machine.encryptChar(c);
			if (encrypted != '!') {
				lightboard.turnOnLight(String.valueOf(encrypted)); //Activate Lightboard
				String curText = outputTape.getText();
				if (((machine.getSpacesOption() == EnigmaSingleton.FOURSPACES)
						&& ((curText.length() + 1) % 5 == 0)) 
						|| ((machine.getSpacesOption() == EnigmaSingleton.FIVESPACES)
								&& ((curText.length() + 1) % 6 == 0))){
					curText += " ";
				}
				outputTape.setText(curText + encrypted);
			} // end if (encrypted != '!')
		}
		@Override
		public void removeUpdate(DocumentEvent arg0) {
			//Do nothing
		}
	}
	
	
}
