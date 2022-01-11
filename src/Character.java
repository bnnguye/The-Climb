import bagel.DrawOptions;
import bagel.Image;
import bagel.Window;
import bagel.util.Point;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

abstract class Character{
        private final double frames = SettingsSingleton.getInstance().getFrames();
        private Player player;
        private String name;
        private String soundPath;
        private Point iconPos;
        private Point pos;
        private Image image;
        private Image icon;
        private Image selected;
        private int[] stats;

        private double timer;
        private double alternateTimer;
        private boolean moving = false;
        private double speed = 1 + GameSettingsSingleton.getInstance().getMapSpeed();

        private boolean shield;
        private boolean noblePhantasm;

        private double speedDownTimer;
        private double speedUpTimer;
        private double minimisedTimer;
        private double stunTimer;
        private double hisokaTimer;

        private boolean gojoAbility;
        private boolean jotaroAbility;


        public Image getIcon() { return icon;}
        public Image getImage() { return image;}
        public String getName() { return name;}

        public void move(String key) {
        }
        public void draw() {
        }

        public void setPosition(Point point) { pos = point;}
        public Point getPos() { return pos;}

        public void setPlayer(Player player) { this.player = player;}
        public Player getPlayer() {
            return player;
        }

        public void popShield() { shield = false;}
        public boolean hasShield() { return shield;}

        public void getPowerUp(PowerUp powerUp){
        }

        public void resetTimer() {
        }

        public String playLine() {
            return soundPath;
        }

        public Image getSelected() { return selected;}
        public Point getIconPos() {return iconPos;}
        public void setIconPos(Point point) { iconPos = point;}

        public void setStats() {

        }
        public int[] getStats() { return stats;}
        public void updateStats(boolean played, boolean won) {

        }
        public void onIce() {
        }
        public void onSlow() {
        }

        public double getMinimisedTimer() {
            return minimisedTimer;
        }
        public double getSpeedDownTimer() {
            return speedDownTimer;
        }
        public double getSpeedUpTimer() {
            return speedUpTimer;
        }

        public boolean hasNoblePhantasm() {return noblePhantasm;}
        public void useNoblePhantasm() {
            noblePhantasm = false;
        }
        public void setGojoAbility(boolean bool) {
            gojoAbility = bool;
        }
        public void setAllMightAbility() {
        }
        public void setHisokaAbility(double timer) { hisokaTimer = timer;}
        public void setJotaroAbility(boolean bool) { jotaroAbility = bool;}
        public boolean isMinimised() {return minimisedTimer > 0;}
        public void gotStunned() {stunTimer = 2*frames;}
    }