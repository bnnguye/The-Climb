import bagel.DrawOptions;
import bagel.Drawing;
import bagel.Font;
import bagel.Image;
import bagel.util.Colour;
import bagel.util.Rectangle;
import bagel.util.Point;

public abstract class   Button {

    private final int FONT_SIZE = 160;
    private String name;
    private boolean hovering = false;
    private Point position;
    private Rectangle box;
    private DrawOptions DO = new DrawOptions();
    private Font font = new Font("res/fonts/DejaVuSans-Bold.ttf", FONT_SIZE);
    private Colour black = new Colour(0, 0, 0);
    Colour whiteTranslucent = new Colour(1, 1, 1, 0.5);
    Colour white = new Colour(1, 1, 1);
    private Music music = new Music();
    private Image image = null;

    public Button(String name, Rectangle rectangle) {
        this.name = name;
        this.box = rectangle;
        this.position = rectangle.bottomLeft();
        DO.setBlendColour(white);
    }

    public void toggleHover(Point point) {
        if (box.intersects(point)) {
            if (!hovering) {
                music.playMusic("music/Hover.wav");
            }

            DO.setBlendColour(white);
            hovering = true;
        } else {
            DO.setBlendColour(whiteTranslucent);
            hovering = false;
        }
    }

    public void draw() {
        if (image != null) {
            image.drawFromTopLeft(position.x, position.y);
        } else {
            font.drawString(String.format("%s", name), position.x, position.y, DO);
        }
    }

    public Image getImage() {return image;}

    public boolean isHovering() {return hovering;}
    public abstract void playAction();
}
