import processing.core.PApplet;

public class Connect4GUI extends PApplet {

    Connect4 connect4;

    public static void main(String[] args) {
        PApplet.runSketch(new String[]{"Connect 4"}, new Connect4GUI());
    }

    public void settings() {
        size(Connect4.COLUMNS * 100, (Connect4.ROWS + 1) * 100);
    }

    public void setup() {
        background(color(44, 62, 80));
        connect4 = new Connect4Logic();
    }


    public void draw() {
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

                ellipse(x * 100 + 50, y * 100 + 75, 90, 90);
            }
        }

        if (connect4.isGameOver()) {
            fill(color(236, 240, 241));
            textSize(50);
            text("Game Over", width / 2 - 117, height - 25);
        } else {
            for (int i = 0; i < Connect4.COLUMNS; i++) {
                if (mouseX > i * 100 && mouseX < (i + 1) * 100) {
                    if (connect4.getPlayer()) {
                        color = color(231, 76, 60);
                    } else {
                        color = color(241, 196, 15);
                    }
                    fill(color);
                    ellipse(i * 100 + 50, 0, 90, 90);
                }
            }
        }
    }

    @Override
    public void mouseClicked() {
        if (!connect4.isGameOver()) {
            for (int i = 0; i < Connect4.COLUMNS; i++) {
                if (mouseX > i * 100 && mouseX < (i + 1) * 100) {
                    try {
                        connect4 = connect4.play(i);
                        long startTime = System.nanoTime();
                        connect4 = connect4.play(connect4.bestMove());
                        long endTime = System.nanoTime();
                        System.out.println((endTime - startTime) / 1000000 + " ms");
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
    }
}
