package org.acitech;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class ResourceLoader {

    private HashMap<String, BufferedImage> textures = new HashMap<>();
    private HashMap<String, String> soundMap = new HashMap<>();

    public boolean load() {
        // Load hardcoded assets and return the result of loading them
        return loadTexture("cow", "cow.png") &&
                loadTexture("scratch", "effect/scratch.png") &&
                loadTexture("ui/inv_bar_default", "ui/inv_bar_default.png") &&
                loadTexture("items/water_material", "items/water_material.png") &&
                loadTexture("items/feather_material", "items/feather_material.png") &&
                loadTexture("items/string_material", "items/string_material.png") &&
                loadSpriteSheet("items/experience", "items/experience_placeholder_size_frame.png", 9, 9) &&
                loadSpriteSheet("player/running", "player/running.png", 32, 32) &&
                loadSpriteSheet("player/idle", "player/idle.png", 32, 32) &&
                loadSpriteSheet("enemies/Rico", "enemies/Rico.png", 32, 32) &&
                loadSpriteSheet("enemies/Pepto", "enemies/Pepto.png", 32, 32) &&
                loadSpriteSheet("effect/scratch", "effect/scratch.png", 32, 32) &&
                loadSpriteSheet("environment/grass", "environment/grass.png", 32, 32) &&
                loadSpriteSheet("environment/grass_tufts", "environment/grass_tufts.png", 32, 32) &&
                loadTexture("environment/dirt", "environment/dirt.png") &&
                loadSpriteSheet("environment/pebbles", "environment/pebbles.png", 32, 32) &&
                loadSound("player_scratch", "player/scratch.wav") &&
                loadSpriteSheet("ui/hearts", "ui/hearts.png", 18, 16) &&
                loadSpriteSheet("ui/mana", "ui/mana.png", 13, 13) &&
                loadSpriteSheet("ui/numbers", "ui/numbers.png", 5, 5);
    }

    public boolean loadTexture(String key, String path) {
        try {
            InputStream is = getClass().getResourceAsStream("/textures/" + path);
            assert is != null;
            BufferedImage image = ImageIO.read(is);
            textures.put(key, image);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean loadSpriteSheet(String key, String path, int width, int height) {
        if (!loadTexture(key, path)) return false;

        BufferedImage sheet = textures.get(key);
        int sheetWidth = sheet.getWidth();
        int sheetHeight = sheet.getHeight();
        if (sheetWidth % width != 0 || sheetHeight % height != 0) {
            System.out.println("\"" + key + "\" sprite sheet is improperly sized");
            return false;
        }

        int rows = sheetWidth / width;
        int cols = sheetHeight / height;

        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                BufferedImage sprite = sheet.getSubimage(x * width, y * height, width, height);
                textures.put(key + "/" + x + ":" + y, sprite);
            }
        }
        return true;
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

    public BufferedImage getTexture(String key) {
        return textures.getOrDefault(key, null);
    }

    public Clip getSound(String key) {
        try {
            String path = soundMap.get(key);
            if (path == null) return null;
            InputStream is = getClass().getResourceAsStream(path);
            assert is != null;
            AudioInputStream ais = AudioSystem.getAudioInputStream(is);
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            return clip;
        } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
            throw new RuntimeException(e);
        }
    }
}
