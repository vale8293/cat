package org.acitech.entities.ai;

import org.acitech.GamePanel;
import org.acitech.entities.Enemy;
import org.acitech.entities.Entity;
import org.acitech.entities.projectiles.Feather;
import org.acitech.utils.Vector2d;

import java.util.Random;

public class Hawk implements AI {
    private final Enemy enemy;
    private int cycle = 0;
    private boolean aniWait = false;
    private int aniCount;
    boolean aggression = true;
    private String state = "idle";

    public Hawk(Enemy enemy) {
        this.enemy = enemy;
    }

    // Defines the Hawk Boss's AI
    @Override
    public void execute(double delta) {

        Vector2d playerPos = GamePanel.getPlayer().position;

        switch (this.cycle) {
            case 0 -> {
                if (this.enemy.position.distance(playerPos) < this.enemy.aggroDistance) {
                    this.aggression = true;
                    this.cycle++;
                }
            }

            case 1 -> {
                if (!this.aniWait) {
                    this.state = "feather";
                    this.enemy.resetTick();
                    this.aniWait = true;
                }

                if (this.enemy.getAnimationTick() == this.enemy.aniLength) {
                    // Spawn a feather projectile
                    new Feather(this.enemy.getRoom(), this.enemy.position.getX(), this.enemy.position.getY(), this.enemy.position.angleTo(playerPos), "enemy");
                    this.aniCount++;
                }

                if (this.aniCount >= 10) {
                    this.cycle++;
                    this.aniCount = 0;
                    this.aniWait = false;
                }
            }

            case 2 -> {
                if (!this.aniWait) {
                    this.state = "run";
                    this.enemy.resetTick();
                    this.aniWait = true;
                }

                this.contact(playerPos, this.chase(playerPos));

                if (this.enemy.getAnimationTick() == this.enemy.aniLength) {
                    this.aniCount++;
                }

                if (this.aniCount == 8) {
                    this.cycle++;
                    this.aniCount = 0;
                    this.aniWait = false;
                }
            }

            case 3 -> {
                if (!this.aniWait) {
                    this.state = "takeoff";
                    this.enemy.resetTick();
                    this.aniWait = true;
                }

                if (this.enemy.getAnimationTick() == this.enemy.aniLength) {
                    this.aniCount++;
                }

                if (this.aniCount >= 1) {
                    this.aniWait = false;
                    this.aniCount = 0;
                    this.cycle++;
                }
            }

            case 4 -> {
                if (!this.aniWait) {
                    this.state = "takeoffSmear";
                    this.enemy.resetTick();
                    this.aniWait = true;
                }

                if (this.enemy.getAnimationTick() == 5 * this.enemy.aniFrameDuration) {
                    this.aniCount++;
                }

                if (this.aniCount >= 1) {
                    for (int i = 0; i < 8; i++) {
                        new Feather(this.enemy.getRoom(), this.enemy.position.getX(), this.enemy.position.getY(), (Math.PI * i / 4), "enemy");
                    }

                    this.aniWait = false;
                    this.aniCount = 0;
                    this.cycle++;
                }
            }

            case 5 -> {
                if (!this.aniWait) {
                    this.enemy.damageTimer = 1200;
                    this.state = "shadowUp";
                    this.enemy.moveSpeed = 0.6;
                    this.enemy.resetTick();
                    this.aniWait = true;
                }

                this.chase(playerPos);

                if (this.enemy.getAnimationTick() == this.enemy.aniLength) {
                    this.aniCount++;
                }

                if (this.aniCount >= 5) {
                    this.enemy.damageTimer = 0;
                    this.aniWait = false;
                    this.aniCount = 0;
                    this.cycle++;
                }
            }

            case 6 -> {
                if (!this.aniWait) {
                    this.enemy.damageTimer = 1200;
                    this.state = "shadowDown";
                    this.enemy.moveSpeed = 1.5;
                    this.enemy.resetTick();
                    this.aniWait = true;
                }

                this.chase(playerPos);

                if (this.enemy.getAnimationTick() == this.enemy.aniLength) {
                    this.aniCount++;
                }

                if (this.aniCount >= 3) {
                    this.enemy.moveSpeed = 0;
                }

                if (this.aniCount >= 5) {
                    this.enemy.damageTimer = 0;
                    this.aniWait = false;
                    this.aniCount = 0;
                    this.cycle++;
                }
            }

            case 7 -> {
                if (!this.aniWait) {
                    this.state = "landingSmear";
                    this.enemy.moveSpeed = 1.1;
                    this.enemy.resetTick();
                    this.aniWait = true;
                }

                if (this.enemy.getAnimationTick() == 2 * this.enemy.aniFrameDuration) {
                    this.aniCount++;
                }

                if (this.aniCount >= 1) {
                    for (int i = 0; i < 16; i++) {
                        new Feather(this.enemy.getRoom(), this.enemy.position.getX(), this.enemy.position.getY(), (Math.PI * i / 8), "enemy");
                    }

                    this.aniWait = false;
                    this.aniCount = 0;
                    this.cycle++;
                }
            }

            case 8 -> {
                if (!this.aniWait) {
                    this.state = "landing";
                    this.enemy.resetTick();
                    this.aniWait = true;
                }

                if (this.enemy.getAnimationTick() == this.enemy.aniLength) {
                    this.aniWait = false;
                    this.cycle = 1;
                }
            }
        }
    }

    public Vector2d chase(Vector2d playerPos) {
        // Gets the angle between the player and the enemy
        Vector2d direction = playerPos.directionTo(this.enemy.position).multiply(0.5);

        this.enemy.acceleration.set(direction);
        this.enemy.acceleration.multiply(this.enemy.moveSpeed);
        return direction;
    }

    public void contact(Vector2d playerPos, Vector2d direction) {
        // If the enemy makes contact with the player
        if (this.enemy.position.distance(playerPos) < Math.max(this.enemy.width / 2, this.enemy.height / 2)) {
            // Deal damage w/ elemental effect (none by default)
            if (GamePanel.getPlayer().damageTimer == 0) {
                GamePanel.getPlayer().damageTaken(this.enemy.attackDamage, this.enemy.damageElement);
            }

            // Knock back the enemy and player
            this.enemy.velocity.set(this.enemy.kbMult * -direction.getX(), this.enemy.kbMult * -direction.getY());
            GamePanel.getPlayer().velocity = this.enemy.velocity.copy().multiply((double) -GamePanel.getPlayer().kbMult / this.enemy.kbMult);
        }
    }

    @Override
    public void damageHandler(Entity damager) {}

    public int getStateNum() {
        return switch (this.state) {
            case "idle" -> 0;
            case "feather" -> 1;
            case "run" -> 2;
            case "takeoff" -> 3;
            case "takeoffSmear" -> 4;
            case "shadowUp" -> 5;
            case "shadowDown" -> 6;
            case "landingSmear" -> 7;
            case "landing" -> 8;
            default -> 2;
        };
    }

    public int getCycle() {
        return this.cycle;
    }

}