/*
 * Copyright (c) 2017 the original author or authors.
 */
package main.gameobjects;

import main.gamelevels.GameLevel;
import city.cs.engine.*;
import main.CollisionHandler;
import org.jbox2d.common.Vec2;

/**
 * Abstract ship class.
 *
 * @author James Kayes
 */
public abstract class Ship extends Walker implements Collidable {

    protected float health;
    protected float maxHealth;
    /**
     * Health is recharged after a timer.
     */
    protected float healthRechargeTimer;
    protected float healthRechargeDelay;
    protected float healthRechargeRate;
    /**
     * The direction vector to determine the direction that the ship is facing.
     */
    protected Vec2 facingDirection = new Vec2();
    protected float acceleration;
    protected float maxSpeed;
    protected float rotationSpeed;
    protected float shootSpeed; // Speed of the projectiles fired by this object.
    protected float shootTimer; // Used to keep track of time between shots.
    /**
     * Seconds between shots.
     */
    protected float shootRate;
    protected boolean canShoot;
    protected GameLevel currentWorld;
    /**
     * Has this ships health changed this update?
     */
    protected boolean healthChanged = false; // 
    protected Vec2 spawnPosition;
    /**
     * Bounding circle surrounding this ship
     */
    protected float boundingRadius;

    /**
     *
     * @param world world
     * @param shipShape shape
     */
    public Ship(World world, Shape shipShape) {
        super(world, shipShape);

        currentWorld = (GameLevel) this.getWorld();
        this.addCollisionListener(new CollisionHandler(this));
    }

    /**
     *
     * @param deltaTime time between updates
     */
    public abstract void accelerate(float deltaTime);

    /**
     *
     * @param deltaTime time between updates
     */
    public abstract void rotateLeft(float deltaTime);

    /**
     *
     * @param deltaTime time between updates.
     */
    public abstract void rotateRight(float deltaTime);

    /**
     *
     * @param deltaTime time between updates.
     */
    public void rechargeHealth(float deltaTime) {
        if (health < maxHealth) {
            this.changeHealth(+healthRechargeRate * deltaTime);
        } else {
            healthRechargeTimer = healthRechargeDelay;
        }

    }

    /**
     *
     * @param deltaTime time between updates
     */
    public abstract void update(float deltaTime);

    /**
     *
     */
    public abstract void shoot();

    /**
     *
     * @param image image to change to
     */
    public void changeImage(BodyImage image) {
        this.removeAllImages();
        this.addImage(image);
    }

    public float getBoundingRadiusSquared() {
        return (this.boundingRadius * this.boundingRadius);
    }

    /**
     * Get the value of the health variable ACCSESSOR
     *
     * @return the value of health
     */
    public float getHealth() {
        return health;
    }

    /**
     * Change ships health MUTATOR
     *
     * @param amount amount to change health by
     */
    public void changeHealth(float amount) {
        this.health += amount;
        healthChanged = true;
        if (amount < 0) {
            healthRechargeTimer = this.healthRechargeDelay;
        }
        if (this instanceof EnemyShip) {
            if (this.health <= 0) {
                System.out.println("Enemy(" + this.hashCode() + ") Destroyed");
            }
            else {
                System.out.println("Enemy Health(" + this.hashCode() + "): " + this.getHealth());
            }
        }

    }

    /**
     *
     * @param position the position for this ship to spawn
     */
    public void setSpawnPos(Vec2 position) {
        this.spawnPosition = position;
    }
}
