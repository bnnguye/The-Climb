public class PowerUpsSettingsSingleton {

    private static PowerUpsSettingsSingleton single_instance = null;


    private double speedUpFrequency = 0.95;
    private double speedDownFrequency = 0.95;
    private double minimiserFrequency = 0.95;
    private double shieldFrequency = 0.95;

    public double getSpeedUpFrequency() {
        return speedUpFrequency;
    }

    public double getSpeedDownFrequency() {
        return speedDownFrequency;
    }

    public double getMinimiserFrequency() {
        return minimiserFrequency;
    }

    public double getShieldFrequency() {
        return shieldFrequency;
    }

    public double getNoblePhantasmFrequency() {
        return noblePhantasmFrequency;
    }

    private double noblePhantasmFrequency = 0.95;
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

    public void changeFrequency(double speedUp, double speedDown, double minimiser, double shield, double noblePhantasm) {
        this.speedUpFrequency = speedUp;
        if (speedUp <= 0) {
            this.speedUp = false;
        }
        this.speedDownFrequency = speedDown;
        if (speedDown <= 0) {
            this.speedDown = false;
        }
        this.minimiserFrequency = minimiser;
        if (minimiser <= 0) {
            this.minimiser = false;
        }
        this.shieldFrequency = shield;
        if (shield <= 0) {
            this.shield = false;
        }
        this.noblePhantasmFrequency = noblePhantasm;
        if (noblePhantasm <= 0) {
            this.noblePhantasm = false;
        }

    }

}
