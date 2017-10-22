/*
 * Copyright (c) 2017 the original author or authors.
 */
package main;

import city.cs.engine.*;

import java.util.HashMap;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Map;

/**
 * Used for key input
 * @author James Kayes
 */
public class KeyHandler implements KeyListener {   
    private final WorldView view;
    private Game game;
    private Map<String, Object> keysPressed = new HashMap<String, Object>(); // Used for storing what keys are currently held down.
    
    /**
     * 
     * @param view passed in to allow the key handler access to this
     * @param game passed in to allow the key handler access to this
     */
    public KeyHandler(WorldView view, Game game) {
        this.view = view;
        this.game = game;
       
    }
    
    /**
     * Sets the passed in key as down
     * @param keyChar character to set as down in the keysPressed map
     */
    public void setKeyDown(String keyChar) {
        keysPressed.put(keyChar, true);
    }
    
    /**
     * Sets the passed in key as up
     * @param keyChar character to set as up in the keysPressed map
     */
    public void setKeyUp(String keyChar) {
        keysPressed.put(keyChar, false);
    }
    
    /**
     * Provides access to the map object
     * @return keysPressed - The current map of keys to if they are currently pressed
     */
    public Map<String, Object> getKeyMap() {
        return keysPressed;
    }
    
    /**
     * @param ke key event
     */
    @Override
    public void keyTyped(KeyEvent ke) {
        
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        String key = String.valueOf(ke.getKeyChar());
        keysPressed.put(key, true);
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        String key = String.valueOf(ke.getKeyChar());
        keysPressed.put(key, false);
    }
}
