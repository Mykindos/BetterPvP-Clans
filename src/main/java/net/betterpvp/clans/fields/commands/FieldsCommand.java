package net.betterpvp.clans.fields.commands;

import net.betterpvp.clans.fields.mysql.FieldsRepository;
import net.betterpvp.core.client.Rank;
import net.betterpvp.core.command.Command;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Map.Entry;


public class FieldsCommand extends Command {

    public FieldsCommand() {
        super("fields", new String[]{}, Rank.ADMIN);

    }

    @SuppressWarnings("deprecation")
    @Override
    public void execute(Player player, String[] args) {
        if (args != null) {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("reset")) {
                    for (Entry<Location, Material> block : FieldsRepository.blocks.entrySet()) {
                        block.getKey().getBlock().setType(block.getValue());
                    }

                    UtilMessage.message(player, "Fields", "You have replenished the fields.");
                }
            }
        }
    }

    @Override
    public void help(Player player) {
    }

}
