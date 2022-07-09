import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import processing.core.PApplet;
import processing.core.PFont;
import util.Button;
import util.SelectButton;
import util.CircleButton;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Jerome Habanz
 */
public class Connect4GUI extends PApplet {
    Connect4 connect4;
    Button buttonPvC, buttonPvP, buttonUndo, buttonMenu;
    boolean inMenu;
    boolean withAI;
    PFont font;
    ExecutorService buttonListener;
    boolean calculating;

    public static void main(String[] args) {
        BasicConfigurator.configure();
        Connect4Logic.logger.setLevel(Level.OFF);
        PApplet.runSketch(new String[]{"Connect 4"}, new Connect4GUI());
    }

    /**
     * Initialize the game
     */
    public void settings() {
        size(Connect4.COLUMNS * 100, (Connect4.ROWS + 1) * 100);

        buttonListener = Executors.newSingleThreadExecutor();

        inMenu = true;
        withAI = false;
        calculating = false;

        connect4 = new Connect4Logic();
    }

    public void setup() {
        background(color(44, 62, 80));
        font = createFont("files/Oswald-Bold.ttf", 24);
        buttonPvC = new SelectButton(width / 2, height / 2, "PLAYER VS COMPUTER", font);
        buttonPvP = new SelectButton(width / 2, height / 2 + 110, "PLAYER VS PLAYER", font);
        buttonUndo = new CircleButton(width - 50, height - 40, "UNDO", font);
        buttonMenu = new CircleButton(width - 150, height - 40, "MENU", font);
    }

    /**
     * Drawing menu or board depending on inMenu
     */
    public void draw() {
        if (inMenu) {
            drawMenu();
        } else {
            drawBoard();
        }
    }

    /**
     * Draw menu with buttons
     */
    public void drawMenu() {
        background(color(44, 62, 80));


        textFont(font);
        fill(255);
        textSize(100);
        text("CONNECT 4", width / 2 - textWidth("CONNECT 4") / 2, height / 4);


        buttonPvC.draw(this);
        buttonPvP.draw(this);
    }

    /**
     * Draw GameOver text, the board and the current piece on mouse x
     */
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
        if (!calculating) { //listener is not shutdown??
            fill(connect4.getPlayer() ? color(243, 165, 157) : color(248, 225, 135));

            int height = Connect4.ROWS - (connect4.getNextHeight(mouseX / 100));

            if (height < Connect4.ROWS) {
                ellipse(mouseX / 100 * 100 + 50, height * 100 + 50, 90, 90);
            }
        }
    }

    /**
     * Generate a thread detecting any mouse Collision with button while rendering the board
     */
    public void mouseClicked() {
        if (!calculating) {
            buttonListener.submit(this::mouseOverAnyButton);
        }
    }

    /**
     * Check for any mouse collision with buttons
     */
    public void mouseOverAnyButton() {
        try {
            calculating = true;
            if (inMenu) {
                if (buttonPvC.mouseOver(mouseX, mouseY)) {
                    inMenu = false;
                    withAI = true;
                    connect4 = new Connect4Logic();

                    //AI FIRST
                    //connect4 = connect4.play(connect4.bestMove());
                }
                if (buttonPvP.mouseOver(mouseX, mouseY)) {
                    inMenu = false;
                    withAI = false;
                    connect4 = new Connect4Logic();
                }
            } else {
                if (buttonUndo.mouseOver(mouseX, mouseY)) {
                    connect4 = connect4.undoMove();
                }
                if (buttonMenu.mouseOver(mouseX, mouseY)) {
                    inMenu = true;
                }

                if (!connect4.isGameOver() && mouseY < height - 100) {
                    connect4 = connect4.play(mouseX / 100);
                    if (withAI) connect4 = connect4.play(connect4.bestMove());
                }
            }
            calculating = false;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
