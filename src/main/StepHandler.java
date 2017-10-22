/*
 * Copyright (c) 2017 the original author or authors.
 */
package main;

import city.cs.engine.*;
import main.gameobjects.*;

/**
 * Triggers events at the start/end of each step, and deals with user input.
 * @author James Kayes
 */
public class StepHandler implements StepListener {
    
    /**
    * Keeps track of the time between steps
    */
    private static float deltaTime;
    private final KeyHandler keyHandler;
    private final Game game;
    private final Ship player;
    private boolean spaceDown = false;
    
    /**
     *
     * @param keyHandler key handler reference
     * @param game reference
     */
    public StepHandler(KeyHandler keyHandler, Game game) {
        this.keyHandler = keyHandler;
        this.game = game;
        this.player = game.getCurrentWorld().getPlayer();
    }
    
    // 

    /**
     * Is executed at the start of each step of the game world
     * @param e step event
     */
    @Override
    public void preStep(StepEvent e) {     
    }

    /**
     * Is executed at the end of each step of the game world
     * @param e step event
     */
    @Override
    public void postStep(StepEvent e) {
        deltaTime = e.getStep();
        
        game.getCurrentWorld().update(deltaTime);
        
        // Loops through the map of keys as updated by the keyHandler:
        for(Object key: keyHandler.getKeyMap().keySet())
        {
            String keyChar = (String)key;
            Object value = keyHandler.getKeyMap().get(key);
            if (keyChar.equals(" ") && (boolean)value == false) {
                spaceDown = false;
            }
            if((value != null) && ((boolean)value == true))
            {
                // Key events:
                switch(keyChar)
                {
                    case "w":
                    {
                        player.accelerate(deltaTime);
                        break;
                    }
                    case "a":
                    {
                        player.rotateLeft(deltaTime);
                        break;
                    }
                    case "d":
                    {
                        player.rotateRight(deltaTime);
                        break;
                    }
                    case " ":
                    {
                        if(!spaceDown) {
                            player.shoot();
                            spaceDown = true;
                            break;
                        }
                    }
                }
            }
            
        }
    }
    
}
