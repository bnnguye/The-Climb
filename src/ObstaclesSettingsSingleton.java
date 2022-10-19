public class ObstaclesSettingsSingleton {

    private static ObstaclesSettingsSingleton single_instance = null;

    private double rockFrequency = 0.98;
    private double ballFrequency = 0.99;
    private double stunBallFrequency = 0.998;
    private boolean rocks = true;
    private boolean balls = true;
    private boolean stunBalls = true;
    private double minimumFrequency = 0.98;



    public synchronized static ObstaclesSettingsSingleton getInstance() {
        if (single_instance == null) {
            single_instance = new ObstaclesSettingsSingleton();
        }
        return single_instance;
    }

    public void changeFrequency(String obstacle, double frequency) {
        if (obstacle.equalsIgnoreCase("Ball")) {
            this.ballFrequency = frequency;
        }
        else if (obstacle.equalsIgnoreCase("Rock")) {
            this.rockFrequency = frequency;
        }
        else if (obstacle.equalsIgnoreCase("StunBall")) {
            this.stunBallFrequency = frequency;
        }
    }

    public void applySettings(boolean rocks, boolean balls, boolean stunBalls) {
        this.rocks = rocks;
        this.balls = balls;
        this.stunBalls = stunBalls;
    }

    public void toggle(String obstacle) {
        if (obstacle.equalsIgnoreCase("Rock")) {
            this.rocks = !this.rocks;
        }
        else if (obstacle.equalsIgnoreCase("Ball")) {
            this.balls = !this.balls;
        }
        else if (obstacle.equalsIgnoreCase("StunBall")) {
            this.stunBalls = !this.stunBalls;
        }
    }

    public boolean isObstacle(String string) {
        if (string.equalsIgnoreCase("Rock")) {
            return this.rocks;
        }
        else if (string.equalsIgnoreCase("Ball")) {
            return this.balls;
        }
        if (string.equalsIgnoreCase("StunBall")) {
            return this.stunBalls;
        }
        return false;
    }

    public double getFrequency(String obstacle) {
        if (obstacle.equalsIgnoreCase("rock")) {
            return rockFrequency;
        }
        else if (obstacle.equalsIgnoreCase("ball")) {
            return ballFrequency;
        }
        else if (obstacle.equalsIgnoreCase("stunball")) {
            return stunBallFrequency;
        }
        return 0;
    }

    public boolean isRocks() {return this.rocks;}
    public boolean isBalls() {return this.balls;}

    public boolean isStunBalls() {
        return this.stunBalls;
    }

    public double getMinimumFrequency() {
        return minimumFrequency;
    }
}
