import bagel.Image;
import bagel.Window;
import bagel.util.Point;

import java.util.ArrayList;

abstract class Character extends Object {

    private Player player;
    private String name;
    private String soundPath;
    private Image icon;
    private Point iconPos;
    private Image selected;
    private Image image;
    private Point pos;
    private int[] stats;

    private boolean shield = false;
    private boolean speedUp = false;
    private boolean speedDown = false;
    private int speedDownTimer = 0;
    private int speedUpTimer = 0;
    private boolean minimised = false;
    private int minimisedTimer = 0;
    private boolean noblePhantasm = false;
    private boolean gojoAbility = false;
    private boolean hisokaAbility = false;
    private boolean jotaroAbility = false;
    private boolean yugiAbility = false;
    private double stunTime = 0;

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

    public String playLine() {
        return null;
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

    public void gotStunned() {}
}