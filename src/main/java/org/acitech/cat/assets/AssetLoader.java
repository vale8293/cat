package org.acitech.cat.assets;

import org.jetbrains.annotations.Nullable;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Objects;

public class AssetLoader {

    // UI
    public static BufferedImage UI_INV_BAR_DEFAULT;
    public static BufferedImage UI_MENU;
    public static BufferedImage UI_CURSOR;
    public static SpriteSheet UI_HEARTS;
    public static SpriteSheet UI_MANA;
    public static SpriteSheet UI_FONT;
    public static SpriteSheet UI_STREAK_BAR;

    // Player
    public static SpriteSheet PLAYER_BASE;
    public static SpriteSheet PLAYER_FIRE;
    public static SpriteSheet PLAYER_AQUA;
    public static SpriteSheet PLAYER_WIND;
    public static BufferedImage PLAYER_DEATH;

    // Enemies
    public static SpriteSheet ENEMIES_RICO;
    public static SpriteSheet ENEMIES_PEPTO;
    public static SpriteSheet ENEMIES_JORDAN;
    public static SpriteSheet ENEMIES_PTERI;

    // Items
    public static BufferedImage ITEMS_WATER_MATERIAL;
    public static BufferedImage ITEMS_FEATHER_MATERIAL;
    public static BufferedImage ITEMS_STRING_MATERIAL;
    public static SpriteSheet ITEMS_EXPERIENCE;
    public static BufferedImage ITEMS_ATTACK_POTION;
    public static BufferedImage ITEMS_HEALTH_POTION;
    public static BufferedImage ITEMS_MANA_POTION;
    public static BufferedImage ITEMS_SPEED_POTION;

    // Spell Items
    public static BufferedImage SPELLS_FIRE_TOME_LV1;
    public static BufferedImage SPELLS_AQUA_TOME_LV1;

    // Projectiles
    public static SpriteSheet PROJECTILES_FIREBALL;
    public static SpriteSheet PROJECTILES_AQUABALL;
    public static SpriteSheet PROJECTILES_WINDBALL;
    public static Sound FIREBALL;
    public static SpriteSheet PROJECTILES_FEATHER;

    // Effects
    public static SpriteSheet EFFECT_EXPLOSION_FIRE;
    public static SpriteSheet EFFECT_EXPLOSION_AQUA;
    public static SpriteSheet EFFECT_EXPLOSION_WIND;
    public static Sound EXPLOSION;
    // Environment
    public static SpriteSheet ENVIRONMENT_GRASS;
    public static SpriteSheet ENVIRONMENT_GRASS_TUFTS;
    public static BufferedImage ENVIRONMENT_DIRT;
    public static SpriteSheet ENVIRONMENT_PEBBLES;
    public static BufferedImage ENVIRONMENT_CLOUDS;

    // Attacks
    public static SpriteSheet EFFECT_SCRATCH;
    public static Sound SCRATCH;

    // Misc.
    public static BufferedImage COW;

    public static SpriteSheet getPlayerByElement(String key) {
        return switch (key.toLowerCase()) {
            case "fire" -> PLAYER_FIRE;
            case "aqua" -> PLAYER_AQUA;
            case "wind" -> PLAYER_WIND;
            default -> PLAYER_BASE;
        };
    }

    public static SpriteSheet getExplosionByType(String key) {
        return switch (key.toLowerCase()) {
            case "fire" -> EFFECT_EXPLOSION_AQUA;
            case "aqua" -> EFFECT_EXPLOSION_FIRE;
            case "wind" -> EFFECT_EXPLOSION_WIND;
            default -> throw new IllegalStateException("Unexpected value: " + key.toLowerCase());
        };
    }

    public static SpriteSheet getEnemyByName(String key) {
        return switch (key.toLowerCase()) {
            case "jordan" -> ENEMIES_JORDAN;
            case "pepto" -> ENEMIES_PEPTO;
            case "pteri" -> ENEMIES_PTERI;
            case "rico" -> ENEMIES_RICO;
            default -> throw new IllegalStateException("Unexpected value: " + key.toLowerCase());
        };
    }

