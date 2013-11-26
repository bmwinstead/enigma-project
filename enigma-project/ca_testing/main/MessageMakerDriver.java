package main;

import views.MessageMakerPanel;

public class MessageMakerDriver {
    public static void main(String[] args) {
        new MessageMakerDriver();
    }
    
    public MessageMakerDriver(){
        
        //Create the GUI Panel      
        MessageMakerPanel frame = new MessageMakerPanel();
        frame.setVisible(true);

    }

}
