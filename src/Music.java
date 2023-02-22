import javax.sound.sampled.*;
import java.io.File;

public class Music {
    private String fileName;
    private AudioInputStream audioInput;
    private Clip clip;
    private FloatControl gainControl;

    public Music(String fileName, double volume) {
        this.fileName = fileName;
        try {
            File musicPath = new File(fileName);
            audioInput = AudioSystem.getAudioInputStream(musicPath);
            clip = AudioSystem.getClip();
            clip.open(audioInput);
            clip.start();
            if (clip != null) {
                gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(20f * (float) Math.log10(volume/86.0));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void stop() {
        int currentFrameLength = clip.getFrameLength();
        clip.stop();
        clip.setFramePosition(currentFrameLength);
        System.out.println("stopped " + fileName);
    }

    public void pause() {
        if (clip.isActive()) {
            int currentFrameLength = clip.getFrameLength();
            clip.stop();
            clip.setFramePosition(currentFrameLength);
        }
    }

    public void resume() {
        if (clip != null) {
            clip.start();
        }
    }

    public double getVolume() {
        return Math.exp(gainControl.getValue() / 20f) * 86.0;
    }

    public void setVolume(double volume) {
        if (volume/100.0 > 1) {
            volume = 100.0;
        }
        else if (volume < 0) {
            volume = 0;
        }
//        System.out.println("Volume: " + volume/100.0 + ", Adj: " + (20f * (float) Math.log10(volume/86.0)));
        gainControl.setValue(20f * (float) Math.log10(volume/86.0));
    }

    public String getFileName() {
        return fileName;
    }

    public boolean aboutToEnd() {
        return this.clip.getFrameLength() - clip.getFramePosition() < 1000;
    }

    public boolean hasEnded() {
        return !this.clip.isActive();
    }

    public void restart() {
        clip.setFramePosition(0);
    }

    public int getFrameLength() {return clip.getFrameLength();}

    public int getFramePosition() {return clip.getFramePosition();}

    public void setLoop(boolean bool) {
        if (bool) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

}
