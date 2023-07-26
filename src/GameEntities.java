import bagel.Image;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.ArrayList;

public class GameEntities {
    private static GameEntities single_instance = null;
    
    private SettingsSingleton settingsSingleton = SettingsSingleton.getInstance();
    private GameSettingsSingleton gameSettingsSingleton = GameSettingsSingleton.getInstance();

    private final ArrayList<PowerUp> powerUps = new ArrayList<>();
    private final ArrayList<Obstacle> obstacles = new ArrayList<>();

    private final ArrayList<PowerUp> powerUpsToRemove = new ArrayList<>();
    private final ArrayList<Obstacle> obstaclesToRemove = new ArrayList<>();

    public static synchronized GameEntities getInstance() {
        if (single_instance == null) {
            single_instance = new GameEntities();
        }
        return single_instance;
    }

    public ArrayList<PowerUp> getPowerUps() { return powerUps;}
    public ArrayList<Obstacle> getObstacles() { return obstacles;}


    public void checkCollisionObstacles() {
        for (Obstacle obstacle: obstacles) {
            Rectangle obstacleRectangle = getBoundingBoxOf(obstacle.getImage(), obstacle.getPos());
            for (Player player : settingsSingleton.getPlayers()) {
                if (player.getCharacter().getRectangle().intersects(obstacleRectangle)) {
                    if (!player.getCharacter().isDead()) {
                        obstaclesToRemove.add(obstacle);
                        if (player.getCharacter().hasShield()) {
                            player.getCharacter().popShield();
                        }
                        else {
                            if (obstacle.getName().equals("StunBall")) {
                                player.getCharacter().gotStunned();
                            }
                            else {
                                player.getCharacter().reduceLive();
                            }
                        }
                    }
                }
            }
        }
        obstacles.removeAll(obstaclesToRemove);
    }

    public void checkCollisionPowerUps() {
        for (PowerUp powerUp: powerUps) {
            for (Player player : settingsSingleton.getPlayers()) {
                if (player.getCharacter().getRectangle().intersects(getBoundingBoxOf(powerUp.getImage(), powerUp.getPos()))) {
                    if (!player.getCharacter().isDead()) {
                        powerUp.gainPowerUp(player);
                        powerUpsToRemove.add(powerUp);
                        break;
                    }
                }
            }
        }
        powerUps.removeAll(powerUpsToRemove);
    }

    public void spawnPowerUps() {
        double spawnNo = Math.random()*5;
        if (spawnNo < 1) {
            if (gameSettingsSingleton.getPowerUpsSettingsSingleton().isPowerUp("SpeedUp")) {
                if (Math.random() < gameSettingsSingleton.getPowerUpsSettingsSingleton().getFrequency("SpeedUp")) {
                    powerUps.add(new PowerUpSpeedUp());
                }
            }
        }
        else if (spawnNo < 2) {
            if (gameSettingsSingleton.getPowerUpsSettingsSingleton().isPowerUp("SpeedDown")) {
                if (Math.random() < gameSettingsSingleton.getPowerUpsSettingsSingleton().getFrequency("SpeedDown")) {
                    powerUps.add(new SpeedDown());
                }
            }
        }
        else if (spawnNo < 3) {
            if (gameSettingsSingleton.getPowerUpsSettingsSingleton().isPowerUp("Minimiser")) {
                if (Math.random() < gameSettingsSingleton.getPowerUpsSettingsSingleton().getFrequency("Minimiser")) {
                    powerUps.add(new PowerUpMinimiser());
                }
            }
        }
        else if (spawnNo < 4) {
            if (gameSettingsSingleton.getPowerUpsSettingsSingleton().isPowerUp("Shield")) {
                if (Math.random() < gameSettingsSingleton.getPowerUpsSettingsSingleton().getFrequency("Shield")) {
                    powerUps.add(new PowerUpShield());
                }
            }
        }
        else {
            if (gameSettingsSingleton.getPowerUpsSettingsSingleton().isPowerUp("SpecialAbilityPoints")) {
                if (Math.random() < gameSettingsSingleton.getPowerUpsSettingsSingleton().getFrequency("SpecialAbilityPoints")) {
                    powerUps.add(new PowerUpAbility());
                }
            }
        }
    }

    public void spawnObstacles() {
        if (gameSettingsSingleton.getObstaclesSettingsSingleton().isRocks()) {
            if (Math.random() < ObstaclesSettingsSingleton.getInstance().getFrequency("Rock")) {
                obstacles.add(new ObstacleRock());
            }
        }
        if (gameSettingsSingleton.getObstaclesSettingsSingleton().isBalls()) {
            if (Math.random()
                    < ObstaclesSettingsSingleton.getInstance().getFrequency("Ball")) {
                obstacles.add(new ObstacleBall());
            }
        }
        if (gameSettingsSingleton.getObstaclesSettingsSingleton().isStunBalls()) {
            if (Math.random()
                    < ObstaclesSettingsSingleton.getInstance().getFrequency("StunBall")) {
                obstacles.add(new ObstacleStunBall());
            }
        }
    }

    public void updateObjects() {
        for (Obstacle obstacle : obstacles) {
            obstacle.move();
            if ((obstacle.getPos().y > Window.getHeight()) && (!obstaclesToRemove.contains(obstacle))) {
                obstaclesToRemove.add(obstacle);
            }
        }
        for (PowerUp powerUp : powerUps) {
            powerUp.move();
            if (powerUp.getPos().y > Window.getHeight()) {
                powerUpsToRemove.add(powerUp);
            }
        }
    }

    public Rectangle getBoundingBoxOf(Image image, Point pos) {
        return image.getBoundingBoxAt(new Point(pos.x + image.getWidth()/2,  pos.y + image.getHeight()/2));
    }

    public void update() {
        powerUps.remove(powerUpsToRemove);
        obstacles.remove(obstaclesToRemove);
    }

}
