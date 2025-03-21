import java.awt.event.*;
import java.util.ArrayList;

public class Controls implements KeyListener{
    // Singleton
    private static final Controls instance = new Controls();    // The single instance of this class
    private Controls(){ keys = new ArrayList<>(); }             // a private constructor to prevent more objects
    public static Controls getInstance(){ return instance; }    // public access to the instance



    // You should try to understand the code below
    private final ArrayList<KeyEvent> keys;                     // Inputs are stored in a list

    public void clear(){                                        // Once inputs are handled, remove them from the list
        keys.clear();
    }

    public ArrayList<KeyEvent> getKeys() {                      // access to the list
        return keys;
    }

    @Override public void keyPressed(KeyEvent e) {              // Once a key is pressed we add it to the list
        keys.add(e);
    }





    // We must implement these methods from the interface, but don't actually need them.
    // You can ignore this.
    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}


}
