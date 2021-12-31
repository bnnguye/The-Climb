import java.util.ArrayList;

public class HeadNode {
    private double frames = SettingsSingleton.getInstance().getFrames();
    // 0: Don't move, 1: North, 2: South, 3: West, 4: East, 5: NW, 6: NE, 7: SW, 8: SE
    ArrayList<Node> rootNodes;


    public HeadNode() {
        for (int i = 0; i < 9; i++) {
            rootNodes.add(new Node(i, 0.5 * frames));
        }
    }

    public ArrayList<Node> getRootNodes() {
        return rootNodes;
    }
}
