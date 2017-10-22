/*
 * Copyright (c) 2017 the original author or authors.
 */
package main.gameobjects;

import city.cs.engine.*;

/**
 * Object class for pickups.
 * @author James Kayes
 */
public class PickupObject extends GameObject {

    /**
     *
     */
    protected PickupFixture pickupFixture;

    /**
     *
     * @param world world
     * @param objectShape shape
     */
    public PickupObject(World world, Shape objectShape) {
        super(world, objectShape);
        pickupFixture = new PickupFixture(this, objectShape);
        
    }
    
    /**
     *
     * @param deltaTime time between updates
     */
    @Override
    public void update(float deltaTime) {
    }

    /**
     *
     * @param other other object involved with the collision
     */
    @Override
    public void collisionResponse(Collidable other) {
    }
    
}
