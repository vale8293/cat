package org.acitech.entities;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.awt.*;
import java.util.Vector;

abstract public class Entity {

    protected Vector2D position;
    protected Vector2D velocity;
    protected Vector2D acceleration;
    protected double friction;

    Entity() {
        this.position = new Vector2D(0, 0);
        this.velocity = new Vector2D(0, 0);
        this.acceleration = new Vector2D(0, 0);
        this.friction = 1;
    }

    public void tickEntity(double delta) {
        this.tick(delta);

        this.velocity = this.velocity.add(this.acceleration);
        this.acceleration = new Vector2D(0, 0);
        this.velocity = this.velocity.scalarMultiply(this.friction);
        this.position = this.position.add(this.velocity.scalarMultiply(delta)); // TODO: check if this is actually the place to use delta time
    }

    abstract protected void tick(double delta);
    abstract public void draw(Graphics2D ctx);

}
