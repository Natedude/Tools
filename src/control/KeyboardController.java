package control;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

//inspired by https://tips4java.wordpress.com/2013/06/09/motion-using-the-keyboard/
//and https://tips4java.wordpress.com/2009/08/30/global-event-listeners/
public class KeyboardController {
    //I do want a delay for firing bullets so there isn't a pause after press before system decides delay
    //I want multiple keys to work when they are held down
    //I want to easily reconfigure the keys with custom key choices by user
    //I want the Commands to be able to be recorded for replays
    //I also want this to work in the future if I am not using a swing component or any windowed component,
    // maybe the display could be a different type of device like a laser.... hmmm, the post processor would still
    // need some kind of component in order to be in focus, right? but it listens on all components
    // I hope that this is not significantly slower than using key bindings
    // ? Would this work with modifier keys? seems so because the e.getModifiersEx

    /*
    Desired:
    Create dynamic/reconfigurable key bindings with MODIFIERS with DELAY for a SINGLE component,
    SET of components, or
    GLOBAL

    Can start with GLOBAL, then add a SINGLE, or add a SET
    or
    Start with SINGLE or SET, and then add more SINGLES or SETS or a GLOBAL

    create a map for GLOBAL with <KeyWithModifiers, mappedKeysMap>
    create a map for components, <JComponent, HashMap>
    that gives that component's HashMap for mappedKeys <KeyWithModifiers>

    ?? what if key is held down and then a modifier is pressed? modifier ignored for already pressed keys, only effects keys hit after it is held

    *need to detect what component is in focus to implement the SINGLE and SET
    KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner()

    ?? What data structure for keys with modifiers?

    ?? Want this to limit only one instance being made
    ?? Do I want it to be globally accessible? if both, then do Singleton.

    ?? What kind of event queue should this work with?

    ?? How do I get it to trigger events on a delay?
    -seems like each pressed key is going to need a timer, maybe only keys that are found in a map?
    -maybe there could be a base delay for all keys?
     */


    //GLOBAL with <KeyWithModifiers, mappedKeysMap>
    private Map<KeyStroke,CommandDelayTimer> globalMappedKeys = new HashMap<>();
    //map for SINGLE & SET of components, <JComponent, HashMap>
    private Map<Component,HashMap<KeyStroke,CommandDelayTimer>> componentToMappedKeys = new HashMap<>();
    //map for currently pressed keys, to implement DELAY
    private Map<KeyStroke, CommandDelayTimer> pressedKeys = new HashMap<>();

    private Component focusedComponent;

    //Constructor
    //needs JComponent passed in, will be the JFrame or the JPanel, or maybe just the InputMap and ActionMap
    public KeyboardController(){
        //does this need to be in constructor? could it be somewhere better?
        KeyEventPostProcessor postProcessor = new KeyEventPostProcessor() {
            public boolean postProcessKeyEvent(KeyEvent e) {
                //System.out.println(e);
                //getID gets 401 for pressed and 402 for released
                int id = e.getID();
                if(id == KeyEvent.KEY_PRESSED){
                    keyPressed(e);
                } else if (id == KeyEvent.KEY_RELEASED){
                    keyReleased(e);
                }
                return true;
            }
        };

        //make sure focused component is always the currently focused component using a change listener
        focusedComponent = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
        //System.out.println("FOCUS: focusedComponent SET: " + focusedComponent.getName());
        PropertyChangeListener propertyChangeListener = new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent e) {
                String propertyName = e.getPropertyName();
                if ("focusOwner".equals(propertyName)) {
                    focusedComponent = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
//                    if(focusedComponent != null){
//                        System.out.println("FOCUS: focusedComponent changed: " + focusedComponent.getName());
//                    }

                }
            }
        };
        DefaultKeyboardFocusManager.getCurrentKeyboardFocusManager().
                addPropertyChangeListener(propertyChangeListener);
        DefaultKeyboardFocusManager.getCurrentKeyboardFocusManager().
                addKeyEventPostProcessor(postProcessor);
    }
