package org.acitech.cat.entities.ai;

import org.acitech.cat.GamePanel;
import org.acitech.cat.entities.Enemy;
import org.acitech.cat.entities.Entity;
import org.acitech.cat.utils.Vector2d;

public class Fighter implements AI {
    private final Enemy enemy;

    public Fighter(Enemy enemy) {
        this.enemy = enemy;
    }

    // Defines basic AI for enemies like Rico and Pepto
    @Override
    public void execute(double delta) {
        Vector2d playerPos = GamePanel.getPlayer().position;

        // Gets the angle between the player and the enemy
        Vector2d direction = playerPos.directionTo(this.enemy.position).multiply(0.5);

        // If the enemy is close enough to the player, start Fighter AI
        if (this.enemy.position.distance(playerPos) < this.enemy.aggroDistance) {
            this.enemy.acceleration.set(direction.getX(), direction.getY());
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

    }
}