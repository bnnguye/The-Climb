import bagel.Image;
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
    private Image selected = new Image(String.format("res/charactersS/%s/Selected.png", this.name));
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
        int space;
        if (!this.activating) {
            senkuuObstacles.clear();
            timer = 6*frames;
            this.activating = true;
            space = (int) Math.round(Math.random()*80);
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
            }
            else {
                for (ObstacleArrow obstacle: senkuuObstacles) {
                    obstacle.update();
                    for (Player player: players) {
                        if (player.getId() != user.getId()) {
                            Image playerImage = player.getCharacter().getImage();
                            Point playerPos = player.getCharacter().getPos();
                            Rectangle playerRectangle = new Rectangle(new Point(playerPos.x, playerPos.y), playerImage.getWidth(), playerImage.getHeight());
                            if (player.getCharacter().isMinimised()) {
                                playerRectangle = new Rectangle(new Point(playerPos.x - playerImage.getWidth()/2, playerPos.y - playerImage.getHeight()/2), playerImage.getWidth()/2, playerImage.getHeight()/2);
                            }
                            if (playerRectangle.intersects(obstacle.getImage().getBoundingBoxAt(obstacle.getPos()))) {
                                if (!player.isDead()) {
                                    player.setDead();
                                }
                            }
                        }
                    }
                }
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

    public void renderAbility() {
        if (timer > 4 * frames) {
            Image noblePhantasm = new Image("res/charactersS/Senkuu/NoblePhantasm.png");
            noblePhantasm.drawFromTopLeft(0,0);
        }
        else {
            for (ObstacleArrow arrow: senkuuObstacles) {
                arrow.draw();
            }
        }
    }

    public String getSoundPath() {return soundPath;}

}