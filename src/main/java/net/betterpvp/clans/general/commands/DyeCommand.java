package net.betterpvp.clans.general.commands;

import net.betterpvp.core.client.Rank;
import net.betterpvp.core.command.Command;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DyeCommand extends Command {

    public DyeCommand() {
        super("dye", new String[]{}, Rank.PLAYER);
    }

    @Override
    public void execute(Player player, String[] args) {
        if(args != null && args.length == 1){
            ItemStack hand = player.getInventory().getItemInMainHand();
            if(hand != null){
                if(hand.getType().name().contains("WOOL")){
                    dye(player, args[0], "_WOOL");
                }else if(hand.getType().name().endsWith("CONCRETE")){
                   dye(player, args[0], "_CONCRETE");
                }else if(hand.getType().name().endsWith("GLASS")){
                    dye(player, args[0], "_STAINED_GLASS");
                }
            }
        }
    }

    private void dye(Player player, String mat, String type){
        Material newMat = Material.valueOf(mat.toUpperCase() + type);
        if(newMat != null){
            player.getInventory().getItemInMainHand().setType(newMat);
        }else{
            UtilMessage.message(player, "Dye", "Could not find this colour.");
        }
    }

    @Override
    public void help(Player player) {

    }
}
