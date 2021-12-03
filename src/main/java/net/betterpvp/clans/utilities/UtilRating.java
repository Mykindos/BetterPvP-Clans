package net.betterpvp.clans.utilities;

import net.betterpvp.clans.classes.Role;
import net.betterpvp.clans.combat.ratings.Rating;
import net.betterpvp.clans.combat.ratings.RatingRepository;
import net.betterpvp.clans.gamer.Gamer;

public class UtilRating {

    private static float getProbability(float ratingA, float ratingB) {
        return 1.0f / (1 + (float) (Math.pow(10, (ratingA - ratingB) / 400)));
    }

    public static void adjustRating(Gamer killer, Role killerRole, Gamer killed, Role killedRole) {
        float pA = getProbability(killed.getRating(killedRole), killer.getRating(killerRole));

        int adjustment = (int) (30 * (1 - pA));


        Rating killerRating = killer.getRatings().get(killerRole.getName());
        killerRating.setRating(killerRating.getRating() + adjustment);
        killerRating.setLastKill(System.currentTimeMillis());
        killer.getRatings().put(killerRole.getName(), killerRating);

        Rating killedRating = killed.getRatings().get(killedRole.getName());
        killedRating.setRating(killedRating.getRating() - adjustment);
        killed.getRatings().put(killedRole.getName(), killedRating);

        RatingRepository.updateRating(killer, killerRole.getName());
        RatingRepository.updateRating(killed, killedRole.getName());
    }
}
