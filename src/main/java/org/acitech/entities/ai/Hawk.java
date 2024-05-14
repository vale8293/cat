package org.acitech.entities.ai;

import org.acitech.GamePanel;
import org.acitech.entities.Enemy;
import org.acitech.entities.Entity;
import org.acitech.entities.projectiles.Feather;
import org.acitech.utils.Vector2d;

import java.util.Random;

public class Hawk implements AI {
    private final Enemy enemy;
    Random randomCycle = new Random();
    private int cycle = 0;
    private boolean aniWait = false;
    private int aniCount;
    private int turnTimer;
    boolean aggression = true;
    private String state = "idle";
    private int stateNum;

    public Hawk(Enemy enemy) {
        this.enemy = enemy;
    }

    // Defines the Hawk Boss's AI
    @Override
    public void execute(double delta) {
        if (this.turnTimer > 0) {
            this.turnTimer--;
        } else {
            this.turnTimer = 30;
        }

        switch (this.state) {
            case "run" -> this.stateNum = 0;
            case "takeoff" -> this.stateNum = 1;
            case "idle" -> this.stateNum = 2;
            case "feather" -> this.stateNum = 3;
            case "shadow" -> this.stateNum = 4;
            case "landing" -> this.stateNum = 5;
//            default -> this.stateNum = 0;
        }

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
                    this.state = "takeoff";
                    this.enemy.resetTick();
                    this.aniWait = true;
                }

                if (this.enemy.getAnimationTick() == this.enemy.aniLength) {
                    this.aniWait = false;
                    this.aniCount = 0;
                    this.cycle++;
                }
            }

            case 2 -> {
                if (!this.aniWait) {
                    this.state = "feather";
                    this.enemy.resetTick();
                    this.aniWait = true;
                }

                if (this.enemy.getAnimationTick() == this.enemy.aniLength) {
                    this.aniCount++;

                    // Spawn a feather projectile
                    new Feather(this.enemy.getRoom(), this.enemy.position.getX(), this.enemy.position.getY(), this.enemy.position.angleTo(playerPos), "enemy");
                }

                if (this.aniCount == 10) {
                    this.cycle++;
                    this.aniCount = 0;
                    this.aniWait = false;
                }
            }

            case 3 -> {
                if (!this.aniWait) {
                    this.state = "landing";
                    this.enemy.resetTick();
                    this.aniWait = true;
                }

                if (this.aggression) {
                    this.aniWait = false;
                    this.cycle++;
                }
            }

            case 4 -> {
                if (!this.aniWait) {
                    this.state = "run";
                    this.enemy.resetTick();
                    this.aniWait = true;
                }

                // Gets the angle between the player and the enemy
                Vector2d direction = playerPos.directionTo(this.enemy.position).multiply(0.5);

                this.enemy.acceleration.set(direction);
                this.enemy.acceleration.multiply(this.enemy.moveSpeed);

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

                if (this.enemy.getAnimationTick() == this.enemy.aniLength) {
                    this.aniCount++;
                }

                if (this.aniCount == 8) {
                    this.cycle = 1;
                    this.aniWait = false;
                }
            }
        }
    }

    @Override
    public void damageHandler(Entity damager) {

    }


}