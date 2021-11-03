import bagel.Image;
import bagel.util.Point;
import java.lang.Math.*;

public class Enemy extends Character {
    Image image = new Image("src/images/characters/Betelgeuse/worshipper.png");
    int health = 100;
    Point point;
    int speed = 2;

    public Enemy() {
    }

    public void reduceHealth(int i) {
        health = health - i;
    }
    public int getHealth() { return health;}
    public Image getImage() {return image;}
    public Point getPosition() {return position;}

    public void move() {

    }

}
