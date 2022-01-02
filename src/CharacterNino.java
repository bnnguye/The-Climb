import bagel.DrawOptions;
import bagel.Image;
import bagel.Window;
import bagel.util.Point;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CharacterNino extends Character{
    private final double frames = SettingsSingleton.getInstance().getFrames();
    private Player player;
    private String name = "Nino";
    private String soundPath = String.format("music/%s.wav", this.name);
    private Point iconPos;
    private Point pos;
    Image image;
    Image icon = new Image(String.format("res/icons/%s.png", this.name));
    Image selected = new Image(String.format("res/Selected/%s_Selected.png", this.name));
    int[] stats = new int[2];

    int timer = 120;
   final double speed = 1 + GameSettingsSingleton.getInstance().getMapSpeed();

    boolean shield = false;
    boolean speedUp = false;
    boolean speedDown = false;
    int speedDownTimer = 0;
    int speedUpTimer = 0;
    boolean minimised = false;
    int minimisedTimer = 0;
    boolean noblePhantasm = false;
    boolean gojoAbility = false;
    boolean hisokaAbility = false;
    private int hisokaTimer = 0;
    boolean jotaroAbility = false;
    boolean yugiAbility = false;
    private double stunTime = 0;


    public Image getIcon() { return this.icon;}
    public Image getImage() { return this.image;}
    public String getName() { return this.name;}

    public void move(String key) {
        if (stunTime > 0) {
            stunTime --;
        }
        else {
            if (!this.jotaroAbility) {
                double currentSpeed = this.speed;
                if (this.yugiAbility) {
                    currentSpeed = this.speed * 1.25;
                }
                if (this.hisokaAbility) {
                    if (this.hisokaTimer < 2 * 144) {
                        this.hisokaTimer++;
                    } else {
                        this.hisokaAbility = false;
                        this.hisokaTimer = 0;
                    }
                }
                if (this.speedUp) {
                    currentSpeed = 2 * this.speed;
                } else if (this.speedDown) {
                    currentSpeed = 0.5 * this.speed;
                }
                if (this.gojoAbility) {
                    currentSpeed = 0.5 * this.speed;
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
                    if (!this.hisokaAbility) {
                        this.pos = new Point(new_X, new_Y);
                    }
                }
            }
        }
    }
    public void draw() {
        Image picture;
        if (this.speedUp) {
            if (this.speedUpTimer > 0) {
                this.speedUpTimer --;
            }
            else if (this.speedUpTimer <= 0) {
                this.speedUp = false;
            }
        }
        if (this.speedDown) {
            if (this.speedDownTimer > 0) {
                this.speedDownTimer --;
            }
            else if (this.speedDownTimer <= 0) {
                this.speedDown = false;
            }
        }
        picture = new Image(String.format("res/characters/%s/%s_Left.png", this.name, this.name));
        if (this.timer < 60) {
            picture = new Image(String.format("res/characters/%s/%s_Left.png", this.name, this.name));
        }
        else if (this.timer > 60) {
            picture  = new Image(String.format("res/characters/%s/%s_Right.png", this.name, this.name));
        }
        if (minimisedTimer < 0 ){
            minimised = false;
        }
        else {
            minimisedTimer--;
        }
        if (this.timer <= 0) {
            this.timer = 120;
        }
        this.timer --;
        this.image = picture;
        if (minimised) {
            picture.draw(this.pos.x, this.pos.y, new DrawOptions().setScale(0.5, 0.5));
        }
        else {
            picture.draw(this.pos.x, this.pos.y);
        }
        if (this.shield) {
            Image bubble = new Image("res/bubble.png");
            bubble.draw(this.pos.x, this.pos.y);
        }
    }

    public void setPosition(Point point) { this.pos = point;}
    public Point getPos() { return this.pos;}
    public void setPlayer(Player player) { this.player = player;}

    public Player getPlayer() {
        return this.player;
    }
    public void decreaseMinimisedTimer() {
        if (minimisedTimer > 0) {
            this.minimisedTimer--;}
        else {
            minimised = false;}
    }
    public void popShield() { this.shield = false;}
    public boolean hasShield() { return this.shield;}

    public void getPowerUp(PowerUp powerUp){
        if (powerUp.getName().equals("Shield")) {
            if (!this.shield) {
                this.shield = true;
            }
        }
        else if (powerUp.getName().equals("Minimiser")) {
            this.minimised = true;
            this.minimisedTimer = 420;
        }
        else if (powerUp.getName().equals("SpeedUp")) {
            this.speedUp = true;
            this.speedUpTimer = 420;
        }
        else if (powerUp.getName().equals("SpeedDown")) {
            this.speedDown = true;
            this.speedDownTimer = 420;
        }
        else if (powerUp.getName().equals("NoblePhantasm")) {
            this.noblePhantasm = true;
        }
    }

    public void resetTimer() {
        this.minimisedTimer = 0;
        this.minimised = false;
        this.speedUpTimer = 0;
        this.speedUp = false;
        this.speedDown = false;
        this.speedDownTimer = 0;
        this.shield = false;
        this.noblePhantasm = false;
        this.gojoAbility = false;
        this.hisokaAbility = false;
        this.jotaroAbility = false;
        this.stunTime = 0;
    }
    public String playLine() {
        return this. soundPath;
    }

    public Image getSelected() { return this.selected;}
    public Point getIconPos() {return this.iconPos;}
    public void setIconPos(Point point) { this.iconPos = point;}

    public void setStats() {
        String line;
        String[] lineSplit;
        try {
            Scanner scanner = new Scanner(new File("stats/Stats.txt"));
            while(scanner.hasNextLine()) {
                line = scanner.nextLine();
                if (line.equals(this.name)) {
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
    public int[] getStats() { return this.stats;}
    public void updateStats(boolean played, boolean won) {
        if (played) {
            this.stats[0] ++;
        }
        if (won) {
            this.stats[1] ++;
        }
    }
    public void onIce() {
        this.speedUp = true;
        this.speedUpTimer = 2;
    }
    public void onSlow() {
        this.speedDown = true;
        this.speedDownTimer = 2;
    }
    public int getMinimisedTimer() {
        return this.minimisedTimer;
    }
    public int getSpeedDownTimer() {
        return this.speedDownTimer;
    }
    public int getSpeedUpTimer() {
        return this.speedUpTimer;
    }

    public boolean hasNoblePhantasm() {return this.noblePhantasm;}
    public void useNoblePhantasm() {
        this.noblePhantasm = false;
    }
    public void setGojoAbility(boolean bool) {
        this.gojoAbility = bool;
    }
    public void setAllMightAbility() {
        this.speedUp = true;
        this.speedDownTimer = 0;
        this.speedDown = false;
        this.shield = true;
        this.hisokaAbility = false;
        this.gojoAbility = false;
        this.jotaroAbility = false;
    }
    public void setHisokaAbility(boolean bool) { this.hisokaAbility = bool;}
    public void setJotaroAbility(boolean bool) { this.jotaroAbility = bool;}
    public void setYugiAbility(boolean bool) { this.yugiAbility = bool;}
    public boolean isMinimised() {return this.minimised;}
    public void gotStunned() {stunTime = 2*frames;}
}