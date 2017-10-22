/*
 * Copyright (c) 2017 the original author or authors.
*/
package main.gamelevels;

import main.*;
import city.cs.engine.*;
import main.levelelements.*;
import main.gameobjects.*;
import main.levelelements.StaticLevelObject.Facing;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import org.jbox2d.common.Vec2;

/**
 * Used to load in levels, and manage their data.
 * @author James Kayes
 */
public class GameLevel extends World {

    protected Game game;
    protected boolean mouseAttached = false;
    protected ArrayList<Coin> coins = new ArrayList<>();
    protected ArrayList<Asteroid> asteroids = new ArrayList<>();
    protected ArrayList<GameObject> gameObjects = new ArrayList<>();
    protected ArrayList<EnemyShip> enemies = new ArrayList<>();
    protected ArrayList<StaticBlock> staticBlocks = new ArrayList<>();
    protected ArrayList<Edge> edges = new ArrayList<>();
    protected ArrayList<Spikes> spikes = new ArrayList<>();
    protected ArrayList<Diagonal> diagonals = new ArrayList<>();

    private Ship player;
    private Vec2 playerSpawnPos;
    
    private String levelDataFile;
    private int levelNumber;

    /**
     *
     * @param game reference to the game
     * @param levelNumber the number of the level to attempt to load
     */
    public GameLevel(Game game, int levelNumber) {
        super();
        this.game = game;
        this.levelNumber = levelNumber;
        this.levelDataFile = "data/levels/" + levelNumber + ".txt";
        this.setGravity(0.0f);
        this.playerSpawnPos = new Vec2(0.0f, 0.0f);
        
    }
    
    /**
     * Level complete event - attempts to load next level from file
     */
    public void levelComplete() {
        game.goToLevel(new GameLevel(game, this.levelNumber + 1));
    }
    
    /**
     * Populates the world with objects according to the level design.
     */
    public void populate() {
         // Spawn ship:
        this.player = new PlayerShip(this, this.getGame(), this.playerSpawnPos);
        // Levels can be loaded in from files, to allow quick/easy level design:
        try(BufferedReader levelData = new BufferedReader(new FileReader(levelDataFile))){
            
            // Loop through each cell of a grid:
            for(int y = 9; y >= 0; y--) {
                String line = levelData.readLine();
                for(int x = 0; x < 15; x++) {
                    char blockType = line.charAt(x); // Read specific character.
                    this.spawnElement(blockType, x, y); // Spawn element as read from file in the correct location.
                    if(blockType == 'S') { // Player spawns at this position:
                        player.setPosition(this.playerSpawnPos);
                    }
                }
            }
        }
        catch(FileNotFoundException e) {
            System.out.println(e);
            if(this.levelNumber != 1) {
                System.out.println("Attempted to load level 1 again, as the level failed to load");
                this.levelNumber = 0; // Restart from level 1 if unable to load level.
            }
            else {
                System.out.println("Attempted to load level 1, but the file cannot be found.");
                System.out.println("Terminating...");
                System.exit(101); // Level 1 file cannot be found.
            }
        }
        catch(IOException e) {
            System.out.println(e);
        }
        catch(StringIndexOutOfBoundsException e){
            if(this.levelNumber > 1) {
                System.out.println(e);
                System.out.println("Level format incorrect");
                System.out.println("Attempting to load next level...");
                this.levelComplete();
            }
            else {
                System.out.println("First level format incorrect");
                System.out.println("Terminating...");
                System.exit(1);
            }
        } 
    }
    

