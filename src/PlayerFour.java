import bagel.Image;
import bagel.Input;
import bagel.Keys;
import bagel.Window;
import bagel.util.Point;

public class PlayerFour extends Player{
    private int id = 4;
    private Keys key = Keys.M;
    private Image cursor = new Image(String.format("res/cursors/P%d.png", this.id));
    private Point pos;

    private Character character = null;
    private SideCharacter sideCharacter = null;
    private boolean characterChosen = false;
    private boolean sideCharacterChosen = false;
    private Map mapChosen = null;

    private boolean dead = false;

    private static int noOfWins = 0;

    public PlayerFour() {
        this.pos = new Point(Window.getWidth()/2, Window.getHeight()/2);
    }

    public void setPos(Input input) {
        double new_X = pos.x;
        double new_Y = pos.y;
        if (input.isDown(Keys.O)) {
            new_Y -= 10;
        }
        if (input.isDown(Keys.K)) {
            new_X -= 10;
        }
        if (input.isDown(Keys.L)) {
            new_Y += 10;
        }
        if (input.isDown(Keys.SEMICOLON)) {
            new_X += 10;
        }
        if (((0 < new_X) && (new_X < Window.getWidth())) && ((0 < new_Y) && (new_Y < Window.getHeight()))) {
            this.pos = new Point(new_X, new_Y);
        }
    }
    public void moveCharacter(Input input) {
        if ((input.isDown(Keys.O)) && (input.isDown(Keys.K))) {
            getCharacter().move("WA");
        }
        else if ((input.isDown(Keys.O)) && (input.isDown(Keys.SEMICOLON))) {
            getCharacter().move("WD");
        }
        else if ((input.isDown(Keys.L)) && (input.isDown(Keys.K))) {
            getCharacter().move("SA");
        }
        else if ((input.isDown(Keys.L)) && (input.isDown(Keys.SEMICOLON))) {
            getCharacter().move("SD");
        }
        else if (input.isDown(Keys.SEMICOLON)) {
            getCharacter().move("D");
        }
        else if (input.isDown(Keys.O)) {
            getCharacter().move("W");
        }
        else if (input.isDown(Keys.K)) {
            getCharacter().move("A");
        }
        else if (input.isDown(Keys.L)) {
            getCharacter().move("S");
        }
        else if (input.isDown(Keys.M)) {
            getCharacter().move("SPACE");
        }
    }

    public Point getPos() { return this.pos;}
    public Image getCursor() { return this.cursor;}
    public int getId() { return this.id;}
    public Character getCharacter() {
        return this.character;
    }
    public SideCharacter getSideCharacter() {
        return this.sideCharacter;
    }
    public Map getMapChosen() {
        return this.mapChosen;
    }
    public Keys getKey() {return this.key;}
    public int getNoOfWins() {return this.noOfWins;}

    public void setCharacter(Character character) {
        this.character = character;
    }

    public void setSideCharacter(SideCharacter character) {
        this.sideCharacter = character;
    }

    public boolean getCharacterChosen() {return this.character != null;}
    public boolean getSideCharacterChosen() {return this.sideCharacter != null;}
    public boolean isDead() {
        return this.dead;
    }
    public void setDead() {
        this.dead = !this.dead;
    }

    public void reset() {
        this.dead = false;
        this.character = null;
        this.sideCharacter = null;
        this.mapChosen = null;
    }
    public void recordWin() {
        this.noOfWins += 1;
    }

    public void setMapChosen(Map map) {
        this.mapChosen = map;
    }
}
