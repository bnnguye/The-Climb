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

    public static synchronized GameEntities getInstance() {
        if (single_instance == null) {
            single_instance = new GameEntities();
        }
        return single_instance;
    }

    public ArrayList<PowerUp> getPowerUps() { return powerUps;}
    public ArrayList<Obstacle> getObstacles() { return obstacles;}


    public void checkCollisionObstacles() {
        ArrayList<Obstacle> obstaclesToRemove = new ArrayList<>();
        for (Obstacle obstacle: obstacles) {
            for (Player player : settingsSingleton.getPlayers()) {
                Character character = player.getCharacter();
                if (character.getRectangle().intersects(obstacle.getBoundingBox())) {
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
        ArrayList<PowerUp> powerUpsToRemove = new ArrayList<>();
        for (PowerUp powerUp: powerUps) {
            for (Player player : settingsSingleton.getPlayers()) {
                Character character = player.getCharacter();
                if (character.getRectangle().intersects(powerUp.getRectangle())) {
                    if (!character.isDead() && !character.hasPowerUp()) {
                        if (powerUp.getClass() == PowerUpAbility.class) {
                            powerUp.activate(character);
                        }
                        else {
                            character.setPowerUp(powerUp);
                        }
                        break;
                    }
                }
                powerUpsToRemove.add(powerUp);
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
        ArrayList<Obstacle> obstaclesToRemove = new ArrayList<>();
        ArrayList<PowerUp> powerUpsToRemove = new ArrayList<>();

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
        obstacles.removeAll(obstaclesToRemove);
        powerUps.removeAll(powerUpsToRemove);
    }
}
