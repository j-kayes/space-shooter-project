/*
 * Copyright (c) 2017 the original author or authors.
 */
package main.gameobjects;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;

/**
 * Asteroid object class.
 * @author James Kayes.
 */  
public class Asteroid extends GameObject {

    /**
     *
     */
    public static final BodyImage ASTEROID_1 = new BodyImage("data/asteroid_1.png", 3.5f);

    /**
     *
     */
    public static final Shape ASTEROID_SHAPE_1 = new PolygonShape(0.52f, 1.65f, 1.75f, 0.91f, 1.36f, -0.42f, -0.65f, -1.47f, -1.48f, -1.65f, -1.75f, -1.12f, -0.56f ,1.1f);

    /**
     *
     */
    public static final BodyImage ASTEROID_2 = new BodyImage("data/asteroid_2.png", 3.5f);

    /**
     *
     */
    public static final Shape ASTEROID_SHAPE_2 = new PolygonShape(-0.72f,-1.12f, 0.52f,-1.13f, 1.2f,-0.42f, 1.19f,0.56f, 0.59f,1.19f, -0.82f,1.1f, -1.3f,0.21f, -1.29f,-0.48f);

    /**
     *
     */
    public static final BodyImage ASTEROID_3 = new BodyImage("data/asteroid_3.png", 3.5f);

    /**
     *
     */
    public static final Shape ASTEROID_SHAPE_3 = new PolygonShape(0.33f,-0.82f, -0.98f,-0.92f, -1.25f,-0.71f, -1.13f,-0.17f, 0.04f,0.83f, 0.8f,0.93f, 1.11f,0.85f, 1.05f,0.0f);
    private boolean spawning = true; // Denotes that this is currently trying to find a place to spawn.
    
    /**
     * 
     * @param world world as passed in
     * @param type type of the object
     */
    public Asteroid(World world, Shape type) {
        super(world, type);
        
        if(type == ASTEROID_SHAPE_1)
        {
            this.addImage(ASTEROID_1);
        }
        else if (type == ASTEROID_SHAPE_2)
        {
            this.addImage(ASTEROID_2);
        }
        else if (type == ASTEROID_SHAPE_3)
        {
            this.addImage(ASTEROID_3);
        }
    }

    /**
     * Update function.
     * @param deltaTime time between calls to update
     */
    @Override
    public void update(float deltaTime) {
        this.spawning = false;
    }
    
    /**
     * Object collision event.
     * @param other the other object
     */
    @Override
    public void collisionResponse(Collidable other) {
        if (spawning) {
            float randomX = (float)(40.0*(Math.random()) - 20.0);
            float randomY = (float)(30.0*(Math.random()) - 15.0);
            
            this.setPosition(new Vec2(randomX, randomY));
        }
    }
}
