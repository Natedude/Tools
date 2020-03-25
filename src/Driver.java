import control.KeyboardController;
import control.TestCommand;
import visual.Screen;


public class Driver {

    public static void main(String[] args) {

        //Initialize Devices:
        //AUDIO

        //CONTROLLER
        KeyboardController k = new KeyboardController();
        k.addCommand("A", new TestCommand(), 1000);

        //VIDEO
        Screen s = new Screen(); //pass in the key listener? does it need to be added to the JFrame for it to work while JFrame in focus?

        //===============================
        //Start Game: (& pass in devices)

    }

}
