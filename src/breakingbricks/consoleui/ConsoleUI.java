package breakingbricks.consoleui;

import breakingbricks.core.Field;
import breakingbricks.core.GameState;
import breakingbricks.core.Tile;
import breakingbricks.core.TileState;

import java.util.Scanner;

public class ConsoleUI {
    private final Field field;

    private final Scanner scanner = new Scanner(System.in);

    public ConsoleUI(Field field) {
        this.field = field;
    }

    public void play() {
        int a = field.getHealthCount();
        do {
            printField();
            processInput();
        } while (field.getState() == GameState.PLAYING);

        printField();
        System.out.println();
        System.out.print("Wanna play over? Y/N ");
        String line = scanner.nextLine().toUpperCase();
        if ("Y".equals(line)) {
            field.setState(GameState.PLAYING);
            int b = field.getColumnCount();
            int c = field.getRowCount();
            Field field = new Field(c, b, a);
            ConsoleUI ui = new ConsoleUI(field);
            ui.play();
        }
        if ("N".equals(line))
            System.exit(0);
    }


    private void printField() {
        System.out.print("Health: ");
        System.out.print(field.getHealthCount());
        System.out.println();
        System.out.print("Score: ");
        System.out.print(field.getScore());
        System.out.println();
        for (int row = 0; row < field.getRowCount(); row++) {
            System.out.print((char) (field.getRowCount() - row - 1 + 'A'));
            for (int column = 0; column < field.getColumnCount(); column++) {
                Tile tile = field.getTile(row, column);
                System.out.print(" ");
                if (tile.getState() == TileState.COLORED) {
                    switch (tile.getColor()) {
                        case YELLOW -> System.out.print("Y");
                        case ORANGE -> System.out.print("O");
                        case RED -> System.out.print("R");
                        default -> throw new IllegalArgumentException("Incorrect. tile state");
                    }
                } else
                    System.out.print("-");
            }
            System.out.println();
        }
        System.out.print(" ");
        for (int column = 0; column < field.getColumnCount(); column++) {
            System.out.print(" ");
            System.out.print(column + 1);
        }
        System.out.println();
    }

    private void processInput() {
        System.out.print("Enter command (X - exit, A1 - select): ");
        String line = scanner.nextLine().toUpperCase();
        if ("X".equals(line))
            System.exit(0);
        if (line.length() == 2) {
            int row = 'A' - line.charAt(0) + field.getRowCount() - 1;
            int column = line.charAt(1) - '1';
            field.selectTile(row, column);
        }
    }
}




