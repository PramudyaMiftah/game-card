package assetsmanager;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.InputStream;

public class SoundManager {
    private static Clip backgroundMusic;

    // Method untuk memutar sound effect sekali (misal: klik, hover)
    public static void playSound(String soundName) {
        try {
            InputStream audioSrc = SoundManager.class.getResourceAsStream("/assets/sounds/" + soundName);
            if (audioSrc == null) {
                System.err.println("Sound file not found: " + soundName);
                return;
            }
            InputStream bufferedIn = new BufferedInputStream(audioSrc);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(bufferedIn);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method untuk memutar musik latar secara berulang
    public static void loopMusic(String soundName) {
        stopMusic(); // Hentikan musik sebelumnya jika ada
        try {
            InputStream audioSrc = SoundManager.class.getResourceAsStream("/assets/sounds/" + soundName);
            if (audioSrc == null) {
                System.err.println("Sound file not found: " + soundName);
                return;
            }
            InputStream bufferedIn = new BufferedInputStream(audioSrc);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(bufferedIn);
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioIn);
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method untuk menghentikan musik latar
    public static void stopMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
        }
    }
}