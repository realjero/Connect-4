package util;

import processing.core.PApplet;
import processing.core.PFont;

/**
 * @author Jerome Habanz
 */
public abstract class Button {
    protected int x, y, w, h;
    protected String label;
    protected int size;
    protected PFont font;

    public Button(int x, int y, int w, int h, String label, int size, PFont font) {
        //Center the button on the x and y coordinates
        this.x = x - w / 2;
        this.y = y - h / 2;
        this.w = w;
        this.h = h;
        this.label = label;
        this.size = size;
        this.font = font;
    }

    public abstract void draw(PApplet p);

    public abstract boolean mouseOver(int mouseX, int mouseY);
}
