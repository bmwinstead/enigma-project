package main.java.GUINew;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class IOPanel extends JPanel {
	private JLabel lightA, lightB, lightC, lightD, lightE, lightF, lightG,
			lightH, lightI, lightJ, lightK, lightL, lightM, lightN, lightO,
			lightP, lightQ, lightR, lightS, lightT, lightU, lightV, lightW,
			lightX, lightY, lightZ;
	String[] letterChoices = { "A", "B", "C", "D", "E", "F", "G", "H", "I",
			"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
			"W", "X", "Y", "Z" };
	private JLayeredPane layeredPane;

	public IOPanel() {
		layeredPane = new JLayeredPane();

		JLabel backgroundImage = new JLabel();
		backgroundImage.setBackground(Color.black);
		backgroundImage.setSize(300, 300);
		backgroundImage.setOpaque(true);
		layeredPane.add(backgroundImage, new Integer(1), 1);
		layeredPane.setPreferredSize(new Dimension(300, 300));
		this.add(layeredPane, BorderLayout.CENTER);

		// Light board Icons for "lit up" design
		ImageIcon plugLightA = (new javax.swing.ImageIcon // Light A
		(getClass().getResource("/main/resources/images/a.jpg")));
		lightA = new JLabel(plugLightA);
		// lightA.setBounds(140, 305, 160, 316);
		lightA.setBorder(null);
		layeredPane.add(lightA, new Integer(0), 0);

		ImageIcon plugLightB = (new javax.swing.ImageIcon // Light B
		(getClass().getResource("/main/resources/images/b.jpg")));
		lightB = new JLabel(plugLightB);
		// lightB.setBounds(245, 330, 265, 340);
		lightB.setBorder(null);
		layeredPane.add(lightB, new Integer(0), 0);

		ImageIcon plugLightC = (new javax.swing.ImageIcon // Light C
		(getClass().getResource("/main/resources/images/c.jpg")));
		lightC = new JLabel(plugLightC);
		// lightC.setBounds(201, 330, 211, 340);
		lightC.setBorder(null);
		layeredPane.add(lightC, new Integer(0), 0);

		ImageIcon plugLightD = (new javax.swing.ImageIcon // Light D
		(getClass().getResource("/main/resources/images/d.jpg")));
		lightD = new JLabel(plugLightD);
		// lightD.setBounds(185, 305, 205, 316);
		lightD.setBorder(null);
		layeredPane.add(lightD, new Integer(0), 0);

		ImageIcon plugLightE = (new javax.swing.ImageIcon // Light E
		(getClass().getResource("/main/resources/images/e.jpg")));
		lightE = new JLabel(plugLightE);
		// lightE.setBounds(175, 281, 195, 291);
		lightE.setBorder(null);
		layeredPane.add(lightE, new Integer(0), 0);

		ImageIcon plugLightF = (new javax.swing.ImageIcon // Light F
		(getClass().getResource("/main/resources/images/f.jpg")));
		lightF = new JLabel(plugLightF);
		// lightF.setBounds(210, 305, 230, 316);
		lightF.setBorder(null);
		layeredPane.add(lightF, new Integer(0), 0);

		ImageIcon plugLightG = (new javax.swing.ImageIcon // Light G
		(getClass().getResource("/main/resources/images/g.jpg")));
		lightG = new JLabel(plugLightG);
		// lightG.setBounds(230, 305, 260, 316);
		lightG.setBorder(null);
		layeredPane.add(lightG, new Integer(0), 0);

		ImageIcon plugLightH = (new javax.swing.ImageIcon // Light H
		(getClass().getResource("/main/resources/images/h.jpg")));
		lightH = new JLabel(plugLightH);
		// lightH.setBounds(252, 304, 282, 315);
		lightH.setBorder(null);
		layeredPane.add(lightH, new Integer(0), 0);

		ImageIcon plugLightI = (new javax.swing.ImageIcon // Light I
		(getClass().getResource("/main/resources/images/i.jpg")));
		lightI = new JLabel(plugLightI);
		// lightI.setBounds(292, 281, 312, 291);
		lightI.setBorder(null);
		layeredPane.add(lightI, new Integer(0), 0);

		ImageIcon plugLightJ = (new javax.swing.ImageIcon // Light J
		(getClass().getResource("/main/resources/images/j.jpg")));
		lightJ = new JLabel(plugLightJ);
		// lightJ.setBounds(278, 304, 308, 315);
		lightJ.setBorder(null);
		layeredPane.add(lightJ, new Integer(0), 0);

		ImageIcon plugLightK = (new javax.swing.ImageIcon // Light K
		(getClass().getResource("/main/resources/images/k.jpg")));
		lightK = new JLabel(plugLightK);
		// lightK.setBounds(298, 304, 328, 315);
		lightK.setBorder(null);
		layeredPane.add(lightK, new Integer(0), 0);

		ImageIcon plugLightL = (new javax.swing.ImageIcon // Light L
		(getClass().getResource("/main/resources/images/l.jpg")));
		lightL = new JLabel(plugLightL);
		// lightL.setBounds(312, 329, 342, 339);
		lightL.setBorder(null);
		layeredPane.add(lightL, new Integer(0), 0);

		ImageIcon plugLightM = (new javax.swing.ImageIcon // Light M
		(getClass().getResource("/main/resources/images/m.jpg")));
		lightM = new JLabel(plugLightM);
		// lightM.setBounds(289, 330, 319, 340);
		lightM.setBorder(null);
		layeredPane.add(lightM, new Integer(0), 0);

		ImageIcon plugLightN = (new javax.swing.ImageIcon // Light N
		(getClass().getResource("/main/resources/images/n.jpg")));
		lightN = new JLabel(plugLightN);
		// lightN.setBounds(265, 330, 295, 340);
		lightN.setBorder(null);
		layeredPane.add(lightN, new Integer(0), 0);

		ImageIcon plugLightO = (new javax.swing.ImageIcon // Light O
		(getClass().getResource("/main/resources/images/o.jpg")));
		lightO = new JLabel(plugLightO);
		// lightO.setBounds(316, 281, 336, 291);
		lightO.setBorder(null);
		layeredPane.add(lightO, new Integer(0), 0);

		ImageIcon plugLightP = (new javax.swing.ImageIcon // Light P
		(getClass().getResource("/main/resources/images/p.jpg")));
		lightP = new JLabel(plugLightP);
		// lightP.setBounds(131, 330, 141, 340);
		lightP.setBorder(null);
		layeredPane.add(lightP, new Integer(0), 0);

		ImageIcon plugLightQ = (new javax.swing.ImageIcon // Light Q
		(getClass().getResource("/main/resources/images/q.jpg")));
		lightQ = new JLabel(plugLightQ);
		// lightQ.setBounds(127, 281, 147, 291);
		lightQ.setBorder(null);
		layeredPane.add(lightQ, new Integer(0), 0);

		ImageIcon plugLightR = (new javax.swing.ImageIcon // Light R
		(getClass().getResource("/main/resources/images/r.jpg")));
		lightR = new JLabel(plugLightR);
		// lightR.setBounds(197, 281, 217, 291);
		lightR.setBorder(null);
		layeredPane.add(lightR, new Integer(0), 0);

		ImageIcon plugLightS = (new javax.swing.ImageIcon // Light S
		(getClass().getResource("/main/resources/images/s.jpg")));
		lightS = new JLabel(plugLightS);
		// lightS.setBounds(162, 304, 182, 315);
		lightS.setBorder(null);
		layeredPane.add(lightS, new Integer(0), 0);

		ImageIcon plugLightT = (new javax.swing.ImageIcon // Light T
		(getClass().getResource("/main/resources/images/t.jpg")));
		lightT = new JLabel(plugLightT);
		// lightT.setBounds(220, 281, 240, 291);
		lightT.setBorder(null);
		layeredPane.add(lightT, new Integer(0), 0);

		ImageIcon plugLightU = (new javax.swing.ImageIcon // Light U
		(getClass().getResource("/main/resources/images/u.jpg")));
		lightU = new JLabel(plugLightU);
		// lightU.setBounds(270, 281, 290, 291);
		lightU.setBorder(null);
		layeredPane.add(lightU, new Integer(0), 0);

		ImageIcon plugLightV = (new javax.swing.ImageIcon // Light V
		(getClass().getResource("/main/resources/images/v.jpg")));
		lightV = new JLabel(plugLightV);
		// lightV.setBounds(225, 330, 235, 340);
		lightV.setBorder(null);
		layeredPane.add(lightV, new Integer(0), 0);

		ImageIcon plugLightW = (new javax.swing.ImageIcon // Light W
		(getClass().getResource("/main/resources/images/w.jpg")));
		lightW = new JLabel(plugLightW);
		// lightW.setBounds(150, 281, 170, 291);
		lightW.setBorder(null);
		layeredPane.add(lightW, new Integer(0), 0);

		ImageIcon plugLightX = (new javax.swing.ImageIcon // Light X
		(getClass().getResource("/main/resources/images/x.jpg")));
		lightX = new JLabel(plugLightX);
		// lightX.setBounds(175, 330, 195, 340);
		lightX.setBorder(null);
		layeredPane.add(lightX, new Integer(0), 0);

		ImageIcon plugLightY = (new javax.swing.ImageIcon // Light Y
		(getClass().getResource("/main/resources/images/y.jpg")));
		lightY = new JLabel(plugLightY);
		// lightY.setBounds(150, 330, 170, 340);
		lightY.setBorder(null);
		layeredPane.add(lightY, new Integer(0), 0);

		ImageIcon plugLightZ = (new javax.swing.ImageIcon // Light Z
		(getClass().getResource("/main/resources/images/z.jpg")));
		lightZ = new JLabel(plugLightZ);
		// lightZ.setBounds(245, 281, 265, 291);
		lightZ.setBorder(null);
		layeredPane.add(lightZ, new Integer(0), 0);
	}

	private void resetLights(){
		
	}
	public void turnOnLight(String s) {
		resetLights();
		switch (s.toUpperCase().charAt(s.length())) {
		case 'A':
			break;
		case 'B':
			break;
		case 'C':
			break;
		case 'D':
			break;
		case 'E':
			break;
		case 'F':
			break;
		case 'G':
			break;
		case 'H':
			break;
		case 'I':
			break;
		case 'J':
			break;
		case 'K':
			break;
		case 'L':
			break;
		case 'M':
			break;
		case 'N':
			break;
		case 'O':
			break;
		case 'P':
			break;
		case 'Q':
			break;
		case 'R':
			break;
		case 'S':
			break;
		case 'T':
			break;
		case 'U':
			break;
		case 'V':
			break;
		case 'W':
			break;
		case 'X':
			break;
		case 'Y':
			break;
		case 'Z':
			layeredPane.setLayer(lightZ, new Integer(3), 3);
			break;
		default:
			System.out.println("What?");
			break;
		}
	}
}