    /**
     * Spawns a level component of the specified type at position x,y on the grid:
     * @param blockType the type of block to spawn
     * @param x the x-grid position to spawn this
     * @param y the y-grid position to spawn this
     */
    public void spawnElement(char blockType, int x, int y) {
        switch (blockType) {
            case '.':
                break;
            case 'x':
                this.staticBlocks.add(new StaticBlock(this, StaticBlock.BLOCK_SHAPE, x, y));
                break;
            case '^':
                this.edges.add(new Edge(this, StaticBlock.BLOCK_SHAPE, x, y, Facing.UP));
                break;
            case '>':
                this.edges.add(new Edge(this, StaticBlock.BLOCK_SHAPE, x, y, Facing.RIGHT));
                break;
            case '<':
                this.edges.add(new Edge(this, StaticBlock.BLOCK_SHAPE, x, y, Facing.LEFT));
                break;
            case 'v':
                this.edges.add(new Edge(this, StaticBlock.BLOCK_SHAPE, x, y, Facing.DOWN));
                break;
            case 'U':
                this.spikes.add(new Spikes(this, Spikes.SPIKES_SHAPE, x, y, Facing.UP));
                break;
            case 'u':
                this.spikes.add(new Spikes(this, Spikes.SPIKES_SHAPE, x, y, Facing.UP));
                this.coins.add(new Coin(this, Coin.COIN_SHAPE, x, y, new Vec2(0.0f, 1.5f)));
                break;
            case 'L':
                this.spikes.add(new Spikes(this, Spikes.SPIKES_SHAPE, x, y, Facing.LEFT));
                break;
            case 'l':
                this.spikes.add(new Spikes(this, Spikes.SPIKES_SHAPE, x, y, Facing.LEFT));
                this.coins.add(new Coin(this, Coin.COIN_SHAPE, x, y, new Vec2(-1.5f, 0.0f)));
                break;
            case 'R':
                this.spikes.add(new Spikes(this, Spikes.SPIKES_SHAPE, x, y, Facing.RIGHT));
                break;
            case 'r':
                this.spikes.add(new Spikes(this, Spikes.SPIKES_SHAPE, x, y, Facing.RIGHT));
                this.coins.add(new Coin(this, Coin.COIN_SHAPE, x, y, new Vec2(1.5f, 0.0f)));
                break;
            case 'D':
                this.spikes.add(new Spikes(this, Spikes.SPIKES_SHAPE, x, y, Facing.DOWN));
                break;
            case 'd':
                this.spikes.add(new Spikes(this, Spikes.SPIKES_SHAPE, x, y, Facing.DOWN));
                this.coins.add(new Coin(this, Coin.COIN_SHAPE, x, y, new Vec2(0.0f, -1.5f)));
                break;
            case '/':
                this.diagonals.add(new Diagonal(this, Diagonal.DIAGONAL_SHAPE, x, y));
                break;
            case '}': {
                Diagonal element = new Diagonal(this, Diagonal.DIAGONAL_SHAPE, x, y);
                element.rotateDegrees(90.f);
                this.diagonals.add(element);
                break;
            }
            case '{': {
                Diagonal element = new Diagonal(this, Diagonal.DIAGONAL_SHAPE, x, y);
                element.rotateDegrees(180.f);
                this.diagonals.add(element);
                break;
            } 
            case '\\': {
                Diagonal element = new Diagonal(this, Diagonal.DIAGONAL_SHAPE, x, y);
                element.rotateDegrees(270.f);
                this.diagonals.add(element);
                break;
            } 
            case 'C':
                this.coins.add(new Coin(this, Coin.COIN_SHAPE, x, y));
                break;
            case 'S':
                this.playerSpawnPos = new Vec2(3.0f*x - 21.0f, 3.0f*y - 13.5f);
                this.player.setSpawnPos(this.playerSpawnPos);
                break;
            case 'E':
                EnemyShip ship = new EnemyShip(this, new Vec2(3.0f*x - 21.0f, 3.0f*y - 13.5f));
                ship.setTarget(player);
                this.enemies.add(ship);
                break;
            default:
                System.out.println("Unrecognized symbol '" + blockType + "'");
                break;
        }
    }
    
    /**
     * spawns asteroids with a random speed and velocity
     * @param number number of asteroids to attempt to spawn.
     */
    public void spawnAsteroids(int number) {
        for(int asteroid = 0; asteroid < number; asteroid++) {
            float randomX = (float)(40.0*(Math.random()) - 20.0);
            float randomY = (float)(30.0*(Math.random()) - 15.0);
            float randomTorque = (float)(1200.0*Math.random() - 600.0);
            float randomVelX = (float)(8.0*Math.random() - 4.0);
            float randomVelY = (float)(8.0*Math.random() - 4.0);
            
            Asteroid a;
            if(Math.random() < (1.0/3.0)) {
                a = new Asteroid(this, Asteroid.ASTEROID_SHAPE_1);
                
            }
            else if (Math.random() < (2.0/3.0)) {
                a = new Asteroid(this, Asteroid.ASTEROID_SHAPE_2);
            }
            else {
                a = new Asteroid(this, Asteroid.ASTEROID_SHAPE_3);
            }
            a.addCollisionListener(new CollisionHandler(a));
            a.setPosition(new Vec2(randomX, randomY));
            a.applyTorque(randomTorque);
            a.setLinearVelocity(new Vec2(randomVelX, randomVelY));
            gameObjects.add(a);
           
        }
}

