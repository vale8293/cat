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
        int cobalt = health; // thanks cobalt :)

        for (int i = 0; i < GamePanel.player.maxHealth / 2; i++) {
            BufferedImage texture;

            if (cobalt >= 2) {
                texture = Main.getResources().getTexture("ui/hearts/0:0"); // full
                cobalt -= 2;
            } else if (cobalt == 1) {
                texture = Main.getResources().getTexture("ui/hearts/1:0"); // half
                cobalt --;
            } else {
                texture = Main.getResources().getTexture("ui/hearts/2:0"); // empty
            }

            ctx.drawImage(texture, x + 16, 16, 72, 64, Main.getGamePanel());

            x += texture.getWidth() * 4 + 4;
        }

        x = 0;

        for (int i = 0; i < GamePanel.player.maxMana / 6; i++) {
            BufferedImage texture;

            switch (mana) {
                case 6:
                    texture = Main.getResources().getTexture("ui/mana/0:0");
                    break;

                case 5:
                    texture = Main.getResources().getTexture("ui/mana/1:0");
                    break;

                case 4:
                    texture = Main.getResources().getTexture("ui/mana/2:0");
                    break;

                case 3:
                    texture = Main.getResources().getTexture("ui/mana/3:0");
                    break;

                case 2:
                    texture = Main.getResources().getTexture("ui/mana/4:0");
                    break;

                case 1:
                    texture = Main.getResources().getTexture("ui/mana/5:0");
                    break;

                default:
                    texture = Main.getResources().getTexture("ui/mana/6:0");
                    break;
            }

            ctx.drawImage(texture, x + 16, 84, 52, 52, Main.getGamePanel());

            x += texture.getWidth() * 4 + 4;
        }

    }

}
