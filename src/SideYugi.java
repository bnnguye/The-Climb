import bagel.Drawing;
import bagel.Font;
import bagel.Image;
import bagel.Window;
import bagel.util.Colour;
import bagel.util.Point;

import java.util.ArrayList;

public class SideYugi extends SideCharacter{
    private final double frames = SettingsSingleton.getInstance().getFrames();
    private String name = "Yugi";
    private String soundPath = String.format("music/%s.wav", this.name);
    Image icon = new Image(String.format("res/charactersS/Yugi/Icon.PNG", this.name));
    boolean activating = false;
    double timer;
    int radius = 0;
    Music music = new Music();
    Music musicEnd = new Music();
    private Image selected = new Image(String.format("res/charactersS/%s/Selected.png", this.name));
    ArrayList<ExodiaPiece> exodiaPieces = new ArrayList<>();
    ArrayList<ExodiaPiece> exodiaPiecesCollected = new ArrayList<>();
    private Point iconPos;


    public String getName() {
        return this.name;
    }

    public Image getIcon() {return this.icon;}
    public void setIconPos(Point point) {this.iconPos = point;}
    public Point getIconPos() {return this.iconPos;}
    public Image getSelected() {return this.selected;}
    public boolean isActivating() {return this.activating;}
    public String playLine() {return this.soundPath;}

    public void activateAbility(Player user, ArrayList<Player> players, ArrayList<Obstacle> obstacles, ArrayList<PowerUp> powerUps, Map map) {
        if(!this.activating) {
            this.music.playMusic("music/Yugi.wav");
            this.activating = true;
            exodiaPieces.clear();
            exodiaPiecesCollected.clear();
            exodiaPieces.add(new ExodiaPiece("Head",new Point(Math.random()*Window.getWidth(), - Window.getHeight() - map.getCurrentHeight() - Math.random()*(map.getHeight() - map.getCurrentHeight()))));
            exodiaPieces.add(new ExodiaPiece("LeftArm", new Point(Math.random()*Window.getWidth(), - Window.getHeight() - map.getCurrentHeight() - Math.random()*(map.getHeight() - map.getCurrentHeight()))));
            exodiaPieces.add(new ExodiaPiece("RightArm", new Point(Math.random()*Window.getWidth(), - Window.getHeight() - map.getCurrentHeight() - Math.random()*(map.getHeight() - map.getCurrentHeight()))));
            exodiaPieces.add(new ExodiaPiece("LeftLeg", new Point(Math.random()*Window.getWidth(), - Window.getHeight() - map.getCurrentHeight() - Math.random()*(map.getHeight() - map.getCurrentHeight()))));
            exodiaPieces.add(new ExodiaPiece("RightLeg", new Point(Math.random()*Window.getWidth(), - Window.getHeight() - map.getCurrentHeight() - Math.random()*(map.getHeight() - map.getCurrentHeight()))));
            exodiaPiecesCollected = new ArrayList<>();
        }
        else {
            ArrayList<ExodiaPiece> exodiaPiecesToRemove = new ArrayList<>();
            Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), new Colour(0, 0, 0, (5 - exodiaPieces.size())*0.03));
            for (ExodiaPiece exodiaPiece: exodiaPieces) {
                exodiaPiece.move();
                if (exodiaPiece.getImage().getBoundingBoxAt(exodiaPiece.getPos()).intersects(user.getCharacter().getImage().getBoundingBoxAt(user.getCharacter().getPos()))) {
                    exodiaPiecesToRemove.add(exodiaPiece);
                    exodiaPiecesCollected.add(exodiaPiece);
                }
                for (Player player: players) {
                    if (player.getId() != user.getId()) {
                        if (exodiaPiece.getImage().getBoundingBoxAt(exodiaPiece.getPos()).intersects(player.getCharacter().getImage().getBoundingBoxAt(player.getCharacter().getPos()))) {
                            exodiaPiece.setPos(new Point( exodiaPiece.getPos().x, Window.getHeight()+ 200));
                        }
                    }
                }
                if (exodiaPiece.getPos().y > Window.getHeight()) {
                    exodiaPiece.setPos(new Point(exodiaPiece.getPos().x, - Window.getHeight() - map.getCurrentHeight() - Math.random()*(map.getHeight() - map.getCurrentHeight())));
                }
            }
            exodiaPieces.removeAll(exodiaPiecesToRemove);
            if (exodiaPieces.size() == 0) {
                if(!this.animating) {
                    this.animating = true;
                    timer = 31 * frames;
                    musicEnd.playMusic("music/Exodia.wav");
                }
            }
            if (this.animating) {
                if (timer == 0) {
                    for (Player player: players) {
                        if (player.getId() != user.getId()) {
                            if (!player.isDead()) {
                                player.setDead();
                            }
                        }
                    }
                    this.animating = false;
                    this.activating = false;
                }
                timer--;
            }
        }
    }

    public void reset() {
        this.activating = false;
        this.animating = false;
        this.timer = 0;
        this.radius = 0;
    }


    public boolean isAnimating() {
        return this.animating;
    }


    public ArrayList<ExodiaPiece> getExodiaPiecesCollected() {return this.exodiaPiecesCollected;}

    public void renderAbility() {
        for (ExodiaPiece exodiaPiece : exodiaPieces) {
            exodiaPiece.getImage().draw(exodiaPiece.getPos().x, exodiaPiece.getPos().y);
        }
        if (animating) {
            Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), new Colour(0, 0, 0, 0.9));
            if (timer > 24 * frames) {
                Image yugi = new Image("res/charactersS/Yugi/Yugi.png");
                yugi.drawFromTopLeft(0, 0);
            } else if (timer > 6 * frames) {
                Image head = new Image("res/charactersS/Yugi/HeadImage.png");
                Image leftArm = new Image("res/charactersS/Yugi/LeftArmImage.png");
                Image rightArm = new Image("res/charactersS/Yugi/RightArmImage.png");
                Image leftLeg = new Image("res/charactersS/Yugi/LeftLegImage.png");
                Image rightLeg = new Image("res/charactersS/Yugi/RightLegImage.png");
                head.drawFromTopLeft(Window.getWidth() / 2 - head.getWidth() / 2, 0);
                if (timer < 20 * frames) {
                    leftArm.drawFromTopLeft(Window.getWidth() / 2 + head.getWidth() / 2, 0);
                }
                if (timer < 16 * frames) {
                    rightArm.drawFromTopLeft(Window.getWidth() / 2 - head.getWidth() * 1.5, 0);
                }
                if (timer < 12 * frames) {
                    leftLeg.drawFromTopLeft(Window.getWidth() / 2, head.getHeight());
                }
                if (timer < 8 * frames) {
                    rightLeg.drawFromTopLeft(Window.getWidth() / 2 - rightLeg.getWidth(), head.getHeight());
                }
            } else if (timer > 1 * frames) {
                Image yugi = new Image("res/Renders/Yugi.png");
                Image exodia = new Image("res/charactersS/Yugi/NoblePhantasm.png");
                exodia.drawFromTopLeft(0, 0);
                yugi.drawFromTopLeft(0, 0);
            }
        }
    }
    public String getSoundPath() {return soundPath;}
}
