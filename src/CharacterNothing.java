import bagel.DrawOptions;
import bagel.Image;
import bagel.Window;
import bagel.util.Point;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CharacterNothing extends Character{
    private final double frames = SettingsSingleton.getInstance().getFrames();
    private Player player;
    private String name = "Nothing";
    private String soundPath = "music/Silence.wav";;
    private Point iconPos;
    private Point pos;
    private Image image;
    private Image icon;
    private Image selected = new Image(String.format("res/Selected/%s_Selected.png", name));
    private int[] stats = new int[2];

    private double timer = 0;
    private double alternateTimer = 2 * frames;
    final double speed = 1 + GameSettingsSingleton.getInstance().getMapSpeed();

    private boolean shield = false;
    private boolean noblePhantasm = false;

    private double speedDownTimer = 0;
    private double speedUpTimer = 0;
    private double minimisedTimer = 0;
    private double stunTimer = 0;
    private double hisokaTimer = 0;

    private boolean gojoAbility = false;
    private boolean jotaroAbility = false;


    public Image getIcon() { return icon;}
    public Image getImage() { return image;}
    public String getName() { return name;}

    public void move(String key) {
        if (stunTimer >= 0) {
            stunTimer --;
        }

        double currentSpeed = speed;

        if (hisokaTimer >= 0) {
            hisokaTimer--;
        }
        if (speedUpTimer >= 0) {
            currentSpeed = speed + 2;
            speedUpTimer--;
        }

        if (speedDownTimer >= 0) {
            currentSpeed = speed - 1;
            speedDownTimer--;
        }

        if (gojoAbility) {
            currentSpeed = speed - 1;
        }

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
        if (((0 < new_X) && (new_X < Window.getWidth())) && ((0 < new_Y) && (new_Y < Window.getHeight()))) {
            if (((!(hisokaTimer >= 0)) || (!(stunTimer >= 0))) || (!jotaroAbility)) {
                pos = new Point(new_X, new_Y);
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
        timer --;
        image = picture;
        if (minimisedTimer > 0) {
            picture.draw(pos.x, pos.y, new DrawOptions().setScale(0.5, 0.5));
            minimisedTimer--;
        }
        else {
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

    public void getPowerUp(PowerUp powerUp){
        if (powerUp.getName().equals("Shield")) {
            shield = true;
        }
        else if (powerUp.getName().equals("Minimiser")) {
            minimisedTimer = 5 * frames;
        }
        else if (powerUp.getName().equals("SpeedUp")) {
            speedUpTimer = 5 * frames;
        }
        else if (powerUp.getName().equals("SpeedDown")) {
            speedDownTimer = 5 * frames;
        }
        else if (powerUp.getName().equals("NoblePhantasm")) {
            noblePhantasm = true;
        }
    }

    public void resetTimer() {
        minimisedTimer = 0;
        speedUpTimer = 0;
        speedDownTimer = 0;
        stunTimer = 0;
        hisokaTimer = 0;
        shield = false;
        noblePhantasm = false;
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
        speedUpTimer = speedUpTimer + 2;
    }
    public void onSlow() {
        speedDownTimer = speedDownTimer - 2;
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
        speedUpTimer = 1;
        stunTimer = 0;
        speedDownTimer = 0;
        shield = true;
        hisokaTimer = 0;
    }
    public void setHisokaAbility(double timer) { hisokaTimer = timer;}
    public void setJotaroAbility(boolean bool) { jotaroAbility = bool;}
    public boolean isMinimised() {return minimisedTimer > 0;}
    public void gotStunned() {stunTimer = 2*frames;}
}