package org.acitech.assets;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class ResourceLoader {

    // UI
    public static BufferedImage ui__inv_bar_default;
    public static BufferedImage ui__menu;
    public static BufferedImage ui__cursor;
    public static SpriteSheet ui__hearts;
    public static SpriteSheet ui__mana;
    public static SpriteSheet ui__font;
    public static SpriteSheet ui__streak_bar;

    // Player
    public static SpriteSheet player__base;
    public static SpriteSheet player__fire;
    public static SpriteSheet player__aqua;
    public static SpriteSheet player__wind;
    public static BufferedImage player__death;

    // Enemies
    public static SpriteSheet enemies__Rico;
    public static SpriteSheet enemies__Pepto;
    public static SpriteSheet enemies__Jordan;
    public static SpriteSheet enemies__Pteri;

    // Items
    public static BufferedImage items__water_material;
    public static BufferedImage items__feather_material;
    public static BufferedImage items__string_material;
    public static SpriteSheet items__experience;
    public static BufferedImage items__attack_potion;
    public static BufferedImage items__health_potion;
    public static BufferedImage items__mana_potion;
    public static BufferedImage items__speed_potion;

    /** Load hardcoded assets and return the result of loading them */
    public boolean load() {
        // UI
        ui__inv_bar_default = loadTexture("ui/inv_bar_default.png");
        ui__menu = loadTexture("ui/menu.png");
        ui__cursor = loadTexture("ui/cursor.png");
        ui__hearts = new SpriteSheet(loadTexture("ui/hearts.png"), 18, 16);
        ui__mana = new SpriteSheet(loadTexture("ui/mana.png"), 13, 13);
        ui__font = new SpriteSheet(loadTexture("ui/font.png"), 5, 5);
        ui__streak_bar = new SpriteSheet(loadTexture("ui/streak_bar.png"), 32, 10);

        // Player
        player__base = new SpriteSheet(loadTexture("player/base.png"), 32, 32);
        player__fire = new SpriteSheet(loadTexture("player/fire.png"), 32, 32);
        player__aqua = new SpriteSheet(loadTexture("player/aqua.png"), 32, 32);
        player__wind = new SpriteSheet(loadTexture("player/wind.png"), 32, 32);
        player__death = loadTexture("player/death.png");

        // Enemies
        enemies__Rico = new SpriteSheet(loadTexture("enemies/Rico.png"), 32, 32);
        enemies__Pepto = new SpriteSheet(loadTexture("enemies/Pepto.png"), 32, 32);
        enemies__Jordan = new SpriteSheet(loadTexture("enemies/Jordan.png"), 32, 32);
        enemies__Pteri = new SpriteSheet(loadTexture("enemies/Pteri.png"), 96, 96);

        // Items
        items__water_material = loadTexture("items/water_material.png");
        items__feather_material = loadTexture("items/feather_material.png");
        items__string_material = loadTexture("items/string_material.png");
        items__experience = new SpriteSheet(loadTexture("items/experience.png"), 12, 12);
        items__attack_potion = loadTexture("items/attack_potion.png");
        items__health_potion = loadTexture("items/health_potion.png");
        items__mana_potion = loadTexture("items/mana_potion.png");
        items__speed_potion = loadTexture("items/speed_potion.png");

                // Spell Items
                loadTexture("spells/fire_tome_lv1", "spells/fire_tome_lv1.png") &&
                loadTexture("spells/aqua_tome_lv1", "spells/aqua_tome_lv1.png") &&

                // Spells
                loadSpriteSheet("projectiles/fireball", "projectiles/fireball.png", 32, 32) &&
                loadSpriteSheet("projectiles/aquaball", "projectiles/aquaball.png", 32, 32) &&
                loadSpriteSheet("projectiles/windball", "projectiles/windball.png", 32, 32) &&
                loadSound("fireball", "player/fireCast.wav") &&

                // Other Projectiles
                loadSpriteSheet("projectiles/featherProj", "projectiles/featherProj.png", 16, 16) &&

                // Effects
                loadSpriteSheet("effect/explosion_fire", "effect/explosion_fire.png", 32, 32) &&
                loadSpriteSheet("effect/explosion_aqua", "effect/explosion_aqua.png", 32, 32) &&
                loadSpriteSheet("effect/explosion_wind", "effect/explosion_wind.png", 32, 32) &&
                loadSound("explosion", "player/explosion.wav") &&

                // Environment
                loadSpriteSheet("environment/grass", "environment/grass.png", 32, 32) &&
                loadSpriteSheet("environment/grass_tufts", "environment/grass_tufts.png", 32, 32) &&
                loadTexture("environment/dirt", "environment/dirt.png") &&
                loadSpriteSheet("environment/pebbles", "environment/pebbles.png", 32, 32) &&
                loadTexture("environment/clouds", "environment/clouds.png") &&

                // Attacks
                loadSpriteSheet("effect/scratch", "effect/scratch.png", 32, 32) &&
                loadSound("scratch", "player/scratch.wav") &&

                // Misc.
                loadTexture("cow", "cow.png");
    }

    public BufferedImage loadTexture(String path) {
        try {
            InputStream is = getClass().getResourceAsStream("/textures/" + path);
            assert is != null;
            return ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean loadSound(String key, String path) {
        InputStream is = getClass().getResourceAsStream("/sounds/" + path);
        if (is == null) {
            System.out.println("Failed to load sound \"" + key + "\"");
            return false;
        }
        soundMap.put(key, "/sounds/" + path);
        return true;
    }
}
