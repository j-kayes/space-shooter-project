/*
 * Copyright (c) 2017 the original author or authors.
 */
package main.levelelements;

import city.cs.engine.*;
import main.gameobjects.Collidable;

/**
 * Diagonal static level element.
 * @author James Kayes
 */
public class Diagonal extends StaticLevelObject {

    /**
     *
     */
    public static final BodyImage DIAGONAL_IMAGE = new BodyImage("data/diagonal.png", 3.0f);

    /**
     *
     */
    public static final Shape DIAGONAL_SHAPE = new PolygonShape(1.5f, 1.5f, 1.5f, -1.5f, -1.5f, -1.5f);
    
    /**
     *
     * @param w reference to the  world/level
     * @param s shape of the object
     * @param x x-grid location to spawn this
     * @param y y-grid location to spawn this
     */
    public Diagonal(World w, Shape s, int x, int y) {
        super(w, s, x, y);
        this.addImage(DIAGONAL_IMAGE);
        
    }

    /**
     *
     * @param other the object involved with the collision
     */
    @Override
    public void collisionResponse(Collidable other) {
        // 
    }
    
}
