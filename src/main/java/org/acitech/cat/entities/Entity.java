package org.acitech.cat.entities;

import org.acitech.cat.tilemap.Room;
import org.acitech.cat.tilemap.Tile;
import org.acitech.cat.utils.Vector2d;

import java.awt.*;

abstract public class Entity {

    public Vector2d position;
    public Vector2d velocity;
    public Vector2d acceleration;
    protected double friction;
    protected boolean disposed = false;
    private Room room;

    public Entity(Room room) {
        room.addNewEntity(this);

        // Initialize the variables
        this.position = new Vector2d(0, 0);
        this.velocity = new Vector2d(0, 0);
        this.acceleration = new Vector2d(0, 0);
        this.friction = 0.9;
        this.room = room;
    }

    public void tickEntity(double delta) {
        // Execute the tick implementation of the entity
        this.tick(delta);

        // Calculate the updated velocity and new position
        Vector2d rubberBand = this.position.copy();

        this.velocity.add(this.acceleration);
        this.acceleration.zero();
        this.velocity.multiply(this.friction);
        this.position.add(this.velocity.copy().multiply(delta)); // TODO: check if this is actually the place to use delta time (it's not)

        Vector2d expectedTilePos = this.position.copy().divide(Tile.tileSize).floor();
        Tile expectedTile = this.room.getTile((int) expectedTilePos.getX(), (int) expectedTilePos.getY());

        if (expectedTile == null) {
            Vector2d diffPos = rubberBand
                    .copy()
                    .divide(Tile.tileSize)
                    .floor()
                    .subtract(expectedTilePos);

            this.position.set(rubberBand);

            if (diffPos.getX() == 1.0d) {
                this.velocity.setX(0);
                this.position.setX((int) expectedTilePos.getX() * Tile.tileSize + Tile.tileSize + 1);
            } else if (diffPos.getX() == -1.0d) {
                this.velocity.setX(0);
                this.position.setX((int) expectedTilePos.getX() * Tile.tileSize - 1);
            }
            if (diffPos.getY() == 1.0d) {
                this.velocity.setY(0);
                this.position.setY((int) expectedTilePos.getY() * Tile.tileSize + Tile.tileSize + 1);
            } else if (diffPos.getY() == -1.0d) {
                this.velocity.setY(0);
                this.position.setY((int) expectedTilePos.getY() * Tile.tileSize - 1);
            }

            this.position.add(this.velocity.copy().multiply(delta));
        }
    }

    /** Ticks the entity */
    abstract protected void tick(double delta);
    /** Draws the entity */
    abstract public void draw(Graphics2D ctx);

    /** @return The room the entity belongs to */
    public Room getRoom() {
        return this.room;
    }
    public void changeRoom(Room room) {
        this.room.removeEntity(this);
        this.room = room;
        room.addNewEntity(this);
    }

    /** Marks the entity up for disposal */
    public void dispose() {
        this.disposed = true;
    }
    public boolean isDisposed() {
        return disposed;
    }
}
