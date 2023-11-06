package org.acitech.entities;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.awt.*;

abstract public class Entity {

    public Vector2D position;
    public Vector2D velocity;
    public Vector2D acceleration;
    protected double friction;
    protected boolean disposed = false;

    protected Entity() {
        // Initialize the variables
        this.position = new Vector2D(0, 0);
        this.velocity = new Vector2D(0, 0);
        this.acceleration = new Vector2D(0, 0);
        this.friction = 0.9;
    }

    public void tickEntity(double delta) {
        // Execute the tick implementation of the entity
        this.tick(delta);

        // Calculate the updated velocity and new position
        this.velocity = this.velocity.add(this.acceleration);
        this.acceleration = new Vector2D(0, 0);
        this.velocity = this.velocity.scalarMultiply(this.friction);
        this.position = this.position.add(this.velocity.scalarMultiply(delta)); // TODO: check if this is actually the place to use delta time (it's not)
    }

    abstract protected void tick(double delta);
    abstract public void draw(Graphics2D ctx);

    public void dispose() {
        this.disposed = true;
    }
    public boolean isDisposed() {
        return disposed;
    }
}
