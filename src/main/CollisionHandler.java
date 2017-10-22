/*
 * Copyright (c) 2017 the original author or authors.
 */
package main;

import city.cs.engine.*;
import main.gameobjects.*;

/**
 * Provides the collision response for objects that have this handler added to them.
 * @author James Kayes
 */
public class CollisionHandler implements CollisionListener {
    /**
     * Object that this object handles the events for.
     */
    private Collidable object;
    
    /**
     *
     * @param object object
     */
    public CollisionHandler(Collidable object) {
        this.object = object;
    }
    
    /**
     *
     * @param e collision
     */
    @Override
    public void collide(CollisionEvent e) {
        Collidable otherObject = (Collidable)e.getOtherBody();
        object.collisionResponse(otherObject);
        
    }
    
}