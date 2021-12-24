import bagel.Drawing;
import bagel.Image;
import bagel.Window;
import bagel.util.Colour;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.ArrayList;

public class SidePuck extends SideCharacter{
    private final int frames = 144;
    String name = "Puck";
    String soundPath = String.format("music/%s.wav", this.name);
    Image icon = new Image(String.format("res/charactersS/%s/Icon.PNG", this.name));
    boolean activating = false;
    boolean animating = false;
    int timer;
    Image selected = new Image(String.format("res/Selected/%s_Selected.png", this.name));

    ArrayList<Shard> shards;
    ArrayList<Shard> shardsToRemove;



    public String getName() {
        return this.name;
    }
    public Image getIcon() {return this.icon;}
    public void setIconPos(Point point) {this.iconPos = point;}
    public Point getIconPos() {return this.iconPos;}
    public Image getSelected() {return this.selected;}
    public boolean isActivating() {return this.activating;}
    public String playLine() {return this.soundPath;}

    public void activateAbility(Player user, ArrayList<Player> players, ArrayList<Obstacle> obstacles, ArrayList<PowerUp> powerUps, Map map) {
        if(!this.activating) {
            this.activating = true;
            this.timer = 5 * frames;
            shards = new ArrayList<>();
            shardsToRemove = new ArrayList<>();
        }
        else {
            if (timer > 3*frames) {
                Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), new Colour(0, 0, 0, 0.5));
                Image noblePhantasm = new Image(String.format("res/charactersS/%s/NoblePhantasm.png", this.name));
                noblePhantasm.drawFromTopLeft(0,0);
                this.animating = true;
            }
            else {
                Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), new Colour(0, 0, 1, 0.5));
                this.animating = false;
                for (Player player: players) {
                    if (!player.getSideCharacter().getName().equals("Puck")) {
                        player.getCharacter().onSlow();
                    }

                    if (shards.size() > 0) {
                        for (Shard shard: shards) {
                            shard.draw();
                            if (shard.getImage().getBoundingBoxAt(shard.getPos()).intersects(player.getCharacter().getImage().getBoundingBoxAt(player.getCharacter().getPos()))) {
                                player.setDead();
                            }
                            shard.move();
                            if (shard.getPos().y > Window.getHeight()) {
                                shardsToRemove.add(shard);
                            }
                        }
                        shards.removeAll(shardsToRemove);
                    }
                }
                spawnShards();
            }
            this.timer--;
            if (timer < 0) {
                this.activating = false;
            }
        }
    }

    public void reset() {
        this.activating = false;
        this.animating = false;
        this.timer = 0;
    }


    public boolean isAnimating() {
        return this.animating;
    }

    public void spawnShards() {
        if (Math.random() > 0.97) {
            shards.add(new Shard());
        }
    }
}
