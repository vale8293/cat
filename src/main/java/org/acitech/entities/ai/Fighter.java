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
        double angle = Math.atan2(playerPos.getY() - this.enemy.position.getY(), playerPos.getX() - this.enemy.position.getX());
        double x = Math.cos(angle) * 0.5;
        double y = Math.sin(angle) * 0.5;

        // Declares AI Specific variables
        boolean gotScratched = scratchCheck(x, y);
        boolean gotHitBullet = bulletCheck(x, y);

        // If the enemy is close enough to the player, start Fighter AI
        if (this.enemy.position.distance(playerPos) < this.enemy.aggroDistance) {
            this.enemy.acceleration.set(x, y);
            this.enemy.acceleration.multiply(this.enemy.moveSpeed);

            // If the enemy makes contact with the player
            if (this.enemy.position.distance(playerPos) < Math.max(this.enemy.width / 2, this.enemy.height / 2)) {
                // Deal damage w/ elemental effect (none by default)
                if (GamePanel.player.damageTimer == 0) {
                    GamePanel.player.damageTaken(this.enemy.damage, this.enemy.damageElement);
                }

                // Knock back the enemy and player
                this.enemy.velocity.set(this.enemy.kbMult * -x, this.enemy.kbMult * -y);
                GamePanel.player.velocity = this.enemy.velocity.copy().multiply((double) -GamePanel.player.kbMult / this.enemy.kbMult);
            }
        }
    }
}
