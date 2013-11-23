package enigma;

/**
 * 
 * @author Ellen Ohlmacher
 * @author Team Enigma
 * @version 0.7
 * 
 * A driver to create the Enigma GUI panel. Currently this works as an app,
 * but the team intends to have both app and applet options in the future. 
 *
 */
public class Enigma {

    public static void main(String[] args) {
        new Enigma();
    }
    
    public Enigma(){
        
        //Create the GUI Panel      
        EnigmaGUI frame = new EnigmaGUI();
        frame.setVisible(true);

    }
}
