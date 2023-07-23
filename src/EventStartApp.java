import bagel.DrawOptions;
import bagel.Drawing;
import bagel.Window;
import bagel.util.Colour;

public class EventStartApp extends EventInterface {
    private boolean canInteract = true;

    public EventStartApp(int frames, String event) {
        this.event = event;
        this.frames = TimeLogger.getInstance().getFrames() + frames;
    }

    @Override
    public int getFrames() {
        return frames;
    }

    @Override
    public String getEvent() {
        return event;
    }

    @Override
    public boolean isCanInteract() {
        return canInteract;
    }

    public void process() {
        double currentTime = TimeLogger.getInstance().getFrames();
        double refreshRate = SettingsSingleton.getInstance().getRefreshRate();
        Drawing.drawRectangle(0,0, Window.getWidth(), Window.getHeight(), ColourPresets.WHITE.toColour());
        if (frames - currentTime <= 3.5 * refreshRate) {
            String string = "Made by Bill Nguyen";
            FontSize introFont = new FontSize(Fonts.STORYTIME, 80);
            introFont.getFont().drawString(string,
                    (Window.getWidth() - introFont.getFont().getWidth(string))/2, Window.getHeight()/2,
                    new DrawOptions().setBlendColour(ColourPresets.BLACK.toColour()));
        }
        if (frames - currentTime > 1 * refreshRate) {
            Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), new Colour(1,1,1, 1 - 2*( currentTime - 0.5 * refreshRate)/(frames - 0.5 * refreshRate)));
        }
        else if (frames - currentTime < 1 * refreshRate){
            Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), new Colour(1,1,1, 2*( currentTime - 1.5 * refreshRate)/(frames - 1.5 * refreshRate)));
        }
        if (frames - currentTime == 1) {
            SettingsSingleton.getInstance().setGameState(-100);
        }
    }
}
