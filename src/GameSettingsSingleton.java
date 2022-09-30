import java.util.ArrayList;

public class GameSettingsSingleton {

    private ObstaclesSettingsSingleton obstaclesSettingsSingleton = ObstaclesSettingsSingleton.getInstance();
    private PowerUpsSettingsSingleton powerUpsSettingsSingleton = PowerUpsSettingsSingleton.getInstance();
    private static ArrayList<Slider> allSliders = new ArrayList<>();
    private ArrayList<Slider> currentSliders = new ArrayList<>();
    private int page = 0;
    private double mapSpeed = 1;
    private int lives = 1;
    private Map map;
    private static GameSettingsSingleton single_instance = null;

    public synchronized static GameSettingsSingleton getInstance() {
        if (single_instance == null) {
            single_instance = new GameSettingsSingleton();
            
            allSliders.add(new Slider("Minimiser", "PowerUp"));
            allSliders.add(new Slider("Shield", "PowerUp"));
            allSliders.add(new Slider("SpecialAbilityPoints", "PowerUp"));
            allSliders.add(new Slider("SpeedUp", "PowerUp"));
            allSliders.add(new Slider("Rock", "obstacles"));
            allSliders.add(new Slider("BowlingBall", "obstacles"));
            allSliders.add(new Slider("StunBall", "obstacles"));

        }
        return single_instance;
    }

    public double getMapSpeed() {
        return mapSpeed;
    }
    public void setMapSpeed(double mapSpeed) {
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
        this.lives = lives;
    }
}