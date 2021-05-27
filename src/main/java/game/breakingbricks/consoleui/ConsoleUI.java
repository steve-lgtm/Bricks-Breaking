package game.breakingbricks.consoleui;

import game.breakingbricks.core.Field;
import game.breakingbricks.core.GameState;
import game.breakingbricks.core.Tile;
import game.breakingbricks.core.TileState;
import game.entity.Comment;
import game.entity.Rating;
import game.entity.Score;
import game.service.CommentService;
import game.service.RatingService;
import game.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConsoleUI {
    public static final String GAME_NAME = "breakingbricks";
    private static final Pattern INPUT_PATTERN = Pattern.compile("([A-I,X])([1-9])");
    private static final Pattern NAME_PATTERN = Pattern.compile("([A-Za-z]{3,10})");
    private static final Pattern RATING_PATTERN = Pattern.compile("([1-5])");
    private static final Pattern YN_PATTERN = Pattern.compile("([Yy,Nn])");
    private final Field field;
    private final Scanner scanner = new Scanner(System.in);

    private final int health;
    private String name;
    private String comment;
    private int rate = 0;

    @Autowired
    private ScoreService scoreService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private RatingService ratingService;

    public ConsoleUI(Field field, int health, ScoreService scoreService, CommentService commentService, RatingService ratingService) {
        this.field = field;
        this.health = health;
        this.scoreService = scoreService;
        this.commentService = commentService;
        this.ratingService = ratingService;
    }


    public ConsoleUI(Field field) {
        this.field = field;
        health = field.getHealthCount();
    }

    public void play() {
        processName();

        System.out.println("----------------------------");
        printTopScores();

        do {
            printField();
            processInput();
        } while (field.getState() == GameState.PLAYING);

        System.out.println("----------------------------");
        System.out.print("     GAME OVER!!!\n");

        scoreService.addScore(new Score(GAME_NAME, name, field.getScore(), new Date()));

        printField();
        printAVGrating();
        processRating();
        printTopComments();
        processComment();
        processPlayOver();
        System.out.println();
    }

    private void printField() {
        System.out.println("----------------------------");
        System.out.printf("Health: %d    Score: %d\n", field.getHealthCount(), field.getScore());
        System.out.println("----------------------------");
        for (int row = 0; row < field.getRowCount(); row++) {
            System.out.print((char) (field.getRowCount() - row - 1 + 'A'));
            for (int column = 0; column < field.getColumnCount(); column++) {
                Tile tile = field.getTile(row, column);
                System.out.print(" ");
                if (tile.getState() == TileState.COLORED) {
                    switch (tile.getColor()) {
                        case YELLOW -> System.out.print("Y");
                        case BLUE -> System.out.print("B");
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
        System.out.println("----------------------------");
    }

    private void printTopScores() {
        List<Score> scores = scoreService.getTopScores(GAME_NAME);
        System.out.printf("Top Scores:\n");
        for (Score score : scores) {
            System.out.printf("%s %d\n", score.getPlayer(), score.getPoints());
        }
    }

    private void printTopComments() {
        List<Comment> comments = commentService.getComments(GAME_NAME);
        System.out.println("----------------------------");
        System.out.printf("Top Comments:\n");
        for (Comment comment : comments) {
            System.out.printf("%s: %s \n", comment.getPlayer(), comment.getComment());
        }
    }

    private void printAVGrating() {
        System.out.print("Avarage rating: ");
        System.out.print(ratingService.getAverageRating(GAME_NAME));
    }

    private void processInput() {
        System.out.print("Enter command (X - exit, A1 - select): ");
        String line = scanner.nextLine().toUpperCase();
        if ("X".equals(line)) {
            field.setState(GameState.FAILED);
            return;
        }
        Matcher matcher = INPUT_PATTERN.matcher(line);
        if (matcher.matches()) {
            if (line.length() == 2) {
                int row = 'A' - line.charAt(0) + field.getRowCount() - 1;
                int column = line.charAt(1) - '1';
                field.selectTile(row, column);
            }
        } else {
            System.out.print("Enter A-I 1-9! ");
            processInput();
        }
    }

    private void processName() {
        System.out.print("Enter your name(a-Z MIN-3 Max-10): ");
        String line = scanner.nextLine();
        Matcher matcher = NAME_PATTERN.matcher(line);
        if (matcher.matches()) {
            name = line;
        } else {
            System.out.print("a-Z MIN-3 Max-10! ");
            processName();
        }
    }

    private void processPlayOver() {
        System.out.println("----------------------------");
        System.out.print("Wanna play over? (Y/N): ");
        String line = scanner.nextLine().toUpperCase();
        Matcher matcher = YN_PATTERN.matcher(line);
        if (matcher.matches()) {
            if ("Y".equals(line)) {
                System.out.println("----------------------------");
                field.setState(GameState.PLAYING);
                int b = field.getColumnCount();
                int c = field.getRowCount();
                Field field = new Field(c, b, health);
                ConsoleUI ui = new ConsoleUI(field);
                ui.play();
            }
            if ("N".equals(line))
                System.exit(0);
        } else {
            System.out.println("Y or N ! ");
            processPlayOver();
        }
    }

    private void processComment() {
        System.out.println("----------------------------");
        System.out.print("Would you like add comment? (Y/N): ");
        String line = scanner.nextLine().toUpperCase();
        Matcher matcher = YN_PATTERN.matcher(line);
        if (matcher.matches()) {
            if ("Y".equals(line)) {
                System.out.print("OK, so tell something: ");
                comment = scanner.nextLine();
                commentService.addComment(new Comment(GAME_NAME, name, comment, new Date()));
            }
        } else {
            System.out.println("Y or N ! ");
            processComment();
        }
    }

    private void processRating() {
        System.out.println();
        System.out.println("----------------------------");
        System.out.print("Would you like add rating? (Y/N): ");
        String line = scanner.nextLine().toUpperCase();
        Matcher matcher = YN_PATTERN.matcher(line);
        if (matcher.matches()) {
            if ("Y".equals(line)) {
                System.out.print("How did you like the game? (1-5): ");
                String rating = scanner.nextLine();
                Matcher ratingmatcher = RATING_PATTERN.matcher(rating);
                while (!ratingmatcher.matches()) {
                    System.out.print("Enter number (1-5): ");
                    rating = scanner.nextLine();
                    ratingmatcher = RATING_PATTERN.matcher(rating);
                }
                if (ratingmatcher.matches()) {
                    rate = Integer.parseInt(ratingmatcher.group(1));
                }
                ratingService.setRating(new Rating(GAME_NAME, name, rate, new Date()));
            }
        } else {
            System.out.print("Y or N ! ");
            processRating();
        }
    }
}




