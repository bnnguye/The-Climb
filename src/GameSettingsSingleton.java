public class GameSettingsSingleton {

    private ObstaclesSettingsSingleton obstaclesSettingsSingleton = new ObstaclesSettingsSingleton();
    private PowerUpsSettingsSingleton powerUpsSettingsSingleton = new PowerUpsSettingsSingleton();
    private int page = 0;

    public double getMapSpeed() {
        return mapSpeed;
    }

    public void setMapSpeed(double mapSpeed) {
        this.mapSpeed = mapSpeed;
    }

    private double mapSpeed = 1;

    private static GameSettingsSingleton single_instance = null;

    public synchronized static GameSettingsSingleton getInstance() {
        if (single_instance == null) {
            single_instance = new GameSettingsSingleton();

        }
        return single_instance;
    }

    public ObstaclesSettingsSingleton getObstaclesSettingsSingleton() {return obstaclesSettingsSingleton;}
    public PowerUpsSettingsSingleton getPowerUpsSettingsSingleton() {return powerUpsSettingsSingleton;}
    public int getPage() {return page;}
    public void setPage(int num) {page = num;}

}