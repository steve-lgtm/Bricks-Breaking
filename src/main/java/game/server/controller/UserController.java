package game.server.controller;

import game.entity.User;
import game.service.DataService;
import game.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;


@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class UserController {
    private User loggedUser;
    private User checkUser;
    @Autowired
    private DataService dataService;
    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/login")
    public String login(String login, String password) {
        checkUser= new User(login,password);
        if (dataService.userAuthentication(checkUser)) {
            loggedUser = checkUser;

            return "redirect:/breakingbricks";
        }

        return "redirect:/";
    }

    @RequestMapping("/register")
    public String register(String login, String password) {
        checkUser= new User(login,password);
        if (dataService.registerUser(checkUser)) {

        }
        return "redirect:/";
    }

    @RequestMapping("/logout")
    public String logout() {
        loggedUser = null;
        return "redirect:/";
    }

    public User getLoggedUser() {
        return loggedUser;
    }

    public boolean isLogged() {
        return loggedUser != null;
    }
}
