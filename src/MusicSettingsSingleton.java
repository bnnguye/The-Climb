import java.util.ArrayList;

public class MusicSettingsSingleton {
    private static MusicSettingsSingleton single_instance = null;
    private ArrayList<Music> musicList = new ArrayList<>();

    public synchronized static MusicSettingsSingleton getInstance() {
        if (single_instance == null) {
            single_instance = new MusicSettingsSingleton();

        }
        return single_instance;
    }

    public void addMusic(Music music) {
        musicList.add(music);
    }
}
