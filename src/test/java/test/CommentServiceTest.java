package test;

import game.entity.Comment;
import game.service.CommentService;
import game.service.CommentServiceJDBC;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CommentServiceTest {
    private CommentService createService() {
        //return new ScoreServiceFile();
        return new CommentServiceJDBC();
    }

    @Test
    public void addComment() {
        CommentService service = createService();
    }

    @Test
    void addCommentsTest() {
        CommentService service = createService();
        service.reset();
        Date date = new Date();
        service.addComment(new Comment("breakingbricks", "jano", "wau", date));
        List<Comment> comments = service.getComments("breakingbricks");

        assertEquals(1, comments.size());
        assertEquals("breakingbricks", comments.get(0).getGame());
        assertEquals("jano", comments.get(0).getPlayer());
        assertEquals("wau", comments.get(0).getComment());
        assertEquals(date, comments.get(0).getCommentedOn());
    }

    @Test
    void addComments3Test() {
        CommentService service = createService();
        service.reset();
        Date date = new Date();
        service.addComment(new Comment("breakingbricks", "jano", "wau", date));
        service.addComment(new Comment("breakingbricks", "feri", "super", date));
        service.addComment(new Comment("breakingbricks", "eva", "cool", date));

        List<Comment> comments = service.getComments("breakingbricks");

        assertEquals(3, comments.size());
        assertEquals("breakingbricks", comments.get(0).getGame());
        assertEquals("jano", comments.get(0).getPlayer());
        assertEquals("wau", comments.get(0).getComment());
        assertEquals(date, comments.get(0).getCommentedOn());
        assertEquals("breakingbricks", comments.get(1).getGame());
        assertEquals("feri", comments.get(1).getPlayer());
        assertEquals("super", comments.get(1).getComment());
        assertEquals(date, comments.get(1).getCommentedOn());
        assertEquals("breakingbricks", comments.get(2).getGame());
        assertEquals("eva", comments.get(2).getPlayer());
        assertEquals("cool", comments.get(2).getComment());
        assertEquals(date, comments.get(2).getCommentedOn());
    }

    @Test
    public void testAddComments10() {
        CommentService service = createService();
        for (int i = 0; i < 20; i++)
            service.addComment(new Comment("breakingbricks", "jano", "wau", new Date()));
        assertEquals(10, service.getComments("breakingbricks").size());
    }

    @Test
    public void testReset() {
        CommentService service = createService();
        service.reset();
        assertEquals(0, service.getComments("breakingbricks").size());
    }

    @Test
    public void testPersistance() {
        CommentService service = createService();
        service.reset();
        service.addComment(new Comment("breakingbricks", "jano", "wau", new Date()));

        service = createService();
        assertEquals(1, service.getComments("breakingbricks").size());
    }
}