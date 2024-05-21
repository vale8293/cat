package org.acitech.cat.tilemap;

import org.acitech.cat.assets.AssetLoader;

import java.awt.image.BufferedImage;
import java.util.List;

public enum Decor {

    GRASS_TUFTS_1(AssetLoader.ENVIRONMENT_GRASS_TUFTS.getSprite(0, 0)),
    GRASS_TUFTS_2(AssetLoader.ENVIRONMENT_GRASS_TUFTS.getSprite(1, 0)),
    GRASS_TUFTS_3(AssetLoader.ENVIRONMENT_GRASS_TUFTS.getSprite(2, 0)),
    GRASS_TUFTS_4(AssetLoader.ENVIRONMENT_GRASS_TUFTS.getSprite(3, 0)),
    GRASS_TUFTS_5(AssetLoader.ENVIRONMENT_GRASS_TUFTS.getSprite(4, 0)),
    GRASS_TUFTS_6(AssetLoader.ENVIRONMENT_GRASS_TUFTS.getSprite(5, 0)),
    GRASS_TUFTS_7(AssetLoader.ENVIRONMENT_GRASS_TUFTS.getSprite(6, 0)),
    GRASS_TUFTS_8(AssetLoader.ENVIRONMENT_GRASS_TUFTS.getSprite(7, 0)),
    DIRT_PEBBLES_1(AssetLoader.ENVIRONMENT_PEBBLES.getSprite(0, 0)),
    DIRT_PEBBLES_2(AssetLoader.ENVIRONMENT_PEBBLES.getSprite(1, 0)),
    DIRT_PEBBLES_3(AssetLoader.ENVIRONMENT_PEBBLES.getSprite(2, 0)),
    DIRT_PEBBLES_4(AssetLoader.ENVIRONMENT_PEBBLES.getSprite(3, 0));

    public static final List<Decor> GRASS_DECORS = List.of(GRASS_TUFTS_1, GRASS_TUFTS_2, GRASS_TUFTS_3, GRASS_TUFTS_4, GRASS_TUFTS_5, GRASS_TUFTS_6, GRASS_TUFTS_7, GRASS_TUFTS_8);
    public static final List<Decor> DIRT_DECORS = List.of(DIRT_PEBBLES_1, DIRT_PEBBLES_2, DIRT_PEBBLES_3, DIRT_PEBBLES_4);

    private final BufferedImage texture;

    Decor(BufferedImage texture) {
        this.texture = texture;
    }

    public BufferedImage getTexture() {
        return texture;
    }

}
