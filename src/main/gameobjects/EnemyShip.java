/*
 * Copyright (c) 2017 the original author or authors.
 */
package main.gameobjects;

import city.cs.engine.*;
import main.CollisionHandler;
import org.jbox2d.common.Vec2;

/**
 * Controls the AI and logic involved with the enemy.
 *
 * @author James Kayes (c) 2017
 */
public class EnemyShip extends Ship {

    /**
     * Enum to store the various potential states for the AI.
     */
    enum EnemyState {
        SEEK, SHOOT, WAIT
    };
    public static final Shape ENEMY_SHAPE = new PolygonShape(0.25f, -0.13f, -0.24f, -0.13f, -1.11f, 0.33f, -0.88f, 1.43f, 0.86f, 1.42f, 1.12f, 0.33f);
    public static final BodyImage ENEMY_SHIP_BOOST_IMAGE = new BodyImage("data/enemy_ship_boost.png", 3.0f);
    public static final BodyImage ENEMY_SHIP_IMAGE = new BodyImage("data/enemy_ship.png", 3.0f);
    /**
     * Max line of sight shooting distance
     */
    private static final float MAX_SHOOT_DIST = 14.0f;
    /**
     * Slowdown range
     */
    private static final float SLOWDOWN_RANGE = 50.0f; // Represents the square of the distance.
    /**
     * Seek range
     */
    private static final float SEEK_RANGE = 30.0f * 30.0f; // Represents the square of the distance
    /**
     * Current AI state.
     */
    private EnemyState aiState;
    private Ship target;
    /**
     * Vector from this ship to the target ship.
     */
    private Vec2 toTarget;
    /**
     * Line of sight vector for shooting.
     */
    private Vec2 lineOfSightShoot;
    /**
     * Angle from the facing direction of the enemy to the target ship.
     */
    private double angleToTarget;
    /**
     * Used to determine what gun to shoot from.
     */
    private boolean leftGunActive = true;
    private boolean destoryed = false;

    public boolean isDestoryed() {
        return destoryed;
    }

    /**
     *
     * @param world world
     * @param position initial position of enemy
     */
    public EnemyShip(World world, Vec2 position) {
        super(world, ENEMY_SHAPE);

        this.acceleration = 450.0f;
        this.maxSpeed = 5.0f;
        this.rotationSpeed = 180.0f;
        this.health = 100.0f;
        this.shootSpeed = 15.0f; // Speed of the projectiles fired.
        this.shootTimer = 0.0f; // Used to keep track of time between shots.
        this.shootRate = 0.15f; // Seconds between shots. 
        this.canShoot = true;
        this.facingDirection = new Vec2(0, -1);
        this.lineOfSightShoot = new Vec2(0, -1);
        this.lineOfSightShoot.mul(MAX_SHOOT_DIST);
        this.setPosition(position);
        this.aiState = EnemyState.WAIT;
        this.rotateDegrees(180.0f);
        
        addImage(ENEMY_SHIP_IMAGE);
    }

    /**
     *
     * @param deltaTime time between updates
     */
    @Override
    public void accelerate(float deltaTime) {
        this.changeImage(ENEMY_SHIP_BOOST_IMAGE);
        this.applyForce(facingDirection.mul((deltaTime * acceleration) * this.getMass()));

        // Limit speed:
        if (this.getLinearVelocity().length() > maxSpeed) {
            Vec2 velocityDirection = this.getLinearVelocity().mul(1 / (this.getLinearVelocity().length()));
            this.setLinearVelocity(velocityDirection.mul(maxSpeed));
        }
    }

    /**
     *
     * @param deltaTime time between updates
     */
    public void deccelerate(float deltaTime) {
        this.applyForce(this.getLinearVelocity().mul((deltaTime * -acceleration) * this.getMass()));
    }

    /**
     * Rotates the enemy until it is facing in the direction of the target
     *
     * @param deltaTime time between updates
     */
    public void aim(float deltaTime) {
        // Lead shots in the direction of the targets movement:
        if (target.getLinearVelocity().lengthSquared() > 0) {
            float targetSpeed = target.getLinearVelocity().length();
            Vec2 targetVelDirection = target.getLinearVelocity().mul(1.0f / targetSpeed);
            float d = toTarget.length();
            this.toTarget = toTarget.add(targetVelDirection.mul(d*(targetSpeed/shootSpeed)));
        }

        if (this.angleToTarget > 0.01 * Math.PI) {
            // Used to calculate what way to turn:
            float crossProduct = ((this.facingDirection.x) * (this.toTarget.y) - (this.toTarget.x) * (this.facingDirection.y));
            if (crossProduct > 0) {
                this.rotateLeft(deltaTime);
            } else {
                this.rotateRight(deltaTime);
            }

        }
    }

    /**
     *
     * @param other other object
     */
    @Override
    public void collisionResponse(Collidable other) {
        
    }

    /**
     *
     * @param deltaTime tie between updates
     */
    @Override
    public void rotateLeft(float deltaTime) {
        this.rotateDegrees(deltaTime * rotationSpeed);
    }

