import java.util.ArrayList;

public class MusicPlayer {

    private double maxMainVol = 1.0d;
    private double maxEffectVol = 1.0d;

    private static MusicPlayer musicPlayer = null;
    private final Music mainMusic = new Music("music/Silence.wav");
    private final ArrayList<Music> musics = new ArrayList<>();
    private double mainVolume = 0.2d;
    private double effectVolume = 1d;


    public synchronized static MusicPlayer getInstance() {
        if (musicPlayer == null) {
            musicPlayer = new MusicPlayer();
        }
        return musicPlayer;
    }

    public void addMusic(String fileName) {
        musics.add(new Music(fileName));
    }

    public void addMusic(Music music) {
        musics.add(music);
    }

    public void setMainMusic(String fileName) {
        if (!fileName.equalsIgnoreCase(mainMusic.getFileName())) {
            mainMusic.changeMusic(fileName);
        }
    }

    public void update() {
        ArrayList<Music> musicToRemove = new ArrayList<>();
        for (Music music: musics) {
            if (music.getVolume() !=  effectVolume) {
                music.setVolume(effectVolume);
            }
            if (music.hasEnded()) {
                musicToRemove.add(music);
            }
        }
        musics.removeAll(musicToRemove);
        mainMusic.setVolume(mainVolume);
        if (mainMusic.hasEnded()) {
            mainMusic.play();
        }
    }

    public void setMainVolume(double volume) {
        mainVolume = volume > maxMainVol ? maxMainVol : volume;
    }

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
            music.getClip().stop();
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
        this.maxMainVol = maxVol;
    }

    public void changeMaxEffect(double maxVol) {
        this.maxEffectVol = maxVol;
    }

    public double getMaxMainVol() {
        return maxMainVol;
    }

    public double getMaxEffectVol() {
        return maxEffectVol;
    }

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
                music.stopMusic();
                musics.remove(music);
                return;
            }
        }
    }
}
