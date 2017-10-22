/*
 * Copyright (c) 2017 the original author or authors.
 */
package main;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import main.gameobjects.PlayerShip;

/**
 * Controls mouse input.
 * @author James Kayes
 */
public class MouseHandler extends MouseAdapter {

    private WorldView view;
    private PlayerShip player;
    private Vec2 target;
    private double angleToTarget;
    
    /**
     * 
     * @param view view
     * @param player player
     */
    public MouseHandler(WorldView view, PlayerShip player) {
        this.view = view;
        this.player = player;

    }

    @Override
    public void mousePressed(MouseEvent e) {
        player.shoot();
        
    }
}
