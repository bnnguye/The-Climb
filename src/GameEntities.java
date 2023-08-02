import Enums.Obstacles;
import Enums.PowerUps;
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
            Rectangle obstacleRectangle = obstacle.getBoundingBox();
            for (Player player : settingsSingleton.getPlayers()) {
                Character character = player.getCharacter();
                if (character.getRectangle().intersects(obstacleRectangle)) {
                    if (!character.isDead()) {
                        obstaclesToRemove.add(obstacle);
                        if (character.hasShield()) {
                            character.popShield();
                        }
                        else {
                            obstacle.collide(character);
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
                Character character = player.getCharacter();
                if (character.getRectangle().intersects(powerUp.getRectangle())) {
                    if (!character.isDead() && !character.hasPowerUp()) {
                        character.setPowerUp(powerUp);
                        powerUpsToRemove.add(powerUp);
                        break;
                    }
                }
            }
        }
        powerUps.removeAll(powerUpsToRemove);
    }

    public void spawnPowerUps() {
        double spawnNo = Math.random()*4;
        if (spawnNo < 1) {
            if (gameSettingsSingleton.getPowerUpsSettingsSingleton().isPowerUp(PowerUps.SPEEDUP)) {
                if (Math.random() < gameSettingsSingleton.getPowerUpsSettingsSingleton().getFrequency(PowerUps.SPEEDUP)) {
                    powerUps.add(new PowerUpSpeedUp());
                }
            }
        }
        else if (spawnNo < 2) {
            if (gameSettingsSingleton.getPowerUpsSettingsSingleton().isPowerUp(PowerUps.MINIMISER)) {
                if (Math.random() < gameSettingsSingleton.getPowerUpsSettingsSingleton().getFrequency(PowerUps.MINIMISER)) {
                    powerUps.add(new PowerUpMinimiser());
                }
            }
        }
        else if (spawnNo < 3) {
            if (gameSettingsSingleton.getPowerUpsSettingsSingleton().isPowerUp(PowerUps.SHIELD)) {
                if (Math.random() < gameSettingsSingleton.getPowerUpsSettingsSingleton().getFrequency(PowerUps.SHIELD)) {
                    powerUps.add(new PowerUpShield());
                }
            }
        }
        else {
            if (gameSettingsSingleton.getPowerUpsSettingsSingleton().isPowerUp(PowerUps.ABILITY)) {
                if (Math.random() < gameSettingsSingleton.getPowerUpsSettingsSingleton().getFrequency(PowerUps.ABILITY)) {
                    powerUps.add(new PowerUpAbility());
                }
            }
        }
    }

    public void spawnObstacles() {
        if (gameSettingsSingleton.getObstaclesSettingsSingleton().isObstacle(Obstacles.ROCK)) {
            if (Math.random() < ObstaclesSettingsSingleton.getInstance().getFrequency(Obstacles.ROCK)) {
                obstacles.add(new ObstacleRock());
            }
        }
        if (gameSettingsSingleton.getObstaclesSettingsSingleton().isObstacle(Obstacles.BALL)) {
            if (Math.random()
                    < ObstaclesSettingsSingleton.getInstance().getFrequency(Obstacles.BALL)) {
                obstacles.add(new ObstacleBall());
            }
        }
        if (gameSettingsSingleton.getObstaclesSettingsSingleton().isObstacle(Obstacles.STUNBALL)) {
            if (Math.random()
                    < ObstaclesSettingsSingleton.getInstance().getFrequency(Obstacles.STUNBALL)) {
                obstacles.add(new ObstacleStunBall());
            }
        }
        if (gameSettingsSingleton.getObstaclesSettingsSingleton().isObstacle(Obstacles.SPEEDDOWN)) {
            if (Math.random()
                    < ObstaclesSettingsSingleton.getInstance().getFrequency(Obstacles.SPEEDDOWN)) {
                obstacles.add(new ObstacleSpeedDown());
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
            if (powerUp.pos.y > Window.getHeight()) {
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
