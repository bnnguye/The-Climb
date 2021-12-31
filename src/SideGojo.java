import bagel.Drawing;
import bagel.Font;
import bagel.Image;
import bagel.Window;
import bagel.util.Colour;
import bagel.util.Point;

import java.util.ArrayList;

public class SideGojo extends SideCharacter{
    private final double frames = SettingsSingleton.getInstance().getFrames();
    String name = "Gojo";
    String soundPath = String.format("music/%s.wav", this.name);
    Image icon = new Image(String.format("res/charactersS/%s/Icon.PNG", this.name));
    boolean activating = false;
    double timer;
    Image selected = new Image(String.format("res/Selected/%s_Selected.png", this.name));

    int radius = 0;



    public String getName() {
        return this.name;
    }
    public Image getIcon() {return this.icon;}
    public void setIconPos(Point point) {this.iconPos = point;}
    public Point getIconPos() {return this.iconPos;}
    public Image getSelected() {return this.selected;}
    public boolean isActivating() {return this.activating;}
    public void reset() {
        this.activating = false;
        this.animating = false;
        this.timer = 0;
        this.radius = 0;
    }
    public boolean isAnimating() {
        return this.animating;
    }
    public String playLine() {return this.soundPath;}


    public void activateAbility(Player user, ArrayList<Player> players, ArrayList<Obstacle> obstacles, ArrayList<PowerUp> powerUps, Map map) {
        if(!this.activating) {
            this.activating = true;
            this.timer = 8 * frames;
        }

        if (this.timer > 6*frames) {
            this.animating = true;
            if (radius < Window.getWidth()/1.5) {
                radius += 2;
            }
            Drawing.drawRectangle(0,0,Window.getWidth(),Window.getHeight(), new Colour(0,0,0,0.9));
            Image gojo = new Image("res/charactersS/Gojo/NoblePhantasm2.png");
            gojo.drawFromTopLeft(0,0);
            Font font = new Font("res/fonts/DejaVuSans-Bold.ttf", 160);
            //font.drawString("DOMAIN EXPANSION", Window.getWidth()/12, Window.getHeight()*0.8);
        }
        else {
            this.animating = false;
            if (radius < Window.getWidth()/1.5) {
                Drawing.drawCircle(user.getCharacter().getPos(), radius, new Colour(0,0,0));
                radius += 3;
            }
            else {
                Drawing.drawRectangle(0,0,Window.getWidth(),Window.getHeight(), new Colour(0,0,0,0.9));
                Drawing.drawCircle(new Point(Window.getWidth()/2, Window.getHeight()/2), 50, new Colour(1, 1, 1, 1));
                if (timer % 10 == 0) {
                    Drawing.drawCircle(new Point(Window.getWidth()/2, Window.getHeight()/2), 600, new Colour(0.5, 0, 0, 0.4));
                }
                else if (timer % 14 == 0) {
                    Drawing.drawCircle(new Point(Window.getWidth()/2, Window.getHeight()/2), 400, new Colour(0, 0, 0.5, 0.6));
                }
                else if (timer % 16 == 0) {
                    Drawing.drawCircle(new Point(Window.getWidth()/2, Window.getHeight()/2), 200, new Colour(0.5, 0, 0.5, 0.7));
                }
                else if (timer % 17 == 0) {
                    Drawing.drawCircle(new Point(Window.getWidth()/2, Window.getHeight()/2), 100, new Colour(0.5, 0, 0.5, 0.9));
                }
                warp(players, user);
            }
            for (Player player: players) {
                if (player.getId() != user.getId()) {
                    if (player.getCharacter().getPos().distanceTo(new Point(Window.getWidth()/2, Window.getHeight()/2)) < 100){
                        if (!player.isDead()) {
                            player.setDead();
                        }
                    }
                }
            }
            for (Obstacle obstacle: obstacles) {
                obstacle.setGojoAbility(true);
            }
        }
        this.timer--;
        if (this.timer <= 0) {
            this.activating = false;
            for (Obstacle obstacle: obstacles) {
                obstacle.setGojoAbility(false);
            }
            this.radius = 0;
        }
    }
    public void warp(ArrayList<Player> players, Player user) {
        double magneticPower = 3;
        for (Player player: players) {
            if (user.getId() != player.getId()) {
                if (player.getCharacter().getPos().x < Window.getWidth()/2) {
                    player.getCharacter().setPosition(new Point(player.getCharacter().getPos().x + magneticPower * (player.getCharacter().getPos().x/Window.getWidth()/2) , player.getCharacter().getPos().y));
                }
                else {
                    player.getCharacter().setPosition(new Point(player.getCharacter().getPos().x - magneticPower * (player.getCharacter().getPos().x/Window.getWidth()/2), player.getCharacter().getPos().y));
                }
                if (player.getCharacter().getPos().y < Window.getHeight()/2) {
                    player.getCharacter().setPosition(new Point(player.getCharacter().getPos().x, player.getCharacter().getPos().y + magneticPower * (player.getCharacter().getPos().y/Window.getHeight()/2)));
                }
                else {
                    player.getCharacter().setPosition(new Point(player.getCharacter().getPos().x, player.getCharacter().getPos().y - magneticPower *(player.getCharacter().getPos().y/Window.getHeight()/2)));
                }
            }
        }
    }
}
