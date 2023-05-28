import Enums.ComputerType;
import bagel.util.Rectangle;

import java.util.ArrayList;

public class ComputerEasy extends Computer {

    public ComputerEasy(int id) {
        super(id);
    }

    protected void loadMoves(ArrayList<Obstacle> obstacles) {
        if (obstacles.size() > 0) {
            // update computer move every second, so computer is holding an input for one second
            Map map = GameSettingsSingleton.getInstance().getMap();
            ArrayList<CollisionBlock> collisionBlocks; // next level of computer

            ArrayList<Rectangle> safeSpots = getSafeSpots(obstacles);
            Rectangle safeSpot = safeSpots.get((int) (Math.random() * safeSpots.size()));

            if (safeSpot != null) {
                loadMovesToSafeSpot(safeSpot);
            }
        }
    }
}
