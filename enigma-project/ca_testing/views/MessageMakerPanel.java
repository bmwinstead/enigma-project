package views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerListModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import main.java.GUI.EnigmaGUI;
import main.java.enigma.EnigmaMachine;

/**
 * This testing tool creates large numbers of messages as encrypted by an Enigma
 * Machine. The "Input Text Box" now acts as an entry point for a filename.
 * ".txt" will automatically be appended to the file name, and the file will be
 * placed in the test directory. The default filename is "multiEncryptOut.txt".
 * The "Browse..." button can be used to upload a text file. Each line of text
 * is treated as a different message when encrypted. The "Output Text Box" has
 * been replaced with a dropdown containing multiple rotor settings reset
 * options. "Reset" will reset the Enigma to the indicator settings displayed on
 * the GUI after each encrypted message. "Don't Reset" will use whatever
 * indicator settings are in the machine after the encryption of the most recent
 * message. "Random" will encrypt the first message using the letters shown on
 * the GUI, and then will choose 3 or 4 random letters for each subsequent line.
 * In this mode, the indicator letters will be printed on the start of each line
 * of the output file, before the message itself. There is no space between the
 * indicator letters and the message. "First Letters" will ignore the GUI
 * settings entirely and use the first three or four letters of each line of
 * text as the indicator settings. These letters will NOT be encrypted as part
 * of the message. A file produced by the "Random" setting can be decrypted
 * using the "First Letters" setting, provided the rest of the proper key is
 * entered.
 * 
 * @author J.Ikley
 * 
 */
public class MessageMakerPanel extends JFrame{
    //GUI Elements
    private JComboBox<String> fourthRotorChoice;
    private JComboBox<String>  leftRotorChoice;
    private JComboBox<String>  middleRotorChoice;
    private JComboBox<String>  rightRotorChoice;
    private JComboBox<String>  reflectorChoice;
    private JComboBox<String>  fourthRotorRingSetting;
    private JComboBox<String>  leftRotorRingSetting;
    private JComboBox<String>  middleRotorRingSetting;
    private JComboBox<String>  rightRotorRingSetting;
    private JComboBox<String>   plugA, plugB, plugC, plugD, plugE,plugF, plugG, plugH,
            plugI, plugJ, plugK, plugL, plugM, plugN, plugO, plugP, plugQ, 
            plugR, plugS, plugT, plugU, plugV, plugW, plugX, plugY, plugZ;
    private JComboBox<String> resetCBox;
    private JSpinner fourthRotorPosition;
    private JSpinner leftRotorPosition;
    private JSpinner middleRotorPosition;
    private JSpinner rightRotorPosition;
    private JTextArea inputTextArea;
    private JTextField fileTextField;
    
    //Machine Options
    int[] threeRotorChoices = new int[3];
    int[] fourRotorChoices = new int[4];
    char[] threeRingSettings = new char[3];
    char[] fourRingSettings = new char[4];
    char[] threeInitialPositions = new char[3];
    char[] fourInitialPositions = new char[4];
    int finalReflectorChoice;
    String fileString; //For file decryption
    boolean[] plugSettingUsed = {false, false, false, false, false, false, 
        false, false, false, false, false, false, false, false, false, false, 
        false, false, false, false, false, false, false, false, false, false};
    
    //Plugboard Mapping
    String[] plugMap = {"A", "B", "C", "D", "E", "F", "G", "H", "I", 
            "J", "K", "L", "M", "N","O", "P", "Q", "R", "S", "T", "U", "V", 
            "W", "X", "Y", "Z"};
    String plugboardMap; //Used to send plugoard settings to enigmaMachine
    @SuppressWarnings("rawtypes")
	JComboBox[] comboMap = new JComboBox[26];
    
