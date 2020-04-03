package net.betterpvp.clans.general.commands;


import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.clans.skills.selector.page.ClassSelectionPage;
import net.betterpvp.core.client.Rank;
import net.betterpvp.core.command.Command;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.entity.Player;

public class BuildCommand extends Command {

    public BuildCommand() {
        super("build", new String[]{}, Rank.PLAYER);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void execute(Player p, String[] args) {
        if (args == null) {
            Gamer gamer = GamerManager.getOnlineGamer(p);
            if (gamer != null) {

                UtilMessage.message(p, "Convenience", "You opened the Build Editor.");
                p.openInventory(new ClassSelectionPage(p).getInventory());

            }

        }

    }

    @Override
    public void help(Player player) {
        // TODO Auto-generated method stub

    }

}