package org.acitech.cat.inputs;

import org.acitech.cat.utils.Vector2d;

import java.awt.event.MouseEvent;

public class Click {
    private final int x;
    private final int y;
    private final int button;

    public Click(MouseEvent event) {
        this.x = event.getX();
        this.y = event.getY();
        this.button = event.getButton();
    }

    public int getX() {
        return this.x;
    }
    public int getY() {
        return this.y;
    }
    public Vector2d toVector() {
        return new Vector2d(this.x, this.y);
    }
    public int getButton() {
        return button;
    }
}
