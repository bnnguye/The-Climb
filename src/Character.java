import bagel.Image;
import bagel.Window;
import bagel.util.Point;

import java.util.ArrayList;

abstract class Character extends Object {

    Player player;
    String name;
    Image icon;
    Point iconPos;
    Image selected;
    Image image;
    Point pos;
    int[] stats;
    Music music = new Music();
    int timer = 120;
    final double speed = 2;

    boolean shield = false;
    boolean bullet = false;
    int ammo = 0;
    boolean speedUp = false;
    boolean speedDown = false;
    int speedDownTimer = 0;
    int speedUpTimer = 0;
    boolean minimised = false;
    int minimisedTimer = 0;
    boolean noblePhantasm = false;
    boolean gojoAbility = false;
    boolean hisokaAbility = false;
    boolean jotaroAbility = false;
    boolean yugiAbility = false;

    public Image getIcon() {
        return icon;
    }

    public Image getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public void move(String key) {
    }

    public void draw() {
    }

    public void setPosition(Point point) {
    }

    public Point getPos() {
        return pos;
    }

    public void setPlayer(Player player) {
    }

    public Player getPlayer() {
        return player;
    }

    public void decreaseMinimisedTimer() {
    }

    public void popShield() {
    }

    public boolean hasShield() {
        return shield;
    }

    public void getPowerUp(PowerUp powerUp) {
    }

    public void resetTimer() {
    }

    public void playLine() {
    }

    public Image getSelected() {
        return selected;
    }

    public Point getIconPos() {
        return iconPos;
    }

    public void setIconPos(Point point) {
    }

    public void stopMusic() {
    }

    public void setStats() {
    }

    public int[] getStats() {
        return stats;
    }

    public void updateStats(boolean played, boolean won) {
    }

    public void onIce() {
        speedUp = true;
        speedUpTimer++;
    }

    public void onSlow() {
        speedDown = true;
        speedDownTimer++;
    }

    public int getMinimisedTimer() {
        return minimisedTimer;
    }

    public int getSpeedDownTimer() {
        return speedDownTimer;
    }

    public int getSpeedUpTimer() {
        return speedUpTimer;
    }

    public boolean hasNoblePhantasm() {
        return noblePhantasm;
    }

    public void useNoblePhantasm() {
        noblePhantasm = false;
    }

    public void setGojoAbility(boolean bool) {
        gojoAbility = bool;
    }

    public void setAllMightAbility() {
    }

    public void setHisokaAbility(boolean bool) {
    }

    public void setJotaroAbility(boolean bool) {
    }

    public boolean isMinimised() {
        return minimised;
    }

    public void setYugiAbility(boolean bool) {}
}