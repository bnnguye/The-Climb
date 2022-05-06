import bagel.Image;
import bagel.Input;
import bagel.Keys;
import bagel.Window;
import bagel.util.Point;

import java.util.ArrayList;
import java.util.HashMap;

public class Player {
    private static ArrayList<Player> players = new ArrayList<>();
    private int id;
    private HashMap<String, Keys> controls = new HashMap<>();
    private Point pos;

    private Character character = null;
    private SideCharacter sideCharacter = null;
    private boolean characterChosen = false;
    private boolean sideCharacterChosen = false;
    private Map mapChosen = null;

    private int lives;
    private boolean dead = false;

    private static int noOfWins = 0;

    public Player(int id) {
        pos = new Point(Window.getWidth()/2, Window.getHeight()/2);
        this.id = id;
        if (id == 1) {
            controls.put("Up", Keys.W);
            controls.put("Left", Keys.A);
            controls.put("Down", Keys.S);
            controls.put("Right", Keys.D);
            controls.put("Primary", Keys.T);
        }
        else if (id == 2) {
            controls.put("Up", Keys.UP);
            controls.put("Left", Keys.LEFT);
            controls.put("Down", Keys.DOWN);
            controls.put("Right", Keys.RIGHT);
            controls.put("Primary", Keys.P);
        }
        else if (id == 3) {
            controls.put("Up", Keys.Y);
            controls.put("Left", Keys.G);
            controls.put("Down", Keys.H);
            controls.put("Right", Keys.J);
            controls.put("Primary", Keys.Z);
        }
        else if (id == 4) {
            controls.put("Up", Keys.O);
            controls.put("Left", Keys.K);
            controls.put("Down", Keys.L);
            controls.put("Right", Keys.SEMICOLON);
            controls.put("Primary", Keys.M);
        }
        if ((id > 0) && (id < 5)) {
            getInstance().add(this);
        }
    }

    public synchronized static ArrayList<Player> getInstance() {
        if (players == null) {
            players = new ArrayList<>();
        }
        return players;
    }


    public void setPos(Input input) {
        double new_X = pos.x;
        double new_Y = pos.y;
        if (input.isDown(controls.get("Up"))) {
            new_Y -= 10;
        }
        if (input.isDown(controls.get("Left"))) {
            new_X -= 10;
        }
        if (input.isDown(controls.get("Down"))) {
            new_Y += 10;
        }
        if (input.isDown(controls.get("Right"))) {
            new_X += 10;
        }
        if (((0 < new_X) && (new_X < Window.getWidth())) && ((0 < new_Y) && (new_Y < Window.getHeight()))) {
            this.pos = new Point(new_X, new_Y);
        }
    }
    public void moveCharacter(Input input) {
        if ((input.isDown(controls.get("Up"))) && (input.isDown(controls.get("Left")))) {
            getCharacter().move("WA");
        }
        else if ((input.isDown(controls.get("Up"))) && (input.isDown(controls.get("Right")))) {
            getCharacter().move("WD");
        }
        else if ((input.isDown(controls.get("Down"))) && (input.isDown(controls.get("Left")))) {
            getCharacter().move("SA");
        }
        else if ((input.isDown(controls.get("Down"))) && (input.isDown(controls.get("Right")))) {
            getCharacter().move("SD");
        }
        else if (input.isDown(controls.get("Right"))) {
            getCharacter().move("D");
        }
        else if (input.isDown(controls.get("Up"))) {
            getCharacter().move("W");
        }
        else if (input.isDown(controls.get("Left"))) {
            getCharacter().move("A");
        }
        else if (input.isDown(controls.get("Down"))) {
            getCharacter().move("S");
        }
        else {
            getCharacter().move(null);
        }
    }

    public Point getPos() { return this.pos;}
    public Image getCursor() { return new Image(String.format("res/cursors/P%s.png", this.id));}
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
    public void setLives(int lives) {
        this.lives = lives;
    }
    public int getLives() {return this.lives;}

    public Keys getControl(String control) {
        return controls.get(control);
    }
}