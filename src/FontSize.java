import bagel.DrawOptions;
import bagel.Font;

public class FontSize {
    private Font font;

    public Font getFont() {
        return font;
    }

    public int getSize() {
        return size;
    }

    private int size;

    public FontSize(String fontName, int size) {
        this.font = new Font(fontName, size);
        this.size = size;
    }

    public void draw(String string, double x, double y) {
        font.drawString(string, x, y);
    }

    public void draw(String string, double x, double y, DrawOptions DO) {
        font.drawString(string, x, y, DO);
    }
}
