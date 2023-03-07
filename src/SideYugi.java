import bagel.DrawOptions;
import bagel.Drawing;
import bagel.Image;
import bagel.Window;
import bagel.util.Colour;
import bagel.util.Point;

import java.util.ArrayList;

public class SideYugi extends SideCharacter{
    private final double frames = SettingsSingleton.getInstance().getRefreshRate();

    private String name = CharacterNames.YUGI;
    private String power = "EXODIA";
    private String desc = "Yugi's deck was given to him by his grandpa, which has\n" +
            "no bad cards. Specifically, it contains the (5) pieces of Exodia.\n" +
            "At the start of the game, Yugi transforms, and thus starts\n" +
            "finding the pieces of Exodia. When all 5 are in his hands,\n" +
            "Exodia is summoned and obliterates his opponents!";

    boolean activating = false;
    double timer;


    ArrayList<ExodiaPiece> exodiaPieces = new ArrayList<>();
    ArrayList<ExodiaPiece> exodiaPiecesCollected = new ArrayList<>();

    public String getName() {
        return this.name;
    }
    public String getPower() { return this.power;}
    public String getDesc() { return this.desc;}
    public String getSoundPath() {return String.format("music/sidecharacters/%s/%s.wav", this.name, this.name);}

    public boolean isActivating() {return this.activating;}
    public boolean isAnimating() {
        return this.animating;
    }
    public void reset() {
        this.activating = false;
        this.animating = false;
        timer = 0;
    }


    public void activateAbility(Player user, ArrayList<Obstacle> obstacles, ArrayList<PowerUp> powerUps) {
        Map map = GameSettingsSingleton.getInstance().getMap();
        ArrayList<Player> players = SettingsSingleton.getInstance().getPlayers();
        if(!this.activating) {
            this.timer = 3 * frames;
            MusicPlayer.getInstance().addMusic("music/sidecharacters/yami yugi/Yugi Transformation.wav");
            this.activating = true;
            this.animating = true;
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
            for (ExodiaPiece exodiaPiece : exodiaPieces) {
                exodiaPiece.move();
                if (exodiaPiece.getImage().getBoundingBoxAt(new Point(exodiaPiece.getPos().x - exodiaPiece.getImage().getWidth()/2, exodiaPiece.getPos().y - exodiaPiece.getImage().getHeight()/2)).intersects(user.getCharacter().getImage().getBoundingBoxAt(user.getCharacter().getPos()))) {
                    exodiaPiecesToRemove.add(exodiaPiece);
                    exodiaPiecesCollected.add(exodiaPiece);
                }
                for (Player player : players) {
                    if (player.getId() != user.getId()) {
                        if (exodiaPiece.getImage().getBoundingBoxAt(new Point(exodiaPiece.getPos().x - exodiaPiece.getImage().getWidth()/2, exodiaPiece.getPos().y - exodiaPiece.getImage().getHeight()/2)).intersects(player.getCharacter().getImage().getBoundingBoxAt(player.getCharacter().getPos()))) {
                            exodiaPiece.setPos(new Point(exodiaPiece.getPos().x, Window.getHeight() + 200));
                        }
                    }
                }
                if (exodiaPiece.getPos().y > Window.getHeight()) {
                    exodiaPiece.setPos(new Point(exodiaPiece.getPos().x, -2*Window.getHeight()));
                }
            }
            exodiaPieces.removeAll(exodiaPiecesToRemove);
            if (exodiaPieces.size() == 0) {
                if (!this.animating) {
                    this.animating = true;
                    timer = 18 * frames;
                    MusicPlayer.getInstance().addMusic("music/sidecharacters/YAMI YUGI/Exodia.wav");
                }
                if (timer == 0) {
                    for (Player player : players) {
                        if (player.getId() != user.getId()) {
                            if (!player.getCharacter().isDead()) {
                                player.getCharacter().setLives(0);
                            }
                        }
                    }
                    this.animating = false;
                    this.activating = false;
                }
            }
        }

        if (timer < 0) {
            animating = false;
        }
        timer--;
    }

    public ArrayList<ExodiaPiece> getExodiaPiecesCollected() {return this.exodiaPiecesCollected;}

    public void renderAbility() {
        for (ExodiaPiece exodiaPiece : exodiaPieces) {
            exodiaPiece.getImage().draw(exodiaPiece.getPos().x, exodiaPiece.getPos().y);
        }
        Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), new Colour(0, 0, 0, (5 - exodiaPieces.size()) * 0.03));
        if (animating) {
            if (exodiaPiecesCollected.size() != 5) {
                Image moto = new Image("res/sidecharacters/YAMI YUGI/Moto.png");
                Image yami = new Image("res/sidecharacters/YAMI YUGI/Yami.png");
                Drawing.drawRectangle(0,0,Window.getWidth(), Window.getHeight(), new Colour(0,0,0,0.8));
                moto.draw(Window.getWidth()/2, Window.getHeight() - moto.getHeight()/2, new DrawOptions().setBlendColour(1,1,1,timer/(2*(float)frames)));
                yami.draw(Window.getWidth()/2, Window.getHeight() - moto.getHeight()/2, new DrawOptions().setBlendColour(1,1,1,1.0 - (timer/(2*(float)frames))));
            }
            else {

                Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), new Colour(0, 0, 0, 0.9));
                if (timer > 14 * frames) {
                    Image yugi = new Image("res/sidecharacters/YAMI YUGI/Yugi.png");
                    yugi.drawFromTopLeft(0, 0);
                } else if (timer > 6 * frames) {
                    Image head = new Image("res/sidecharacters/YAMI YUGI/HeadImage.png");
                    Image leftArm = new Image("res/sidecharacters/YAMI YUGI/LeftArmImage.png");
                    Image rightArm = new Image("res/sidecharacters/YAMI YUGI/RightArmImage.png");
                    Image leftLeg = new Image("res/sidecharacters/YAMI YUGI/LeftLegImage.png");
                    Image rightLeg = new Image("res/sidecharacters/YAMI YUGI/RightLegImage.png");
                    head.drawFromTopLeft(Window.getWidth() / 2 - head.getWidth() / 2, 0);
                    if (timer < 13 * frames) {
                        leftArm.drawFromTopLeft(Window.getWidth() / 2 + head.getWidth() / 2, 0);
                    }
                    if (timer < 12 * frames) {
                        rightArm.drawFromTopLeft(Window.getWidth() / 2 - head.getWidth() * 1.5, 0);
                    }
                    if (timer < 11 * frames) {
                        leftLeg.drawFromTopLeft(Window.getWidth() / 2, head.getHeight());
                    }
                    if (timer < 10 * frames) {
                        rightLeg.drawFromTopLeft(Window.getWidth() / 2 - rightLeg.getWidth(), head.getHeight());
                    }
                } else if (timer > 1 * frames) {
                    Image yugi = new Image("res/SideCharacters/YAMI YUGI/Yugi.png");
                    Image exodia = new Image("res/sidecharacters/YAMI YUGI/Exodia.png");
                    exodia.drawFromTopLeft(0, 0);
                    yugi.drawFromTopLeft(0, 0);
                }
            }
        }
    }
}
