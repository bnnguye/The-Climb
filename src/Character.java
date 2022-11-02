import bagel.DrawOptions;
import bagel.Drawing;
import bagel.Image;
import bagel.Window;
import bagel.util.Colour;
import bagel.util.Point;
import bagel.util.Rectangle;
import org.lwjgl.system.windows.RECT;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Character {
        private final double frames = SettingsSingleton.getInstance().getFrames();
        private Player player;
        private String name;
        private String soundPath;
        private Point iconPos;
        private Point pos;
        private Image image;
        private Rectangle rectangle;
        private Image icon;
        private Image selected;
        private int[] stats = new int[2];

        private double timer = 0;
        private double alternateTimer = 1 * frames;
        private boolean moving = false;
        private double speed = 1 + GameSettingsSingleton.getInstance().getMapSpeed();

        private boolean shield = false;
        private double specialAbilityBar = 0;

        private double speedDownTimer = 0;
        private double speedUpTimer = 0;
        private double minimisedTimer = 0;
        private double stunTimer = 0;

        private double hisokaTimer = 0;
        private boolean gojoAbility = false;
        private boolean jotaroAbility = false;


        public Character(String name) {
                this.name = name;
                selected = new Image(String.format("res/characters/%s/Selected.png", name));
                icon = new Image(String.format("res/characters/%s/Icon.png", name));
                soundPath = String.format("music/%s.wav", name);
                image = new Image(String.format("res/characters/%s/%s_Left.png", name, name));
        }

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
                double currentSpeed = speed;

                if (speedDownTimer > 0 || gojoAbility) {
                        currentSpeed = speed/5*3;
                        speedDownTimer--;
                }
                if (speedUpTimer > 0) {
                        currentSpeed = speed*2;
                        speedUpTimer--;
                }

                if (key != null) {
                        double new_X = pos.x;
                        double new_Y = pos.y;
                        if (key.equals("WA")) {
                                new_Y -= currentSpeed;
                                new_X -= currentSpeed;
                        }
                        if (key.equals("WD")) {
                                new_Y -= currentSpeed;
                                new_X += currentSpeed;
                        }
                        if (key.equals("SA")) {
                                new_Y += currentSpeed;
                                new_X -= currentSpeed;
                        }
                        if (key.equals("SD")) {
                                new_Y += currentSpeed;
                                new_X += currentSpeed;
                        }
                        if (key.equals("W")) {
                                new_Y -= currentSpeed;
                        }
                        if (key.equals("A")) {
                                new_X -= currentSpeed;
                        }
                        if (key.equals("S")) {
                                new_Y += currentSpeed;
                        }
                        if (key.equals("D")) {
                                new_X += currentSpeed;
                        }
                        Point newPoint = new Point(new_X, new_Y);
                        if (((0 < new_X) && (new_X < Window.getWidth())) && ((0 < new_Y) && (new_Y < Window.getHeight()))) {
                                pos = newPoint;
                                moving = true;
                        }
                } else {
                        moving = false;
                        if (!GameSettingsSingleton.getInstance().getMap().hasFinished() && !GameSettingsSingleton.getInstance().getMap().getJotaroAbility()) {
                                pos = new Point(pos.x, pos.y + GameSettingsSingleton.getInstance().getMapSpeed());
                        }
                }
        }

        public void draw() {
                Image picture = new Image(String.format("res/characters/%s/%s_Left.png", name, name));
                if (timer < alternateTimer/2) {
                        picture = new Image(String.format("res/characters/%s/%s_Left.png", name, name));
                }
                else if (timer > alternateTimer/2) {
                        picture  = new Image(String.format("res/characters/%s/%s_Right.png", name, name));
                }
                if (timer <= 0) {
                        timer = alternateTimer;
                }
                if (moving) {
                        timer --;
                }
                image = picture;
                if (minimisedTimer > 0) {
                        rectangle = new Rectangle(new Point(pos.x - image.getWidth()/4, pos.y - image.getHeight()/4), image.getWidth()/2, image.getHeight()/2);
                        //Drawing.drawRectangle(new Point(pos.x - image.getWidth()/4, pos.y - image.getHeight()/4), image.getWidth()/2, image.getHeight()/2, new Colour(0,0,0,0.5));
                        picture.draw(pos.x, pos.y, new DrawOptions().setScale(0.5, 0.5));
                        minimisedTimer--;
                }
                else {
                        rectangle = new Rectangle(new Point(pos.x - image.getWidth()/2, pos.y - image.getHeight()/2), image.getWidth(), image.getHeight());
                        //Drawing.drawRectangle(new Point(pos.x - image.getWidth()/2, pos.y - image.getHeight()/2), image.getWidth(), image.getHeight(), new Colour(0,0,0,0.5));
                        picture.draw(pos.x, pos.y);
                }
                if (shield) {
                        Image bubble = new Image("res/bubble.png");
                        bubble.draw(pos.x, pos.y);
                }
        }

        public void setPosition(Point point) { pos = point;}
        public Point getPos() { return pos;}

        public void setPlayer(Player player) { this.player = player;}
        public Player getPlayer() {
                return player;
        }

        public void popShield() { shield = false;}
        public boolean hasShield() { return shield;}

        public void resetTimer() {
                minimisedTimer = 0;
                speedUpTimer = 0;
                speedDownTimer = 0;
                stunTimer = 0;
                hisokaTimer = 0;
                specialAbilityBar = 0;
                moving = false;
                shield = false;
                gojoAbility = false;
                jotaroAbility = false;
        }

        public String playLine() {
                return soundPath;
        }

        public Image getSelected() { return selected;}
        public Point getIconPos() {return iconPos;}
        public void setIconPos(Point point) { iconPos = point;}

        public void setStats() {
                String line;
                String[] lineSplit;
                try {
                        Scanner scanner = new Scanner(new File("stats/Stats.txt"));
                        while(scanner.hasNextLine()) {
                                line = scanner.nextLine();
                                if (line.equals(name)) {
                                        break;
                                }
                        }
                        if(scanner.hasNextLine()) {
                                line = scanner.nextLine();
                                lineSplit = line.split(" ");
                                int played = Integer.parseInt(lineSplit[lineSplit.length - 1]);
                                stats[0] = played;
                        }
                        if(scanner.hasNextLine()) {
                                line = scanner.nextLine();
                                lineSplit = line.split(" ");
                                int won = Integer.parseInt(lineSplit[lineSplit.length - 1]);
                                stats[1] = won;
                        }
                } catch (FileNotFoundException e) {
                        e.printStackTrace();
                }
        }
        public int[] getStats() { return stats;}
        public void updateStats(boolean played, boolean won) {
                if (played) {
                        stats[0] ++;
                }
                if (won) {
                        stats[1] ++;
                }
        }
        public void onIce() {
                speedUpTimer += 1;
        }
        public void onSlow() {
                speedDownTimer += 1;
        }

        public boolean hasSpecialAbility() {return specialAbilityBar > 99;}
        public void useSpecialAbility() {
                specialAbilityBar = 0;
        }
        public void gainSpecialAbilityBar(double num) {
                specialAbilityBar += num;
        }
        public double getSpecialAbilityBar() {return specialAbilityBar;}
        public void setAllMightAbility() {
                speedUpTimer = 1;
                stunTimer = 0;
                speedDownTimer = 0;
                shield = true;
                hisokaTimer = 0;
                jotaroAbility = false;
                gojoAbility = false;
        }
        public void setHisokaAbility(double timer) { hisokaTimer = timer;}
        public boolean isMinimised() {return minimisedTimer > 0;}
        public void gotStunned() {stunTimer = frames;}
        public Rectangle getRectangle() {return rectangle;}
        public void setJotaroAbility(boolean bool) {
                jotaroAbility = bool;
        }

        public boolean canMove() {
                return !(hisokaTimer > 0 || jotaroAbility || stunTimer > 0);
        }

        public void updateCharacter() {
                if (stunTimer > 0) {
                        stunTimer--;
                }
                if (hisokaTimer > 0) {
                        hisokaTimer--;
                }
        }

        public void speedUp() {
                speedUpTimer = 3 * frames;
        }
        public void speedDown() {
                speedDownTimer = 3 * frames;
        }
        public void minimiser() {
                minimisedTimer = 3 * frames;
        }
        public void shield() {
                shield = true;
        }
}