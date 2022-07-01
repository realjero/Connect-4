import processing.core.PApplet;
import processing.core.PFont;
import util.Button;
import util.SelectButton;
import util.CircleButton;

import javax.swing.*;

public class Connect4GUI extends PApplet {
    Connect4 connect4;
    Button buttonPvC, buttonPvP, buttonUndo, buttonMenu;
    boolean inMenu;
    boolean withAI;
    PFont font;

    public static void main(String[] args) {
        PApplet.runSketch(new String[]{"Connect 4"}, new Connect4GUI());
    }

    public void settings() {
        size(Connect4.COLUMNS * 100, (Connect4.ROWS + 1) * 100);
    }

    public void setup() {
        background(color(44, 62, 80));
        connect4 = new Connect4Logic();

        font = createFont("files/Oswald-Bold.ttf", 24);

        buttonPvC = new SelectButton(width / 2, height / 2, "PLAYER VS COMPUTER");
        buttonPvP = new SelectButton(width / 2, height / 2 + 110, "PLAYER VS PLAYER");
        buttonUndo = new CircleButton(width - 50, height - 40, "UNDO");
        buttonMenu = new CircleButton(width - 150, height - 40, "MENU");

        inMenu = true;
        withAI = false;
    }

    public void draw() {
        int color;

        if (inMenu) {
            showMenu();
        } else {
            showBoard();

            buttonUndo.draw(this);
            buttonMenu.draw(this);

            if (connect4.isGameOver()) {
                fill(color(236, 240, 241));
                textSize(50);
                String label = "";
                switch (connect4.intIsGameOver()) {
                    case 1:
                        label = "YELLOW WON";
                        break;
                    case 2:
                        label = "RED WON";
                        break;
                    case 3:
                        label = "TIE";
                        break;
                    default:
                        break;
                }
                text(label, width / 2 - textWidth(label) / 2, height - textDescent());
            } else {
                for (int c = 0; c < Connect4.COLUMNS; c++) {
                    if (mouseX > c * 100 && mouseX < (c + 1) * 100) {
                        if (connect4.getPlayer()) {
                            color = color(243, 165, 157);
                        } else {
                            color = color(248, 225, 135);
                        }
                        fill(color);
                        int height = Connect4.ROWS - (connect4.getNextHeight(c));
                        System.out.println(height);
                        if (height < Connect4.ROWS) {
                            ellipse(c * 100 + 50, height * 100 + 50, 90, 90);
                        }
                    }

                }
            }
        }
    }

    public void showMenu() {
        background(color(44, 62, 80));

        textFont(font);
        fill(255);
        textSize(100);
        text("CONNECT 4", width / 2 - textWidth("CONNECT 4") / 2, height / 4);

        buttonPvC.draw(this);
        buttonPvP.draw(this);
    }

    public void showBoard() {
        background(color(44, 62, 80));
        int color;

        for (int y = 0; y < Connect4.ROWS; y++) {
            for (int x = 0; x < Connect4.COLUMNS; x++) {
                switch (connect4.getBoard(x, y)) {
                    case 1:
                        color = color(231, 76, 60);
                        break;
                    case 2:
                        color = color(241, 196, 15);
                        break;
                    default:
                        color = color(236, 240, 241);
                        break;
                }

                noStroke();
                fill(color);

                ellipse(x * 100 + 50, y * 100 + 50, 90, 90);
            }
        }
    }

    @Override
    public void mouseClicked() {
        if (inMenu) {
            if (buttonPvC.mouseOverButton(mouseX, mouseY)) {
                inMenu = false;
                withAI = true;
            }
            if (buttonPvP.mouseOverButton(mouseX, mouseY)) {
                inMenu = false;
                withAI = false;
            }
        } else {
            if (buttonUndo.mouseOverButton(mouseX, mouseY)) {
                try {
                    connect4 = connect4.undoMove();
                    if (withAI) {
                        connect4 = connect4.undoMove();
                    }
                } catch (RuntimeException e) {
                    System.out.println(e.getMessage());
                }

            }

            if (buttonMenu.mouseOverButton(mouseX, mouseY)) {
                inMenu = true;
                connect4 = new Connect4Logic();
            }

            if (!connect4.isGameOver() && mouseY < height - 100) {
                for (int i = 0; i < Connect4.COLUMNS; i++) {
                    if (mouseX > i * 100 && mouseX < (i + 1) * 100) {
                        try {
                            connect4 = connect4.play(i);
                            if (withAI) {
                                connect4 = connect4.play(connect4.bestMove());
                            }
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }
            }
        }
    }
}
