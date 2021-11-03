import bagel.Drawing;
import bagel.Font;
import bagel.Image;
import bagel.Window;
import bagel.util.Colour;
import bagel.util.Point;

import java.util.ArrayList;

public class SideGojo extends SideCharacter{
    private final int frames = 144;
    String name = "Gojo";
    Image icon = new Image(String.format("res/charactersS/Gojo/Icon.PNG", this.name));
    boolean activating = false;
    int timer = 0;
    int radius = 0;
    Music music = new Music();
    Image selected = new Image(String.format("res/Selected/%s_Selected.png", this.name));


    public String getName() {
        return this.name;
    }

    public Image getIcon() {return this.icon;}
    public void setIconPos(Point point) {this.iconPos = point;}
    public Point getIconPos() {return this.iconPos;}
    public Image getSelected() {return this.selected;}
    public boolean isActivating() {return this.activating;}

    public void activateAbility(Player user,ArrayList<Player> players, ArrayList<Obstacle> obstacles) {
        if(!this.activating) {
            this.music.playMusic("music/Gojo.wav");
            this.music.played = true;
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
            stopMusic();
            this.music.played = false;
            for (Obstacle obstacle: obstacles) {
                obstacle.setGojoAbility(false);
            }
            this.radius = 0;
        }
    }

    public void reset() {
        this.activating = false;
        this.animating = false;
        stopMusic();
        this.timer = 0;
        this.radius = 0;
    }

    public void stopMusic() {
        if (this.music.played) {
            this.music.stopMusic();
        }
    }

    public boolean isAnimating() {
        return this.animating;
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
