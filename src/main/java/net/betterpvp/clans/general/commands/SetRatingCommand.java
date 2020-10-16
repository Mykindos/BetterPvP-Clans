package net.betterpvp.clans.general.commands;

import net.betterpvp.clans.classes.Role;
import net.betterpvp.clans.combat.ratings.Rating;
import net.betterpvp.clans.combat.ratings.RatingRepository;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.core.client.Rank;
import net.betterpvp.core.command.Command;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SetRatingCommand extends Command {

    public SetRatingCommand() {
        super("setrating", new String[]{}, Rank.ADMIN);
    }

    @Override
    public void execute(Player player, String[] args) {
        if(args != null && args.length == 3){
            Gamer target = GamerManager.getGamer(args[0]);
            if(target != null){
                Role role = Role.getRole(args[1]);
                int newRating = Integer.parseInt(args[2]);

                Rating rating = target.getRatings().get(role.getName());
                rating.setRating(newRating);

                target.getRatings().put(role.getName(), rating);
                RatingRepository.updateRating(target, role.getName());

                UtilMessage.message(player, "Admin", "Updated rating for " + ChatColor.GREEN + target.getClient().getName());
            }else{
                UtilMessage.message(player, "Admin", "Could not find a gamer with this name.");
            }
        }
    }

    @Override
    public void help(Player player) {

    }
}
