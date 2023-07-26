import bagel.DrawOptions;
import bagel.Font;
import bagel.util.Colour;
import bagel.util.Point;

import java.util.ArrayList;

public class StringDisplay {

    private String name;
    private Point topLeft;
    private double time;
    private boolean permanent = false;
    private FontSize fontSize;
    private String tag = "N/A";

    private double transparency = 1;
    private double r = 0;
    private double g = 0;
    private double b = 0;

    public StringDisplay(String name, double time, FontSize fontSize, Point point) {
        this.time = time;
        this.name = name;
        this.fontSize = fontSize;
        topLeft = point;
    }

    public StringDisplay(String name, boolean bool, FontSize fontSize, Point point) {
        this.time = time*TimeLogger.getInstance().getRefreshRate();
        this.name = name;
        this.fontSize = fontSize;
        this.permanent = bool;
        topLeft = point;
    }

    public StringDisplay(String name, double time, FontSize fontSize, Point point, String tag) {
        this.time = time;
        this.name = name;
        this.fontSize = fontSize;
        this.tag = tag;
        topLeft = point;
    }

    public StringDisplay(String name, boolean bool, FontSize fontSize, Point point, String tag) {
        this.time = time*TimeLogger.getInstance().getRefreshRate();
        this.name = name;
        this.fontSize = fontSize;
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
    public boolean isPermanent() {return permanent;}

    public void update() {
        time--;
    }

    public void draw() {
        if (permanent) {
            fontSize.draw(name, topLeft.x, topLeft.y, new DrawOptions().setBlendColour(r,g,b,transparency));
        }
        else {
            if (time > 0) {
                fontSize.draw(name, topLeft.x, topLeft.y);
            }
        }
    }

    public String getTag() {return tag;}

    public void setColour(double r, double g, double b, double t) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.transparency = t;
    }

    public void setColour(double r, double g, double b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

}