    //Rotor, Ring, Plugboard, and Reflector Strings
    String[] rotorChoices = {"ROTOR I","ROTOR II","ROTOR III","ROTOR IV", 
        "ROTOR V", "ROTOR VI", "ROTOR VII", "ROTOR VIII"};
    String[] fourthRotorChoices = {"","BETA","GAMMA"};
    String[] reflectorChoices = {"REFLECTOR B","REFLECTOR C",
        "REFLECTOR B THIN", "REFLECTOR C THIN"};
    String[] letterChoices = {"A", "B", "C", "D", "E", "F", "G", "H", "I", 
        "J", "K", "L", "M", "N","O", "P", "Q", "R", "S", "T", "U", "V", 
        "W", "X", "Y", "Z"};
    String[] fourthLetterChoices = {"", "A", "B", "C", "D", "E", "F", "G", 
        "H", "I", "J", "K", "L", "M", "N","O", "P", "Q", "R", "S", "T", "U", 
        "V","W", "X", "Y", "Z"};
	String[] resetOptionsChoices = { "Reset", "Don't Reset", "Random",
			"First Letters" };

    public MessageMakerPanel() {
        //GUI Set-Up
        setTitle("Enigma Multi-Message Generator");
        setSize(715, 735);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        //New Layered Pane (The art magic!)
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(700, 700));
        add(layeredPane, BorderLayout.CENTER);
        
        //Background Image
        ImageIcon bgImage;
        bgImage = (new javax.swing.ImageIcon
                (getClass().getResource("/main/resources/images/backgroundImage.jpg")));

        JLabel backgroundImage = new JLabel(bgImage);
        backgroundImage.setBounds(0, 0, 700, 700);
        backgroundImage.setBorder(null);
        backgroundImage.setOpaque(true);
        layeredPane.add(backgroundImage, new Integer(1), 1);
        
    
        //Rotor Boxes
        fourthRotorChoice = new JComboBox<String>(fourthRotorChoices);
        fourthRotorChoice.setBounds(19, 89, 124, 28);
        fourthRotorChoice.setSelectedIndex(0);
        layeredPane.add(fourthRotorChoice, new Integer(2), 2);
        
        leftRotorChoice = new JComboBox<String>(rotorChoices);
        leftRotorChoice.setBounds(148, 89, 124, 28);
        leftRotorChoice.setSelectedIndex(0);
        layeredPane.add(leftRotorChoice, new Integer(2), 2);

        middleRotorChoice = new JComboBox<String>(rotorChoices);
        middleRotorChoice.setBounds(276, 89, 124, 28);
        middleRotorChoice.setSelectedIndex(1);
        layeredPane.add(middleRotorChoice, new Integer(2), 2);

        rightRotorChoice = new JComboBox<String>(rotorChoices);
        rightRotorChoice.setBounds(404, 89, 124, 28);
        rightRotorChoice.setSelectedIndex(2);
        layeredPane.add(rightRotorChoice, new Integer(2), 2);
        
        //Reflector Box
        reflectorChoice = new JComboBox<String>(reflectorChoices);
        reflectorChoice.setBounds(552, 89, 124, 28);
        reflectorChoice.setSelectedIndex(0);
        layeredPane.add(reflectorChoice, new Integer(2), 2);
        
        //Ring Setting Boxes
        fourthRotorRingSetting = new JComboBox<String>(fourthLetterChoices);
        fourthRotorRingSetting.setBounds(55, 126, 46, 28);
        fourthRotorRingSetting.setSelectedIndex(0);
        layeredPane.add(fourthRotorRingSetting, new Integer(2), 2);
        
        leftRotorRingSetting = new JComboBox<String>(letterChoices);
        leftRotorRingSetting.setBounds(187, 126, 46, 28);
        leftRotorRingSetting.setSelectedIndex(0);
        layeredPane.add(leftRotorRingSetting, new Integer(2), 2);

