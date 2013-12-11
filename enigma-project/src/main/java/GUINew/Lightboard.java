package main.java.GUINew;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.nio.file.Paths;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

/**
 * Lightboard... provides a visual display of the output
 * character from a character-by-character decryption.
 * @author Team Enigma
 * @version 0.9
 * @date Nov 30, 2013
 * 
 */

@SuppressWarnings("serial")
public class Lightboard extends JPanel {
	private JLabel lightA, lightB, lightC, lightD, lightE, lightF, lightG,
			lightH, lightI, lightJ, lightK, lightL, lightM, lightN, lightO,
			lightP, lightQ, lightR, lightS, lightT, lightU, lightV, lightW,
			lightX, lightY, lightZ;
	String[] letterChoices = { "A", "B", "C", "D", "E", "F", "G", "H", "I",
			"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
			"W", "X", "Y", "Z" };
	private JLayeredPane layeredPane;
	private String filePrefix = "/main/resources";
	public Lightboard() {
		String testPath = filePrefix + "/images/lightboardImage.jpg";
		try{
			getClass().getResource(testPath);
		} catch(Exception e){
			filePrefix = "";
		}
		layeredPane = new JLayeredPane();
		// Light board Icons for background
		ImageIcon background = (new javax.swing.ImageIcon // Background Image
		(getClass().getResource(filePrefix + "/images/lightboardImage.jpg")));
		JLabel backgroundImage = new JLabel(background);
		backgroundImage.setBackground(Color.black);
		backgroundImage.setBorder(null);
		backgroundImage.setSize(318, 131);
		backgroundImage.setOpaque(true);
		layeredPane.add(backgroundImage, new Integer(1), 1);
		layeredPane.setPreferredSize(new Dimension(318, 131));
		this.add(layeredPane, BorderLayout.CENTER);

		// Light board Icons for "lit up" design
		ImageIcon plugLightA = (new javax.swing.ImageIcon // Light A
		(getClass().getResource(filePrefix + "/images/a.jpg")));
		lightA = new JLabel(plugLightA);
		//lightA.setBounds(140, 305, 160, 316);
		lightA.setBounds(15, 37, 45, 77);
		lightA.setBorder(null);
		layeredPane.add(lightA, new Integer(0), 0);

		ImageIcon plugLightB = (new javax.swing.ImageIcon // Light B
		(getClass().getResource(filePrefix + "/images/b.jpg")));
		lightB = new JLabel(plugLightB);
		lightB.setBounds(245-125, 330-268, 265-115, 340-239);
		lightB.setBorder(null);
		layeredPane.add(lightB, new Integer(0), 0);

		ImageIcon plugLightC = (new javax.swing.ImageIcon // Light C
		(getClass().getResource(filePrefix + "/images/c.jpg")));
		lightC = new JLabel(plugLightC);
		lightC.setBounds(201-125, 330-268, 211-115, 340-239);
		lightC.setBorder(null);
		layeredPane.add(lightC, new Integer(0), 0);

		ImageIcon plugLightD = (new javax.swing.ImageIcon // Light D
		(getClass().getResource(filePrefix + "/images/d.jpg")));
		lightD = new JLabel(plugLightD);
		lightD.setBounds(185-125, 305-268, 205-115, 316-239);
		lightD.setBorder(null);
		layeredPane.add(lightD, new Integer(0), 0);

		ImageIcon plugLightE = (new javax.swing.ImageIcon // Light E
		(getClass().getResource(filePrefix + "/images/e.jpg")));
		lightE = new JLabel(plugLightE);
		lightE.setBounds(175-125, 281-268, 195-115, 291-239);
		lightE.setBorder(null);
		layeredPane.add(lightE, new Integer(0), 0);

		ImageIcon plugLightF = (new javax.swing.ImageIcon // Light F
		(getClass().getResource(filePrefix + "/images/f.jpg")));
		lightF = new JLabel(plugLightF);
		lightF.setBounds(210-125, 305-268, 230-115, 316-239);
		lightF.setBorder(null);
		layeredPane.add(lightF, new Integer(0), 0);

		ImageIcon plugLightG = (new javax.swing.ImageIcon // Light G
		(getClass().getResource(filePrefix + "/images/g.jpg")));
		lightG = new JLabel(plugLightG);
		lightG.setBounds(230-125, 305-268, 260-115, 316-239);
		lightG.setBorder(null);
		layeredPane.add(lightG, new Integer(0), 0);

		ImageIcon plugLightH = (new javax.swing.ImageIcon // Light H
		(getClass().getResource(filePrefix + "/images/h.jpg")));
		lightH = new JLabel(plugLightH);
		lightH.setBounds(252-125, 304-268, 282-115, 315-239);
		lightH.setBorder(null);
		layeredPane.add(lightH, new Integer(0), 0);

		ImageIcon plugLightI = (new javax.swing.ImageIcon // Light I
		(getClass().getResource(filePrefix + "/images/i.jpg")));
		lightI = new JLabel(plugLightI);
		lightI.setBounds(292-125, 281-268, 312-115, 291-239);
		lightI.setBorder(null);
		layeredPane.add(lightI, new Integer(0), 0);

		ImageIcon plugLightJ = (new javax.swing.ImageIcon // Light J
		(getClass().getResource(filePrefix + "/images/j.jpg")));
		lightJ = new JLabel(plugLightJ);
		lightJ.setBounds(278-125, 304-268, 308-115, 315-239);
		lightJ.setBorder(null);
		layeredPane.add(lightJ, new Integer(0), 0);

		ImageIcon plugLightK = (new javax.swing.ImageIcon // Light K
		(getClass().getResource(filePrefix + "/images/k.jpg")));
		lightK = new JLabel(plugLightK);
		lightK.setBounds(298-125, 304-268, 328-112, 315-239);
		lightK.setBorder(null);
		layeredPane.add(lightK, new Integer(0), 0);

		ImageIcon plugLightL = (new javax.swing.ImageIcon // Light L
		(getClass().getResource(filePrefix + "/images/l.jpg")));
		lightL = new JLabel(plugLightL);
		lightL.setBounds(312-125, 329-268, 342-115, 339-239);
		lightL.setBorder(null);
		layeredPane.add(lightL, new Integer(0), 0);

		ImageIcon plugLightM = (new javax.swing.ImageIcon // Light M
		(getClass().getResource(filePrefix + "/images/m.jpg")));
		lightM = new JLabel(plugLightM);
		lightM.setBounds(289-125, 330-268, 319-115, 340-239);
		lightM.setBorder(null);
		layeredPane.add(lightM, new Integer(0), 0);

		ImageIcon plugLightN = (new javax.swing.ImageIcon // Light N
		(getClass().getResource(filePrefix + "/images/n.jpg")));
		lightN = new JLabel(plugLightN);
		lightN.setBounds(265-125, 330-268, 295-115, 340-239);
		lightN.setBorder(null);
		layeredPane.add(lightN, new Integer(0), 0);

		ImageIcon plugLightO = (new javax.swing.ImageIcon // Light O
		(getClass().getResource(filePrefix + "/images/o.jpg")));
		lightO = new JLabel(plugLightO);
		lightO.setBounds(316-125, 281-268, 336-115, 291-239);
		lightO.setBorder(null);
		layeredPane.add(lightO, new Integer(0), 0);

		ImageIcon plugLightP = (new javax.swing.ImageIcon // Light P
		(getClass().getResource(filePrefix + "/images/p.jpg")));
		lightP = new JLabel(plugLightP);
		lightP.setBounds(127-125, 330-268, 147-115, 340-239);
		lightP.setBorder(null);
		layeredPane.add(lightP, new Integer(0), 0);

		ImageIcon plugLightQ = (new javax.swing.ImageIcon // Light Q
		(getClass().getResource(filePrefix + "/images/q.jpg")));
		lightQ = new JLabel(plugLightQ);
		lightQ.setBounds(127-125, 281-268, 147-115, 291-239);
		lightQ.setBorder(null);
		layeredPane.add(lightQ, new Integer(0), 0);

		ImageIcon plugLightR = (new javax.swing.ImageIcon // Light R
		(getClass().getResource(filePrefix + "/images/r.jpg")));
		lightR = new JLabel(plugLightR);
		lightR.setBounds(197-125, 281-268, 217-115, 291-239);
		lightR.setBorder(null);
		layeredPane.add(lightR, new Integer(0), 0);

		ImageIcon plugLightS = (new javax.swing.ImageIcon // Light S
		(getClass().getResource(filePrefix + "/images/s.jpg")));
		lightS = new JLabel(plugLightS);
		lightS.setBounds(162-125, 304-268, 182-115, 315-239);
		lightS.setBorder(null);
		layeredPane.add(lightS, new Integer(0), 0);

		ImageIcon plugLightT = (new javax.swing.ImageIcon // Light T
		(getClass().getResource(filePrefix + "/images/t.jpg")));
		lightT = new JLabel(plugLightT);
		lightT.setBounds(220-125, 281-268, 240-115, 291-239);
		lightT.setBorder(null);
		layeredPane.add(lightT, new Integer(0), 0);

		ImageIcon plugLightU = (new javax.swing.ImageIcon // Light U
		(getClass().getResource(filePrefix + "/images/u.jpg")));
		lightU = new JLabel(plugLightU);
		lightU.setBounds(270-125, 281-268, 290-115, 291-239);
		lightU.setBorder(null);
		layeredPane.add(lightU, new Integer(0), 0);

		ImageIcon plugLightV = (new javax.swing.ImageIcon // Light V
		(getClass().getResource(filePrefix + "/images/v.jpg")));
		lightV = new JLabel(plugLightV);
		lightV.setBounds(225-125, 330-268, 235-115, 340-239);
		lightV.setBorder(null);
		layeredPane.add(lightV, new Integer(0), 0);

		ImageIcon plugLightW = (new javax.swing.ImageIcon // Light W
		(getClass().getResource(filePrefix + "/images/w.jpg")));
		lightW = new JLabel(plugLightW);
		lightW.setBounds(150-125, 281-268, 170-115, 291-239);
		lightW.setBorder(null);
		layeredPane.add(lightW, new Integer(0), 0);

		ImageIcon plugLightX = (new javax.swing.ImageIcon // Light X
		(getClass().getResource(filePrefix + "/images/x.jpg")));
		lightX = new JLabel(plugLightX);
		lightX.setBounds(175-125, 330-268, 195-115, 340-239);
		lightX.setBorder(null);
		layeredPane.add(lightX, new Integer(0), 0);

		ImageIcon plugLightY = (new javax.swing.ImageIcon // Light Y
		(getClass().getResource(filePrefix + "/images/y.jpg")));
		lightY = new JLabel(plugLightY);
		lightY.setBounds(150-125, 330-268, 170-115, 340-239);
		lightY.setBorder(null);
		layeredPane.add(lightY, new Integer(0), 0);

		ImageIcon plugLightZ = (new javax.swing.ImageIcon // Light Z
		(getClass().getResource(filePrefix + "/images/z.jpg")));
		lightZ = new JLabel(plugLightZ);
		lightZ.setBounds(245-125, 281-268, 265-115, 291-239);
		lightZ.setBorder(null);
		layeredPane.add(lightZ, new Integer(0), 0);
	}

