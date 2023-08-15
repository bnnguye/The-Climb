import Enums.PowerUps;

public class PowerUpsSettingsSingleton {

    private static PowerUpsSettingsSingleton single_instance = null;


    private double speedUpFrequency = 0.003;
    private double speedDownFrequency = 0.003;
    private double minimiserFrequency = 0.003;
    private double shieldFrequency = 0.003;
    private double specialAbilityFrequency = 0.003;

    private boolean speedUp = true;
    private boolean speedDown = true;
    private boolean minimiser = true;
    private boolean shield = true;
    private boolean specialAbility = true;


    public synchronized static PowerUpsSettingsSingleton getInstance() {
        if (single_instance == null) {
            single_instance = new PowerUpsSettingsSingleton();

        }
        return single_instance;
    }

    public void applySettings(boolean speedUp, boolean speedDown, boolean minimiser, boolean shield, boolean specialAbility) {
        this.speedUp = speedUp;
        this.speedDown = speedDown;
        this.minimiser = minimiser;
        this.shield = shield;
        this.specialAbility = specialAbility;
    }

    public void toggle(PowerUps powerUp) {
        if (powerUp == PowerUps.SPEEDUP) {
            speedUp = !speedUp;
        }
        else if (powerUp == PowerUps.MINIMISER) {
            minimiser = !minimiser;
        }
        else if (powerUp == PowerUps.SHIELD) {
            shield = !shield;
        }
        else if (powerUp == PowerUps.ABILITY) {
            specialAbility = !specialAbility;
        }
    }

    public void changeFrequency(double speedUp, double minimiser, double shield, double specialAbility) {
        this.speedUpFrequency = speedUp;
        if (speedUp <= 0) {
            this.speedUp = false;
        }
        this.minimiserFrequency = minimiser;
        if (minimiser <= 0) {
            this.minimiser = false;
        }
        this.shieldFrequency = shield;
        if (shield <= 0) {
            this.shield = false;
        }
        this.specialAbilityFrequency = specialAbility;
        if (specialAbility <= 0) {
            this.specialAbility = false;
        }

    }


    public double getFrequency(PowerUps type) {
        if (type == PowerUps.SPEEDUP) {
            return this.speedUpFrequency;
        }
        else if (type == PowerUps.SHIELD) {
            return this.shieldFrequency;
        }
        else if (type == PowerUps.MINIMISER) {
            return this.minimiserFrequency;
        }
        else if (type == PowerUps.ABILITY) {
            return this.specialAbilityFrequency;
        }
        return 0;
    }

    public boolean isPowerUp(PowerUps type) {
        if (type == PowerUps.ABILITY) {
            return specialAbility;
        }
        else if (type == PowerUps.SHIELD) {
            return shield;
        }
        else if (type == PowerUps.MINIMISER) {
            return minimiser;
        }
        else if (type == PowerUps.SPEEDUP) {
            return speedUp;
        }
        return false;
    }

    public void changeFrequency(PowerUps type, double frequency) {
        if (type == PowerUps.ABILITY) {
            specialAbilityFrequency = frequency;
        }
        else if (type == PowerUps.SHIELD) {
            shieldFrequency = frequency;
        }
        else if (type == PowerUps.MINIMISER) {
            minimiserFrequency = frequency;
        }
        else if (type == PowerUps.SPEEDUP) {
            speedUpFrequency = frequency;
        }
    }

}
