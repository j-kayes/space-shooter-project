/*
 * Copyright (c) 2017 the original author or authors.
 */
package main.gameobjects;

import city.cs.engine.*;
import main.CollisionHandler;

/**
 * An abstract class for all the dynamic non-ship objects in the game.
 * @author James Kayes
 */
public abstract class GameObject extends DynamicBody implements Collidable{   

    /**
     *
     * @param world world
     * @param objectShape shape of the object
     */
    public GameObject(World world, Shape objectShape) {
        super(world, objectShape);
        this.addCollisionListener(new CollisionHandler(this));
    }
    
    /**
     *
     * @param deltaTime time between calls to update
     */
    abstract public void update(float deltaTime);
    
    /**
     * Changes the image for the game object.
     * @param image image to change this game object to
     */
    public void changeImage(BodyImage image) {
        this.removeAllImages();
        this.addImage(image);
    }
}