	public void resetLights(){
		layeredPane.setLayer(lightA, new Integer(0), 0);
		layeredPane.setLayer(lightB, new Integer(0), 0);
		layeredPane.setLayer(lightC, new Integer(0), 0);
		layeredPane.setLayer(lightD, new Integer(0), 0);
		layeredPane.setLayer(lightE, new Integer(0), 0);
		layeredPane.setLayer(lightF, new Integer(0), 0);
		layeredPane.setLayer(lightG, new Integer(0), 0);
		layeredPane.setLayer(lightH, new Integer(0), 0);
		layeredPane.setLayer(lightI, new Integer(0), 0);
		layeredPane.setLayer(lightJ, new Integer(0), 0);
		layeredPane.setLayer(lightK, new Integer(0), 0);
		layeredPane.setLayer(lightL, new Integer(0), 0);
		layeredPane.setLayer(lightM, new Integer(0), 0);
		layeredPane.setLayer(lightN, new Integer(0), 0);
		layeredPane.setLayer(lightO, new Integer(0), 0);
		layeredPane.setLayer(lightP, new Integer(0), 0);
		layeredPane.setLayer(lightQ, new Integer(0), 0);
		layeredPane.setLayer(lightR, new Integer(0), 0);
		layeredPane.setLayer(lightS, new Integer(0), 0);
		layeredPane.setLayer(lightT, new Integer(0), 0);
		layeredPane.setLayer(lightU, new Integer(0), 0);
		layeredPane.setLayer(lightV, new Integer(0), 0);
		layeredPane.setLayer(lightW, new Integer(0), 0);
		layeredPane.setLayer(lightX, new Integer(0), 0);
		layeredPane.setLayer(lightY, new Integer(0), 0);
		layeredPane.setLayer(lightZ, new Integer(0), 0);
	}
	
