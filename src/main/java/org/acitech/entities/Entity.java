package org.acitech.entities;

import org.acitech.utils.Vector2d;

import java.awt.*;

abstract public class Entity {

    public Vector2d position;
    public Vector2d velocity;
    public Vector2d acceleration;
    protected double friction;
    protected boolean disposed = false;

    public Entity() {
        // Initialize the variables
        this.position = new Vector2d(0, 0);
        this.velocity = new Vector2d(0, 0);
        this.acceleration = new Vector2d(0, 0);
        this.friction = 0.9;
    }

    public void tickEntity(double delta) {
        // Execute the tick implementation of the entity
        this.tick(delta);

        // Calculate the updated velocity and new position
        this.velocity = this.velocity.add(this.acceleration);
        this.acceleration = new Vector2d(0, 0);
        this.velocity = this.velocity.multiply(this.friction);
        this.position = this.position.add(this.velocity.multiply(delta)); // TODO: check if this is actually the place to use delta time (it's not)
    }

    abstract protected void tick(double delta);
    abstract public void draw(Graphics2D ctx);

    /**
     * Marks the entity up for disposal
     */
    public void dispose() {
        this.disposed = true;
    }
    public boolean isDisposed() {
        return disposed;
    }
}
