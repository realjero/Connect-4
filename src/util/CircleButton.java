package util;

import processing.core.PApplet;

public class CircleButton extends Button {
    public CircleButton(int x, int y, String label) {
        super(x + 75 / 2, y + 75 / 2, 75, 75, label, 20);
    }

    public void draw(PApplet p) {

        p.fill(p.color(241, 196, 15));
        p.noStroke();
        p.ellipse(x, y, w, h);

        if(mouseOverButton(p.mouseX, p.mouseY)) {
            p.fill(255);
        } else {
            p.fill(p.color(52, 73, 94));
        }
        p.textFont(p.createFont("files/Oswald-Bold.ttf", 24));
        p.textSize(size);

        p.text(label, x - p.textWidth(label) / 2, y + p.textDescent() + 5);
    }

    public boolean mouseOverButton(int mouseX, int mouseY) {
        return Math.sqrt(Math.pow(x - mouseX, 2) + Math.pow(y - mouseY, 2)) <= w / 2;
    }
}
