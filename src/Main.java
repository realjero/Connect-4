
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        new Main().startGame();
    }

    public void startGame() {
        VierGewinnt vg = new VierGewinnt();
        Scanner scanner = new Scanner(System.in);
        boolean player = true;

        while (!vg.isGameOver()) {
            System.out.println(vg);
            System.out.print("\nSpalte (0-6): ");
            vg = vg.playMove(scanner.nextInt(), player);
            player = !player;
        }

        System.out.println(vg);
        System.out.println("\nSpiel beendet!");
    }
}