//get global and get component specific
//    public control.Command getCommand(){
//
//    }

    private void keyPressed(KeyEvent e) {
        //get key code and modifiers
        int keycode = e.getExtendedKeyCode();
        int modifiers = e.getModifiersEx();
        //System.out.println("PRESSED: " + KeyEvent.getKeyText(keycode) + " with " + KeyEvent.getModifiersExText(modifiers));

        KeyStroke keyStroke = KeyStroke.getKeyStrokeForEvent(e);
        System.out.println("PRESSED: " + keyStroke.toString() + " -- at: " + System.currentTimeMillis());

        //add to pressedKeys
        //TODO maybe add pressedKeys ArrayList and a search method if there is lag
        //pressedKeys.add

        //check if in maps
        if(globalMappedKeys.containsKey(keyStroke)){
            //start CommandDelayTimer
            globalMappedKeys.get(keyStroke).startTimer();
        }

        //if componentMap has component
        if(componentToMappedKeys.containsKey(focusedComponent)){
            HashMap<KeyStroke, CommandDelayTimer> componentHashMap = componentToMappedKeys.get(focusedComponent);
            if(componentHashMap.containsKey(keyStroke)){
                componentHashMap.get(keyStroke).startTimer();
            }
        }
    }

    private void keyReleased(KeyEvent e) {
        KeyStroke keyStroke = KeyStroke.getKeyStrokeForEvent(e);
        //remove 'released'
        String s = keyStroke.toString();
        String newKeyStrokeString = s.replace("released ","");
        keyStroke = KeyStroke.getKeyStroke(newKeyStrokeString);
        System.out.println("RELEASED: " + newKeyStrokeString);

        //check if in maps
        if(globalMappedKeys.containsKey(keyStroke)){ //TODO only the pressed KeyStroke is in the map!!!
            //stop CommandDelayTimer
            globalMappedKeys.get(keyStroke).stopTimer();
        }

        //if componentMap has component
        if(componentToMappedKeys.containsKey(focusedComponent)){
            HashMap<KeyStroke, CommandDelayTimer> componentHashMap = componentToMappedKeys.get(focusedComponent);
            if(componentHashMap.containsKey(keyStroke)){
                componentHashMap.get(keyStroke).stopTimer();
            }
        }
    }

    //add to SINGLE JComponent
    public void addCommand(JComponent component, String keyStrokeString, Command command, int delayMillis){
        System.out.println("ERROR: addCommand SINGLE Component NOT YET IMPLEMENTED");
    }

    //add to SET of JComponents
    public void addCommand(JComponent[] components, String keyStrokeString, Command command, int delayMillis){
        System.out.println("ERROR: addCommand SET of Components NOT YET IMPLEMENTED");
    }

    //add to GLOBAL
    //how to type correctly at https://docs.oracle.com/javase/7/docs/api/javax/swing/KeyStroke.html#getKeyStroke(java.lang.String)
    // modifiers seperated by space then released then uppercase char or key name
    public void addCommand(String keyStrokeString, Command command, int delayMillis){
        KeyStroke keyStroke = KeyStroke.getKeyStroke(keyStrokeString);
        if(keyStroke == null){
            System.out.println("KeyStroke incorrectly written: " + keyStrokeString);
        }
        globalMappedKeys.put(keyStroke, new CommandDelayTimer(command, delayMillis));
    }


//    private KeyStroke getKeyStrokeFromString(String keyStrokeString){
//        int offset = keyStrokeString.lastIndexOf(" ");
//        String key = offset == -1 ? keyStrokeString :  keyStrokeString.substring( offset + 1 );
//        String modifiers = keyStrokeString.replace(key, "");
//
//    }


    /**
     * Uses Swing Timer to execute commands at fixed millisecond rate
     */
    private class CommandDelayTimer implements ActionListener{
        private Command command;
        private int delayMillis;
        private Timer timer;

        //TODO maybe make delayMillis == -1 if no repetition and have startTimer just execute the command like with pause?
        CommandDelayTimer(Command command, int delayMillis){
            this.command = command;
            this.delayMillis = delayMillis;
            timer = new Timer(delayMillis, this);
            timer.setInitialDelay( 0 );
        }
        CommandDelayTimer(Command command){
            this.command = command;
            this.delayMillis = -1;
        }

        //TODO need public if class is private?
        Command getCommand(){
            return command;
        }

        void startTimer(){
            if(delayMillis == -1){
                command.execute();
            } else {
                if(!timer.isRunning()){
                    System.out.println("TIMER STARTED -- at: " + System.currentTimeMillis());
                    timer.start();
                }
            }
        }
        void stopTimer(){
            if(delayMillis != -1){
                timer.stop();
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            //TODO the tanks will need to be able to tell if the game is paused, maybe pass in game?
            //System.out.println("EXECUTED -- at: " + System.currentTimeMillis());
            command.execute();
        }
    }
}
