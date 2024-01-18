package org.acitech.utils;

public class Vector2d {
    private double x;
    private double y;

    public Vector2d() {
        this.x = 0;
        this.y = 0;
    }
    public Vector2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }

    public Vector2d set(double x, double y) {
        this.x = x;
        this.y = y;
        return this;
    }
    public Vector2d setX(double x) {
        this.x = x;
        return this;
    }
    public Vector2d setY(double y) {
        this.y = y;
        return this;
    }

    public Vector2d add(Vector2d vector) {
        return this.add(vector.getX(), vector.getY());
    }
    public Vector2d add(double scalar) {
        return this.add(scalar, scalar);
    }
    public Vector2d add(double x, double y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public Vector2d subtract(Vector2d vector) {
        return this.subtract(vector.getX(), vector.getY());
    }
    public Vector2d subtract(double scalar) {
        return this.subtract(scalar, scalar);
    }
    public Vector2d subtract(double x, double y) {
        this.x -= x;
        this.y -= y;
        return this;
    }

    public Vector2d multiply(Vector2d vector) {
        return this.multiply(vector.getX(), vector.getY());
    }
    public Vector2d multiply(double scalar) {
        return this.multiply(scalar, scalar);
    }
    public Vector2d multiply(double x, double y) {
        this.x *= x;
        this.y *= y;
        return this;
    }

    public Vector2d divide(Vector2d vector) {
        return this.divide(vector.getX(), vector.getY());
    }
    public Vector2d divide(double scalar) {
        return this.divide(scalar, scalar);
    }
    public Vector2d divide(double x, double y) {
        this.x /= x;
        this.y /= y;
        return this;
    }

    /** @return The distance to another vector */
    public double distance(Vector2d vector) {
        return Math.sqrt(this.distanceSquared(vector));
    }
    /** @return The squared distance to another vector */
    public double distanceSquared(Vector2d vector) {
        return Math.pow(vector.getX() - this.x, 2) + Math.pow(vector.getY() - this.y, 2);
    }

    /** @return The vector's magnitude */
    public double length() {
        return Math.sqrt(this.lengthSquared());
    }
    /** @return The vector's magnitude squared */
    public double lengthSquared() {
        return this.x * this.x + this.y * this.y;
    }
    public Vector2d setLength(double magnitude) {
        return this.normalize().multiply(magnitude, magnitude);
    }

    public Vector2d pow(double exponent) {
        this.x = Math.pow(this.x, exponent);
        this.y = Math.pow(this.y, exponent);
        return this;
    }
    public Vector2d ceil() {
        this.x = Math.ceil(this.x);
        this.y = Math.ceil(this.y);
        return this;
    }
    public Vector2d floor() {
        this.x = Math.floor(this.x);
        this.y = Math.floor(this.y);
        return this;
    }
    public Vector2d round() {
        this.x = Math.round(this.x);
        this.y = Math.round(this.y);
        return this;
    }
    public Vector2d abs() {
        this.x = Math.abs(this.x);
        this.y = Math.abs(this.y);
        return this;
    }

    public Vector2d negate() {
        this.x = -this.x;
        this.y = -this.y;
        return this;
    }
    public Vector2d zero() {
        return this.set(0, 0);
    }
    public Vector2d normalize() {
        double mag = this.length();
        return this.divide(mag, mag);
    }
    public double dot(Vector2d vector) {
        return this.x * vector.getX() + this.y * vector.getY();
    }
    public double cross(Vector2d vector) {
        return this.x * vector.getY() - this.y * vector.getX();
    }
    public Vector2d limit(double max) {
        double magSq = this.lengthSquared();
        if (magSq > max * max) {
            this.divide(Math.sqrt(magSq)).multiply(max);
        }
        return this;
    }
    public Vector2d copy() {
        return new Vector2d(this.x, this.y);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Vector2d)) return false;
        Vector2d vector = (Vector2d) object;
        return this.x == vector.getX() && this.y == vector.getY();
    }
    @Override
    public String toString() {
        return "<" + x + "," + y + ">";
    }
}