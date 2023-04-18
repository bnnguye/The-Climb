import bagel.util.Rectangle;

import java.util.ArrayList;
import java.util.Collections;

public class Computer extends Player {

    private final int lookAheadXSeconds = 1 * SettingsSingleton.getInstance().getRefreshRate();

    private ArrayList<Controls> moves = new ArrayList<>();

    private Character character = null;
    private SideCharacter sideCharacter = null;

    public Computer(int id) {
        super(id);
    }

    @Override
    public void moveComputer(ArrayList<Obstacle> obstacles) {
        if (moves.size() == 0) {
            loadMoves(obstacles);
        }
        character.move(moves.get(0));
    }

    public void loadMoves(ArrayList<Obstacle> obstacles) {
        // update computer move every second, so computer is holding an input for one second
        Map map = GameSettingsSingleton.getInstance().getMap();
        ArrayList<CollisionBlock> collisionBlocks; // next level of computer

        ArrayList<Controls> controlList = new ArrayList<>();
        Collections.addAll(controlList, Controls.values());

        Character tempChar = new Character(character.getFullName());
        for (int i = 0; i < lookAheadXSeconds; i++) {
            for (Controls control: Controls.values()) {
                controlList.add()
            }
        }

        this.moves = controlList;
    }

    private ArrayList<Rectangle> getDangerousSpots(ArrayList<Obstacle> obstacles) {
        ArrayList<Rectangle> safeSpots = new ArrayList<>();
        safeSpots.add(new Rectangle(0,0, 1920, 1080));

        for (int i = 0; i < lookAheadXSeconds; i++) {
            for (Obstacle obstacle: obstacles) {
                obstacle.move();
            }
        }

        for (Obstacle obstacle: obstacles) {

        }

        return safeSpots;

    }

    public void setCharacter(Character character) {
        this.character = character;
    }
    public Character getCharacter() {
        return this.character;
    }

}
