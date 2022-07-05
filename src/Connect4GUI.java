import processing.core.PApplet;
import processing.core.PFont;
import util.Button;
import util.SelectButton;
import util.CircleButton;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Connect4GUI extends PApplet {
    Connect4 connect4;
    Button buttonPvC, buttonPvP, buttonUndo, buttonMenu;
    boolean inMenu;
    boolean withAI;
    PFont font;
    ExecutorService service;

    public static void main(String[] args) {
        PApplet.runSketch(new String[]{"Connect 4"}, new Connect4GUI());
    }

    public void settings() {
        size(Connect4.COLUMNS * 100, (Connect4.ROWS + 1) * 100);
        service = Executors.newSingleThreadExecutor();
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
        if (inMenu) {
            drawMenu();
        } else {
            drawBoard();
        }
    }

    public void drawMenu() {
        background(color(44, 62, 80));


        textFont(font);
        fill(255);
        textSize(100);
        text("CONNECT 4", width / 2 - textWidth("CONNECT 4") / 2, height / 4);


        buttonPvC.draw(this);
        buttonPvP.draw(this);
    }

    public void drawBoard() {
        background(color(44, 62, 80));
        int color;


        buttonUndo.draw(this);
        buttonMenu.draw(this);

        //Game Over
        if (connect4.isGameOver()) {
            fill(color(236, 240, 241));
            textSize(50);
            String label = switch (connect4.intIsGameOver()) {
                case 1 -> "YELLOW WON";
                case 2 -> "RED WON";
                case 3 -> "TIE";
                default -> "";
            };
            text(label, width / 2 - textWidth(label) / 2, height - textDescent());
        }


        //Draw the Board
        for (int c = 0; c < Connect4.COLUMNS; c++) {
            for (int r = 0; r < Connect4.ROWS; r++) {
                color = switch (connect4.getBoard(c, r)) {
                    case 1 -> color(231, 76, 60);
                    case 2 -> color(241, 196, 15);
                    default -> color(236, 240, 241);
                };

                noStroke();
                fill(color);

                ellipse(c * 100 + 50, r * 100 + 50, 90, 90);
            }
        }


        //Draw the current player on mouse X
        color = connect4.getPlayer() ? color(243, 165, 157) : color(248, 225, 135);

        fill(color);
        int height = Connect4.ROWS - (connect4.getNextHeight(mouseX / 100));

        if (height < Connect4.ROWS) {
            ellipse(mouseX / 100 * 100 + 50, height * 100 + 50, 90, 90);
        }
    }

    public void mouseClicked() {
        if (!service.isShutdown()) {
            service.submit(this::mouseOverAnyButton);
        }
    }

    public void mouseOverAnyButton() {
        try {
            if (inMenu) {
                if (buttonPvC.mouseOver(mouseX, mouseY)) {
                    inMenu = false;
                    withAI = true;
                }
                if (buttonPvP.mouseOver(mouseX, mouseY)) {
                    inMenu = false;
                    withAI = false;
                }
            } else {
                if (buttonUndo.mouseOver(mouseX, mouseY)) {
                    connect4 = connect4.undoMove();
                    if (withAI) connect4 = connect4.undoMove();
                }

                if (buttonMenu.mouseOver(mouseX, mouseY)) {
                    inMenu = true;
                    connect4 = new Connect4Logic();
                }

                if (!connect4.isGameOver() && mouseY < height - 100) {
                    connect4 = connect4.play(mouseX / 100);
                    if (withAI) {
                        long startTime = System.nanoTime();
                        connect4 = connect4.play(connect4.bestMove());
                        System.out.println((System.nanoTime() - startTime) / 1000000 + " ms");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
