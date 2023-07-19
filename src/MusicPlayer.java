import java.io.IOException;
import java.util.ArrayList;

public class MusicPlayer {

    private static double maxMainVol = 50d;
    private static double maxEffectVol = 100d;
    private static double mainVolume = 0d;
    private static double effectVolume = 100d;

    private static MusicPlayer musicPlayer = null;
    private static Music mainMusic = new Music("music/misc/Silence.wav", mainVolume);
    private final ArrayList<Music> musics = new ArrayList<>();
    private final ArrayList<Music> musicsEnded = new ArrayList<>();


    public synchronized static MusicPlayer getInstance() {
        if (musicPlayer == null) {
            musicPlayer = new MusicPlayer();
        }
        return musicPlayer;
    }

    public void addMusic(String fileName) {
        Music newMusic = new Music(fileName, effectVolume);
        musics.add(newMusic);
        newMusic.play();
    }

    public void setMainMusic(String fileName) {
        if (!fileName.equalsIgnoreCase(mainMusic.getFileName())) {
            mainMusic.stop();
            mainMusic = new Music(fileName, mainVolume);
            mainMusic.setLoop(true);
        }
    }

    public void update() throws IOException {
        ArrayList<Music> musicToRemove = new ArrayList<>();
        for (Music music: musics) {
            music.setVolume(effectVolume);
            if (music.hasEnded()) {
                musicsEnded.add(music);
                musicToRemove.add(music);
            }
        }
        musics.removeAll(musicToRemove);
        mainMusic.setVolume(mainVolume);
    }

    public void setMainVolume(double volume) { mainVolume = volume > maxMainVol ? maxMainVol : volume; }

    public void setEffectVolume(double volume) {
        effectVolume = volume > maxEffectVol ? maxEffectVol : volume;
    }

    public void restart(String fileName) {
        if (fileName == null) {
            mainMusic.restart();
        }
        else {
            for (Music music: musics) {
                if (music.getFileName().equalsIgnoreCase(fileName)) {
                    music.restart();
                    return;
                }
            }
        }
    }

    public void clear() {
        for (Music music: musics) {
            music.stop();
        }
        musics.clear();
    }

    public Music getSound(String sound) {
        for (Music music: musics) {
            if (music.getFileName().equalsIgnoreCase(sound)) {
                return music;
            }
        }
        return null;
    }

    public Music getMainMusic() {
        return mainMusic;
    }

    public void changeMaxMain(double maxVol) {
        maxMainVol = maxVol;
    }

    public void changeMaxEffect(double maxVol) {
        maxEffectVol = maxVol;
    }

    public double getMaxMainVol() {
        return maxMainVol;
    }

    public double getMaxEffectVol() {
        return maxEffectVol;
    }

    public double getMainVolume() { return mainVolume;}

    public double getEffectVolume() { return effectVolume;}

    public boolean contains(String fileName) {
        for (Music music: musics) {
            if (fileName.equalsIgnoreCase(music.getFileName())) {
                return true;
            }
        }
        return false;
    }

    public void remove(String fileName) {
        for (Music music: musics) {
            if (fileName.equalsIgnoreCase(music.getFileName())) {
                music.stop();
                musics.remove(music);
                return;
            }
        }
    }

    public boolean hasEnded(String fileName) {
        for (Music music: musicsEnded) {
            if (music.getFileName().equals(fileName)) {
                return true;
            }
        }
        return false;
    }

    public void clearEnded() {
        musicsEnded.clear();
    }
}
