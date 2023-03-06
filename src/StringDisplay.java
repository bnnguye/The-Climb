import bagel.Font;
import bagel.util.Point;

public class StringDisplay {

    private String name;
    private double time;
    private Point topLeft;
    private boolean permanent = false;
    private Font font;

    public String tag = "N/A";

    public StringDisplay(String name, double time, int fontSize, Point point) {
        this.time = time;
        this.name = name;
        this.font = new Font("res/fonts/DejaVuSans-Bold.ttf", fontSize);
        topLeft = point;
    }

    public StringDisplay(String name, boolean bool, int fontSize, Point point) {
        this.time = time*SettingsSingleton.getInstance().getRefreshRate();
        this.name = name;
        this.font = new Font("res/fonts/DejaVuSans-Bold.ttf", fontSize);
        this.permanent = bool;
        topLeft = point;
    }

    public StringDisplay(String name, double time, int fontSize, Point point, String tag) {
        this.time = time;
        this.name = name;
        this.font = new Font("res/fonts/DejaVuSans-Bold.ttf", fontSize);
        this.tag = tag;
        topLeft = point;
    }

    public StringDisplay(String name, boolean bool, int fontSize, Point point, String tag) {
        this.time = time*SettingsSingleton.getInstance().getRefreshRate();
        this.name = name;
        this.font = new Font("res/fonts/DejaVuSans-Bold.ttf", fontSize);
        this.permanent = bool;
        this.tag = tag;
        topLeft = point;
    }

    public double getTime() {
        return time;
    }
    public String getName() {
        return name;
    }

    public void update() {
        time--;
    }

    public void draw() {
        if (permanent) {
            font.drawString(name, topLeft.x, topLeft.y);
        }
        else {
            if (time > 0) {
                font.drawString(name, topLeft.x, topLeft.y);
            }
        }
    }

}
