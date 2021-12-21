import java.util.ArrayList;

public class Computer extends Player {

    public void createSafeRoute(Map map, ArrayList<Obstacle> obstacles) {
        Map mapClone;
        ArrayList<Obstacle> obstaclesOnScreen = (ArrayList<Obstacle>) obstacles.clone();
        //ArrayList<ArrayList<Node>> allCombinations = createTree();

        //for ()
        for (Obstacle obstacle: obstaclesOnScreen) {

        }
    }

    public Node createTree() {
        HeadNode head = new HeadNode();
        int height = 5;

        for (int i = 0; i < height; i++) {
            //Node newNode = new Node()
            //head.getRootNodes().get(i);
        }
        return null;
    }

    public void traverseTree(Node node) {

    }

    public void updateSafeRoute() {

    }

    public int recursiveFunction(int num) {
        if (num > 144) {
            return num;
        }
        else {
            num++;
            return recursiveFunction(num);
        }
    }
}
