import bagel.DrawOptions;
import bagel.Image;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Character {

        private final double frames = SettingsSingleton.getInstance().getRefreshRate();

        private String name;
        private String lastName = "";

        private Point pos = new Point(0,0);
        private Image image;
        private Rectangle rectangle;

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
        private boolean gojoAbility = false;

        private int lives;
        private boolean dead = false;


        public Character(String name) {
                this.name = name.split(" ")[0];
                if (name.split(" ").length > 1) {
                        this.lastName = name.split(" ")[1];
                }
                image = new Image(String.format("res/characters/%s/Left.png", name));
        }

        public Image getIcon() {
                return new Image(String.format("res/characters/%s/Icon.png", name));
        }

        public Image getImage() {
                return image;
        }

        public String getName() {
                return name;
        }

        public String getLastName() {return lastName;}

        public String getFullName() {
                if (lastName.equals("")) {
                        return name;
                }
                return name + " " + lastName;
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
                        if (!GameSettingsSingleton.getInstance().getMap().hasFinished()) {
                                pos = new Point(pos.x, pos.y + GameSettingsSingleton.getInstance().getMapSpeed());
                        }
                }
        }

        public void draw() {
                Image picture = new Image(String.format("res/characters/%s/Left.png", name, name));
                if (timer < alternateTimer/2) {
                        picture = new Image(String.format("res/characters/%s/Left.png", name, name));
                }
                else if (timer > alternateTimer/2) {
                        picture  = new Image(String.format("res/characters/%s/Right.png", name, name));
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

        public void popShield() { shield = false;}
        public boolean hasShield() { return shield;}

        public void reset() {
                minimisedTimer = 0;
                speedUpTimer = 0;
                speedDownTimer = 0;
                stunTimer = 0;
                specialAbilityBar = 0;
                moving = false;
                shield = false;
                gojoAbility = false;
                dead = false;
        }

        public String playLine() {
                return String.format("music/%s.wav", name);
        }

        public Image getSelected() { return new Image(String.format("res/characters/%s/Selected.png", name));}

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
                gojoAbility = false;
        }
        public boolean isMinimised() {return minimisedTimer > 0;}
        public boolean isDead() {
                return this.dead;
        }
        public void gotStunned() {stunTimer = frames;}
        public Rectangle getRectangle() {return rectangle;}

        public boolean canMove() { return !(stunTimer > 0);
        }

        public void updateCharacter() {
                if (stunTimer > 0) {
                        stunTimer--;
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

        public void setDead(boolean bool) {
                this.dead = bool;
        }

        public void setLives(int lives) {
                this.lives = lives;
        }
        public int getLives() {return this.lives;}
}