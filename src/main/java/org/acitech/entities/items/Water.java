package org.acitech.entities.items;

import org.acitech.GamePanel;
import org.acitech.Main;
import org.acitech.entities.Entity;
import org.acitech.entities.Item;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Water extends Item {

    private int width = 36;
    private int height = 36;

    public Water(double startX, double startY) {
        super(startX, startY);
    }

    @Override
    protected void tick(double delta) {
        // UFUCK

        super.tick(delta);

        // hehgwetr
    }
    @Override
    public void draw(Graphics2D ctx) {
        BufferedImage texture = Main.getResources().getTexture("Water");
    }

}
