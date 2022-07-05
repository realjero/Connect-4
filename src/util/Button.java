package util;

import processing.core.PApplet;

public abstract class Button {
    protected int x, y, w, h;
    protected String label;
    protected int size;

    public Button(int x, int y, int w, int h, String label, int size) {
        //Center the button on the x and y coordinates
        this.x = x - w / 2;
        this.y = y - h / 2;
        this.w = w;
        this.h = h;
        this.label = label;
        this.size = size;
    }

    public abstract void draw(PApplet p);

    public abstract boolean mouseOver(int mouseX, int mouseY);
}
