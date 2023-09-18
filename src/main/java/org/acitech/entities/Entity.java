package org.acitech.entities;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.awt.*;
import java.util.Vector;

abstract public class Entity {

    protected Vector2D position;
    protected Vector2D velocity;
    protected Vector2D acceleration;

    Entity() {
        this.position = new Vector2D(0, 0);
        this.velocity = new Vector2D(0, 0);
        this.acceleration = new Vector2D(0, 0);
    }

    public void tickEntity(double delta) {
        this.tick(delta);

        // TODO: calculate the difference in position, velocity and acceleration
    }

    abstract protected void tick(double delta);
    abstract public void draw(Graphics2D ctx);

}
