import bagel.Drawing;
import bagel.Image;
import bagel.Window;
import bagel.util.Colour;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.ArrayList;

public class SideSenkuu extends SideCharacter{
    private final double frames = SettingsSingleton.getInstance().getFrames();
    private String name = "Senkuu";
    private String soundPath = String.format("music/%s.wav", this.name);
    Image icon = new Image(String.format("res/charactersS/%s/Icon.PNG", this.name));
    boolean activating = false;
    double timer;
    Image selected = new Image(String.format("res/Selected/%s_Selected.png", this.name));
    private Point iconPos;
    private ArrayList<ObstacleArrow> senkuuObstacles = new ArrayList<>();


    public String getName() {
        return this.name;
    }
    public Image getIcon() {return this.icon;}
    public void setIconPos(Point point) {this.iconPos = point;}
    public Point getIconPos() {return this.iconPos;}
    public Image getSelected() {return this.selected;}
    public boolean isActivating() {return this.activating;}
    public void reset() {
        this.activating = false;
        this.animating = false;
        this.timer = 0;
    }
    public String playLine() {return this.soundPath;}

    public void activateAbility(Player user, ArrayList<Player> players, ArrayList<Obstacle> obstacles, ArrayList<PowerUp> powerUps, Map map) {
        Image noblePhantasm = new Image("res/charactersS/Senkuu/NoblePhantasm.png");
        int space;
        if (!this.activating) {
            senkuuObstacles.removeAll(senkuuObstacles);
            timer = 6*frames;
            this.activating = true;
            space = (int) Math.round(Math.random()*80);
            System.out.println(space);
            ObstacleArrow mock = new ObstacleArrow(new Point(0,0));
            Image mockImage = mock.getImage();
            for (int i = 0; i < 80; i++) {
                if (!(Math.abs(i - space) < 10)) {
                    senkuuObstacles.add(new ObstacleArrow(new Point(i * mockImage.getWidth(), 0)));
                }
            }
        }
        else {
            if (timer > 4*frames) {
                this.animating = true;
                noblePhantasm.drawFromTopLeft(0,0);
            }
            else {
                for (ObstacleArrow obstacle: senkuuObstacles) {
                    obstacle.update();
                    for (Player player: players) {
                        if (player.getId() != user.getId()) {
                            Point pos = player.getCharacter().getPos();
                            Image playerImage = player.getCharacter().getImage();
                            Rectangle playerRectangle = new Rectangle(new Point(pos.x - playerImage.getWidth()/2, pos.y - playerImage.getHeight()/2), playerImage.getWidth(), playerImage.getHeight());
                            //Drawing.drawRectangle(new Point(pos.x - playerImage.getWidth()/2, pos.y - playerImage.getHeight()/2), playerImage.getWidth(), playerImage.getHeight(), new Colour(1,0,0,0.5));
                            //Drawing.drawRectangle(obstacle.getPos(), obstacle.getImage().getWidth(), obstacle.getImage().getHeight(), new Colour(0,1,0,0.5));
                            if (playerRectangle.intersects(obstacle.getImage().getBoundingBoxAt(new Point(obstacle.getPos().x - obstacle.getImage().getWidth()/2, obstacle.getPos().y - obstacle.getImage().getHeight()/2)))) {
                                if (!player.isDead()) {
                                    player.setDead();
                                }
                            }
                        }
                    }
                }
                powerUps.removeAll(powerUps);
                obstacles.removeAll(obstacles);
                this.animating = false;
            }
            timer--;
        }
        if (timer <= 0) {
            senkuuObstacles.removeAll(senkuuObstacles);
            this.activating = false;
        }
    }


    public boolean isAnimating() {
        return this.animating;
    }

}