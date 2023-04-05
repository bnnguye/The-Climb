public class GameSettingsSingleton {

    private ObstaclesSettingsSingleton obstaclesSettingsSingleton = ObstaclesSettingsSingleton.getInstance();
    private PowerUpsSettingsSingleton powerUpsSettingsSingleton = PowerUpsSettingsSingleton.getInstance();
    private int page = 0;
    private double mapSpeed = 1;
    private int lives = 1;
    private Map map;
    private static GameSettingsSingleton single_instance = null;

    public synchronized static GameSettingsSingleton getInstance() {
        if (single_instance == null) {
            single_instance = new GameSettingsSingleton();
        }
        return single_instance;
    }

    public double getMapSpeed() {
        return mapSpeed;
    }
    public void setMapSpeed(double mapSpeed) {
        if (mapSpeed < 1) {
            mapSpeed = 1;
        }
        else if (mapSpeed > 3) {
            mapSpeed = 3;
        }
        this.mapSpeed = mapSpeed;
    }
    public ObstaclesSettingsSingleton getObstaclesSettingsSingleton() {return obstaclesSettingsSingleton.getInstance();}
    public PowerUpsSettingsSingleton getPowerUpsSettingsSingleton() {return powerUpsSettingsSingleton.getInstance();}
    public int getPage() {return page;}
    public void setPage(int num) {page = num;}

    public void setMap(Map map) {
        this.map = map;
    }

    public Map getMap() {
        return map;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        if (lives > 0) {
            this.lives = lives;
        }
    }
}