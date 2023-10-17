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
                loadSpriteSheet("player/running", "player/running.png", 32, 32) &&
                loadSpriteSheet("enemies/Rico", "enemies/Rico.png", 32, 32) &&
                loadSpriteSheet("effect/scratch", "effect/scratch.png", 32, 32) &&
                loadSpriteSheet("environment/grass", "environment/grass.png", 32, 32) &&
                loadSound("player_scratch", "player/scratch.wav") &&
                loadSpriteSheet("ui/hearts", "ui/hearts.png", 18, 16) &&
                loadSpriteSheet("ui/mana", "ui/mana.png", 13, 13) &&
                loadSpriteSheet("ui/numbers", "ui/numbers.png", 3, 5);
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
        if (sheetWidth % width != 0 || sheetHeight % height != 0) return false;

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
        if (is == null) return false;
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
