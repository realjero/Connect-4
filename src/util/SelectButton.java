package util;

import processing.core.PApplet;
import processing.core.PFont;

/**
 * @author Jerome Habanz
 */
public class SelectButton extends Button {
    public SelectButton(int x, int y, String label, PFont font) {
        super(x, y, 500, 100, label, 50, font);
    }

    public void draw(PApplet p) {

        p.fill(p.color(241, 196, 15));
        p.noStroke();
        p.rect(x, y, w, h);

        if (mouseOver(p.mouseX, p.mouseY)) {
            p.fill(255);
        } else {
            p.fill(p.color(52, 73, 94));
        }

        p.textFont(font);
        p.textSize(size);

        p.text(label, x + w / 2 - p.textWidth(label) / 2, y + h / 2 + p.textDescent() + 5);
    }

    public boolean mouseOver(int mouseX, int mouseY) {
        return mouseX > x && mouseX < x + w && mouseY > y && mouseY < y + h;
    }
}
