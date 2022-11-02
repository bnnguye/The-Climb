import bagel.*;
import bagel.util.Colour;
import bagel.util.Rectangle;
import bagel.util.Point;

import java.util.ArrayList;
import java.util.Arrays;

public class Button {

    private ArrayList<Button> buttons = ButtonsSingleton.getInstance().getButtons();
    private ArrayList<Button> buttonsToRemove = ButtonsSingleton.getInstance().getButtonsToRemove();
    private ArrayList<Slider> sliders = ButtonsSingleton.getInstance().getSliders();
    private ArrayList<Slider> slidersToRemove = ButtonsSingleton.getInstance().getSlidersToRemove();
    private ArrayList<Button> buttonsToAdd = ButtonsSingleton.getInstance().getButtonsToAdd();
    
    private SettingsSingleton settingsSingleton = SettingsSingleton.getInstance();
    private GameSettingsSingleton gameSettingsSingleton = GameSettingsSingleton.getInstance();

    private int FONT_SIZE;
    private final DrawOptions DO = new DrawOptions();
    private Font font;
    private final Music music = new Music();

    private final String name;
    private final String displayString;
    private final Rectangle box;
    private final Point position;
    private Image image = null;
    private double scale;

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
        this.scale = 1;
    }
    // Constructor for Strings/Buttons without images
    public Button(String name, int fontSize, double width, double length, Point topLeft)  {
        this.name = name;
        this.displayString = name;
        this.box = new Rectangle(topLeft, width, length);
        this.position = topLeft;
        FONT_SIZE = fontSize;
        font = new Font("res/fonts/DejaVuSans-Bold.ttf", FONT_SIZE);
        this.scale = 1;
        DO.setBlendColour(whiteTranslucent);
    }

    // Constructor for Images
    public Button(String name, Image image, Point topLeft) {
        this.name = name;
        this.displayString = name;
        this.box = image.getBoundingBoxAt(new Point(topLeft.x + image.getWidth()/2, topLeft.y + image.getHeight()/2));
        this.position = topLeft;
        this.image = image;
        this.scale = 1;
        DO.setBlendColour(whiteTranslucent);
    }

    public Button(String name, Image image, Point topLeft, double scale) {
        this.name = name;
        this.displayString = name;
        Rectangle collisionImage = new Rectangle(topLeft.x + image.getWidth()*scale/2,
                topLeft.y + image.getHeight()*scale/2,
                image.getWidth()*scale, image.getHeight()*scale);
        this.box = collisionImage;
        this.position = topLeft;
        this.image = image;
        this.scale = scale;
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
        DO.setScale(scale, scale);
        if (image != null) {
            // need to adjust as boundingBoxAt takes centre, not topLeft
            image.drawFromTopLeft(position.x, position.y, DO);
        } else if (font != null) {
            font.drawString(displayString, position.x, position.y + (box.bottom() - box.top()), DO);
        }
    }

    public Image getImage() {return image;}

    public boolean isHovering() {return hovering;}
    public void playAction() {
        if (name.equalsIgnoreCase("Back To Start")) {
            settingsSingleton.setGameStateString("Menu");
        }
        else if (name.equalsIgnoreCase("Back")) {
            settingsSingleton.setGameState(1);
        }
        else if (name.equalsIgnoreCase("Create Map")) {
            settingsSingleton.setGameState(9);
        }
        else if (name.equalsIgnoreCase("Exit")) {
            Window.close();
        }
        else if (name.equalsIgnoreCase("4")) {
            settingsSingleton.setPlayers(4);
            settingsSingleton.setGameState(3);
        }
        else if (name.equalsIgnoreCase("Game Settings")) {
            settingsSingleton.setGameState(10);
        }
        else if (name.equalsIgnoreCase("Left Arrow")) {
            if (gameSettingsSingleton.getPage() > 0) {
                gameSettingsSingleton.setPage(gameSettingsSingleton.getPage() - 1);
                buttonsToRemove.addAll(buttons);
                slidersToRemove.addAll(sliders);
                buttonsToAdd.add(new Button("Left Arrow",
                        new Image("res/arrows/LeftArrow.png"),
                        new Point(600, 0),
                        0.5));
                buttonsToAdd.add(new Button("Right Arrow",
                        new Image("res/arrows/RightArrow.png"),
                        new Point(Window.getWidth() - 750, 0),
                        0.5));
                if (gameSettingsSingleton.getPage() == 0) {
                    buttonsToAdd.add(new Button("Decrease Map Speed",
                            new Image("res/arrows/LeftArrow.png"),
                            new Point(Window.getWidth()/2 + 30, 200),
                            0.5));
                    buttonsToAdd.add(new Button("Increase Map Speed",
                            new Image("res/arrows/RightArrow.png"),
                            new Point(Window.getWidth()/2 + 130, 200),
                            0.5));
                }
                else if (gameSettingsSingleton.getPage() == 1) {
                    addPowerUpSliders();
                }
                else if (gameSettingsSingleton.getPage() == 2) {
                    addObstacleSliders();
                }
            }
        }
        else if (name.equalsIgnoreCase("PLAY")) {
                settingsSingleton.setGameState(1);
        }
        else if (name.equalsIgnoreCase("Retry")) {
                if (settingsSingleton.getGameMode() < 99) {
                    settingsSingleton.setGameStateString("Retry");
                }
                else {
                    settingsSingleton.setGameStateString("Continue");
                }
        }
        else if (name.equalsIgnoreCase("Right Arrow")) {
                if (gameSettingsSingleton.getPage() < 2) {
                    gameSettingsSingleton.setPage(gameSettingsSingleton.getPage() + 1);
                    buttonsToRemove.addAll(buttons);
                    slidersToRemove.addAll(sliders);
                    buttonsToAdd.add(new Button("Left Arrow",
                            new Image("res/arrows/LeftArrow.png"),
                            new Point(600, 0),
                            0.5));
                    buttonsToAdd.add(new Button("Right Arrow",
                            new Image("res/arrows/RightArrow.png"),
                            new Point(Window.getWidth() - 750, 0),
                            0.5));
                    if (gameSettingsSingleton.getPage() == 1) {
                        addPowerUpSliders();
                    }
                    else if (gameSettingsSingleton.getPage() == 2) {
                        addObstacleSliders();
                    }
                }
        }
        else if (name.equalsIgnoreCase("Story")) {
            settingsSingleton.setGameMode(0);
            settingsSingleton.setGameStateString("STORY");
        }
        else if (name.equalsIgnoreCase("3")) {
            settingsSingleton.setPlayers(3); settingsSingleton.setGameState(3);
        }
        else if (name.equalsIgnoreCase("2")) {
            settingsSingleton.setGameState(3);
        }
        else if (name.equalsIgnoreCase("VS")) {
            settingsSingleton.setGameMode(1);
            settingsSingleton.setGameStateString("VS");
        }
        else if (name.equalsIgnoreCase("Decrease Map Speed")) {
            gameSettingsSingleton.setMapSpeed(gameSettingsSingleton.getMapSpeed() - 0.1);
        }
        else if (name.equalsIgnoreCase("Increase Map Speed")) {
            gameSettingsSingleton.setMapSpeed(gameSettingsSingleton.getMapSpeed() + 0.1);
        }
    }

    public String getName() {
        return name;
    }
    public void setNight() {
        night = true;
    }

    public void addPowerUpSliders() {
        sliders.addAll(Arrays.asList(
                new Slider("Minimiser", "PowerUp", new Point(400, 300)),
                new Slider("SpeedUp", "PowerUp", new Point(400, 400)),
                new Slider("SpeedDown", "PowerUp", new Point(400, 500)),
                new Slider("Shield", "PowerUp", new Point(400, 600)),
                new Slider("SpecialAbilityPoints", "PowerUp", new Point(400, 700))));
    }

    public void addObstacleSliders() {
        sliders.addAll(Arrays.asList(
                new Slider("Ball", "obstacle", new Point(400, 300)),
                new Slider("Rock", "obstacle", new Point(400, 400)),
                new Slider("StunBall", "obstacle", new Point(400, 500))));
    }

}
