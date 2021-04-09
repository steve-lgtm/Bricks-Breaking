package game;

import game.breakingbricks.consoleui.ConsoleUI;
import game.breakingbricks.core.Field;

public class Main {
    public static void main(String[] args) {
        Field field = new Field(9, 9, 5);
        ConsoleUI ui = new ConsoleUI(field);
        ui.play();
    }
}
