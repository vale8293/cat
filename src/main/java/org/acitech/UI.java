package org.acitech;

import org.acitech.GamePanel;
import org.acitech.KeyHandler;
import org.acitech.Main;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.image.BufferedImage;

public class UI {

    public void draw(Graphics2D ctx) {
        int health = GamePanel.player.health;
        int mana = GamePanel.player.mana;

        int x = 0;

        for (int i = 0; i < GamePanel.player.maxHealth / 2; i++) {
            BufferedImage texture;

            if (health >= 2) {
                //Draw full heart
                texture = Main.getResources().getTexture("ui/hearts/0:0");
                health -= 2;
            }
            else if (health >= 1) {
                //Draw half heart
                texture = Main.getResources().getTexture("ui/hearts/1:0");
                health -= 1;
            }
            else {
                //Draw empty heart
                texture = Main.getResources().getTexture("ui/hearts/2:0");
            }

            ctx.drawImage(texture, x + 20, 20, 32, 36, Main.getGamePanel());

            x += texture.getWidth() + 4;
        }

    }

}
