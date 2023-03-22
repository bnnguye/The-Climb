import bagel.util.Point;

import java.util.ArrayList;

public class Computer extends Player {

    private final int framesInAdvance = 1 * SettingsSingleton.getInstance().getRefreshRate();

    private ArrayList<Controls> moves = new ArrayList<>();

    private Character character = null;
    private SideCharacter sideCharacter = null;

    public Computer(int id) {
        super(id);
    }

    public void moveComputer(ArrayList<Obstacle> obstacles) {
        if (moves.size() == 0) {
            loadMoves(obstacles);
        }
        character.move(moves.get(0));
    }

    public void loadMoves(ArrayList<Obstacle> obstacles) {
        ArrayList<ArrayList<Controls>> controlTree = new ArrayList<>();
        try {
            Character charClone = (Character) character.clone();
            for (int i = 0; i < framesInAdvance; i++) {
                for (Controls control: Controls.values()) {
                    charClone.move(control);
                    for (Obstacle obstacle: obstacles) {
                        obstacle.move();
                        if (!charClone.getImage().getBoundingBoxAt(charClone.getPos()).intersects(obstacle.getImage().getBoundingBoxAt(obstacle.getPos()))) {

                        }
                    }
                }
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }
}
