public class ObstaclesSettingsSingleton {

    private static ObstaclesSettingsSingleton single_instance = null;

    private double frequency = 0.95;
    private boolean rocks = true;
    private boolean balls = true;



    public synchronized static ObstaclesSettingsSingleton getInstance() {
        if (single_instance == null) {
            single_instance = new ObstaclesSettingsSingleton();
        }
        return single_instance;
    }

    public void changeFrequency(double frequency) {
        this.frequency = frequency;
    }

    public void applySettings(boolean rocks, boolean balls) {
        this.rocks = rocks;
        this.balls = balls;
    }

    public boolean isRocks() {return this.rocks;}
    public boolean isBalls() {return this.balls;}
    public double getFrequency() {return this.frequency;}
}
