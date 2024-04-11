package org.acitech.entities.ai;

import org.acitech.GamePanel;
import org.acitech.entities.Enemy;
import org.acitech.entities.Entity;
import org.acitech.entities.Projectile;
import org.acitech.entities.Scratch;

abstract public class EnemyAI {
    final Enemy enemy;

    public EnemyAI(Enemy enemy) {
        this.enemy = enemy;
    }

    public abstract void execute(double delta);

    // Defines basic AI for when the player scratches an enemy
    protected boolean scratchCheck(double x, double y) {
        boolean gotScratched = false;

        // Looks for any instances of a scratch
        for (Entity entity : GamePanel.entities) {
            if (!(entity instanceof Scratch scratch)) continue;

            // Gets the position of the scratch
            double dist = scratch.position.distance(this.enemy.position);

            // If the scratch makes contact with the enemy
            // regain 1 mana
            // knock it back, lose 1hp, and start i-frames, extend streak
            if (dist < 100) {
                gotScratched = true;

                if (this.enemy.damageTimer == 0) {
                    if (GamePanel.player.mana < GamePanel.player.maxMana) {
                        GamePanel.player.mana += 1;
                    }
                    this.enemy.velocity.set(this.enemy.kbMult * -x, this.enemy.kbMult * -y);
                    this.enemy.dealDamage(GamePanel.player.scratchDamage - this.enemy.defense, this.enemy.damageElement);
                    this.enemy.damageTimer = this.enemy.immunity;
                    GamePanel.player.streakTimer = GamePanel.player.streakTimerMax;
                }
            }
        }

        return gotScratched;
    }

    protected boolean bulletCheck(double x, double y) {
        boolean gotHitBullet = false;

        // Looks for any instances of a bullet-type projectile
        for (Entity entity : GamePanel.entities) {
            if (!(entity instanceof Projectile projectile)) continue;

            // Gets the position of the projectile
            double dist = projectile.position.distance(this.enemy.position);

            // If the projectile makes contact with the enemy
            // knock it back, lose hp equal to the damage, and start i-frames, extend streak, decrease projectile collisions
            if (dist < 100) {
                gotHitBullet = true;

                if (this.enemy.damageTimer == 0) {
                    this.enemy.velocity.set(this.enemy.kbMult * -x, this.enemy.kbMult * -y);
                    this.enemy.dealDamage(projectile.damage - this.enemy.defense, "base");
                    this.enemy.damageTimer = this.enemy.immunity;
                    GamePanel.player.streakTimer = GamePanel.player.streakTimerMax;
                    projectile.collisions--;
                }
            }
        }

        return gotHitBullet;
    }

}