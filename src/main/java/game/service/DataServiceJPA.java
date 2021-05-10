package game.service;

import game.entity.Score;
import game.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class DataServiceJPA implements DataService {
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public boolean registerUser(User User) throws DataException {
        if(isRegistered(User.getLogin()))
            return false;
        else {
                entityManager.persist(User);
                return true;
        }
    }

    @Override
    public boolean userAuthentication(User user) throws DataException {
        if(isRegistered(user.getLogin()) == true &&
                correctPassword(user.getLogin(), user.getPassword())){
            return true;
        }
        return false;
    }

    private boolean correctPassword(String login, String password) {
        if(((Number) entityManager.createQuery("SELECT count(login) from User WHERE login =: login and password =: password")
                .setParameter("login", login)
                .setParameter("password", password)
                .getSingleResult()).intValue() > 0){
            return true;
        }
        return false;
    }

    @Override
    public boolean isRegistered(String login) throws DataException {
        if(((Number) entityManager.createQuery("SELECT count(login) from User WHERE login =: login")
                .setParameter("login", login).getSingleResult()).intValue() > 0){
            return true;
        }
        return false;
    }




}

