import bagel.Image;
import bagel.util.Point;

public class ExodiaPiece {
    private String name;
    Image image;
    private Point pos;
    double speed = 1 + GameSettingsSingleton.getInstance().getMapSpeed();

    public ExodiaPiece(String name, Point pos) {
        this.name = name;
        this.image = new Image(String.format("res/charactersS/Yugi/Cards/%s.png", this.name));
        this.pos = pos;
    }

    public void setPos(Point point) { this.pos = point; }

    public void move(){ this.pos = new Point(this.pos.x, this.pos.y + speed);}

    public Image getImage() {
        return image;
    }

    public Point getPos() {
        return pos;
    }

}
