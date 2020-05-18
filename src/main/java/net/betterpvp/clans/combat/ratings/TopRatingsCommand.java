package net.betterpvp.clans.combat.ratings;

import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.core.client.Rank;
import net.betterpvp.core.command.Command;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TopRatingsCommand extends Command implements Listener {

    public List<Rating> ratings;

    public TopRatingsCommand() {
        super("toprating", new String[]{"topratings"}, Rank.PLAYER);
        ratings = new ArrayList<>();
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args != null) {
            UtilMessage.message(player, ChatColor.GREEN.toString() + ChatColor.BOLD + "Top 10 " + args[0] + " Ratings");
            List<Rating> temp = new ArrayList<>(ratings).stream().filter(r -> r.role.equalsIgnoreCase(args[0])).collect(Collectors.toList());
            temp.sort((o1, o2) -> o2.rating - o1.rating);
            for (int i = 0; i < Math.min(10, temp.size()); i++) {
                Rating rating = temp.get(i);
                UtilMessage.message(player, ChatColor.YELLOW + rating.name + " - " + ChatColor.WHITE + rating.rating);
            }
            UtilMessage.message(player, ChatColor.GREEN + "The ratings leaderboard updates every 30 minutes.");
        } else {
            UtilMessage.message(player, ChatColor.GREEN.toString() + ChatColor.BOLD + "Top 10 Ratings ");
            List<Rating> temp = new ArrayList<>(ratings);
            temp.sort((o1, o2) -> o2.rating - o1.rating);
            for (int i = 0; i < Math.min(10, temp.size()); i++) {
                Rating rating = temp.get(i);
                UtilMessage.message(player, ChatColor.YELLOW + rating.name + " (" + rating.role + ") - " + ChatColor.WHITE + rating.rating);
            }

            UtilMessage.message(player, ChatColor.GREEN + "The ratings leaderboard updates every 30 minutes.");
        }
    }


    @EventHandler
    public void onUpdate(UpdateEvent e) {
        if (ratings.isEmpty() && GamerManager.getGamers().size() > 0) {
            populateRatings();
        }

        if (e.getType() == UpdateEvent.UpdateType.MIN_32) {
            populateRatings();
        }
    }

    private void populateRatings() {
        ratings.clear();
        for (Gamer gamer : GamerManager.getGamers()) {
            for (Map.Entry<String, Integer> rating : gamer.getRatings().entrySet()) {
                ratings.add(new Rating(gamer.getClient().getName(), rating.getKey(), rating.getValue()));
            }
        }
    }

    @Override
    public void help(Player player) {

    }

    private class Rating {
        public String name;
        public String role;
        public int rating;

        public Rating(String name, String role, int rating) {
            this.name = name;
            this.role = role;
            this.rating = rating;
        }
    }
}
