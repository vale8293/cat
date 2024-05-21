package org.acitech.cat.assets;

import org.acitech.cat.Main;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class Sound {

    private final String resourcePath;

    public Sound(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public Clip createClip() {
        try {
            URL soundURL = Main.getResources().getResource(resourcePath);
            assert soundURL != null;
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundURL);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            return clip;
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}