import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Music {
    private String fileName;
    private AudioInputStream audioInput;
    private Clip clip;
    private FloatControl gainControl;

    public Music(String fileName) {
        if (fileName != null) {
            this.fileName = fileName;
            play();
        }
    }

    // plays audio once then stops
    void play() {
        try {
            File musicPath = new File(fileName);
            if (musicPath.exists()) {
                audioInput = AudioSystem.getAudioInputStream(musicPath);
                clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
                if (clip != null) {
                    gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void loop() {
        try {
            AudioInputStream input = AudioSystem.getAudioInputStream(new File(fileName));
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

    public void stopMusic() {
        if (clip.isActive()) {
            clip.stop();
        }
    }

    public void pauseMusic() {
        if (clip.isActive()) {
            int currentFrameLength = clip.getFrameLength();
            clip.stop();
            clip.setFramePosition(currentFrameLength);
        }
    }

    public double getVolume() {
        return Math.pow(10f, gainControl.getValue() / 20f);
    }

    public void setVolume(double volume) {
        if (volume < 0d || volume > 1d)
            throw new IllegalArgumentException("Volume not valid: " + volume);
        gainControl.setValue(20f * (float) Math.log10(volume));
    }

    public String getFileName() {
        return fileName;
    }

    public void changeMusic(String fileName) {
        if (!fileName.equalsIgnoreCase(this.fileName)) {
            this.clip.stop();
            this.fileName = fileName;
            play();
        }
    }

    public boolean isActive() {
        return clip.isActive();
    }

    public boolean hasEnded() {
        return clip.getFramePosition() >= clip.getFrameLength();
    }

    public void restart() {
        clip.setFramePosition(0);
    }

    public Clip getClip() { return clip;}
}
