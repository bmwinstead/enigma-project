package main.java.GUINew;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class IOPanel extends JPanel {
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
	private ConfigureOutput output = new ConfigureOutput(); //Configure
	private Lightboard lightboard;
	public IOPanel()  {
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
			try{
				Path file = Paths.get(possibleFile);
				List<String> ls = Files.readAllLines(file, Charset.defaultCharset());
				String encrypted = "";
				for(String s : ls){
					encrypted += machine.encryptString(s) + "\n"; 
				}
				encrypted = output.configure(encrypted); //Text error checking
				System.out.println("Text Error Checking and Conversion");
				bulkOutput.setText(encrypted);
			} catch(InvalidPathException e){
				String s = bulkInput.getText();
				String encrypted = machine.encryptString(s);
				encrypted = output.configure(encrypted); //Text error checking
				System.out.println("Text Error Checking and Conversion");
				bulkOutput.setText(encrypted);
			} catch (IOException e) {
				String s = bulkInput.getText();
				s = machine.encryptString(s);
				lightboard.turnOnLight(s); //Activate Lightboard
				bulkOutput.setText(s);
				if(s.equals("") || s.equals(null))
					bulkOutput.setText("Input text must contain at least one letter or number.");
			} 
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
			char encrypted = machine.encryptChar(c);
			lightboard.turnOnLight(String.valueOf(encrypted)); //Activate Lightboard
			String curText = outputTape.getText();
			outputTape.setText(curText + encrypted);
		}
		@Override
		public void removeUpdate(DocumentEvent arg0) {
			//Do nothing
		}
	}
}
