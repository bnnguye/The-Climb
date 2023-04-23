import bagel.Window;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.ArrayList;
import java.util.Collections;

public class Computer extends Player {

    private final int lookAheadXSeconds = SettingsSingleton.getInstance().getRefreshRate();

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
        if (moves.size() > 0) {
            character.move(moves.get(0));
            moves.remove(0);
        }
        else {
            character.move(Controls.W);
        }
    }

    public void loadMoves(ArrayList<Obstacle> obstacles) {
        if (obstacles.size() > 0) {
            // update computer move every second, so computer is holding an input for one second
            Map map = GameSettingsSingleton.getInstance().getMap();
            ArrayList<CollisionBlock> collisionBlocks; // next level of computer

            Rectangle closestSafeSpot = getClosestSafeSpot(getSafeSpots(obstacles));
            System.out.println("Safe spots: " + getSafeSpots(obstacles).size());
            if (closestSafeSpot != null) {
                loadMovesToClosestSafeSpot(closestSafeSpot);
            }
        }
    }

    private void loadMovesToClosestSafeSpot(Rectangle closestSafeSpot) {
        System.out.println("Current character position: " + this.character.getPos());
        Character character = new Character(CharacterNames.CHIZURU);
        character.setPosition(this.character.getPos());

        while (Math.abs(character.getPos().x - closestSafeSpot.centre().x) > 10 || moves.size() < lookAheadXSeconds) {
            System.out.println("Character pos: " + character.getPos());
            System.out.println("Safe spot centre: " + closestSafeSpot.centre());
            if (character.getPos().x < closestSafeSpot.centre().x) {
                moves.add(Controls.D);
                character.move(Controls.D);
            }
            else if (character.getPos().x > closestSafeSpot.centre().x) {
                moves.add(Controls.A);
                character.move(Controls.A);
            }
        }
    }

    private ArrayList<Rectangle> getSafeSpots(ArrayList<Obstacle> obstacles) {
        System.out.println("Obstacles: " + obstacles.size());
        ArrayList<Rectangle> dangerSpots = new ArrayList<>();
        ArrayList<Rectangle> safeSpots = new ArrayList<>();

        for (Obstacle obstacle: obstacles) {
            Obstacle mockObstacle = new ObstacleRock();
            mockObstacle.setPos(obstacle.getPos());
            for (int i = 0; i < lookAheadXSeconds; i++) {
                mockObstacle.move();
            }
            dangerSpots.add(mockObstacle.getImage().getBoundingBoxAt(mockObstacle.getPos()));
        }

        ArrayList<Rectangle> sortedDangerSpots = sort(dangerSpots);

        double horizontalIndex = 0;
        for (Rectangle dangerSpot: sortedDangerSpots) {
            safeSpots.add(new Rectangle(horizontalIndex, 0, horizontalIndex, dangerSpot.left()));
            horizontalIndex = dangerSpot.right();
            System.out.println("danger spot left" + dangerSpot.left() + ", horizontal index: " + horizontalIndex);
        }

        return safeSpots;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }
    public Character getCharacter() {
        return this.character;
    }

    public ArrayList<Rectangle> sort(ArrayList<Rectangle> obstacles) {
        obstacles.sort((o1, o2)-> compareTo(o1.left(), o2.left()));
        return obstacles;
    }

    private int compareTo(double x1, double x2) {
        return x1 >= x2 ? (int) x2 : (int) x1;
    }

    private Rectangle getClosestSafeSpot(ArrayList<Rectangle> safeSpots) {
        Rectangle closestSpot = null;
        for (Rectangle safeSpot: safeSpots) {
            if (closestSpot == null) {
                closestSpot = safeSpot;
            }
            else {
                if (this.getCharacter().getPos().distanceTo(safeSpot.centre()) < this.getCharacter().getPos().distanceTo(closestSpot.centre())) {
                    closestSpot = safeSpot;
                }
            }
        }
        return closestSpot;
    }

}
