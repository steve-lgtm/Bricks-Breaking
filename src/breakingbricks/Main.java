package breakingbricks;


import breakingbricks.consoleui.ConsoleUI;
import breakingbricks.core.Field;

public class Main {
    public static void main(String[] args) {
        Field field = new Field(5, 5, 5);
        ConsoleUI ui = new ConsoleUI(field);
        ui.play();
    }
}
