package game.service;

import com.sun.istack.NotNull;
import game.entity.Rating;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Transactional
public class RatingServiceJPA implements RatingService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void setRating(Rating rating) {
        entityManager.merge(rating);
    }

    @Override
    public int getAverageRating(String game) {
        return ((Number) entityManager.createQuery("select avg(s.rating) from Rating s where s.game=:game")
                .setParameter("game", game)
                .getSingleResult()).intValue();
    }

    @Override
    public int getRating(String game, String player) {
        return ((Number) entityManager.createQuery("SELECT rating FROM Rating WHERE game =: game AND player=: player order by rating")
                .setParameter("game", game).setParameter("player", player).getSingleResult()).intValue();
    }

    @Override
    public void reset() {
        entityManager.createNativeQuery("delete from rating").executeUpdate();
    }
}

