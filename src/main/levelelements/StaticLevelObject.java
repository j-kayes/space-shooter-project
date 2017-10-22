/*
 * Copyright (c) 2017 the original author or authors.
 */
package main.levelelements;
import city.cs.engine.*;
import main.CollisionHandler;
import main.Game;
import main.gameobjects.Collidable;
import org.jbox2d.common.Vec2;

/**
 * An abstract class for all the static level elements to inherit from.
 * @author James Kayes
 */
public abstract class StaticLevelObject extends StaticBody implements Collidable {

    /**
     * Enum used to determine the direction that this object is facing
     */
    public static enum Facing { 

        /**
         *
         */
        LEFT, 

        /**
         *
         */
        UP, 

        /**
         *
         */
        RIGHT, 

        /**
         *
         */
        DOWN 
    };

    /**
     *
     */
    protected int cell_x;

    /**
     *
     */
    protected int cell_y;
    
    /**
     *
     * @param w world
     * @param s shape
     * @param x x-grid position.
     * @param y y-grid position.
     */
    public StaticLevelObject(World w, Shape s, int x, int y) {
        super(w, s);
        this.setPosition(new Vec2(3.0f*x + Game.GRID_OFFSET_X, 3.0f*y + Game.GRID_OFFSET_Y)); // Sets position acording to map grid.
        this.addCollisionListener(new CollisionHandler(this));
    }
    
}