        middleRotorRingSetting = new JComboBox<String>(letterChoices);
        middleRotorRingSetting.setBounds(315, 126, 46, 28);
        middleRotorRingSetting.setSelectedIndex(0);
        layeredPane.add(middleRotorRingSetting, new Integer(2), 2);

        rightRotorRingSetting = new JComboBox<String>(letterChoices);
        rightRotorRingSetting.setBounds(443, 126, 46, 28);
        rightRotorRingSetting.setSelectedIndex(0);
        layeredPane.add(rightRotorRingSetting, new Integer(2), 2);
        
        //Rotor Position Spinners
        fourthRotorPosition = new JSpinner();
        fourthRotorPosition.setModel(new SpinnerListModel(fourthLetterChoices));
        fourthRotorPosition.setBounds(55, 159, 46, 28);
        fourthRotorPosition.setValue("");
        layeredPane.add(fourthRotorPosition, new Integer(2), 2);

        leftRotorPosition = new JSpinner();
        leftRotorPosition.setModel(new SpinnerListModel(letterChoices));
        leftRotorPosition.setBounds(187, 159, 46, 28);
        leftRotorPosition.setValue("A");
        layeredPane.add(leftRotorPosition, new Integer(2), 2);

        middleRotorPosition = new JSpinner();
        middleRotorPosition.setModel(new SpinnerListModel(letterChoices));
        middleRotorPosition.setBounds(315, 159, 46, 28);
        middleRotorPosition.setValue("A");
        layeredPane.add(middleRotorPosition, new Integer(2), 2);

        rightRotorPosition = new JSpinner();
        rightRotorPosition.setModel(new SpinnerListModel(letterChoices));
        rightRotorPosition.setBounds(443, 159, 46, 28);
        rightRotorPosition.setValue("A");
        layeredPane.add(rightRotorPosition, new Integer(2), 2);
        
        //Plug Board Boxes
        plugA = new JComboBox<String>(letterChoices);
        plugA.setBounds(66, 232, 46, 28);
        plugA.setSelectedIndex(0);
        layeredPane.add(plugA, new Integer(2), 2);
        
        plugB = new JComboBox<String>(letterChoices);
        plugB.setBounds(120, 232, 46, 28);
        plugB.setSelectedIndex(1);
        layeredPane.add(plugB, new Integer(2), 2);
        
        plugC = new JComboBox<String>(letterChoices);
        plugC.setBounds(176, 232, 46, 28);
        plugC.setSelectedIndex(2);
        layeredPane.add(plugC, new Integer(2), 2);
        
        plugD = new JComboBox<String>(letterChoices);
        plugD.setBounds(230, 232, 46, 28);
        plugD.setSelectedIndex(3);
        layeredPane.add(plugD, new Integer(2), 2);
        
        plugE = new JComboBox<String>(letterChoices);
        plugE.setBounds(286, 232, 46, 28);
        plugE.setSelectedIndex(4);
        layeredPane.add(plugE, new Integer(2), 2);
        
        plugF = new JComboBox<String>(letterChoices);
        plugF.setBounds(340, 232, 46, 28);
        plugF.setSelectedIndex(5);
        layeredPane.add(plugF, new Integer(2), 2);
        
        plugG = new JComboBox<String>(letterChoices);
        plugG.setBounds(394, 232, 46, 28);
        plugG.setSelectedIndex(6);
        layeredPane.add(plugG, new Integer(2), 2);
        
        plugH = new JComboBox<String>(letterChoices);
        plugH.setBounds(448, 232, 46, 28);
        plugH.setSelectedIndex(7);
        layeredPane.add(plugH, new Integer(2), 2);
        
        plugI = new JComboBox<String>(letterChoices);
        plugI.setBounds(502, 232, 46, 28);
        plugI.setSelectedIndex(8);
        layeredPane.add(plugI, new Integer(2), 2);
        
        plugJ = new JComboBox<String>(letterChoices);
        plugJ.setBounds(556, 232, 46, 28);
        plugJ.setSelectedIndex(9);
        layeredPane.add(plugJ, new Integer(2), 2);
        
