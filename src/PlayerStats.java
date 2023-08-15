public class PlayerStats {

    private int obstaclesDodged = 0;
    private int closeCalls = 0;
    private int special = 0;
    private int powerUpsUsed = 0;

    private int totalAbilityAccumulated = 0;
    private double totalDistanceTravelled = 0;

    public void obstacleDodged() {
        obstaclesDodged++;
    }

    public void closeCall() {
        closeCalls++;
    }

    public void abilityAccumulated(int abilityPoints) {
        totalAbilityAccumulated += abilityPoints;
    }

    public void addDistance(double distance) {
        totalDistanceTravelled += distance;
    }

    public void usedSpecial() {
        special++;
    }

    public void usedPowerUp() {
        powerUpsUsed++;
    }

    public int getObstaclesDodged() {
        return obstaclesDodged;
    }

    public int getCloseCalls() {
        return closeCalls;
    }

    public int getTotalAbilityAccumulated() {
        return totalAbilityAccumulated;
    }

    public double getTotalDistanceTravelled() {
        return totalDistanceTravelled;
    }

    public double getPoints() {
        return 50* obstaclesDodged + 200 * closeCalls;
    }

    public int getSpecial() {
        return this.special;
    }

    public void reset() {
        obstaclesDodged = 0;
        closeCalls = 0;
        totalAbilityAccumulated = 0;
        totalDistanceTravelled = 0;
        special = 0;
        powerUpsUsed = 0;
    }

}
