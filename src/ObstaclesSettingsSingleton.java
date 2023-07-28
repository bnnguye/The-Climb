import Enums.Obstacles;

public class ObstaclesSettingsSingleton {

    private static ObstaclesSettingsSingleton single_instance = null;
    private double rockFrequency = 0.003;
    private double ballFrequency = 0.003;
    private double stunBallFrequency = 0.003;
    private double speedDownFrequency = 0.003;
    private boolean rocks = true;
    private boolean balls = true;
    private boolean stunBalls = true;
    private boolean speedDown = true;



    public synchronized static ObstaclesSettingsSingleton getInstance() {
        if (single_instance == null) {
            single_instance = new ObstaclesSettingsSingleton();
        }
        return single_instance;
    }

    public void changeFrequency(Obstacles obstacle, double frequency) {
        if (obstacle == Obstacles.ROCK) {
            this.rockFrequency = frequency;
        }
        else if (obstacle == Obstacles.BALL) {
            this.ballFrequency = frequency;
        }
        else if (obstacle == Obstacles.STUNBALL) {
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

    public boolean isObstacle(Obstacles type) {
        if (type == Obstacles.ROCK) {
            return this.rocks;
        }
        else if (type == Obstacles.BALL) {
            return this.balls;
        }
        else if (type == Obstacles.STUNBALL) {
            return this.stunBalls;
        }
        else if (type == Obstacles.SPEEDDOWN) {
            return  this.speedDown;
        }
        return false;
    }

    public double getFrequency(Obstacles obstacle) {
        if (obstacle == Obstacles.ROCK) {
            return rockFrequency;
        }
        else if (obstacle == Obstacles.BALL) {
            return ballFrequency;
        }
        else if (obstacle == Obstacles.STUNBALL) {
            return stunBallFrequency;
        }
        else if (obstacle == Obstacles.SPEEDDOWN) {
            return speedDownFrequency;
        }
        return 0;
    }
}
