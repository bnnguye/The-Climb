//import bagel.Drawing;
//import bagel.Image;
//import bagel.Window;
//import bagel.util.Colour;
//import bagel.util.Point;
//
//import java.util.ArrayList;
//
//public class SidePuck extends SideCharacter{
//    private final double frames = TimeLogger.getInstance().getRefreshRate();
//
//    private String name = "Puck";
//
//    boolean activating = false;
//    boolean animating = false;
//    double timer;
//
//    ArrayList<ObstacleShard> shards;
//    ArrayList<ObstacleShard> shardsToRemove;
//
//    public String getName() {
//        return this.name;
//    }
//
//    @Override
//    public String getPower() {
//        return null;
//    }
//
//    @Override
//    public String getDesc() {
//        return null;
//    }
//
//    @Override
//    public String getSoundPath() {
//        return null;
//    }
//
//    public boolean isActivating() {return this.activating;}
//
//    public void activateAbility(Player user, ArrayList<Player> players, ArrayList<Obstacle> obstacles, ArrayList<PowerUp> powerUps, Map map) {
//        if(!this.activating) {
//            this.activating = true;
//            this.timer = 5 * frames;
//            shards = new ArrayList<>();
//            shardsToRemove = new ArrayList<>();
//        }
//        else {
//            if (timer > 3*frames) {
//                this.animating = true;
//            }
//            else {
//                this.animating = false;
//                for (Player player: players) {
//                    if (!player.getSideCharacter().getName().equals("Puck")) {
//                        player.getCharacter().onSlow();
//                    }
//
//                    if (shards.size() > 0) {
//                        for (ObstacleShard obstacleShard : shards) {
//                            if (obstacleShard.getImage().getBoundingBoxAt(obstacleShard.getPos()).intersects(player.getCharacter().getImage().getBoundingBoxAt(player.getCharacter().getPos()))) {
//                                player.getCharacter().setDead(true);
//                            }
//                            obstacleShard.move();
//                            if (obstacleShard.getPos().y > Window.getHeight()) {
//                                shardsToRemove.add(obstacleShard);
//                            }
//                        }
//                        shards.removeAll(shardsToRemove);
//                    }
//                }
//                spawnShards();
//            }
//            this.timer--;
//            if (timer < 0) {
//                this.activating = false;
//            }
//        }
//    }
//
//    @Override
//    public void setMap(Map map) {
//
//    }
//
//    @Override
//    public void setPowerUps(ArrayList<PowerUp> powerUps) {
//
//    }
//
//    @Override
//    public void setLeft(boolean bool) {
//
//    }
//
//    @Override
//    public ArrayList<ExodiaPiece> getExodiaPiecesCollected() {
//        return null;
//    }
//
//    public void reset() {
//        this.activating = false;
//        this.animating = false;
//        this.timer = 0;
//    }
//
//
//    public boolean isAnimating() {
//        return this.animating;
//    }
//
//    public void spawnShards() {
//        if (Math.random() > 0.97) {
//            shards.add(new ObstacleShard());
//        }
//    }
//
//    public void renderAbility() {
//        if (timer > 3 * frames) {
//            Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), new Colour(0, 0, 0, 0.8));
//            Image special = new Image(String.format("res/sidecharacters/%s/special.png", this.name));
//            special.drawFromTopLeft(0,0);
//        }
//        else {
//            Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), new Colour(0, 0, 1, 0.5));
//            for (ObstacleShard shard: shards) {
//                shard.draw();
//            }
//        }
//    }
//
//}
