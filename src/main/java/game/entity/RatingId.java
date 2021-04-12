package game.entity;

import java.io.Serializable;
import java.util.Objects;

public class RatingId implements Serializable {
    private String game;
    private String player;

    public RatingId (String game, String player) {
        this.game = game;
        this.player = player;
    }

    public RatingId (){

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RatingId ratingId = (RatingId) o;
        return game.equals(ratingId.game) && player.equals(ratingId.player);
    }

    @Override
    public int hashCode() {
        return Objects.hash(game, player);
    }

}