/*
 * Copyright (c) 2017 the original author or authors.
 */
package main.gameobjects;

import city.cs.engine.BodyImage;
import city.cs.engine.CircleShape;
import city.cs.engine.Shape;
import city.cs.engine.World;
import main.Game;
import org.jbox2d.common.Vec2;

/**
 * Coin pickup class.
 * @author James Kayes
 */
public class Coin extends PickupObject implements Collidable {

    /**
     *
     */
    public static final BodyImage COIN_IMAGE = new BodyImage("data/coin.gif");

    /**
     *
     */
    public static final Shape COIN_SHAPE = new CircleShape(0.25f);
    private boolean collected = false;
    

    /**
     * Creates a coin object at the specified grid coordinates
     * @param world reference to the world
     * @param objectShape shape
     * @param cellX x-grid coordinate
     * @param cellY y-grid coordinate
     */
    public Coin(World world, Shape objectShape, int cellX, int cellY) {
        super(world, objectShape);
        this.addImage(COIN_IMAGE);
        
        // Final position is cell position + offset:
        float posX = (3.0f*cellX - 21.0f);
        float posY = (3.0f*cellY - 13.5f);
        this.setPosition(new Vec2(posX, posY));
        
    }
    
    /**
     * Creates a coin object at the specified grid coordinates and the given offset
     * @param world world
     * @param objectShape shape
     * @param cellX x-grid coordinate
     * @param cellY y-grid coordinate
     * @param coinOffset vec2 representing the number of pixels to offset this coin.
     */
    public Coin(World world, Shape objectShape, int cellX, int cellY, Vec2 coinOffset) {
        super(world, objectShape);
        this.addImage(COIN_IMAGE);
        
        // Final position is cell position + offset:
        float posX = (3.0f*cellX + Game.GRID_OFFSET_X) + coinOffset.x;
        float posY = (3.0f*cellY + Game.GRID_OFFSET_Y) + coinOffset.y;
        this.setPosition(new Vec2(posX, posY));
        
    }
    
    /**
     * Creates a new coin at the passed in pixel coordinates.
     * @param world world
     * @param objectShape shape
     * @param position represents the position on the screen
     */
    public Coin(World world, Shape objectShape, Vec2 position) {
        super(world, objectShape);
        this.addImage(COIN_IMAGE);
      
        this.setPosition(position);
        
    }
    
    /**
     *
     * @return true/false value to signify if this coin has been collected yet
     */
    public boolean hasBeenCollected() {
        return collected;
    }
    
    /**
     *
     * @param other other game object involved with the collision
     */
    @Override
    public void collisionResponse(Collidable other) {
        if (other instanceof PlayerShip) {
            this.collected = true;
            this.destroy();
        }
    }
    
    /**
     *
     * @param deltaTime change in the number of seconds between update
     */
    @Override
    public void update(float deltaTime) {
        
    }
    
    
}
