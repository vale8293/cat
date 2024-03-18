package org.acitech.entities.ai;

import org.acitech.GamePanel;
import org.acitech.entities.Enemy;
import org.acitech.utils.Vector2d;

public class Skitter extends EnemyAI {
    private int fleeTimer = 0;

    public Skitter(Enemy enemy) {
        super(enemy);
    }

    // Defines basic AI for enemies like Jordan
    @Override
    public void execute(double delta) {
        Vector2d playerPos = GamePanel.player.position;
        if (fleeTimer > 0) {
            fleeTimer--;
        }

        // Gets the angle between the player and the enemy
        Vector2d direction = playerPos.directionTo(this.enemy.position).multiply(0.5);

        // Checks for scratch & projectile to determine AI state
        boolean gotScratched = scratchCheck(direction.getX(), direction.getY());
        boolean gotHitBullet = bulletCheck(direction.getX(), direction.getY());
        if (gotScratched) {
            fleeTimer = 90;
        } else if (gotHitBullet) {
            fleeTimer = 120;
        }

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
                if (GamePanel.player.damageTimer == 0) {
                    GamePanel.player.damageTaken(this.enemy.damage, this.enemy.damageElement);
                }

                // Knock back the enemy and player
                this.enemy.velocity.set(this.enemy.kbMult * -direction.getX(), this.enemy.kbMult * -direction.getY());
                GamePanel.player.velocity = this.enemy.velocity.copy().multiply((double) -GamePanel.player.kbMult / this.enemy.kbMult);
            }
        }
    }
}