/*
 * Copyright (c) 2017 the original author or authors.
 */
package main.gameobjects;

import main.CollisionHandler;
import city.cs.engine.*;
import main.Game;
import org.jbox2d.common.Vec2;

/**
 * Class encapsulating the players ship.
 * @author James Kayes (c) 2017
 *
 */
public class PlayerShip extends Ship {

    public static final Shape PLAYER_SHAPE = new PolygonShape(0.868f,-0.361f, -0.813f,-0.358f, -0.903f,0.426f, 0.019f,1.19f, 0.939f,0.435f);
    public static final BodyImage PLAYER_SHIP_BOOST_IMAGE = new BodyImage("data/player_ship_boost.png", 2.4f);
    public static final BodyImage PLAYER_LEFT_BOOST_IMAGE = new BodyImage("data/player_ship_boost_l.png", 2.4f);
    public static final BodyImage PLAYER_RIGHT_BOOST_IMAGE = new BodyImage("data/player_ship_boost_r.png", 2.4f);
    public static final BodyImage PLAYER_SHIP_IMAGE = new BodyImage("data/player_ship.png", 2.4f);
 
    private Game game;
    private int lives = 3;
    private int coins = 0;
    private boolean accelerating = false;
    private boolean rotating = false;
    
    /**
     *
     * @param world world
     * @param game game
     * @param spawnPosition spawn position
     */
    public PlayerShip(World world, Game game, Vec2 spawnPosition) {
        super(world, PLAYER_SHAPE);    
        
        addImage(PLAYER_SHIP_IMAGE);
        
        this.spawnPosition = spawnPosition;
        this.health = 100.0f;
        this.acceleration = 500.0f;
        this.maxSpeed = 10.0f;
        this.rotationSpeed = 260.0f;
        this.shootSpeed = 28.0f; // Speed of the projectiles fired.
        this.shootTimer = 0.0f; // Used to keep track of time between shots.
        this.shootRate = 0.125f; // Seconds between shots. 
        this.canShoot = true; 
        this.facingDirection = new Vec2(0, 1); // Up.
        this.game = game;
        this.boundingRadius = 5.0f;
        this.healthRechargeDelay = 2.0f;
        this.healthRechargeRate = 20.0f;
        this.healthRechargeTimer = this.healthRechargeDelay;
        this.maxHealth = this.health;
        
        this.setPosition(this.spawnPosition);
    }

    /**
     *
     * @param deltaTime time between updates
     */
    @Override
    public void accelerate(float deltaTime) {
        this.applyForce(facingDirection.mul((deltaTime*acceleration)*this.getMass()));
        this.changeImage(PLAYER_SHIP_BOOST_IMAGE);
        this.accelerating = true;
        
    }
    
    /**
     *
     * @return facing direction vector for ship.
     */
    public Vec2 getFacingDirection() {
        return this.facingDirection;
    }
    
    /**
     *
     * @param position position to respawn player
     */
    public void respawn(Vec2 position) {
        this.setPosition(position);
        this.health = 100.0f;
        this.acceleration = 1000.0f;
        this.maxSpeed = 15.0f;
        this.rotationSpeed = 180.0f;
        this.shootSpeed = 20.0f; // Speed of the projectiles fired.
        this.shootTimer = 0.0f; // Used to keep track of time between shots.
        this.shootRate = 0.1f; // Seconds between shots. 
        this.canShoot = true; 
        this.facingDirection = new Vec2(0, 1); // Up.
        this.setAngleDegrees(0.0f);
        this.setLinearVelocity(new Vec2(0.0f, 0.0f));
        game.getView().updateHealthBar();
        
    }
    
    /**
     *
     * @param deltaTime time between updates
     */
    @Override
    public void rotateLeft(float deltaTime) {
        this.rotateDegrees(deltaTime*rotationSpeed);
        this.changeImage(PLAYER_RIGHT_BOOST_IMAGE);
        this.rotating = true;
    }

    /**
     *
     * @param deltaTime time between updates
     */
    @Override
    public void rotateRight(float deltaTime) {
        this.rotateDegrees(-deltaTime*rotationSpeed);
        this.changeImage(PLAYER_LEFT_BOOST_IMAGE);
        this.rotating = true;
    }

    /**
    * Player shoot method.
    */
    @Override
    public void shoot() {
        if(this.canShoot) {
            Vec2 shotVelocity = facingDirection.mul(shootSpeed).add(this.getLinearVelocity());
            PlayerShot shot = new PlayerShot(this.getWorld(), shotVelocity);
            shot.setPosition(this.getPosition().add(this.facingDirection));
            shot.addCollisionListener(new CollisionHandler(shot));
            currentWorld.addGameObject(shot);
            canShoot = false;
        }
    }
    
    /**
     * main player update function.
     * @param deltaTime ime between updates
     */
    @Override
    public void update(float deltaTime) {
        if(!this.accelerating || !this.rotating) {
            this.changeImage(PLAYER_SHIP_IMAGE);
        }
        // Calculate ship direction:
        double xDirection = Math.cos(((double)this.getAngle() + Math.PI/2.0));
        double yDirection = Math.sin(((double)this.getAngle() + Math.PI/2.0));
        facingDirection = new Vec2((float)xDirection,(float)yDirection);
        
        // Limit speed:
        if(this.getLinearVelocity().length() > maxSpeed)
        {
            Vec2 velocityDirection = this.getLinearVelocity().mul(1/(this.getLinearVelocity().length()));
            this.setLinearVelocity(velocityDirection.mul(maxSpeed));
        }
        
        // While the player can't shoot:
        if(!this.canShoot) {
            if(shootTimer < shootRate) {
                shootTimer += deltaTime;
            }
            else
            {
                shootTimer = 0.0f;
                this.canShoot = true; // Allow player to shoot again.
            }
        }
        
        if(this.health <= 0) {
            this.lives--;
            if(this.lives == 0) {
                System.out.println("GAME OVER!");
                this.destroy();
                this.game.gameOver();
                System.exit(0);
            }
            else if (this.lives > 0) {
                this.respawn(spawnPosition); 
            }
        }
        this.accelerating = false;
        this.rotating = false;
        
        
        if(this.healthRechargeTimer <= 0) {
            this.rechargeHealth(deltaTime);
        }
        else {
           this.healthRechargeTimer -= deltaTime;
        }
        
   }
    
    /**
     *
     * @param other other object involved with the collision
     */
    @Override
    public void collisionResponse(Collidable other) {
        if (other instanceof Coin) {
            this.coins++;
        }
        if (other instanceof EnemyShot) {
            this.healthRechargeTimer = this.healthRechargeDelay;
        }
    }
    
    /**
     * Changes players health value by the amount passed in.
     * @param ammount amount to change health
     */
    @Override
    public void changeHealth(float ammount) {
        this.health += ammount;
        game.getView().updateHealthBar();
    }
    
    /**
     * Set the value of lives
     * MUTATOR
     * @param lives amount to change lives by
     */
    public void changeLives(int lives) {
        this.lives += lives;
    }
    
    /**
     * Get the value of lives
     * ACCESSOR
     * @return the value of lives
     */
    public int getLives() {
        return lives;
    }
    
    /**
     * Get the number of coins that the player currently has.
     * @return the value of coins
     */
    public int getCoins() {
        return coins;
    }
    
    
}
