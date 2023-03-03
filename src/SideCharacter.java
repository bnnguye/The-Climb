import bagel.Image;
import bagel.util.Point;

import java.util.ArrayList;

public abstract class SideCharacter {
    private final double frames = SettingsSingleton.getInstance().getRefreshRate();
    private String name;
    private String power;
    private String desc;

    boolean activating;
    boolean animating;

    public abstract boolean isActivating();
    public abstract boolean isAnimating();
    public abstract void reset();

    public abstract String getName();
    public abstract String getPower();
    public abstract String getDesc();
    public abstract String getSoundPath();

    public abstract void activateAbility(Player user, ArrayList<Obstacle> obstacles, ArrayList<PowerUp> powerUps);

    public abstract void renderAbility();
}
