package game.service;

public class GamestudioException extends RuntimeException {
    public GamestudioException() { }

    public GamestudioException(String message) {
        super(message);
    }

    public GamestudioException(String message, Throwable cause) {
        super(message, cause);
    }
}
