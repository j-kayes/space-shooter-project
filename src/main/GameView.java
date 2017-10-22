/*
 * Copyright (c) 2017 the original author or authors.
 */
package main;

import main.gamelevels.GameLevel;
import city.cs.engine.UserView;
import city.cs.engine.World;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;
import main.gameobjects.PlayerShip;

/**
 * Represents the view of the world - controls the UI etc.
 * @author James Kayes
 */
public class GameView extends UserView {
    /**
     * Background image.
     */
    private static Image background;
    /**
    * Health box image.
    */
    private static Image healthBox;
    private static Image healthBar;
    private static Image livesIcon;
    
    private static GameLevel level;
    private static PlayerShip player;
    
    private static float healthWidth;
    
    /**
    * Health bar width.
    */
    private static final float INITIAL_HEALTH_BAR_WIDTH = 123;
    
    /**
     * Constructor.
     * @param world world
     * @param width width
     * @param height height
     */
    public GameView(World world, int width, int height) {
        super(world, width, height);
        
        level = (GameLevel)world;
        background = new ImageIcon("data/bg.jpg").getImage();
        healthBox = new ImageIcon("data/ui_health.png").getImage();
        healthBar = new ImageIcon("data/ui_bar.png").getImage();
        livesIcon = new ImageIcon("data/ui_life.png").getImage();
    }
    
    /**
     * Called every frame and updates the background.
     * @param g graphics context
     */
    @Override
    protected void paintBackground(Graphics2D g) {
        g.drawImage(background, 0, 0, this);
    }

    /**
     * Called every frame and updates the background.
     * @param g graphics context
     */
    @Override
    protected void paintForeground(Graphics2D g) {
        player = (PlayerShip)level.getPlayer();
        // HUD:
        g.drawImage(healthBar, 118, 55, this);
        g.drawImage(healthBox, 50, 40, this);
        
        // For each life that the player has:
        for(int i = 0; i < player.getLives(); i++) {
            // Draw a HUD icon to represent it:
            g.drawImage(livesIcon, (120 + 50*i), 100, 50, 50, this);
        }
        
        g.setColor(Color.red);
        g.setFont(g.getFont().deriveFont(20.0f));
        g.drawString("LIVES: ", 50, 150);
        g.setColor(Color.yellow);
        g.drawString("COINS: " + player.getCoins(), 50, 200);
    }
    
    /**
     * Sets the level variable for the view
     * @param level to update the reference
     */
    public void setLevel(GameLevel level) {
        this.level = level;
    }
    
    /**
     * Updates the healthbar size according to the players health value
     */
    public void updateHealthBar() {
        try {
            float healthPercentage = (level.getPlayer().getHealth())/100.0f;
            healthWidth = (healthPercentage)*(INITIAL_HEALTH_BAR_WIDTH);
            Image resizedHealthBar = healthBar.getScaledInstance((int)healthWidth, healthBar.getHeight(this), 0);
            ImageIcon updatedHealthBar = new ImageIcon(resizedHealthBar);
            healthBar = updatedHealthBar.getImage();
        }
        catch(java.lang.IllegalArgumentException e) {
            
        }
        
    }
}