        plugK = new JComboBox<String>(letterChoices);
        plugK.setBounds(66, 289, 46, 28);
        plugK.setSelectedIndex(10);
        layeredPane.add(plugK, new Integer(2), 2);
        
        plugL = new JComboBox<String>(letterChoices);
        plugL.setBounds(120, 289, 46, 28);
        plugL.setSelectedIndex(11);
        layeredPane.add(plugL, new Integer(2), 2);
        
        plugM = new JComboBox<String>(letterChoices);
        plugM.setBounds(176, 289, 46, 28);
        plugM.setSelectedIndex(12);
        layeredPane.add(plugM, new Integer(2), 2);
        
        plugN = new JComboBox<String>(letterChoices);
        plugN.setBounds(230, 289, 46, 28);
        plugN.setSelectedIndex(13);
        layeredPane.add(plugN, new Integer(2), 2);
        
        plugO = new JComboBox<String>(letterChoices);
        plugO.setBounds(286, 289, 46, 28);
        plugO.setSelectedIndex(14);
        layeredPane.add(plugO, new Integer(2), 2);
        
        plugP = new JComboBox<String>(letterChoices);
        plugP.setBounds(340, 289, 46, 28);
        plugP.setSelectedIndex(15);
        layeredPane.add(plugP, new Integer(2), 2);
        
        plugQ = new JComboBox<String>(letterChoices);
        plugQ.setBounds(394, 289, 46, 28);
        plugQ.setSelectedIndex(16);
        layeredPane.add(plugQ, new Integer(2), 2);
        
        plugR = new JComboBox<String>(letterChoices);
        plugR.setBounds(448, 289, 46, 28);
        plugR.setSelectedIndex(17);
        layeredPane.add(plugR, new Integer(2), 2);
        
        plugS = new JComboBox<String>(letterChoices);
        plugS.setBounds(502, 289, 46, 28);
        plugS.setSelectedIndex(18);
        layeredPane.add(plugS, new Integer(2), 2);
        
        plugT = new JComboBox<String>(letterChoices);
        plugT.setBounds(556, 289, 46, 28);
        plugT.setSelectedIndex(19);
        layeredPane.add(plugT, new Integer(2), 2);
        
        plugU = new JComboBox<String>(letterChoices);
        plugU.setBounds(176, 348, 46, 28);
        plugU.setSelectedIndex(20);
        layeredPane.add(plugU, new Integer(2), 2);
        
        plugV = new JComboBox<String>(letterChoices);
        plugV.setBounds(230, 348, 46, 28);
        plugV.setSelectedIndex(21);
        layeredPane.add(plugV, new Integer(2), 2);
        
        plugW = new JComboBox<String>(letterChoices);
        plugW.setBounds(286, 348, 46, 28);
        plugW.setSelectedIndex(22);
        layeredPane.add(plugW, new Integer(2), 2);
        
        plugX = new JComboBox<String>(letterChoices);
        plugX.setBounds(340, 348, 46, 28);
        plugX.setSelectedIndex(23);
        layeredPane.add(plugX, new Integer(2), 2);
        
        plugY = new JComboBox<String>(letterChoices);
        plugY.setBounds(394, 348, 46, 28);
        plugY.setSelectedIndex(24);
        layeredPane.add(plugY, new Integer(2), 2);
        
        plugZ = new JComboBox<String>(letterChoices);
        plugZ.setBounds(448, 348, 46, 28);
        plugZ.setSelectedIndex(25);
        layeredPane.add(plugZ, new Integer(2), 2);
        
