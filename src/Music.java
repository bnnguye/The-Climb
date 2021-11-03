import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Music {
    File musicPath;
    AudioInputStream audioInput;
    Clip clip;
    boolean played = false;

    void playMusic(String musicLocation) {
        try {
            this.musicPath = new File(musicLocation);

            if (musicPath.exists()) {
                audioInput = AudioSystem.getAudioInputStream(musicPath);
                this.clip = AudioSystem.getClip();
                this.clip.open(audioInput);
                this.clip.start();
                played = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public void stopMusic() {
        if (clip.isActive()) {
            played = false;
            clip.stop();
        }
    }

    public void run(String filePath) {
        try {
            AudioInputStream input = AudioSystem.getAudioInputStream(new File(filePath));
            this.clip = AudioSystem.getClip();
            this.clip.open(input);
            this.clip.loop(Clip.LOOP_CONTINUOUSLY);
            this.clip.start();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
