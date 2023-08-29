import Enums.MapNames;
import Enums.Obstacles;
import bagel.Image;
import bagel.Window;
import bagel.util.Point;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Map {
    private final MapNames name;
    private final String character;
    Image mapPeek;
    double height;
    double currentHeight;
    private boolean updated = false;

    TimeLogger timeLogger = TimeLogger.getInstance();
    private ArrayList<Integer> eventTimes = new ArrayList<>();

    ArrayList<Tile> tiles = new ArrayList<>();

    public Map(MapNames name) {
        this.name = name;
        this.mapPeek = new Image(String.format("res/maps/mapPeeks/%s.png", name.toString().replace("_", " ")));

        if (name == MapNames.ROSWAALS_MANSION) {
            character = "ROSWAAL";
        }
        else if (name == MapNames.DRESSROSA) {
            character = "DOFLAMINGO";
        }
        else if (name == MapNames.PLANET_79) {
            character = "FRIEZA";
        }
        else if (name == MapNames.WALL_OF_MARIA) {
            character = "EREN";
        }
        else if (name == MapNames.CENTRAL_CATHEDRAL) {
            character = "HEATHCLIFF";
        }
        else if (name == MapNames.DIOS_MANSION) {
            character = "DIO";
        }
        else if (name == MapNames.GREED_ISLAND) {
            character = "ALL FOR ONE";
        }
        else {
            character = "BILL";
        }

        loadEventTimes();
    }

    public String getCharacter() { return character;}

    public void draw() {
        for(Tile tile: getVisibleTiles()) {
            if ((tile.getPos().y < Window.getHeight() + tile.getImage().getHeight()) && (tile.getPos().y > -tile.getImage().getHeight())) {
                tile.draw();
            }
        }
    }

    public Image getMapPeek() {
        return mapPeek;
    }

    public String getName() {
        return name.toString().replace("_", " ");
    }

    public void updateTiles(double shift) {
        if (shift != 0) {
            updated = true;
        }
        if (shift < 0 && shift + currentHeight < 0) {
            shift = -currentHeight;
        }
        currentHeight += shift;
        ArrayList<Tile>tilesToRemove = new ArrayList<>();
        for (Tile tile: tiles) {
            for (CollisionBlock block: tile.getCollisionBlocks()) {
                block.updatePos(new Point (tile.getPos().x, tile.getPos().y + shift));
            }
            tile.setPos(new Point(tile.getPos().x, tile.getPos().y + shift));
        }
        tiles.removeAll(tilesToRemove);
    }

    public boolean hasFinished() { return currentHeight > height; }

    public void generateMap() {
        loadEventTimes();
        tiles.clear();
        int currentBlocksInRow = 0;
        int currentRow = 0;
        this.height = -Window.getHeight();
        this.currentHeight = 0;
        try {
            Scanner scanner = new Scanner(new File(String.format("res/maps/mapData/%s.txt", name.toString().replace("_", " "))));
            while(scanner.hasNextLine()) {
                String[] line = scanner.nextLine().split(",");
                for (String tileType: line) {
                    tiles.add(tileChooser(tileType, new Point(480 * currentBlocksInRow, 600 + -475 * currentRow)));
                    currentBlocksInRow++;
                    if (currentBlocksInRow > 3) {
                        currentBlocksInRow = 0;
                        this.height += 475;
                        currentRow++;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        ObstaclesSettingsSingleton obstaclesSettings = GameSettingsSingleton.getInstance().getObstaclesSettingsSingleton();

        if (name == MapNames.CENTRAL_CATHEDRAL) {

        }
        else if (name == MapNames.DIOS_MANSION) {
            if (!obstaclesSettings.isObstacle(Obstacles.DIO)) {
                obstaclesSettings.toggle(Obstacles.DIO);
            }
        }

    }

    public Tile tileChooser(String line, Point point) {
        Tile tile = null;
        if (line.equals("Basic")) {
            tile = new TileBasic(point);
        }
        else if (line.equals("BasicTop")) {
            tile = new TileBasicTop(point);
        }
        else if (line.equals("BasicLeft")) {
            tile = new TileBasicLeft(point);
        }
        else if (line.equals("Ice")) {
            tile = new TileIce(point);
        }
        else if (line.equals("IceTop")) {
            tile = new TileIceTop(point);
        }
        else if (line.equals("IceLeft")) {
            tile = new TileIceLeft(point);
        }
        else if (line.equals("Slow")) {
            tile = new TileSlow(point);
        }
        else if (line.equals("SlowLeft")) {
            tile = new TileSlowLeft(point);
        }
        else if (line.equals("BasicSad")) {
            tile = new TileBasicSad(point);
        }
        return tile;
    }
    public ArrayList<Tile> getTiles() {return tiles;}

    public ArrayList<Tile> getVisibleTiles() {
        ArrayList<Tile> visibleTiles = new ArrayList<>();
        for(Tile tile: this.tiles) {
            if ((tile.getPos().y < Window.getHeight() + tile.getImage().getHeight()) && (tile.getPos().y > -tile.getImage().getHeight())) {
                visibleTiles.add(tile);
            }
        }
        return visibleTiles;
    }
    public double getHeight() {
        return height;
    }

    public double getCurrentHeight() {
        return currentHeight;
    }

    public void goToSummit() {
        while(!hasFinished()) {
            updateTiles(1);
        }
    }

    public void descend() {
        updateTiles(-20);
    }

    public void update() {
        updated = false;
        if (eventTimes.contains((int) currentHeight)) {
            createEvent();
            eventTimes.remove(0);
        }
    }

    public boolean hasUpdated() {
        return updated;
    }

    public void setCurrentHeight(double newHeight) {
        generateMap();
        updateTiles(newHeight);
    }

    public void shake(double offset) {
        for (Tile tile: tiles) {
            tile.setPos(new Point(tile.getPos().x + offset, tile.getPos().y));
        }
    }

    public void createEvent() {
        EventsListenerSingleton.EventsListener eventsListener = EventsListenerSingleton.getInstance().getEventsListener();
        if (name == MapNames.ROSWAALS_MANSION) {
            eventsListener.addEvent(new EventRoswaal());
        }
        else if (name == MapNames.DRESSROSA) {
            eventsListener.addEvent(new EventDoflamingo());
        }
        else if (name == MapNames.PLANET_79) {
            eventsListener.addEvent(new EventFrieza());
        }
        else if (name == MapNames.WALL_OF_MARIA) {
            eventsListener.addEvent(new EventEren());
        }
        else if (name == MapNames.CENTRAL_CATHEDRAL) {
            eventsListener.addEvent(new EventHeathcliff());
        }
        else if (name == MapNames.DIOS_MANSION) {
            eventsListener.addEvent(new EventDio());
        }
        else if (name == MapNames.GREED_ISLAND) {
            int random = (int) Math.round(Math.random() * 5);
            if (random == 0) {
                eventsListener.addEvent(new EventRoswaal());
            }
            else if (random == 1) {
                eventsListener.addEvent(new EventFrieza());
            }
            else if (random == 2) {
                eventsListener.addEvent(new EventDoflamingo());
            }
            else if (random == 3) {
                eventsListener.addEvent(new EventEren());
            }
            else if (random == 4) {
                eventsListener.addEvent(new EventHeathcliff());
            }
            else if (random == 5) {
                eventsListener.addEvent(new EventDio());
            }
        }

    };

    public void reverse() {
        ArrayList<Tile> newTiles = new ArrayList<>();
        Tile tile1 = null;
        Tile tile2 = null;
        Tile tile3 = null;
        Tile tile4 = null;
        for (Tile tile: tiles) {
            if (tile1 == null) {
                tile1 = tile;
            }
            else if (tile2 == null) {
                tile2 = tile;
            }
            else if (tile3 == null) {
                tile3 = tile;
            }
            else if (tile4 == null) {
                tile4 = tile;
            }
            else {
                newTiles.addAll(Arrays.asList(tile4, tile3, tile2, tile1));
                tile1 = null;
                tile2 = null;
                tile3 = null;
                tile4 = null;
            }
        }
        tiles = newTiles;
    }

    public void loadEventTimes() {
        eventTimes.clear();
        if (name == MapNames.ROSWAALS_MANSION) {
        }
        else if (name == MapNames.DRESSROSA) {
        }
        else if (name == MapNames.PLANET_79) {
        }
        else if (name == MapNames.WALL_OF_MARIA) {
            eventTimes.addAll(Arrays.asList(100, 3000, 5000));
        }
        else if (name == MapNames.CENTRAL_CATHEDRAL) {
        }
        else if (name == MapNames.DIOS_MANSION) {
            eventTimes.addAll(Arrays.asList(1000, 3000, 5000));
        }
        else if (name == MapNames.GREED_ISLAND) {
        }
        else {
        }
    }
}