    /**
     *
     * @param deltaTime ie between updates
     */
    @Override
    public void rotateRight(float deltaTime) {
        this.rotateDegrees(-deltaTime * rotationSpeed);
    }

    /**
     *
     * @param target target as set
     */
    public void setTarget(Ship target) {
        this.target = target;
    }

    /**
     * Seek target
     *
     * @param deltaTime time between updates
     */
    public void seek(float deltaTime) {
        // Move in the direction of the target:
        accelerate(deltaTime);
    }

    /**
     * Enemy shoot method
     */
    @Override
    public void shoot() {
        if (this.canShoot) {
            if (leftGunActive) {
                Vec2 leftGunPos = this.getPosition().add(facingDirection).add(new Vec2(-0.67f * facingDirection.y, facingDirection.x));
                Vec2 shotVelocity = facingDirection.mul(shootSpeed).add(this.getLinearVelocity());
                EnemyShot shot = new EnemyShot(this.getWorld(), shotVelocity);
                shot.setPosition(leftGunPos);
                shot.addCollisionListener(new CollisionHandler(shot));
                currentWorld.addGameObject(shot);
                canShoot = false;
                leftGunActive = false;
            } else {
                Vec2 rightGunPos = this.getPosition().add(facingDirection).add(new Vec2(0.67f * facingDirection.y, -facingDirection.x));
                Vec2 shotVelocity = facingDirection.mul(shootSpeed).add(this.getLinearVelocity());
                EnemyShot shot = new EnemyShot(this.getWorld(), shotVelocity);
                shot.setPosition(rightGunPos);
                shot.addCollisionListener(new CollisionHandler(shot));
                currentWorld.addGameObject(shot);
                canShoot = false;
                leftGunActive = true;
            }
        }
    }

    /**
     * Main update function for the enemy.
     *
     * @param deltaTime time between updates
     */
    @Override
    public void update(float deltaTime) {
        if(this.health < 0) {
            this.destoryed = true;
        }
        // Update ship direction vector:
        double xDirection = Math.cos(((double) this.getAngle() + Math.PI / 2.0));
        double yDirection = Math.sin(((double) this.getAngle() + Math.PI / 2.0));
        facingDirection = new Vec2((float) xDirection, (float) yDirection);
        facingDirection.normalize();

        // Update shooting line of sight:
        this.lineOfSightShoot = this.facingDirection.mul(MAX_SHOOT_DIST);

        // Calculate vector to target:
        this.toTarget = this.target.getPosition().sub(this.getPosition());

        // This will change the length of the vector to 1 unit:
        this.toTarget.normalize();

        // Calculated from the dot product of these vectors:
        this.angleToTarget = Math.acos(((xDirection) * (this.toTarget.x)) + ((yDirection) * (this.toTarget.y)));
        //this.angleToTarget = Math.toDegrees(angleToTarget);

        if (!this.canShoot) {
            if (shootTimer < shootRate) {
                shootTimer += deltaTime;
            } else {
                shootTimer = 0.0f;
                this.canShoot = true; // Allow ship to shoot again.
            }
        }

        // Update vector to target:
        this.toTarget = this.target.getPosition().sub(this.getPosition());
        
        if(this.healthChanged) {
           aiState = EnemyState.SEEK;
        }

        switch (aiState) {
            case WAIT:
                this.changeImage(ENEMY_SHIP_IMAGE);
                this.deccelerate(deltaTime);
                if (toTarget.lengthSquared() < SEEK_RANGE) {
                    deccelerate(deltaTime);
                    aiState = EnemyState.SEEK;
                }
                break;
            case SEEK:
                // Rotate towards target:
                aim(deltaTime);
                seek(deltaTime);
                /* Check if the enemies line of sight intersects the players bounding radius
                 * does this by using a tracing point along the line of sight:
                 */
                float deltaDist = 0.5f;
                int iterations = (int) (MAX_SHOOT_DIST / deltaDist);
                Vec2 deltaLos = this.lineOfSightShoot.mul(1.0f / this.lineOfSightShoot.length()).mul(deltaDist); // Delta distance along LOS.

                for (int i = 0; i < iterations; i++) {
                    Vec2 testVector = deltaLos.mul(i);
                    if (testVector.sub(toTarget).lengthSquared() < target.getBoundingRadiusSquared()) {
                        aiState = EnemyState.SHOOT;
                        break;
                    }
                }
                if (this.lineOfSightShoot.sub(toTarget).lengthSquared() < target.getBoundingRadiusSquared()) {
                    aiState = EnemyState.SHOOT;
                }
                break;
            case SHOOT:
                // Rotate towards target:
                aim(deltaTime);
                
                if (toTarget.lengthSquared() > SEEK_RANGE) {
                        aiState = EnemyState.WAIT;
                }
                if (toTarget.lengthSquared() < SLOWDOWN_RANGE) {
                    this.changeImage(ENEMY_SHIP_IMAGE);
                    deccelerate(deltaTime);
                }
                else {
                    seek(deltaTime);
                }
                
                shoot();
                break;
        }
        this.healthChanged = false;
    }

}
