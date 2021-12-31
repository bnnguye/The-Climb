public class ObstaclesSettingsSingleton {

    private static ObstaclesSettingsSingleton single_instance = null;

    private double rockFrequency = 0.95;

    public double getRockFrequency() {
        return rockFrequency;
    }

    public double getBallFrequency() {
        return ballFrequency;
    }

    private double ballFrequency = 0.98;
    private boolean rocks = true;
    private boolean balls = true;



    public synchronized static ObstaclesSettingsSingleton getInstance() {
        if (single_instance == null) {
            single_instance = new ObstaclesSettingsSingleton();
        }
        return single_instance;
    }

    public void changeRockFrequency(double frequency) {
        this.rockFrequency = frequency;
    }
    public void changeBallFrequency(double frequency) {
        this.ballFrequency = frequency;
    }

    public void applySettings(boolean rocks, boolean balls) {
        this.rocks = rocks;
        this.balls = balls;
    }

    public boolean isRocks() {return this.rocks;}
    public boolean isBalls() {return this.balls;}
}
