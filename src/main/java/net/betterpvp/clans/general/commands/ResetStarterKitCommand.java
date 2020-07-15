package net.betterpvp.clans.general.commands;

import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.clans.gamer.mysql.GamerRepository;
import net.betterpvp.core.client.Rank;
import net.betterpvp.core.command.Command;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.entity.Player;

public class ResetStarterKitCommand extends Command {

    public ResetStarterKitCommand() {
        super("resetstarterkits", new String[]{}, Rank.DEVELOPER);
    }

    @Override
    public void execute(Player player, String[] args) {
        for(Gamer gamer : GamerManager.getGamers()){
            gamer.setStarterKitClaimed(false);
            GamerRepository.updateStarterKitClaimed(gamer);
        }

        UtilMessage.message(player, "Developer", "Starter kits have been reset.");
    }

    @Override
    public void help(Player player) {

    }
}
