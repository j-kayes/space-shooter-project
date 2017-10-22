/*
 * Copyright (c) 2017 the original author or authors.
 */
package main.gameobjects;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;

/**
 * Enemy projectile.
 * @author James Kayes
 */
public class EnemyShot extends Shot {
    
    private static final float INITIAL_RADIUS = 0.05f;
    private static final BodyImage SHOT_IMAGE = new BodyImage("data/enemy_shot.png", 0.5f);
    
    /**
     * 
     * @param world world
     * @param velocity velocity
     */
    public EnemyShot(World world, Vec2 velocity) {
        super(world, INITIAL_RADIUS, velocity);
        
        this.shotFixture = new SolidFixture(this, this.shotShape, 1.0f);
        this.shotFixture.setRestitution(1.0f);
        this.shotFixture.setDensity(10.0f);
        this.shotDuration = 2.0f;
        
        addImage(SHOT_IMAGE);
    }

    /**
     *
     * @param other other object
     */
    @Override
    public void collisionResponse(Collidable other) {
        if(other instanceof PlayerShip) {
            // Player ship collision event:
            ((PlayerShip)other).changeHealth(-10.0f); // Changes player health by the damage ammount of this shot.
        }
        if(!(other instanceof EnemyShip)) {
            if (other instanceof PlayerShot) {
                ((Shot)other).destroy();
            }
            this.destroy();
        }       
    }
    
}