        //Combo Box Map
        comboMap[0] = plugA;
        comboMap[1] = plugB;
        comboMap[2] = plugC;
        comboMap[3] = plugD;
        comboMap[4] = plugE;
        comboMap[5] = plugF;
        comboMap[6] = plugG;
        comboMap[7] = plugH;
        comboMap[8] = plugI;
        comboMap[9] = plugJ;
        comboMap[10] = plugK;
        comboMap[11] = plugL;
        comboMap[12] = plugM;
        comboMap[13] = plugN;
        comboMap[14] = plugO;
        comboMap[15] = plugP;
        comboMap[16] = plugQ;
        comboMap[17] = plugR;
        comboMap[18] = plugS;
        comboMap[19] = plugT;
        comboMap[20] = plugU;
        comboMap[21] = plugV;
        comboMap[22] = plugW;
        comboMap[23] = plugX;
        comboMap[24] = plugY;
        comboMap[25] = plugZ;
        
        //File Name Entry Area
        JPanel textEntryPanel = new JPanel();
        textEntryPanel.setOpaque(false);
        textEntryPanel.setBounds(108, 539, 221, 82);
        layeredPane.add(textEntryPanel, new Integer(2), 2);
        inputTextArea = new JTextArea(1, 20);
        textEntryPanel.add(inputTextArea);
        
        //Reset Options Area
        JPanel resetPanel = new JPanel();
        resetPanel.setOpaque(false);
        resetPanel.setBounds(358, 539, 221, 82);
        layeredPane.add(resetPanel, new Integer(2), 2);
        resetCBox = new JComboBox<String>(resetOptionsChoices);
        resetCBox.setSelectedIndex(0);
        resetPanel.add(resetCBox);
        
        //File Input and Buttons Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setBounds(108, 651, 471, 35);
        layeredPane.add(buttonPanel, new Integer(2), 2);

        fileTextField = new JTextField(20);
        buttonPanel.add(fileTextField);

        JButton browseButton = new JButton("Browse...");
        buttonPanel.add(browseButton);

        JButton encryptButton = new JButton("Process");
        buttonPanel.add(encryptButton);
        
