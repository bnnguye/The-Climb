import bagel.Image;
import bagel.Input;
import bagel.Keys;
import bagel.Window;
import bagel.util.Point;

public abstract class Player {
    private int id;
    private Keys key;
    private Image cursor;
    private Point pos;

    private Character character = null;
    private SideCharacter sideCharacter = null;
    private boolean characterChosen = false;
    private boolean sideCharacterChosen = false;
    private Map mapChosen = null;

    private boolean dead = false;

    private static int noOfWins = 0;

    public Player() { pos = new Point(Window.getWidth()/2, Window.getHeight()/2);
    }


    public void setPos(Input input) {
    }
    public void moveCharacter(Input input) {
    }

    public Point getPos() { return pos;}
    public Image getCursor() { return cursor;}
    public int getId() { return id;}
    public Character getCharacter() {
        return character;
    }
    public SideCharacter getSideCharacter() {
        return sideCharacter;
    }
    public Map getMapChosen() {
        return mapChosen;
    }
    public Keys getKey() {return key;}
    public int getNoOfWins() {return noOfWins;}

    public void setCharacter(Character character) {
    }

    public void setSideCharacter(SideCharacter character) {
    }

    public void freeCharacter() {}
    public void freeSideCharacter() {}
    public boolean getCharacterChosen() { return characterChosen;}
    public boolean getSideCharacterChosen() {return sideCharacterChosen;}
    public boolean isDead() {
        return dead;
    }
    public void setDead() {
    }

    public void reset() {
    }
    public void recordWin() {
    }

    public void setMapChosen(Map map) {
    }
}