import bagel.DrawOptions;
import bagel.Drawing;
import bagel.Window;
import bagel.util.Colour;
import bagel.util.Point;

public class EventGameStart extends  EventInterface {

    int refreshRate = TimeLogger.getInstance().getRefreshRate();

    private boolean init = false;

    public EventGameStart() {
        this.frames = (5 * refreshRate) + TimeLogger.getInstance().getFrames();
    }

    public void process() {
        int currentTime = TimeLogger.getInstance().getFrames();
        FontSize countDownFont = new FontSize(Fonts.DEJAVUSANS, 250);
        Map map = GameSettingsSingleton.getInstance().getMap();
        canInteract = false;
        Drawing.drawRectangle(0,0, Window.getWidth(), Window.getHeight(), new Colour(0,0,0,0.8));

        if (!init) {
            init = true;
            map.goToSummit();
        }
        else if (frames - currentTime < 4 * refreshRate) {
            if (map.currentHeight > 0) {
                map.descend();
                this.frames++;
            }
        }
        if (frames - currentTime == 3 * refreshRate) {
            MusicPlayer.getInstance().addMusic("music/misc/Start.wav");
        }
        if (frames - currentTime <= 3 * refreshRate) {

            countDownFont.getFont().drawString(String.format("%d", (frames - currentTime)/refreshRate + 1), Window.getWidth()/2 - countDownFont.getFont().getWidth(String.format("%d", (frames - currentTime)/refreshRate + 1))/2, Window.getHeight()/2, new DrawOptions().setBlendColour(ColourPresets.BLACK.toColour()));
        }
        else {
            FontSize mapFont = new FontSize(Fonts.GEOMATRIX, 100);
            String mapName = GameSettingsSingleton.getInstance().getMap().getName();
            mapFont.draw(mapName.toUpperCase(), Window.getWidth()/2 - mapFont.getFont().getWidth(mapName)/2, Window.getHeight()/2 - 50);
        }
    }
}
