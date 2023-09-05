import Enums.Obstacles;

public class ObstaclesSettingsSingleton {

    private static ObstaclesSettingsSingleton single_instance = null;
    private double rockFrequency = 0.01;
    private double ballFrequency = 0.008;
    private double ObstacleDioFrequency = 0.005;
    private double speedDownFrequency = 0.005;
    private boolean rocks = true;
    private boolean balls = true;
    private boolean ObstacleDios = true;
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
        else if (obstacle == Obstacles.DIO) {
            this.ObstacleDioFrequency = frequency;
        }
        else if (obstacle == Obstacles.SPEEDDOWN) {
            this.speedDownFrequency = frequency;
        }
    }

//    public void applySettings(boolean... rocks) {
//        this.rocks = rocks;
//        this.balls = balls;
//        this.ObstacleDios = ObstacleDios;
//    }

    public void toggle(Obstacles obstacle) {
        if (obstacle == Obstacles.ROCK) {
            this.rocks = !this.rocks;
        }
        else if (obstacle == Obstacles.BALL) {

            this.balls = !this.balls;
        }
        else if (obstacle == Obstacles.DIO) {
            this.ObstacleDios = !this.ObstacleDios;
        }
        else if (obstacle == Obstacles.SPEEDDOWN) {
            this.speedDown = !this.speedDown;
        }
    }

    public boolean isObstacle(Obstacles type) {
        if (type == Obstacles.ROCK) {
            return this.rocks;
        }
        else if (type == Obstacles.BALL) {
            return this.balls;
        }
        else if (type == Obstacles.DIO) {
            return this.ObstacleDios;
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
        else if (obstacle == Obstacles.DIO) {
            return ObstacleDioFrequency;
        }
        else if (obstacle == Obstacles.SPEEDDOWN) {
            return speedDownFrequency;
        }
        return 0;
    }
}
