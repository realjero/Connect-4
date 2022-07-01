package util;

import processing.core.PApplet;
import processing.core.PFont;
public abstract class Button {
    protected int x, y, w, h;
    protected String label;
    protected PFont font;
    protected int size;

    public Button(int x, int y, int w, int h, String label, int size) {
        this.x = x - w / 2;
        this.y = y - h / 2;
        this.w = w;
        this.h = h;
        this.label = label;
        this.size = size;
    }

    public abstract void draw(PApplet p);

    public boolean mouseOverButton(int mouseX, int mouseY) {
        return mouseX > x && mouseX < x + w && mouseY > y && mouseY < y + h;
    }
}
