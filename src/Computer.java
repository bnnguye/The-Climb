import Enums.ComputerType;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.ArrayList;
import java.util.Comparator;

public abstract class Computer extends Player {

    /// updateRate refreshes moveList to adapt to newly spawned obstacles
    private final int updateRate = 144;

    private final ArrayList<Controls> moves = new ArrayList<>();

    public ArrayList<Rectangle> safeSpots = null;

    private Character character = null;

    public Computer(int id) {
        super(id);
    }

    @Override
    public void moveComputer(ArrayList<Obstacle> obstacles) {
        Map map = GameSettingsSingleton.getInstance().getMap();

        if (moves.size() < updateRate) {
            moves.clear();
            loadMoves(obstacles);
        }
        if (moves.size() > 0) {
            if (map.getHeight() - map.getCurrentHeight() < 50 || Window.getHeight() - character.getPos().y < 200) {
                character.move(addW(moves.get(0)));
            }
            else {
                character.move(moves.get(0));
            }
            moves.remove(0);
        }
        else {
            if (map.getHeight() - map.getCurrentHeight() < 50 || Window.getHeight() - character.getPos().y < 200) {
                character.move(Controls.W);
            }
        }
    }

    abstract void loadMoves(ArrayList<Obstacle> obstacles);

    protected void loadMovesToSafeSpot(Rectangle closestSafeSpot) {
        Character character = new Character(CharacterNames.CHIZURU);
        character.setPosition(this.character.getPos());

        while (Math.abs(character.getPos().x - closestSafeSpot.centre().x) > character.getSpeed()*2) {
            if (Math.abs(character.getPos().x - closestSafeSpot.centre().x) <= character.getSpeed()*2) {
                moves.add(null);
                character.move(null);
            }
            if (character.getPos().x < closestSafeSpot.centre().x) {
                moves.add(Controls.D);
                character.move(Controls.D);
            }
            else {
                moves.add(Controls.A);
                character.move(Controls.A);
            }

            if (moves.size() > 144) {
                break;
            }
        }
    }

    protected ArrayList<Rectangle> getSafeSpots(ArrayList<Obstacle> obstacles) {
        ArrayList<Rectangle> dangerSpots = new ArrayList<>();
        ArrayList<Rectangle> safeSpots = new ArrayList<>();

        for (Obstacle obstacle: obstacles) {
            Obstacle mockObstacle = new ObstacleRock();
            mockObstacle.setPos(obstacle.getPos());
            for (int i = 0; i < updateRate; i++) {
                mockObstacle.move();
            }
            dangerSpots.add(mockObstacle.getBoundingBox());
        }

        ArrayList<Rectangle> sortedDangerSpots = sort(dangerSpots);
        Rectangle lastR = null;
        for (Rectangle rectangle: sortedDangerSpots) {
            if (lastR == null) {
                lastR = rectangle;
            }
        }

        Rectangle lastDangerSpot = null;
        for (Rectangle dangerSpot: sortedDangerSpots) {
            if (lastDangerSpot == null) {
                safeSpots.add(new Rectangle(new Point(0, 0), dangerSpot.left(), 0));
            }
            else {
                if (!dangerSpot.intersects(lastDangerSpot) && !(dangerSpot.left() - lastDangerSpot.right() < 0)) {
                    safeSpots.add(new Rectangle(new Point(lastDangerSpot.right(), 0), dangerSpot.left() - lastDangerSpot.right(), 0));
                }
            }
            lastDangerSpot = dangerSpot;
        }

        this.safeSpots = safeSpots;

        return safeSpots;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }
    public Character getCharacter() {
        return this.character;
    }

    private ArrayList<Rectangle> sort(ArrayList<Rectangle> obstacles) {
        obstacles.sort(Comparator.comparingDouble(Rectangle::left));
        return obstacles;
    }

    protected Rectangle getClosestSafeSpot(ArrayList<Rectangle> safeSpots) {
        Rectangle closestSpot = null;
        for (Rectangle safeSpot: safeSpots) {
            if (closestSpot == null) {
                closestSpot = safeSpot;
            }
            else {
                if (character.getRectangle().intersects(safeSpot)) {
                    closestSpot = safeSpot;
                    return closestSpot;
                }
                if ((Math.abs(character.getPos().x - safeSpot.left()) < Math.abs(character.getPos().x - closestSpot.left())) ||
                        (Math.abs(character.getPos().x - safeSpot.right()) < Math.abs(character.getPos().x - closestSpot.right()))) {
                    closestSpot = safeSpot;
                }
            }
        }
//        System.out.println("Closest spot: " + closestSpot);
        return closestSpot;
    }

    private Controls addW(Controls move) {
        if (move.equals(Controls.A)) {
            return Controls.WA;
        }
        else if (move.equals(Controls.D)) {
            return Controls.WD;
        }
        else if (move == null) {
            return Controls.W;
        }
        return move;
    }

}