        encryptButton.addActionListener(new ActionListener() {
            private Scanner scanner;

			@Override
            public void actionPerformed(ActionEvent e) {
				File file = new File(fileTextField.getText());
				PrintWriter output;
				String text = inputTextArea.getText();
            
                //Set plugboard map to string
                StringBuilder newBuilder = new StringBuilder();
                for(int i = 0; i < 26; i++){
                    if((letterChoices[i] != comboMap[i].getSelectedItem()) &&
                           plugSettingUsed[i] == false){
                               newBuilder.append(letterChoices[i]).append
                                       (comboMap[i].getSelectedItem());
                        plugSettingUsed[comboMap[i].getSelectedIndex()] = true;
                    }
                }
                plugboardMap = newBuilder.toString();
                
                // Get your Reset Option
                int resetOption = resetCBox.getSelectedIndex();
        		Random random = new Random();
                
                if (fourthRotorChoice.getSelectedIndex() == 0){ //Check for #
                    threeRotorChoices[0] = leftRotorChoice.getSelectedIndex();
                    threeRotorChoices[1] = middleRotorChoice.getSelectedIndex();
                    threeRotorChoices[2] = rightRotorChoice.getSelectedIndex();
                    threeRingSettings[0] = leftRotorRingSetting.
                            getSelectedItem().toString().charAt(0);
                    threeRingSettings[1] = middleRotorRingSetting.
                            getSelectedItem().toString().charAt(0);
                    threeRingSettings[2] = rightRotorRingSetting.
                            getSelectedItem().toString().charAt(0);
                    threeInitialPositions[0] = ((String)(leftRotorPosition.
                            getValue())).toCharArray()[0];
                    threeInitialPositions[1] = ((String)(middleRotorPosition.
                            getValue())).toCharArray()[0];
                    threeInitialPositions[2] = ((String)(rightRotorPosition.
                            getValue())).toCharArray()[0];
                    
                    finalReflectorChoice = reflectorChoice.getSelectedIndex();

                    EnigmaMachine newMachine = 
                            new EnigmaMachine(threeRotorChoices, 
                            finalReflectorChoice, threeRingSettings, 
                            threeInitialPositions, plugboardMap);
                    
                    //Check if text box is used
                    if (fileTextField.getText().length() > 0){
                    	fileString = "";
                        try {
                            scanner = new Scanner(file);
                            String fileName;
                            if (text.length() > 0) {
                            	fileName = text + ".txt";
                            }
                            else {
                            	fileName = "multiEncryptOut.txt";
                            }
                        	output = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)));

                            while (scanner.hasNextLine()) {
                            	String inString = scanner.nextLine();
                            	
                            	// Use first 3 letters of line in file
                            	if (resetOption == 3) {
                            		// Make sure line has enough characters.
                            		if (inString.length() >= 3) {
                            			for (int i = 0; i < 3; i++) {
                            				threeInitialPositions[i] = inString.charAt(i);
                            			}
                            			newMachine.setPositions(threeInitialPositions);
                            			inString = inString.substring(3);
                            		} // end for (int i = 0; i < 3; i++)
                            	} // end first-3 resetOption.
                            	
                            	String outString = "";
                            	
                            	// If random option is used, print indicator.
                            	if (resetOption == 2) {
                            		outString += String.valueOf(threeInitialPositions);
                            	}
                            	
                            	outString += newMachine.encryptString(inString);
                            	outString += "\r\n";
                            	output.append(outString);
                            	
                            	// Reset to rotors to GUI position
                            	if (resetOption == 0) {
                            		newMachine.reset();
                            	} 
                            	// Set rotors to 3 random characters
                            	else if (resetOption == 2) {
                   		
                            		for (int i = 0; i < 3; i++) {
                            			threeInitialPositions[i] = (char) ('A' + random.nextInt(26)); 
                            		} // end for (int i = 0; i < 3; i++)
                            		newMachine.setPositions(threeInitialPositions);
                            	} // end else if (resetOption == 2)
                            } // end while (scanner.hasNextLine())
                            output.close();
                         } 
                        catch (FileNotFoundException ex) {
                            Logger.getLogger(EnigmaGUI.class.getName()).log
                                    (Level.SEVERE, null, ex);
                        }
                        catch (IOException ex) {
                        	Logger.getLogger(EnigmaGUI.class.getName()).log
                            (Level.SEVERE, null, ex);
                        }
                    }
                    else{
                        // outputTextArea.setText(newMachine.encryptString(text));
                    }
                }
                else { //All 4 Rotors selected
                	// JLI fourthRotorChoice receives an adjustment due to
                	//     fewer number of rotors in that drop-down. 
                    fourRotorChoices[0] = fourthRotorChoice.getSelectedIndex() + 7; 
                    fourRotorChoices[1] = leftRotorChoice.getSelectedIndex();
                    fourRotorChoices[2] = middleRotorChoice.getSelectedIndex();
                    fourRotorChoices[3] = rightRotorChoice.getSelectedIndex();
                    fourRingSettings[0] = fourthRotorRingSetting.
                            getSelectedItem().toString().charAt(0);
                    fourRingSettings[1] = leftRotorRingSetting.
                            getSelectedItem().toString().charAt(0);
                    fourRingSettings[2] = middleRotorRingSetting.
                            getSelectedItem().toString().charAt(0);
                    fourRingSettings[3] = rightRotorRingSetting.
                            getSelectedItem().toString().charAt(0);
                    fourInitialPositions[0] = ((String)(fourthRotorPosition.
                            getValue())).toCharArray()[0];
                    fourInitialPositions[1] = ((String)(leftRotorPosition.
                            getValue())).toCharArray()[0];
                    fourInitialPositions[2] = ((String)(middleRotorPosition.
                            getValue())).toCharArray()[0];
                    fourInitialPositions[3] = ((String)(rightRotorPosition.
                            getValue())).toCharArray()[0];
                    
                    finalReflectorChoice = reflectorChoice.getSelectedIndex();
                    EnigmaMachine newFourMachine = 
                            new EnigmaMachine(fourRotorChoices, 
                            finalReflectorChoice, fourRingSettings, 
                            fourInitialPositions, plugboardMap);
                    
                    //Check if text box is used
                    if (fileTextField.getText().length() > 0){
                    	fileString = "";
                        try {
                            scanner = new Scanner(file);
                            String fileName;
                            if (text.length() > 0) {
                            	fileName = text + ".txt";
                            }
                            else {
                            	fileName = "multiEncryptOut.txt";
                            }
                        	output = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)));