	public void turnOnLight(String s) {
		resetLights();
		char switchChar = ' ';
		if (!s.isEmpty()){
			switchChar = s.toUpperCase().charAt(s.length()-1);
		}
		switch (switchChar) {
		case 'A':
			layeredPane.setLayer(lightA, new Integer(3), 3);
			break;
		case 'B':
			layeredPane.setLayer(lightB, new Integer(3), 3);
			break;
		case 'C':
			layeredPane.setLayer(lightC, new Integer(3), 3);
			break;
		case 'D':
			layeredPane.setLayer(lightD, new Integer(3), 3);
			break;
		case 'E':
			layeredPane.setLayer(lightE, new Integer(3), 3);
			break;
		case 'F':
			layeredPane.setLayer(lightF, new Integer(3), 3);
			break;
		case 'G':
			layeredPane.setLayer(lightG, new Integer(3), 3);
			break;
		case 'H':
			layeredPane.setLayer(lightH, new Integer(3), 3);
			break;
		case 'I':
			layeredPane.setLayer(lightI, new Integer(3), 3);
			break;
		case 'J':
			layeredPane.setLayer(lightJ, new Integer(3), 3);
			break;
		case 'K':
			layeredPane.setLayer(lightK, new Integer(3), 3);
			break;
		case 'L':
			layeredPane.setLayer(lightL, new Integer(3), 3);
			break;
		case 'M':
			layeredPane.setLayer(lightM, new Integer(3), 3);
			break;
		case 'N':
			layeredPane.setLayer(lightN, new Integer(3), 3);
			break;
		case 'O':
			layeredPane.setLayer(lightO, new Integer(3), 3);
			break;
		case 'P':
			layeredPane.setLayer(lightP, new Integer(3), 3);
			break;
		case 'Q':
			layeredPane.setLayer(lightQ, new Integer(3), 3);
			break;
		case 'R':
			layeredPane.setLayer(lightR, new Integer(3), 3);
			break;
		case 'S':
			layeredPane.setLayer(lightS, new Integer(3), 3);
			break;
		case 'T':
			layeredPane.setLayer(lightT, new Integer(3), 3);
			break;
		case 'U':
			layeredPane.setLayer(lightU, new Integer(3), 3);
			break;
		case 'V':
			layeredPane.setLayer(lightV, new Integer(3), 3);
			break;
		case 'W':
			layeredPane.setLayer(lightW, new Integer(3), 3);
			break;
		case 'X':
			layeredPane.setLayer(lightX, new Integer(3), 3);
			break;
		case 'Y':
			layeredPane.setLayer(lightY, new Integer(3), 3);
			break;
		case 'Z':
			layeredPane.setLayer(lightZ, new Integer(3), 3);
			break;
		default:
			System.out.println("Turning on lights");
			break;
		}
	}
}
