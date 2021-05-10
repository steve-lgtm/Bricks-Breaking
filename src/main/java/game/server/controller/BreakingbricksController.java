package game.server.controller;


import game.breakingbricks.core.Field;
import game.breakingbricks.core.GameState;
import game.breakingbricks.core.Tile;
import game.entity.Comment;
import game.entity.Rating;
import game.entity.Score;
import game.entity.User;
import game.service.CommentService;
import game.service.RatingService;
import game.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;

@Controller
@RequestMapping("/breakingbricks")
@Scope(WebApplicationContext.SCOPE_SESSION)
public class BreakingbricksController {
    @Autowired
    private UserController userController;

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private RatingService ratingService;

    @Autowired
    private CommentService commentService;

    private Field field = new Field(9, 9, 5);

    @RequestMapping
    public String breakingbricks(@RequestParam(required = false) String row, @RequestParam(required = false) String column, Model model,@RequestParam(required = false)String rating) {
        if (field.getState() == GameState.FAILED && userController.isLogged()&&rating!=null){
            ratingService.setRating(new Rating("breakingbricks",userController.getLoggedUser().getLogin(),Integer.parseInt(rating),new Date()));
        }
        processCommand(row, column);
        fillModel(model);
        return "breakingbricks";
    }

    private void processCommand(String row, String column) {
        try {
            if (field.getState() == GameState.PLAYING)
                field.selectTile(Integer.parseInt(row), Integer.parseInt(column));
            if (field.getState() == GameState.FAILED && userController.isLogged()) {
                scoreService.addScore(new Score("breakingbricks", userController.getLoggedUser().getLogin(), field.getScore(), new Date()));

            }
        } catch (Exception e) {
            //Jaro: Tato vynimka znamena, ze parametre neprisli
        }
    }



    public String getGameOver(){

        StringBuilder sb = new StringBuilder();
        if(field.getState() == GameState.FAILED){
        sb.append("<h1>Game Over</h1>");

        }
        return sb.toString();
    }
    private void fillModel(Model model) {
        model.addAttribute("scores", scoreService.getTopScores("breakingbricks"));
        model.addAttribute("comments", commentService.getComments("breakingbricks"));

    }
    @RequestMapping("/comment")
    public String comment(String comment) {
        commentService.addComment(new Comment("breakingbricks", userController.getLoggedUser().getLogin(), comment, new Date()));
        return "redirect:/breakingbricks";
        }
    @RequestMapping("/new")
    public String newGame(Model model) {
        field = new Field(9, 9, 5);
        fillModel(model);
        return "redirect:/breakingbricks";
    }

    @RequestMapping("/rating")
    public String rating(String rating) {

        ratingService.setRating(new Rating("breakingbricks",userController.getLoggedUser().getLogin(),Integer.parseInt(rating),new Date()));
        return "redirect:/breakingbricks";
    }

    public boolean isOver(){
        if(field.getState() == GameState.FAILED&& userController.isLogged())
            return true;
        else
            return false;
    }
    public String getAverageRating(Model model) {
        StringBuilder sb = new StringBuilder();
        if(field.getState() == GameState.FAILED) {
            model.addAttribute("ratings", ratingService.getAverageRating("breakingbricks"));
        }
        return sb.toString();
    }
    @RequestMapping(value = "/field", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String breakingbricks(@RequestParam(required = false) String row, @RequestParam(required = false) String column) {
        processCommand(row, column);
        return getHtmlField();
    }
    public String getStarsRating() {
        StringBuilder sb = new StringBuilder();
        for (int star = 1; star <= 5; star++) {
            sb.append(String.format("<a href='/breakingbricks?rating=%d'><img src='/images/star%d.png' width='50'></a>",star,star));
        }
        return sb.toString();
    }
    public String getHtmlField() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("<h3>Score: %d    Health: %d  </h3>\n",field.getScore(),field.getHealthCount()));

        sb.append("<table id=\"tablicka\">");

        for (int row = 0; row < field.getRowCount(); row++) {
            sb.append("<tr>\n");
            for (int column = 0; column < field.getColumnCount(); column++) {
                Tile tile = field.getTile(row, column);
                sb.append("<td>");
                sb.append(String.format("<a href='/breakingbricks?row=%d&column=%d'>\n", row, column));
                sb.append("<img src='/images/" + getImageName(tile) + ".png'>");
                sb.append("</a>\n");
                sb.append("</td>\n");
            }
            sb.append("</tr>\n");
        }

        sb.append("</table>\n");
        return sb.toString();
    }
    private String getImageName(Tile tile) {
        switch (tile.getState()) {
            case COLORED:
                switch(tile.getColor()){
                    case RED:
                        return "red";
                    case BLUE:
                        return "blue";
                    case YELLOW:
                        return "yellow";
                }
            case BRAKED:
                return "braked";
            default:
                throw new IllegalArgumentException("Unsupported tile state " + tile.getState());
        }
    }


}
