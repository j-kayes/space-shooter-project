/*
 * Copyright (c) 2017 the original author or authors.
 */
package main.gameobjects;

import main.gameobjects.Collidable;
import city.cs.engine.*;
import org.jbox2d.common.Vec2;

/**
 * The main player shot class.
 * @author James Kayes
 */
public class PlayerShot extends Shot{
    private static final float INITIAL_RADIUS = 0.015f;
    private static final BodyImage SHOT_IMAGE = new BodyImage("data/player_shot.png"); 
    
    /**
     *
     * @param world world
     * @param velocity velocity vector
     */
    public PlayerShot(World world, Vec2 velocity) {
        super(world, INITIAL_RADIUS, velocity);
        
        this.shotFixture = new SolidFixture(this, this.shotShape, 1.0f);
        this.shotFixture.setRestitution(1.0f);
        this.shotFixture.setDensity(20.0f);
        this.shotDuration = 1.2f;
        
        addImage(SHOT_IMAGE);
    }
    
    /**
     *
     * @param other object
     */
    @Override
    public void collisionResponse(Collidable other) {
        if(other instanceof EnemyShip) {
            ((EnemyShip)other).changeHealth(-10.0f);
        }
        if(!(other instanceof PlayerShip)) {
            if (other instanceof EnemyShot) {
                ((Shot)other).destroy();
            }
            this.destroy();
        }       
    }
}
