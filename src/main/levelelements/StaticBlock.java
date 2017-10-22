/*
 * Copyright (c) 2017 the original author or authors.
 */
package main.levelelements;

import city.cs.engine.*;
import main.gameobjects.Collidable;

/**
 * An abstract class for all the static level elements to inherit from.
 * @author James Kayes
 */
public class StaticBlock extends StaticLevelObject {

    /**
     *
     */
    public static final BodyImage BLOCK_IMAGE = new BodyImage("data/block.png", 3.0f);

    /**
     *
     */
    public static final Shape BLOCK_SHAPE = new BoxShape(1.5f, 1.5f);
    
    /**
     *
     * @param w reference to the world
     * @param s shape
     * @param x x-grid location to spawn this
     * @param y y-grid location to spawn this
     */
    public StaticBlock(World w, Shape s, int x, int y) {
        super(w, s, x, y);
        this.addImage(BLOCK_IMAGE);
        
    }

    /**
     *
     * @param other other object involved with the collision.
     */
    @Override
    public void collisionResponse(Collidable other) {
        // 
    }
    
}
