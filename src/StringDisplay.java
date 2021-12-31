public class StringDisplay {

    private String name;
    private int time;

    final double frames = SettingsSingleton.getInstance().getFrames();

    public StringDisplay(String name, int time) {
        this.time = time*144;
        this.name = name;
    }

    public int getTime() {
        return time;
    }
    public String getName() {
        return name;
    }

    public void update() {
        time--;
    }

}
