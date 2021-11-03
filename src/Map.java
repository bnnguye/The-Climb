import bagel.Image;
import bagel.Window;
import bagel.util.Point;

import java.util.ArrayList;

public abstract class Map {
    int level;
    String name;
    Image mapPeek;
    Image map;
    Point pos;
    ArrayList<Tile> tiles;
    int height;
    int currentHeight;
    boolean jotaroAbility = false;

    public void draw() {
    }

    public Image getMapPeek() {
        return mapPeek;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {return level;}

    public void setPos() {}

    public void updateTiles(double shift) {}

    public boolean hasFinished() {
        return true;
    }
    public void generateMap() {}

    public ArrayList<Tile> getTiles() {return tiles;}
    public ArrayList<Tile> getVisibleTiles() {
        ArrayList<Tile> visibleTiles = new ArrayList<>();
        return visibleTiles;
    }
    public int getHeight() {
        return height;
    }

    public int getCurrentHeight() {
        return currentHeight;
    }

    public void setJotaroAbility(boolean bool) { jotaroAbility = bool;}
}
