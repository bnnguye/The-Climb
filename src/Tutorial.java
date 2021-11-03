import bagel.Image;
import bagel.Window;
import bagel.util.Point;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Tutorial extends Character{
    Player player;
    String name = "Tutorial";
    Image icon = new Image("res/icons/Mai.png");
    Point iconPos;
    Image image;
    Image selected = new Image(String.format("res/Selected/%s_Selected.png", this.name));
    Point pos;
    int[] stats = new int[2];

    Music music = new Music();
    int timer = 120;
    final double speed = 2;

    boolean shield = false;
    boolean bullet = false;
    int ammo = 0;
    boolean speedUp = false;
    int speedUpTimer = 0;
    boolean minimised = false;
    int minimisedTimer = 0;
    boolean noblePhantasm = false;

    public Image getIcon() { return this.icon;}
    public Image getImage() { return this.image;}
    public String getName() { return this.name;}

    public void move(String key) {
        if (key.equals("Space")) {
            if (this.noblePhantasm) {
                activateNoblePhantasm();
            }
        }
        double currentSpeed = this.speed;
        if (this.speedUp) {
            currentSpeed = 2 * this.speed;
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
            this.pos = new Point(new_X, new_Y);
        }
    }

    public void draw() {
        Image picture;
        if (this.speedUp) {
            this.speedUpTimer --;
            if (this.speedUpTimer < 0) {
                this.speedUp = false;
            }
        }
        if (!this.minimised) {
            picture = new Image(String.format("res/characters/%s/%s_Left.png", this.name, this.name));
            if (this.timer < 60) {
                picture = new Image(String.format("res/characters/%s/%s_Left.png", this.name, this.name));
            }
            else if (this.timer > 60) {
                picture  = new Image(String.format("res/characters/%s/%s_Right.png", this.name, this.name));
            }
        }
        else {
            this.minimisedTimer--;
            picture = new Image(String.format("res/characters/%s/%s_Left_Mini.png", this.name, this.name));
            if (this.timer < 60) {
                picture = new Image(String.format("res/characters/%s/%s_Left_Mini.png", this.name, this.name));
            }
            else if (this.timer > 60) {
                picture  = new Image(String.format("res/characters/%s/%s_Right_Mini.png", this.name, this.name));
            }
        }
        if (minimisedTimer < 0 ){
            minimised = false;
        }
        if (this.timer <= 0) {
            this.timer = 120;
        }
        this.timer --;
        this.image = picture;
        picture.draw(this.pos.x, this.pos.y);
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

    public void activateNoblePhantasm() {
        getPowerUp(new Shield());
        getPowerUp(new SpeedUp());
        getPowerUp(new Minimiser());
    }

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
            music.playMusic("music/SpeedUp.wav");
        }
        else if (powerUp.getName().equals("NoblePhantasm")) {
            activateNoblePhantasm();
        }
    }

    public void resetTimer() {
        this.minimisedTimer = 0;
        this.minimised = false;
        this.speedUpTimer = 0;
        this.speedUp = false;
        this.shield = false;
    }

    public void playLine() {
        this.music.playMusic(String.format("music/%s.wav", this.name));
        this.music.played = true;
    }
    public void stopMusic() {
        if (this.music.played) {
            this.music.stopMusic();
        }
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
}
