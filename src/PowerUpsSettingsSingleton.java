public class PowerUpsSettingsSingleton {

    private static PowerUpsSettingsSingleton single_instance = null;


    private double speedUpFrequency = 0.997;
    private double speedDownFrequency = 0.997;
    private double minimiserFrequency = 0.997;
    private double shieldFrequency = 0.997;
    private double noblePhantasmFrequency = 0.997;

    private boolean speedUp = true;
    private boolean speedDown = true;
    private boolean minimiser = true;
    private boolean shield = true;
    private boolean noblePhantasm = true;


    public double getFrequency(String string) {
        if (string.equals("SpeedUp")) {
            return this.speedUpFrequency;
        }
        else if (string.equals("SpeedDown")) {
            return this.speedDownFrequency;
        }
        else if (string.equals("Shield")) {
            return this.shieldFrequency;
        }
        else if (string.equals("Minimiser")) {
            return this.minimiserFrequency;
        }
        else if (string.equals("NoblePhantasm")) {
            return this.noblePhantasmFrequency;
        }
        return 0;
    }

    public boolean isPowerUp(String string) {
        if (string.equals("SpeedUp")) {
            return this.speedUp;
        }
        else if (string.equals("SpeedDown")) {
            return this.speedDown;
        }
        else if (string.equals("Minimiser")) {
            return this.minimiser;
        }
        else if (string.equals("Shield")) {
            return this.shield;
        }
        else if (string.equals("NoblePhantasm")) {
            return this.noblePhantasm;
        }
        return false;
    }

    public void changeFrequency(String powerUp, double frequency) {
        if (powerUp.equals("SpeedUp")) {
            speedUpFrequency = frequency;
        }
        else if (powerUp.equals("SpeedDown")) {
            speedDownFrequency = frequency;
        }
        else if (powerUp.equals("Minimiser")) {
            minimiserFrequency = frequency;
        }
        else if (powerUp.equals("Shield")) {
            shieldFrequency = frequency;
        }
        else if (powerUp.equals("NoblePhantasm")) {
            noblePhantasmFrequency = frequency;
        }
    }



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

    public void toggle(String powerUp) {
        if (powerUp.equals("SpeedUp")) {
            speedUp = !speedUp;
        }
        else if (powerUp.equals("SpeedDown")) {
            speedDown = !speedDown;
        }
        else if (powerUp.equals("Minimiser")) {
            minimiser = !minimiser;
        }
        else if (powerUp.equals("Shield")) {
            shield = !shield;
        }
        else if (powerUp.equals("NoblePhantasm")) {
            noblePhantasm = !noblePhantasm;
        }
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
