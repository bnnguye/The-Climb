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
        int currentTime = TimeLogger.getInstance().getFrames();
        int refreshRate = SettingsSingleton.getInstance().getRefreshRate();
        Drawing.drawRectangle(0,0, Window.getWidth(), Window.getHeight(), ColourPresets.WHITE.toColour());
        if (frames - currentTime <= 2.5 * refreshRate) {
            String string = "Made by Bill Nguyen";
            FontSize introFont = new FontSize(Fonts.STORYTIME, 80);
            introFont.getFont().drawString(string,
                    (Window.getWidth() - introFont.getFont().getWidth(string))/2, Window.getHeight()/2,
                    new DrawOptions().setBlendColour(ColourPresets.BLACK.toColour()));
        }
        Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), new Colour(1,1,1, 1 - 2*((double) currentTime - 2 * refreshRate)/(frames - 2 * refreshRate)));
        if (frames <= currentTime) {
            canInteract = true;
            SettingsSingleton.getInstance().setGameState(0);
        }
    }
}
