import bagel.Drawing;
import bagel.Image;
import bagel.Window;
import bagel.util.Colour;
import bagel.util.Point;

import java.util.ArrayList;

public class SideYugi extends SideCharacter{
    private final double frames = SettingsSingleton.getInstance().getRefreshRate();

    private String name = "YAMI YUGI";
    private String power = "EXODIA";
    private String desc = "";

    boolean activating = false;
    double timer;

    ArrayList<ExodiaPiece> exodiaPieces = new ArrayList<>();
    ArrayList<ExodiaPiece> exodiaPiecesCollected = new ArrayList<>();

    public String getName() {
        return this.name;
    }
    public String getPower() { return this.power;}
    public String getDesc() { return this.desc;}

    public boolean isActivating() {return this.activating;}
    public boolean isAnimating() {
        return this.animating;
    }
    public void reset() {
        this.activating = false;
        this.animating = false;
        timer = 0;
    }


    public void activateAbility(Player user, ArrayList<Player> players, ArrayList<Obstacle> obstacles, ArrayList<PowerUp> powerUps, Map map) {
        if(!this.activating) {
            MusicPlayer.getInstance().addMusic("music/Yugi.wav");
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
                    MusicPlayer.getInstance().addMusic("music/Exodia.wav");
                }
            }
            if (this.animating) {
                if (timer == 0) {
                    for (Player player: players) {
                        if (player.getId() != user.getId()) {
                            if (!player.getCharacter().isDead()) {
                                player.getCharacter().setDead(true);
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

    public ArrayList<ExodiaPiece> getExodiaPiecesCollected() {return this.exodiaPiecesCollected;}

    public void renderAbility() {
        for (ExodiaPiece exodiaPiece : exodiaPieces) {
            exodiaPiece.getImage().draw(exodiaPiece.getPos().x, exodiaPiece.getPos().y);
        }
        if (animating) {
            Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), new Colour(0, 0, 0, 0.9));
            if (timer > 24 * frames) {
                Image yugi = new Image("res/sidecharacters/Yugi/Yugi.png");
                yugi.drawFromTopLeft(0, 0);
            } else if (timer > 6 * frames) {
                Image head = new Image("res/sidecharacters/Yugi/HeadImage.png");
                Image leftArm = new Image("res/sidecharacters/Yugi/LeftArmImage.png");
                Image rightArm = new Image("res/sidecharacters/Yugi/RightArmImage.png");
                Image leftLeg = new Image("res/sidecharacters/Yugi/LeftLegImage.png");
                Image rightLeg = new Image("res/sidecharacters/Yugi/RightLegImage.png");
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
                Image yugi = new Image("res/renders/SideCharacters/Yugi.png");
                Image exodia = new Image("res/sidecharacters/Yugi/Exodia.png");
                exodia.drawFromTopLeft(0, 0);
                yugi.drawFromTopLeft(0, 0);
            }
        }
    }
}
