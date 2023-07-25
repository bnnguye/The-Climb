import bagel.Image;
import bagel.util.Point;

import java.util.ArrayList;

public abstract class SideCharacter {

    protected String name;
    protected String power;
    protected String desc;
    protected boolean activating;
    protected boolean animating;

    public abstract boolean isActivating();
    public abstract boolean isAnimating();
    public abstract void reset();

    public abstract String getName();
    public abstract String getPower();
    public abstract String getDesc();
    public abstract String getSoundPath();

    public abstract void activateAbility(Player user);

    public abstract void renderAbility();
}
