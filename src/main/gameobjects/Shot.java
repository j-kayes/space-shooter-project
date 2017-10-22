/*
 * Copyright (c) 2017 the original author or authors.
 */
package main.gameobjects;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;

/**
 * Abstract projectile class.
 * @author James Kayes
 */
public abstract class Shot extends GameObject{

    /**
     * Used to determine how long since the shot has been created.
     */
    protected float age;

    /**
     *
     */
    protected SolidFixture shotFixture;

    /**
     *
     */
    protected Shape shotShape;

    /**
     * Duration that the shot can exist for.
     */
    protected float shotDuration;

    /**
     *
     * @param world world
     * @param radius the radius of the shot
     * @param velocity the velocity that this object moves at
     */
    public Shot(World world, float radius, Vec2 velocity) {
        super(world, new CircleShape(radius));
        this.shotShape = new CircleShape(radius);
        this.setBullet(true);
        this.setLinearVelocity(velocity);
        this.age = 0.0f;     
        
    }
    
    /**
     *
     * @param deltaTime time between updates
     */
    @Override
    public void update(float deltaTime) {
        this.age += deltaTime;
        if(this.age > this.shotDuration)
        {
            this.destroy();
        }
    }
}
