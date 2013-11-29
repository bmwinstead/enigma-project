package main.java.GUINew;

import java.awt.Color;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class IOPanel extends JPanel {
	private JTextField outputTape;
	private JTextField manualInput;
	private JTextArea bulkInput;
	private JTextArea bulkOutput;
	private JTextField fileField;
	private JButton chooseFile;
	private JButton bulkEncryptButton;
	private JLabel tapeLabel;
	private JLabel manualInputLabel;

	public IOPanel() {
		GroupLayout mainLayout = new GroupLayout(this);
		setLayout(mainLayout);
		setBackground(Color.black);
		JPanel panel1 = new JPanel();
		panel1.setBackground(Color.black);
		GroupLayout panel1Layout = new GroupLayout(panel1);
		panel1.setLayout(panel1Layout);
		panel1Layout.setAutoCreateGaps(true);
		panel1Layout.setAutoCreateContainerGaps(true);
		
		tapeLabel = new JLabel("Encrypted Output");
		tapeLabel.setForeground(Color.white);
		outputTape = new JTextField(50);
		outputTape.setEditable(false);
		manualInputLabel = new JLabel("Manual Input");
		manualInputLabel.setForeground(Color.white);
		manualInput = new JTextField(50);

		panel1Layout.setHorizontalGroup(panel1Layout.createSequentialGroup()
				.addGroup(
						panel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(tapeLabel)
								.addComponent(manualInputLabel))
				.addGroup(
						panel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(outputTape)
								.addComponent(manualInput)));
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
		
		JPanel panel2 = new JPanel();
		panel2.setBackground(Color.black);
		GroupLayout panel2Layout = new GroupLayout(panel2);
		panel2.setLayout(panel2Layout);
		panel2Layout.setAutoCreateGaps(true);
		panel2Layout.setAutoCreateContainerGaps(true);
		
		bulkInput = new JTextArea(5, 20);
		bulkInput.setLineWrap(true);
		bulkInput.setWrapStyleWord(true);
		bulkOutput = new JTextArea(5, 20);
		bulkOutput.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(bulkOutput);
		fileField = new JTextField(20);
		chooseFile = new JButton("Browse");
		bulkEncryptButton = new JButton("Encrypt");

		panel2Layout.setHorizontalGroup(panel2Layout.createSequentialGroup()
				.addGroup(
						panel2Layout.createParallelGroup()
								.addComponent(bulkInput)
								.addComponent(fileField))
				.addGroup(
						panel2Layout.createParallelGroup(GroupLayout.Alignment.CENTER)
								.addComponent(scrollPane)
								.addGroup(panel2Layout.createSequentialGroup()
												.addComponent(chooseFile)
												.addComponent(bulkEncryptButton))));
		panel2Layout.setVerticalGroup(panel2Layout.createSequentialGroup()
				.addGroup(
						panel2Layout.createParallelGroup()
								.addComponent(bulkInput)
								.addComponent(scrollPane))
				.addGroup(
						panel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(fileField)
								.addComponent(chooseFile)
								.addComponent(bulkEncryptButton)));
		panel2Layout.linkSize(SwingConstants.VERTICAL,chooseFile,bulkEncryptButton);
		mainLayout.setHorizontalGroup(mainLayout.createParallelGroup()
				.addComponent(panel1)
				.addComponent(panel2));
		mainLayout.setVerticalGroup(mainLayout.createSequentialGroup()
				.addComponent(panel1)
				.addComponent(panel2));
	}
}
