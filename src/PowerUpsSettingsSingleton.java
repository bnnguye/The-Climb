public class PowerUpsSettingsSingleton {

    private static PowerUpsSettingsSingleton single_instance = null;


    private double frequency = 0.95;
    private boolean speedUp = true;
    private boolean speedDown = true;
    private boolean minimiser = true;
    private boolean shield = true;

    public boolean isSpeedUp() {
        return speedUp;
    }

    public boolean isSpeedDown() {
        return speedDown;
    }

    public boolean isMinimiser() {
        return minimiser;
    }

    public boolean isShield() {
        return shield;
    }

    public boolean isNoblePhantasm() {
        return noblePhantasm;
    }

    private boolean noblePhantasm = true;



    public synchronized static PowerUpsSettingsSingleton getInstance() {
        if (single_instance == null) {
            single_instance = new PowerUpsSettingsSingleton();

        }
        return single_instance;
    }

    public void applySettings(boolean speedUp, boolean speedDown, boolean minimiser, boolean shield, boolean noblePhantasm) {
        this.speedUp = speedUp;
        this.speedDown = speedDown;
        this.minimiser = minimiser;
        this.shield = shield;
        this.noblePhantasm = noblePhantasm;
    }

    public void changeFrequency(double frequency) {
        this.frequency = frequency;
    }

    public double getFrequency() {return frequency;}
}