                            while (scanner.hasNextLine()) {
                            	String inString = scanner.nextLine();
                            	
                            	// Use first 3 letters of line in file
                            	if (resetOption == 3) {
                            		// Make sure line has enough characters.
                            		if (inString.length() >= 4) {
                            			for (int i = 0; i < 4; i++) {
                            				fourInitialPositions[i] = inString.charAt(i);
                            			}
                            			newFourMachine.setPositions(fourInitialPositions);
                            			inString = inString.substring(3);
                            		} // end for (int i = 0; i < 3; i++)
                            	} // end first-3 resetOption.
                            	
                            	String outString = "";
                            	
                            	// If random option is used, print indicator.
                            	if (resetOption == 2) {
                            		outString += String.valueOf(fourInitialPositions);
                            	}
                            	
                            	outString += newFourMachine.encryptString(inString);
                            	outString += "\r\n";
                            	output.append(outString);
                            	
                            	// Reset to rotors to GUI position
                            	if (resetOption == 0) {
                            		newFourMachine.reset();
                            	} 
                            	// Set rotors to 3 random characters
                            	else if (resetOption == 2) {
                            		for (int i = 0; i < 3; i++) {
                            			fourInitialPositions[i] = (char) ('A' + random.nextInt(26)); 
                            		} // end for (int i = 0; i < 3; i++)
                            		newFourMachine.setPositions(fourInitialPositions);
                            	} // end else if (resetOption == 2)
                            } // end while (scanner.hasNextLine())
                            output.close();
                         } 
                        catch (FileNotFoundException ex) {
                            Logger.getLogger(EnigmaGUI.class.getName()).log
                                    (Level.SEVERE, null, ex);
                        }
                        catch (IOException ex) {
                        	Logger.getLogger(EnigmaGUI.class.getName()).log
                            (Level.SEVERE, null, ex);
                        }
                    }
                    else{
                        // outputTextArea.setText(newMachine.encryptString(text));
                    }
                }
            }
        });
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();

                if (fileChooser.showOpenDialog(null) == 
                        JFileChooser.APPROVE_OPTION) {
                    fileTextField.setText
                            (fileChooser.getSelectedFile().getAbsolutePath());
                }
            }
        });
        //Action Listeners for Plugboard
        plugA.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PlugboardSwap(0, plugA.getSelectedIndex());
            }
        });
        plugB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PlugboardSwap(1, plugB.getSelectedIndex());
            }
        });
        plugC.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PlugboardSwap(2, plugC.getSelectedIndex());
            }
        });
        plugD.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PlugboardSwap(3, plugD.getSelectedIndex());
            }
        });
        plugE.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PlugboardSwap(4, plugE.getSelectedIndex());
            }
        });
        plugF.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PlugboardSwap(5, plugF.getSelectedIndex());
            }
        });
        plugG.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PlugboardSwap(6, plugG.getSelectedIndex());
            }
        });
        plugH.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PlugboardSwap(7, plugH.getSelectedIndex());
            }
        });
        plugI.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PlugboardSwap(8, plugI.getSelectedIndex());
            }
        });
        plugJ.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PlugboardSwap(9, plugJ.getSelectedIndex());
            }
        });
        plugK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PlugboardSwap(10, plugK.getSelectedIndex());
            }
        });
        plugL.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PlugboardSwap(11, plugL.getSelectedIndex());
            }
        });
        plugM.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PlugboardSwap(12, plugM.getSelectedIndex());
            }
        });
        plugN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PlugboardSwap(13, plugN.getSelectedIndex());
            }
        });
        plugO.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PlugboardSwap(14, plugO.getSelectedIndex());
            }
        });
        plugP.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PlugboardSwap(15, plugP.getSelectedIndex());
            }
        });
        plugQ.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PlugboardSwap(16, plugQ.getSelectedIndex());
            }
        });
        plugR.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PlugboardSwap(17, plugR.getSelectedIndex());
            }
        });
        plugS.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PlugboardSwap(18, plugS.getSelectedIndex());
            }
        });
        plugT.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PlugboardSwap(19, plugT.getSelectedIndex());
            }
        });
        plugU.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PlugboardSwap(20, plugU.getSelectedIndex());
            }
        });
        plugV.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PlugboardSwap(21, plugV.getSelectedIndex());
            }
        });
        plugW.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PlugboardSwap(22, plugW.getSelectedIndex());
            }
        });
        plugX.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PlugboardSwap(23, plugX.getSelectedIndex());
            }
        });
        plugY.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PlugboardSwap(24, plugY.getSelectedIndex());
            }
        });
        plugZ.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PlugboardSwap(25, plugZ.getSelectedIndex());
            }
        });
        //Action Listeners for Rotors and Ring Settings
        fourthRotorRingSetting.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fourthRotorChoice.getSelectedIndex() == 0){
                    fourthRotorRingSetting.setSelectedIndex(0);
                }
            }
        });
        fourthRotorPosition.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent arg0) {
                if (fourthRotorChoice.getSelectedIndex() == 0){
                    fourthRotorPosition.setValue("");
                }
            }
        });
        //Action Listeners for Rotor Selections
        fourthRotorChoice.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fourthRotorChoice.getSelectedIndex() == 0){
                    fourthRotorRingSetting.setSelectedIndex(0);
                    fourthRotorPosition.setValue("");
                }
            }
        });

    }
    
    //Swap the plug board settings
    public void PlugboardSwap(int firstLetter, int secondLetter){
        int foundIt = 0; //0 = false, 1=true, 2 = true but conflict
        int conflictLocation = 0;
        
        if(firstLetter == secondLetter){ //Setting plugboard to default
            for(int i = 0; i < 26; i++){ //Check for existing pairs
                if (plugMap[i].equals(letterChoices[secondLetter]) &&
                        i != firstLetter){
                    plugMap[i] = letterChoices[i];
                    comboMap[i].setSelectedIndex(i); //Reset the pair
                }
            }
        }
        else{ //Setting a change
            for(int j = 0; j < 26; j++){
                if (plugMap[j].equals(letterChoices[secondLetter]) &&
                        j == secondLetter && j != firstLetter){
                    foundIt = 1; //Found the match and pair is free
                }
            }
            for(int k = 0; k < 26; k++){
                if (plugMap[k].equals(letterChoices[firstLetter]) &&
                        k != firstLetter && k != secondLetter){
                    foundIt = 2; //Found the match, but conflict exists
                    conflictLocation = k;
                }
            }
            if (foundIt == 1){ //Make the match
                comboMap[secondLetter].setSelectedIndex(firstLetter);
                plugMap[secondLetter] = letterChoices[firstLetter];
            }
            else if (foundIt == 2){ //Set back to existing pair
                plugMap[firstLetter] = letterChoices[conflictLocation];
                comboMap[firstLetter].setSelectedIndex(conflictLocation);
            }
            else { //Selection is already chosen. Go back to default
                comboMap[firstLetter].setSelectedIndex(firstLetter);
            }
        }
    }
}