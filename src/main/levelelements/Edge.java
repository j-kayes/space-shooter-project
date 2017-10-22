/*
 * Copyright (c) 2017 the original author or authors.
 */
package main.levelelements;

import city.cs.engine.*;
import main.gameobjects.*;

/**
 * Edge static level element.
 * @author James Kayes
 */
public class Edge extends StaticLevelObject {

    /**
     *
     */
    public static final BodyImage EDGE_IMAGE = new BodyImage("data/edge.png", 3.0f);
   
    /**
     *
     * @param w reference to the world
     * @param s shape
     * @param x x-grid location to spawn this
     * @param y y-grid location to spawn this
     * @param facing facing direction
     */
    public Edge(World w, Shape s, int x, int y, Facing facing) {
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
        this.addImage(EDGE_IMAGE);
    }

    /**
     *
     * @param other other object involved in the collision
     */
    @Override
    public void collisionResponse(Collidable other) {
       //
    }
    
}
