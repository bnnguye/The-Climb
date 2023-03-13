public class Computer extends Player {

    private Character character = null;
    private SideCharacter sideCharacter = null;

    public Computer(int id) {
        super(id);
    }

    // Logic of computer: Likes to stay at an x position, y moves based on current situation
    // current situation comprises of obstacles on screen
    // x moves if will collide into a collision bar
    // Computer looks into the current situation every update. Has a list of moves to make for the next X seconds.
    // List of actions are updated
}
