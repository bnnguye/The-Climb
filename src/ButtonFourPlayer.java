import bagel.DrawOptions;
import bagel.Font;
import bagel.util.Colour;
import bagel.util.Point;
import bagel.util.Rectangle;


public class ButtonFourPlayer extends Button {
    private final int FONT_SIZE = 160;
    private String name;
    private boolean hovering = false;
    private Point position;
    private Rectangle box;
    private DrawOptions DO = new DrawOptions();
    private Font font = new Font("res/fonts/DejaVuSans-Bold.ttf", FONT_SIZE);
    Colour black = new Colour(0,0,0);
    Colour whiteTranslucent = new Colour(0, 0, 0, 0.5);


    public ButtonFourPlayer(String name, Rectangle rectangle) {
        super(name, rectangle);
        this.name = name;
        this.box = rectangle;
        this.position = rectangle.bottomLeft();
        DO.setBlendColour(whiteTranslucent);
        if (java.time.LocalTime.now().getHour() > 18) {
            Colour whiteTranslucent = new Colour(1, 1, 1, 0.5);
            DO.setBlendColour(whiteTranslucent);
        }
    }

    public void playAction() {
        SettingsSingleton.getInstance().setPlayers(4);
        SettingsSingleton.getInstance().setGameState(3);
    }

    public void toggleHover(Point point) {
        if (box.intersects(point)) {
            if (!hovering) {
                Music music = new Music();
                music.playMusic("music/Hover.wav");
            }
            DO.setBlendColour(black);
            hovering = true;
            SettingsSingleton.getInstance().setPlayers(4);
        }
        else {
            DO.setBlendColour(whiteTranslucent);
            hovering = false;
        }
    }
    public void draw() { font.drawString(String.format("%s", name), position.x, position.y, DO); }
    public boolean isHovering() {return hovering;}
}