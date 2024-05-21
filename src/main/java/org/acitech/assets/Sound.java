package org.acitech.assets;

import org.acitech.Main;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;

public class Sound {

    private final String resourcePath;

    public Sound(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public Clip createClip() {
        try {
            InputStream inputStream = Main.getResources().getResourceStream(resourcePath);
            assert inputStream != null;
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(inputStream);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            return clip;
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}