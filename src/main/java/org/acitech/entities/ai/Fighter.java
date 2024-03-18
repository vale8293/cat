package org.acitech.entities.ai;

import org.acitech.GamePanel;
import org.acitech.entities.Enemy;
import org.acitech.utils.Vector2d;

public class Fighter extends EnemyAI {
    public Fighter(Enemy enemy) {
        super(enemy);
    }

    // Defines basic AI for enemies like Rico and Pepto
    @Override
    public void execute(double delta) {
        Vector2d playerPos = GamePanel.player.position;

        // Gets the angle between the player and the enemy
        Vector2d direction = playerPos.directionTo(this.enemy.position).multiply(0.5);

        scratchCheck(direction.getX(), direction.getY());
        bulletCheck(direction.getX(), direction.getY());

        // If the enemy is close enough to the player, start Fighter AI
        if (this.enemy.position.distance(playerPos) < this.enemy.aggroDistance) {
            this.enemy.acceleration.set(direction.getX(), direction.getY());
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