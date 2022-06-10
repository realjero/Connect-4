import processing.core.PApplet;

public class VierGewinntGUI extends PApplet {

    VierGewinntLogic vg = new VierGewinnt();
    boolean player;

    public static void main(String[] args) {
        PApplet.runSketch(new String[]{"Connect 4"}, new VierGewinntGUI());
    }

    public void settings() {
        size(VierGewinntLogic.COLUMNS * 100, (VierGewinntLogic.ROWS + 1) * 100);
    }

    public void setup() {
        background(color(44, 62, 80));

        player = true;
    }


    public void draw() {
        background(color(44, 62, 80));
        int color = 0;

        for (int y = 0; y < VierGewinntLogic.ROWS; y++) {
            for (int x = 0; x < VierGewinntLogic.COLUMNS; x++) {
                switch (vg.getBoard(x, y)) {
                    case 1:
                        color = color(241, 196, 15);
                        break;
                    case 2:
                        color = color(231, 76, 60);
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

        if(vg.isGameOver()) {
            fill(color(236, 240, 241));
            textSize(50);
            text("Game Over", width / 2 - 117, height - 25);
        }

        for(int i = 0; i < VierGewinntLogic.COLUMNS; i++) {
            if(mouseX > i * 100 && mouseX < (i + 1) * 100) {
                if(player) {
                    color = color(241, 196, 15);
                } else {
                    color = color(231, 76, 60);
                }
                fill(color);
                ellipse(i * 100 + 50, 0, 90, 90);
                }
            }
    }

    @Override
    public void mouseClicked() {
        for(int i = 0; i < VierGewinntLogic.COLUMNS; i++) {
            if(mouseX > i * 100 && mouseX < (i + 1) * 100) {
                try {
                    vg = vg.playMove(i, player);
                    player = !player;
                    vg = vg.playMove(vg.bestMove(), player);
                    player = !player;
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}
