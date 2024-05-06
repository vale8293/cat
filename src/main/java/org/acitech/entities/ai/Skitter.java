package org.acitech.entities.ai;

import org.acitech.GamePanel;
import org.acitech.entities.Enemy;
import org.acitech.entities.Entity;
import org.acitech.entities.Scratch;
import org.acitech.utils.Vector2d;

public class Skitter implements AI {
    private final Enemy enemy;
    private int fleeTimer = 0;

    public Skitter(Enemy enemy) {
        this.enemy = enemy;
    }

    // Defines basic AI for enemies like Jordan
    @Override
    public void execute(double delta) {
        Vector2d playerPos = GamePanel.getPlayer().position;
        if (fleeTimer > 0) {
            fleeTimer--;
        }

        // Gets the angle between the player and the enemy
        Vector2d direction = playerPos.directionTo(this.enemy.position).multiply(0.5);

        // If the enemy is close enough to the player, start Skitter AI
        if (this.enemy.position.distance(playerPos) < this.enemy.aggroDistance) {

            // Determines whether the enemy should be running towards or away from the player
            if (fleeTimer == 0) {
                this.enemy.acceleration.set(direction.getX(), direction.getY());
            }
            else {
                this.enemy.acceleration.set(-direction.getX() * 1.2, -direction.getY() * 1.2);
            }
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
        }
    }

    @Override
    public void damageHandler(Entity damager) {
        if (damager == null) return;

        if (damager instanceof Scratch) {
            fleeTimer = 90;
        } else {
            fleeTimer = 120;
        }
    }
}