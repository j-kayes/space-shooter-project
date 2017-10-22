
package main.gameobjects;

/**
 * Classes implementing this must provide a collision response.
 * @author James Kayes
 */
public interface Collidable {

    /**
     *
     * @param other other game object involved in the collision
     */
    public void collisionResponse(Collidable other);
}
