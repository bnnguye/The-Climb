public class Node {
    // 0: Don't move, 1: North, 2: South, 3: West, 4: East, 5: NW, 6: NE, 7: SW, 8: SE
    private int command;
    private double time;
    private int level;
    private int commandsUsed;


    private Node previous;
    private Node next;

    public Node(int direction, double duration) {
        if (previous == null) {
            this.level = 0;
        } else {
            this.level = previous.getLevel() + 1;
        }
        this.command = previous.getCommandsUsed();
        previous.updateCommandsUsed();
        this.time = duration;
        this.commandsUsed = 0;
    }

    public int getLevel() {return this.level;}
    public int getCommandsUsed() {return this.commandsUsed;}
    public void updateCommandsUsed() {this.commandsUsed++;}
    public int getCommand() {
        return this.command;
    }
}