    /**
     * Clears all objects in the level.
     */
    public void clear() {
        coins.clear();
        asteroids.clear();
        gameObjects.clear();
        enemies.clear();
        staticBlocks.clear();
        edges.clear();
        spikes.clear();
        diagonals.clear();
    }

    /**
     * Checks if the passed in object is still on screen, and moves it to the opposite side if it's not.
     * @param object object to check
     */
    public void checkBounds(Collidable object) {
        if (object instanceof Ship) {
            Ship ship = (Ship)object;
            if(ship.getPosition().x < Game.topLeft.x) { // If moves off left side:
                ship.setPosition(new Vec2(Game.bottomRight.x, ship.getPosition().y));
            }
            if(ship.getPosition().x > Game.bottomRight.x) { // If moves off right side:
                ship.setPosition(new Vec2(Game.topLeft.x, ship.getPosition().y));
            }
            if(ship.getPosition().y < Game.bottomRight.y) { // If moves off bottom side
                ship.setPosition(new Vec2(ship.getPosition().x, Game.topLeft.y));
            }
            if(ship.getPosition().y > Game.topLeft.y) { // If moves off top side
                ship.setPosition(new Vec2(ship.getPosition().x, Game.bottomRight.y));
            }
        }
        else if (object instanceof Shot) {
            GameObject obj = (GameObject)object;
            if(obj.getPosition().x < Game.topLeft.x) { // If moves off left side:
                obj.destroy();
            }
            if(obj.getPosition().x > Game.bottomRight.x) { // If moves off right side:
                obj.destroy();
            }
            if(obj.getPosition().y < Game.bottomRight.y) { // If moves off bottom side
                obj.destroy();
            }
            if(obj.getPosition().y > Game.topLeft.y) { // If moves off top side
                obj.destroy();
            }
        } 
        else { // Should be GameObject:
            GameObject obj = (GameObject)object;
            if(obj.getPosition().x < Game.topLeft.x) { // If moves off left side:
                obj.setPosition(new Vec2(Game.bottomRight.x, obj.getPosition().y));
            }
            if(obj.getPosition().x > Game.bottomRight.x) { // If moves off right side:
                obj.setPosition(new Vec2(Game.topLeft.x, obj.getPosition().y));
            }
            if(obj.getPosition().y < Game.bottomRight.y) { // If moves off bottom side
                obj.setPosition(new Vec2(obj.getPosition().x, Game.topLeft.y));
            }
            if(obj.getPosition().y > Game.topLeft.y) { // If moves off top side
                obj.setPosition(new Vec2(obj.getPosition().x, Game.bottomRight.y));
            }
        }
          
        
    }
    
    /**
     *  Adds the passed in object to the level.
     * @param object object to add to the level
     */
    public void addGameObject(GameObject object) {
        this.gameObjects.add(object);
    }
    
    /**
     * Gets the player ship.
     * @return playership
     */
    public Ship getPlayer() {
        return player;
    }
    
    /**
     *
     * @return reference to the game
     */
    public Game getGame() {
        return game;
    }

    /**
     * Main game world update function.
     * @param deltaTime the time between updates
     */
    public void update(float deltaTime) {
        
        // This only needs to be done on the first update:
        if(!mouseAttached) {
            game.getView().addMouseListener(new MouseHandler(game.getView(), (PlayerShip)player));
            mouseAttached = true;
            game.getView().updateHealthBar();
        }
        
        player.update(deltaTime);
        checkBounds(player);
         
        // Loop through and update enemies:
        for(int i = 0; i < enemies.size(); i++) {
            EnemyShip enemy = enemies.get(i);
            if(enemy.isDestoryed()) {
                enemy.destroy();
                enemies.remove(i);
                i--;
            }
            else {
                enemy.update(deltaTime);
                checkBounds(enemy);
            }
        }
        // Loop through and update all game objects:
        for(GameObject object: gameObjects) {
            object.update(deltaTime);
            checkBounds(object);
        }
        for(GameObject object: asteroids) {
            object.update(deltaTime);
            checkBounds(object);
        }
        for(int i = 0; i < coins.size(); i++) {
            Coin coin = coins.get(i);
            if(coin.hasBeenCollected()) {
                coins.remove(i);
                i--;
            }
            else {
                coin.update(deltaTime);
                checkBounds(coin);
            }
        }
        if(coins.isEmpty() && enemies.isEmpty()) {
            this.levelComplete();
        }
    }
    
}
