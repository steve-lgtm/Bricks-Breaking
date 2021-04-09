package test;

import game.entity.Rating;
import game.service.RatingService;
import game.service.RatingServiceJDBC;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RatingServiceTest {

    @Test
    public void testGetRating() {
        RatingService service = new RatingServiceJDBC();
        service.reset();
        Date date = new Date();
        service.setRating(new Rating("BlockPuzzle", "Jaro", 2, date));
        int ratings = service.getRating("BlockPuzzle", "Jaro");
        assertEquals(2, ratings);
    }

    @Test
    public void testGetAverageRating() {
        RatingService service = new RatingServiceJDBC();
        service.reset();
        Date date = new Date();
        service.setRating(new Rating("BlockPuzzle", "ondro", 1, date));
        service.setRating(new Rating("BlockPuzzle", "mino", 3, date));
        service.setRating(new Rating("BlockPuzzle", "arachmad", 5, date));
        service.setRating(new Rating("BlockPuzzle", "izeg", 4, date));
        service.setRating(new Rating("BlockPuzzle", "vilo", 5, date));
        service.setRating(new Rating("BlockPuzzle", "atung", 3, date));
        int avgRating = service.getAverageRating("BlockPuzzle");
        assertEquals(4, avgRating);
    }

    @Test
    public void testGetRatingDifferentGame() {
        RatingService service = new RatingServiceJDBC();
        service.reset();
        Date date = new Date();
        service.setRating(new Rating("BlockPuzzle", "ondro", 1, date));
        service.setRating(new Rating("mines", "ondro", 3, date));

        int ondroRating = service.getRating("BlockPuzzle", "ondro");
        assertEquals(1, ondroRating);
    }

    @Test
    public void testSomePlayerRating() {
        RatingService service = new RatingServiceJDBC();
        service.reset();
        Date date = new Date();
        service.setRating(new Rating("BlockPuzzle", "ondro", 1, date));
        service.setRating(new Rating("BlockPuzzle", "ondro", 3, date));

        int ondroRating = service.getRating("BlockPuzzle", "ondro");
        assertEquals(3, ondroRating);
    }
}
