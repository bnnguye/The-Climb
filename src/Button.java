import bagel.*;
import bagel.util.Colour;
import bagel.util.Rectangle;
import bagel.util.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Button {

    private ArrayList<Button> buttons = ButtonsSingleton.getInstance().getButtons();
    private ArrayList<Button> buttonsToRemove = ButtonsSingleton.getInstance().getButtonsToRemove();
    private ArrayList<Slider> sliders = ButtonsSingleton.getInstance().getSliders();
    private ArrayList<Slider> slidersToRemove = ButtonsSingleton.getInstance().getSlidersToRemove();

    private int FONT_SIZE;
    private final DrawOptions DO = new DrawOptions();
    private Font font;
    private final Music music = new Music();

    private final String name;
    private final String displayString;
    private final Rectangle box;
    private final Point position;
    private Image image = null;

    private boolean hovering = false;
    private boolean night = false;
    public Colour white = new Colour(1,1,1);
    public Colour whiteTranslucent = new Colour(1,1,1, 0.5);
    public Colour black = new Colour(0,0,0);
    public Colour blackTranslucent = new Colour(0,0,0, 0.5);


    public Button(String name, String displayString, int fontSize, double width, double length, Point topLeft) {
        this.name = name;
        this.displayString = displayString;
        this.box = new Rectangle(topLeft, width, length);
        this.position = topLeft;
        DO.setBlendColour(whiteTranslucent);
        FONT_SIZE = fontSize;
    }
    // Constructor for Strings/Buttons without images
    public Button(String name, int fontSize, double width, double length, Point topLeft)  {
        this.name = name;
        this.displayString = name;
        this.box = new Rectangle(topLeft, width, length);
        this.position = topLeft;
        FONT_SIZE = fontSize;
        font = new Font("res/fonts/DejaVuSans-Bold.ttf", FONT_SIZE);
        DO.setBlendColour(whiteTranslucent);
    }

    // Constructor for Images
    public Button(String name, Image image, Point topLeft) {
        this.name = name;
        this.displayString = name;
        this.box = image.getBoundingBoxAt(new Point(topLeft.x + image.getWidth()/2, topLeft.y + image.getHeight()/2));
        this.position = topLeft;
        this.image = image;
        DO.setBlendColour(whiteTranslucent);
    }

    public void toggleHover(Point point) {
        if (box.intersects(point)) {
            if (!hovering) {
                music.playMusic("music/Hover.wav");
            }

            if (night) {
                DO.setBlendColour(black);
            }
            else {
                DO.setBlendColour(white);
            }
            hovering = true;
        } else {
            if (night) {
                DO.setBlendColour(blackTranslucent);
            }
            else {
                DO.setBlendColour(whiteTranslucent);
            }
            hovering = false;
        }
    }

    public void draw() {
        if (image != null) {
            // need to adjust as boundingBoxAt takes centre, not topLeft
            image.drawFromTopLeft(position.x, position.y);
        } else {
            font.drawString(displayString, position.x, position.y + (box.bottom() - box.top()), DO);
        }
    }

    public Image getImage() {return image;}

    public boolean isHovering() {return hovering;}
    public void playAction() {
        if (name.equalsIgnoreCase("Back To Start")) {
            SettingsSingleton.getInstance().setGameStateString("Menu");
        }
        else if (name.equalsIgnoreCase("Back")) {
            SettingsSingleton.getInstance().setGameState(1);
        }
        else if (name.equalsIgnoreCase("Create Map")) {
            SettingsSingleton.getInstance().setGameState(9);
        }
        else if (name.equalsIgnoreCase("Exit")) {
            Window.close();
        }
        else if (name.equalsIgnoreCase("4")) {
            SettingsSingleton.getInstance().setPlayers(4);
            SettingsSingleton.getInstance().setGameState(3);
        }
        else if (name.equalsIgnoreCase("Game Settings")) {
            SettingsSingleton.getInstance().setGameState(10);
        }
        else if (name.equalsIgnoreCase("Left Arrow")) {
            if (GameSettingsSingleton.getInstance().getPage() > 0) {
                GameSettingsSingleton.getInstance().setPage(GameSettingsSingleton.getInstance().getPage() - 1);
                ButtonsSingleton.getInstance().getButtons().clear();
            }
        }
        else if (name.equalsIgnoreCase("PLAY")) {
                SettingsSingleton.getInstance().setGameState(1);
        }
        else if (name.equalsIgnoreCase("Retry")) {
                if (SettingsSingleton.getInstance().getGameMode() < 99) {
                    SettingsSingleton.getInstance().setGameStateString("Retry");
                }
                else {
                    SettingsSingleton.getInstance().setGameStateString("Continue");
                }
        }
        else if (name.equalsIgnoreCase("Right Arrow")) {
                if (GameSettingsSingleton.getInstance().getPage() < 2) {
                    GameSettingsSingleton.getInstance().setPage(GameSettingsSingleton.getInstance().getPage() + 1);
                    buttonsToRemove.addAll(buttons);
                }
                if (GameSettingsSingleton.getInstance().getPage() == 1) {
                    Collections.addAll(
                            (new ArrayList<>(Arrays.asList(
                            new Slider("Minimiser", "PowerUp", new Point(400, 300)),
                            new Slider("SpeedUp", "PowerUp", new Point(400, 400)),
                            new Slider("SpeedDown", "PowerUp", new Point(400, 500)),
                            new Slider("Shield", "PowerUp", new Point(400, 600)),
                            new Slider("SpecialAbilityPoints", "PowerUp", new Point(400, 700))))),
                            sliders);
                }
        }
        else if (name.equalsIgnoreCase("Story")) {
            SettingsSingleton.getInstance().setGameMode(0);
            SettingsSingleton.getInstance().setGameStateString("STORY");
        }
        else if (name.equalsIgnoreCase("3")) {
            SettingsSingleton.getInstance().setPlayers(3); SettingsSingleton.getInstance().setGameState(3);
        }
        else if (name.equalsIgnoreCase("2")) {
            SettingsSingleton.getInstance().setGameState(3);
        }
        else if (name.equalsIgnoreCase("VS")) {
            SettingsSingleton.getInstance().setGameMode(1);
            SettingsSingleton.getInstance().setGameStateString("VS");
        }
        else if (name.equalsIgnoreCase("Decrease Map Speed")) {
            GameSettingsSingleton.getInstance().setMapSpeed(GameSettingsSingleton.getInstance().getMapSpeed() - 0.1);
        }
        else if (name.equalsIgnoreCase("Increase Map Speed")) {
            GameSettingsSingleton.getInstance().setMapSpeed(GameSettingsSingleton.getInstance().getMapSpeed() + 0.1);
        }
    }

    public String getName() {
        return name;
    }
    public void setNight() {
        night = true;
    }
}
