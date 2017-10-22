/*
 * Copyright (c) 2017 the original author or authors.
 */
package main;

import main.gamelevels.GameLevel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.geom.Point2D;
import javax.swing.JFrame;
import org.jbox2d.common.Vec2;

/**
 * The main game class.
 * @author James Kayes
 */
public class Game {
    
    /**
     * Width of the game in pixels.
     */
    public static final int WIDTH = 900;
    /**
     * Height of the game in pixels
     */
    public static final int HEIGHT = 600;
    
    /**
     * This is the value added from the x-position in order to correctly position new objects in the game.
     */
    public static final float GRID_OFFSET_X = -21.0f;

    /**
     * This is the value subtracted from the y-position in order to correctly position objects in the game.
     */
    public static final float GRID_OFFSET_Y = -13.5f;
    
    /**
     * World position of the bottom-right corner of screen.
     */
    public static Vec2 bottomRight;

    /**
     * World position of top-left corner of screen.
     */
    public static Vec2 topLeft;
    
    private static GameLevel currentLevel;
    private static GameView view;
    private static JFrame frame;
    private static GameOverPanel gameOver;
    
    private KeyHandler keyHandler;
    private StepHandler stepHandler;

    /**
     * Constructor - starts the game.
     */
    public Game() {    
        this.start();
    }
    
    /**
     * Sets the game up by loading the first level, populating it, and then initializing variables.
     */
    public void start() {
        currentLevel = new GameLevel(this, 1);
        currentLevel.populate();
        
        view = new GameView(currentLevel, Game.WIDTH, Game.HEIGHT);
        view.setBackground(Color.BLACK);
        
        bottomRight = view.viewToWorld(new Point2D.Float(((float)Game.WIDTH)/2.0f, ((float)Game.HEIGHT)/2.0f));
        topLeft = new Vec2(-bottomRight.x, -bottomRight.y);
                
        // For input and event handeling:
        keyHandler = new KeyHandler(view, this);
        stepHandler = new StepHandler(this.keyHandler, this);
        currentLevel.addStepListener(this.stepHandler);
        
        // uncomment this to draw a 1-metre grid over the view
        //view.setGridResolution(1);

        // add some mouse actions
        // add this to the view, so coordinates are relative to the view
        // view.addMouseListener(new MouseHandler(view));

        // display the view in a frame
        frame = new JFrame("Space Shooter");
        frame.addKeyListener(keyHandler);

        // quit the application when the game window is closed
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationByPlatform(true);
        // display the world in the window
        frame.add(view);
        
        // don't let the game window be resized
        frame.setResizable(false);
        
        // make the window visible
        frame.setVisible(true);
        
        // size the game window to fit the world view
        frame.pack();
        
        gameOver = new GameOverPanel(this);

        currentLevel.start();
        frame.requestFocus();
    }
    
    /**
     * Restarts the game.
     */
    public void restart() {
        frame.setVisible(false);
        frame.dispose();
        this.start();
    }
    
    /**
     * Game over event - opens new window.
     */
    public void gameOver() {
        gameOver = new GameOverPanel(this);
        frame.remove(view);
        frame.add(gameOver, BorderLayout.CENTER);
        frame.setVisible(true);
        frame.pack();
    }
    
    /**
     *
     * @return current level
     */
    public GameLevel getCurrentWorld() {
        return currentLevel;
    }
    
    /**
     *
     * @return gameview
     */
    public GameView getView() {
        return this.view;
    }
    
    /**
     *
     * @param level level
     */
    public void goToLevel(GameLevel level) {
        currentLevel.stop();
        
        currentLevel = level;
        currentLevel.populate();
        
        view.setWorld(currentLevel);
        
        // Update input and event handeling:
        keyHandler = new KeyHandler(view, this);
        stepHandler = new StepHandler(this.keyHandler, this);
        currentLevel.addStepListener(this.stepHandler);
        frame.addKeyListener(keyHandler);
        
        view.setLevel(level);
        currentLevel.start();
        frame.requestFocus();
    }
    
    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        new Game();
    }
}
