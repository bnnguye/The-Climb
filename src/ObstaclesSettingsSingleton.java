public class ObstaclesSettingsSingleton {

    private static ObstaclesSettingsSingleton single_instance = null;

    private double rockFrequency = 0.98;
    private double ballFrequency = 0.99;
    private boolean rocks = true;
    private boolean balls = true;



    public synchronized static ObstaclesSettingsSingleton getInstance() {
        if (single_instance == null) {
            single_instance = new ObstaclesSettingsSingleton();
        }
        return single_instance;
    }

    public void changeFrequency(String obstacle, double frequency) {
        if (obstacle.equals("Ball")) {
            this.ballFrequency = frequency;
        }
        else if (obstacle.equals("Rock")) {
            this.rockFrequency = frequency;
        }
    }

    public void applySettings(boolean rocks, boolean balls) {
        this.rocks = rocks;
        this.balls = balls;
    }

    public void toggle(String obstacle) {
        if (obstacle.equals("Rock")) {
            this.rocks = !this.rocks;
        }
        else if (obstacle.equals("Ball")) {
            this.balls = !this.balls;
        }
    }

    public boolean isObstacle(String string) {
        if (string.equals("Rock")) {
            return this.rocks;
        }
        else if (string.equals("Ball")) {
            return this.balls;
        }
        return false;
    }

    public double getRockFrequency() {
        return rockFrequency;
    }

    public double getBallFrequency() {
        return ballFrequency;
    }

    public double getFrequency(String string) {
        if (string.equals("Rock")) {
            return rockFrequency;
        }
        else if (string.equals("Ball")) {
            return ballFrequency;
        }
        return 0;
    }

    public boolean isRocks() {return this.rocks;}
    public boolean isBalls() {return this.balls;}
}
