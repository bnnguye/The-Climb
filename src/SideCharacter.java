import bagel.Image;
import bagel.util.Point;

import java.util.ArrayList;

public abstract class SideCharacter {

    protected ArrayList<Obstacle> obstacles = GameEntities.getInstance().getObstacles();
    protected ArrayList<PowerUp> powerUps = GameEntities.getInstance().getPowerUps();


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