    public static SpriteSheet getProjectileByName(String key) {
        return switch (key.toLowerCase()) {
            case "fireball" -> PROJECTILES_FIREBALL;
            case "aquaball" -> PROJECTILES_AQUABALL;
            case "windball" -> PROJECTILES_WINDBALL;
            case "feather" -> PROJECTILES_FEATHER;
            default -> throw new IllegalStateException("Unexpected value: " + key.toLowerCase());
        };
    }

    /**
     * Load hardcoded assets and return the result of loading them
     */
    public boolean load() {
        try {
            // UI
            UI_INV_BAR_DEFAULT = loadTexture("ui/inv_bar_default.png");
            UI_MENU = loadTexture("ui/menu.png");
            UI_CURSOR = loadTexture("ui/cursor.png");
            UI_HEARTS = new SpriteSheet(Objects.requireNonNull(loadTexture("ui/hearts.png")), 18, 16);
            UI_MANA = new SpriteSheet(Objects.requireNonNull(loadTexture("ui/mana.png")), 13, 13);
            UI_FONT = new SpriteSheet(Objects.requireNonNull(loadTexture("ui/font.png")), 5, 5);
            UI_STREAK_BAR = new SpriteSheet(Objects.requireNonNull(loadTexture("ui/streak_bar.png")), 32, 10);

            // Player
            PLAYER_BASE = new SpriteSheet(Objects.requireNonNull(loadTexture("player/base.png")), 32, 32);
            PLAYER_FIRE = new SpriteSheet(Objects.requireNonNull(loadTexture("player/fire.png")), 32, 32);
            PLAYER_AQUA = new SpriteSheet(Objects.requireNonNull(loadTexture("player/aqua.png")), 32, 32);
            PLAYER_WIND = new SpriteSheet(Objects.requireNonNull(loadTexture("player/wind.png")), 32, 32);
            PLAYER_DEATH = loadTexture("player/death.png");

            // Enemies
            ENEMIES_RICO = new SpriteSheet(Objects.requireNonNull(loadTexture("enemies/Rico.png")), 32, 32);
            ENEMIES_PEPTO = new SpriteSheet(Objects.requireNonNull(loadTexture("enemies/Pepto.png")), 32, 32);
            ENEMIES_JORDAN = new SpriteSheet(Objects.requireNonNull(loadTexture("enemies/Jordan.png")), 32, 32);
            ENEMIES_PTERI = new SpriteSheet(Objects.requireNonNull(loadTexture("enemies/Pteri.png")), 96, 96);

            // Items
            ITEMS_WATER_MATERIAL = loadTexture("items/water_material.png");
            ITEMS_FEATHER_MATERIAL = loadTexture("items/feather_material.png");
            ITEMS_STRING_MATERIAL = loadTexture("items/string_material.png");
            ITEMS_EXPERIENCE = new SpriteSheet(Objects.requireNonNull(loadTexture("items/experience.png")), 12, 12);
            ITEMS_ATTACK_POTION = loadTexture("items/attack_potion.png");
            ITEMS_HEALTH_POTION = loadTexture("items/health_potion.png");
            ITEMS_MANA_POTION = loadTexture("items/mana_potion.png");
            ITEMS_SPEED_POTION = loadTexture("items/speed_potion.png");

            // Spell Items
            SPELLS_FIRE_TOME_LV1 = loadTexture("spells/fire_tome_lv1.png");
            SPELLS_AQUA_TOME_LV1 = loadTexture("spells/aqua_tome_lv1.png");

            // Projectiles
            PROJECTILES_FIREBALL = new SpriteSheet(Objects.requireNonNull(loadTexture("projectiles/fireball.png")), 32, 32);
            PROJECTILES_AQUABALL = new SpriteSheet(Objects.requireNonNull(loadTexture("projectiles/aquaball.png")), 32, 32);
            PROJECTILES_WINDBALL = new SpriteSheet(Objects.requireNonNull(loadTexture("projectiles/windball.png")), 32, 32);
            FIREBALL = new Sound("/sounds/player/fireCast.wav");
            PROJECTILES_FEATHER = new SpriteSheet(Objects.requireNonNull(loadTexture("projectiles/featherProj.png")), 16, 16);

            // Effects
            EFFECT_EXPLOSION_FIRE = new SpriteSheet(Objects.requireNonNull(loadTexture("effect/explosion_fire.png")), 32, 32);
            EFFECT_EXPLOSION_AQUA = new SpriteSheet(Objects.requireNonNull(loadTexture("effect/explosion_aqua.png")), 32, 32);
            EFFECT_EXPLOSION_WIND = new SpriteSheet(Objects.requireNonNull(loadTexture("effect/explosion_wind.png")), 32, 32);
            EXPLOSION = new Sound("/sounds/player/explosion.wav");

            // Environment
            ENVIRONMENT_GRASS = new SpriteSheet(Objects.requireNonNull(loadTexture("environment/grass.png")), 32, 32);
            ENVIRONMENT_GRASS_TUFTS = new SpriteSheet(Objects.requireNonNull(loadTexture("environment/grass_tufts.png")), 32, 32);
            ENVIRONMENT_DIRT = loadTexture("environment/dirt.png");
            ENVIRONMENT_PEBBLES = new SpriteSheet(Objects.requireNonNull(loadTexture("environment/pebbles.png")), 32, 32);
            ENVIRONMENT_CLOUDS = loadTexture("environment/clouds.png");

            // Attacks
            EFFECT_SCRATCH = new SpriteSheet(Objects.requireNonNull(loadTexture("effect/scratch.png")), 32, 32);
            SCRATCH = new Sound("/sounds/player/scratch.wav");

            // Misc.
            COW = loadTexture("cow.png");
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }

        System.out.println(FIREBALL.createClip());

        var array = Arrays.asList( // TODO: since loadTexture doesn't need a Objects.requireNonNull this is here
                // UI
                UI_INV_BAR_DEFAULT,
                UI_MENU,
                UI_CURSOR,
                UI_HEARTS,
                UI_MANA,
                UI_FONT,
                UI_STREAK_BAR,
                // Player
                PLAYER_BASE,
                PLAYER_FIRE,
                PLAYER_AQUA,
                PLAYER_WIND,
                PLAYER_DEATH,
                // Enemies
                ENEMIES_RICO,
                ENEMIES_PEPTO,
                ENEMIES_JORDAN,
                ENEMIES_PTERI,
                // Items
                ITEMS_WATER_MATERIAL,
                ITEMS_FEATHER_MATERIAL,
                ITEMS_STRING_MATERIAL,
                ITEMS_EXPERIENCE,
                ITEMS_ATTACK_POTION,
                ITEMS_HEALTH_POTION,
                ITEMS_MANA_POTION,
                ITEMS_SPEED_POTION,
                // Spell Items
                SPELLS_FIRE_TOME_LV1,
                SPELLS_AQUA_TOME_LV1,
                // Projectiles
                PROJECTILES_FIREBALL,
                PROJECTILES_AQUABALL,
                PROJECTILES_WINDBALL,
                FIREBALL,
                PROJECTILES_FEATHER,
                // Effects
                EFFECT_EXPLOSION_FIRE,
                EFFECT_EXPLOSION_AQUA,
                EFFECT_EXPLOSION_WIND,
                EXPLOSION,
                // Environment
                ENVIRONMENT_GRASS,
                ENVIRONMENT_GRASS_TUFTS,
                ENVIRONMENT_DIRT,
                ENVIRONMENT_PEBBLES,
                ENVIRONMENT_CLOUDS,
                // Attacks
                EFFECT_SCRATCH,
                SCRATCH,
                // Misc.
                COW
        );

        return !array.contains(null);
    }

    private @Nullable BufferedImage loadTexture(String path) {
        InputStream is = getResourceStream("/textures/" + path);
        if (is == null) return null;

        try {
            return ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public @Nullable InputStream getResourceStream(String path) {
        return getClass().getResourceAsStream(path);
    }

    public @Nullable URL getResource(String path) {
        return getClass().getResource(path);
    }
}
