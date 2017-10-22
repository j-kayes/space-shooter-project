/*
 * Copyright (c) 2017 the original author or authors.
 */
package main.levelelements;

import city.cs.engine.*;
import main.gameobjects.*;

/**
 * Spikes static level object.
 * @author James Kayes
 */
public class Spikes extends StaticLevelObject { 

    /**
     *
     */
    public static final BodyImage SPIKES_IMAGE = new BodyImage("data/spikes.png", 3.0f);

    /**
     *
     */
    public static final Shape SPIKES_SHAPE = new PolygonShape(-1.06f,0.01f, 1.05f,0.02f, 1.44f,-1.37f, -1.47f,-1.36f);
    
    /**
     *
     * @param w reference to the world
     * @param s shape
     * @param x x-grid location to spawn this
     * @param y y-grid location to spawn this
     * @param facing facing direction
     */
    public Spikes(World w, Shape s, int x, int y, Facing facing) {
        super(w, s, x, y);
        switch(facing) {
            case LEFT:
               this.setAngleDegrees(90.0f);
               break;
            case UP:
               this.setAngleDegrees(0.0f);
               break;
            case RIGHT:
               this.setAngleDegrees(270.0f);
               break; 
            case DOWN:
               this.setAngleDegrees(180.0f);
               break;
        }
        this.addImage(SPIKES_IMAGE);
    }

    /**
     *
     * @param other other object involved with the collision
     */
    @Override
    public void collisionResponse(Collidable other) {
       if (other instanceof PlayerShip) {
           ((Ship)other).changeHealth(-100.0f);
           
       }
    }
    
}
