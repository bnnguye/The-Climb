public class ObstaclesSettingsSingleton {

    private static ObstaclesSettingsSingleton single_instance = null;

    private double rockFrequency = 0.98;
    private double ballFrequency = 0.99;
    private double stunBallFrequency = 0.995;
    private boolean rocks = true;
    private boolean balls = true;
    private boolean stunBalls = true;



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
        else if (obstacle.equals("StunBall")) {
            this.stunBallFrequency = frequency;
        }
    }

    public void applySettings(boolean rocks, boolean balls, boolean stunBalls) {
        this.rocks = rocks;
        this.balls = balls;
        this.stunBalls = stunBalls;
    }

    public void toggle(String obstacle) {
        if (obstacle.equals("Rock")) {
            this.rocks = !this.rocks;
        }
        else if (obstacle.equals("Ball")) {
            this.balls = !this.balls;
        }
        else if (obstacle.equals("StunBall")) {
            this.stunBalls = !this.stunBalls;
        }
    }

    public boolean isObstacle(String string) {
        if (string.equals("Rock")) {
            return this.rocks;
        }
        else if (string.equals("Ball")) {
            return this.balls;
        }
        if (string.equals("StunBall")) {
            return this.stunBalls;
        }
        return false;
    }

    public double getRockFrequency() {
        return rockFrequency;
    }

    public double getBallFrequency() {
        return ballFrequency;
    }

    public double getStunBallFrequency() {
        return stunBallFrequency;
    }

    public double getFrequency(String string) {
        if (string.equals("Rock")) {
            return rockFrequency;
        }
        else if (string.equals("Ball")) {
            return ballFrequency;
        }
        else if (string.equals("StunBall")) {
            return stunBallFrequency;
        }
        return 0;
    }

    public boolean isRocks() {return this.rocks;}
    public boolean isBalls() {return this.balls;}

    public boolean isStunBalls() {
        return this.stunBalls;
    }
}
