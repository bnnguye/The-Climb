import bagel.Image;
import bagel.Input;
import bagel.Keys;
import bagel.Window;
import bagel.util.Point;

import java.util.ArrayList;
import java.util.HashMap;

public class Player {
    private final int id;
    private final HashMap<String, Keys> controls = new HashMap<>();
    private final PlayerStats playerStats = new PlayerStats();

    private Character character = null;
    private SideCharacter sideCharacter = null;

    private Map mapChosen = null;

    public Player(int id) {
        this.id = id;
        if (id == 1) {
            controls.put("Up", Keys.W);
            controls.put("Left", Keys.A);
            controls.put("Down", Keys.S);
            controls.put("Right", Keys.D);
            controls.put("Primary", Keys.R);
            controls.put("Secondary", Keys.T);
        }
        else if (id == 2) {
            controls.put("Up", Keys.UP);
            controls.put("Left", Keys.LEFT);
            controls.put("Down", Keys.DOWN);
            controls.put("Right", Keys.RIGHT);
            controls.put("Primary", Keys.O);
            controls.put("Secondary", Keys.P);
        }
        else if (id == 3) {
            controls.put("Up", Keys.Y);
            controls.put("Left", Keys.G);
            controls.put("Down", Keys.H);
            controls.put("Right", Keys.J);
            controls.put("Primary", Keys.Z);
            controls.put("Secondary", Keys.X);
        }
        else if (id == 4) {
            controls.put("Up", Keys.O);
            controls.put("Left", Keys.K);
            controls.put("Down", Keys.L);
            controls.put("Right", Keys.SEMICOLON);
            controls.put("Primary", Keys.M);
            controls.put("Secondary", Keys.N);
        }
    }

    public void moveCharacter(Input input) {
        Point previousPos = character.getPos();
        if ((input.isDown(controls.get("Up"))) && (input.isDown(controls.get("Left")))) {
            character.move(Controls.WA);
        }
        else if ((input.isDown(controls.get("Up"))) && (input.isDown(controls.get("Right")))) {
            character.move(Controls.WD);
        }
        else if ((input.isDown(controls.get("Down"))) && (input.isDown(controls.get("Left")))) {
            character.move(Controls.SA);
        }
        else if ((input.isDown(controls.get("Down"))) && (input.isDown(controls.get("Right")))) {
            character.move(Controls.SD);
        }
        else if (input.isDown(controls.get("Right"))) {
            character.move(Controls.D);
        }
        else if (input.isDown(controls.get("Up"))) {
            character.move(Controls.W);
        }
        else if (input.isDown(controls.get("Left"))) {
            character.move(Controls.A);
        }
        else if (input.isDown(controls.get("Down"))) {
            character.move(Controls.S);
        }
        else if (input.wasPressed(controls.get("Primary")) && character.hasSpecialAbility()) {
            character.useSpecialAbility();
            sideCharacter.activateAbility(this);
        }
        else if (input.wasPressed(controls.get("Secondary"))) {
            character.usePowerUp();
        }
        else {
            character.move(null);
        }
        playerStats.addDistance(previousPos.distanceTo(character.getPos()));
    }

    public void moveComputer(ArrayList<Obstacle> obstacleArrayList) {}

    public int getId() { return id;}
    public Character getCharacter() {
        return this.character;
    }
    public SideCharacter getSideCharacter() {
        return this.sideCharacter;
    }
    public Map getMapChosen() {
        return this.mapChosen;
    }
    public Keys getControl(String control) {
        return controls.get(control);
    }

    public void reverseControls() {
        swapControls("Left", "Right");
        swapControls("Up", "Down");
    }

    private void swapControls(String c1, String c2) {
        Keys temp = controls.get(c1);
        controls.put(c1, controls.get(c2));
        controls.put(c2, temp);
    }

    public void setCharacter(Character character) {
        this.character = character;
    }
    public void setSideCharacter(SideCharacter character) {
        this.sideCharacter = character;
    }
    public void setMapChosen(Map map) {
        this.mapChosen = map;
    }

    public void reset() {
        this.character = null;
        this.sideCharacter = null;
        this.mapChosen = null;
    }

    public PlayerStats getPlayerStats() {
        return playerStats;
    }

    public void resetControls() {
        controls.clear();
        if (id == 1) {
            controls.put("Up", Keys.W);
            controls.put("Left", Keys.A);
            controls.put("Down", Keys.S);
            controls.put("Right", Keys.D);
            controls.put("Primary", Keys.R);
            controls.put("Secondary", Keys.T);
        }
        else if (id == 2) {
            controls.put("Up", Keys.UP);
            controls.put("Left", Keys.LEFT);
            controls.put("Down", Keys.DOWN);
            controls.put("Right", Keys.RIGHT);
            controls.put("Primary", Keys.O);
            controls.put("Secondary", Keys.P);
        }
        else if (id == 3) {
            controls.put("Up", Keys.Y);
            controls.put("Left", Keys.G);
            controls.put("Down", Keys.H);
            controls.put("Right", Keys.J);
            controls.put("Primary", Keys.Z);
            controls.put("Secondary", Keys.X);
        }
        else if (id == 4) {
            controls.put("Up", Keys.O);
            controls.put("Left", Keys.K);
            controls.put("Down", Keys.L);
            controls.put("Right", Keys.SEMICOLON);
            controls.put("Primary", Keys.M);
            controls.put("Secondary", Keys.N);
        }
    }
}