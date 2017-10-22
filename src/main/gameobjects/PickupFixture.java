/*
 * Copyright (c) 2017 the original author or authors.
 */
package main.gameobjects;

import city.cs.engine.*;

/**
 * Fixture for pickups.
 * @author James Kayes.
 */
public class PickupFixture extends Sensor {

    /**
     *
     */
    protected SolidFixture shotFixture;
    
    /**
     * 
     * @param body body
     * @param shape shape
     */
    public PickupFixture(Body body, Shape shape) {
        super(body, shape);
        
    }
    
}
