import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Music {
    File musicPath;
    private AudioInputStream audioInput;
    Clip clip;
    FloatControl gainControl;


    void playMusic(String musicLocation) {
        try {
            if (clip != null) {
                gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            }
            this.musicPath = new File(musicLocation);

            if (musicPath.exists()) {
                audioInput = AudioSystem.getAudioInputStream(musicPath);
                this.clip = AudioSystem.getClip();
                this.clip.open(audioInput);
                this.clip.start();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public void stopMusic() {
        if (clip.isActive()) {
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
            if (clip != null) {
                gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            }
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public float getVolume() {
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        return (float) Math.pow(10f, gainControl.getValue() / 20f);
    }

    public void setVolume(float volume) {
        if (volume < 0f || volume > 1f)
            throw new IllegalArgumentException("Volume not valid: " + volume);
        gainControl.setValue(20f * (float) Math.log10(volume));
    }
}
