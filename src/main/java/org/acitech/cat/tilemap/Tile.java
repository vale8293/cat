package org.acitech.cat.tilemap;

import org.acitech.cat.assets.AssetLoader;
import org.acitech.cat.assets.SpriteSheet;
import org.jetbrains.annotations.Nullable;

import java.awt.image.BufferedImage;
import java.util.List;

public class Tile {

    // Define the static tiles
    public static Tile GRASS = new Tile("grass", AssetLoader.ENVIRONMENT_GRASS);
    public static Tile DIRT = new Tile("dirt", AssetLoader.ENVIRONMENT_DIRT);

    public static final List<Tile> TILE_LIST = List.of(GRASS, DIRT);

    public static Tile getTileById(String id) {
        return TILE_LIST.stream().filter(t -> t.id.equals(id)).findFirst().orElse(null);
    }

    public static List<Tile> getConnectedTiles() {
        return TILE_LIST.stream().filter(Tile::isConnected).toList();
    }

    public static final int tileSize = 64;

    // Tile constructor
    private final String id;
    private final BufferedImage texture;
    private final SpriteSheet spriteSheet;
    private final boolean connected;

    public Tile(String id, BufferedImage texture) {
        this.id = id;
        this.texture = texture;
        this.spriteSheet = null;
        this.connected = false;
    }

    public Tile(String id, SpriteSheet spriteSheet) {
        this.id = id;
        this.texture = null;
        this.spriteSheet = spriteSheet;
        this.connected = true;
    }

    public String getId() {
        return id;
    }

    public BufferedImage getTexture(@Nullable Connector connector) {
        if (!this.connected) return null;
        assert spriteSheet != null;

        if (connector == null) return spriteSheet.getSprite(1, 1); // Get center texture

        return spriteSheet.getSprite(connector.getTextureX(), connector.getTextureY());
    }

    public BufferedImage getFullTexture() {
        return connected ? getTexture(null) : texture;
    }

    public boolean isConnected() {
        return connected;
    }
}
