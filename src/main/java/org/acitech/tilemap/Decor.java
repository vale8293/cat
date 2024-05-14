package org.acitech.tilemap;

import org.acitech.Main;

import java.awt.image.BufferedImage;
import java.util.List;

public enum Decor {

    GRASS_TUFTS_1("tuft", "environment/grass_tufts/0:0"),
    GRASS_TUFTS_2("tuft", "environment/grass_tufts/1:0"),
    GRASS_TUFTS_3("tuft", "environment/grass_tufts/2:0"),
    GRASS_TUFTS_4("tuft", "environment/grass_tufts/3:0"),
    GRASS_TUFTS_5("tuft", "environment/grass_tufts/4:0"),
    GRASS_TUFTS_6("tuft", "environment/grass_tufts/5:0"),
    GRASS_TUFTS_7("tuft", "environment/grass_tufts/6:0"),
    GRASS_TUFTS_8("tuft", "environment/grass_tufts/7:0"),
    DIRT_PEBBLES_1("pebble", "environment/pebbles/0:0"),
    DIRT_PEBBLES_2("pebble", "environment/pebbles/1:0"),
    DIRT_PEBBLES_3("pebble", "environment/pebbles/2:0"),
    DIRT_PEBBLES_4("pebble", "environment/pebbles/3:0");

    private final BufferedImage texture;

    public static final List<Decor> GRASS_DECORS = List.of(GRASS_TUFTS_1, GRASS_TUFTS_2, GRASS_TUFTS_3, GRASS_TUFTS_4, GRASS_TUFTS_5, GRASS_TUFTS_6, GRASS_TUFTS_7, GRASS_TUFTS_8);
    public static final List<Decor> DIRT_DECORS = List.of(DIRT_PEBBLES_1, DIRT_PEBBLES_2, DIRT_PEBBLES_3, DIRT_PEBBLES_4);

    Decor(String category, String textureKey) {
        texture = Main.getResources().getTexture(textureKey);
    }

    public BufferedImage getTexture() {
        return texture;
    }

}
