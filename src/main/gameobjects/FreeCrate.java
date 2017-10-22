/*
 * Copyright (c) 2017 the original author or authors.
 */
package main.gameobjects;

import city.cs.engine.*;

/**
 * This class represents a crate object that can freely move through space.
 * @author James Kayes
 */
public class FreeCrate extends GameObject implements Collidable {

    /**
     *
     */
    public static final BodyImage CRATE_IMAGE = new BodyImage("data/crate.png", 2.9f);

    /**
     *
     */
    public static final Shape CRATE_SHAPE = new BoxShape(1.45f, 1.45f);
    
    /**
     *
     * @param world world
     * @param objectShape shape
     */
    public FreeCrate(World world, Shape objectShape) {
        super(world, objectShape);
        this.addImage(CRATE_IMAGE);
    }

    /**
     *
     * @param deltaTime time
     */
    @Override
    public void update(float deltaTime) {
        
    }

    /**
     *
     * @param other object
     */
    @Override
    public void collisionResponse(Collidable other) {
        
    }
    
}
