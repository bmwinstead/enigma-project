package main.java.GUINew;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

import main.java.enigma.EnigmaMachine;

/**
 * Used to share the EnigmaMachine across various components of the GUI.
 * Basically just wraps around the EnigmaMachine class and allows settings to be
 * changed and stuff.
 * 
 * @author bwinstead
 * 
 */
public class EnigmaSingleton extends Observable {
	public final static EnigmaSingleton INSTANCE = new EnigmaSingleton();
	private ArrayList<Observer> observers = new ArrayList<Observer>();
	private EnigmaMachine machine;
	private ConfigureOutput output = new ConfigureOutput(); //Configure text
	private String encryptedString = ""; //String to send Lightboard Update
	private char encryptedChar = ' '; //String to send Lightboard Update

	private EnigmaSingleton() {
		// No constructor for you.
	}

	public void setState(int[] rotorChoices, int reflectorChoice, char[] ringSettings, char[] initialPositions, String plugboardMap) {
		if (rotorChoices.length == 4 && rotorChoices[0] == -1){
			rotorChoices = new int[] { rotorChoices[1], rotorChoices[2],
					rotorChoices[3] };
			ringSettings = new char[] { ringSettings[1], ringSettings[2], ringSettings[3]};
			initialPositions = new char[] { initialPositions[1], initialPositions[2], initialPositions[3]};
		}
		if (plugboardMap == null)
			machine = new EnigmaMachine(rotorChoices,reflectorChoice,ringSettings, initialPositions);
		else
			machine = new EnigmaMachine(rotorChoices, reflectorChoice,
					ringSettings, initialPositions, plugboardMap);

		System.out.println("Changing rotors to: "
				+ Arrays.toString(rotorChoices));
		System.out.println("Changing reflector to: " + reflectorChoice);
		System.out.println("Changing ring settings to: "
				+ String.valueOf(ringSettings));
		System.out.println("Changing rotor positions to: "
				+ String.valueOf(initialPositions));
		System.out.println("Changing plugboard to: " + plugboardMap);
	}

	public void setState(int[] rotorChoices, int reflectorChoice,
			char[] ringSettings) {
		machine.setRotorChoices(rotorChoices, reflectorChoice);
		machine.setRingSettings(ringSettings);
		System.out.println("Changing rotors to: "
				+ Arrays.toString(rotorChoices));
		System.out.println("Changing reflector to: " + reflectorChoice);
		System.out.println("Changing ring settings to: "
				+ String.valueOf(ringSettings));
	}

	public void setPositions(char[] rotorPositions) {
		System.out.println("Setting rotor positions to: " + String.valueOf(rotorPositions));
		machine.setPositions(rotorPositions);
	}

	public void setPlugboard(String pbMap) {
		System.out.println("Setting plugboard to: " + pbMap);
		machine.setPlugboard(pbMap);
	}

	public char encryptChar(char c) {
		System.out.println("Encrypting char " + c);
		c = output.configure(c); //Text error checking
		System.out.println("Text Error Checking and Conversion");
		encryptedChar = machine.encryptChar(c); 
		encryptedString = Character.toString(encryptedChar);//For Lightboard submission
		notifyObservers();
		return encryptedChar;
	}

	public String encryptString(String s) {
		System.out.println("Encrypting string " + s);
		s = output.configure(s); //Text error checking
		System.out.println("Text Error Checking and Conversion");
		encryptedString = machine.encryptString(s); //For Lightboard submission
		notifyObservers();
		return encryptedString;
	}
	
	@Override
	public void addObserver(Observer obs){
		observers.add(obs);
	}
	@Override
	public void notifyObservers(){
		System.out.println("Notifying, rotors are currently " + String.valueOf(machine.getPositions()));
		for(Observer obs : observers){
			obs.update(this, String.valueOf(machine.getPositions()));
		}
	}
	
	//Update Lightboard
	public String getEncryptedString(){
		System.out.println("Setting Light Board");
		return encryptedString;
	}
}
