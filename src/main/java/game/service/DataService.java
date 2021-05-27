package game.service;

import game.entity.User;

public interface DataService {
   boolean registerUser(User user) throws DataException;

   boolean userAuthentication(User user) throws DataException;
   boolean isRegistered(String login) throws DataException;
}
