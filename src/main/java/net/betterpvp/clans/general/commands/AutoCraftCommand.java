package net.betterpvp.clans.general.commands;

import net.betterpvp.clans.classes.Role;
import net.betterpvp.clans.utilities.UtilClans;
import net.betterpvp.core.client.Rank;
import net.betterpvp.core.command.Command;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AutoCraftCommand extends Command {

    public AutoCraftCommand() {
        super("autocraft", new String[]{"autoc"}, Rank.PLAYER);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void execute(Player player, String[] args) {
        if(args != null){
            if(args.length == 1){
                if(Role.getRole(args[0]) == null) return;
                switch(args[0].toLowerCase()){
                    case "assassin":
                        if(remove(player, Material.LEATHER, 24)){
                            giveItems(player, Role.getRole(args[0]));
                        }
                        break;
                    case "knight":
                        if(remove(player, Material.IRON_INGOT, 24)){
                            giveItems(player, Role.getRole(args[0]));
                        }
                        break;
                    case "paladin":
                        if(remove(player, Material.GOLD_INGOT, 24)){
                            giveItems(player, Role.getRole(args[0]));
                        }
                        break;
                    case "ranger":
                        if(remove(player, Material.EMERALD, 24)){
                            giveItems(player, Role.getRole(args[0]));
                        }
                        break;
                    case "gladiator":
                        if(remove(player, Material.DIAMOND, 24)){
                            giveItems(player, Role.getRole(args[0]));
                        }
                        break;
                    case "warlock":
                        if(remove(player, Material.NETHERITE_INGOT, 24)){
                            giveItems(player, Role.getRole(args[0]));
                        }
                        break;
                }
            }
        }
    }

    private void giveItems(Player p, Role r){
        p.getInventory().addItem(UtilClans.updateNames(new ItemStack(r.getHelmet())));
        p.getInventory().addItem(UtilClans.updateNames(new ItemStack(r.getChestplate())));
        p.getInventory().addItem(UtilClans.updateNames(new ItemStack(r.getLeggings())));
        p.getInventory().addItem(UtilClans.updateNames(new ItemStack(r.getBoots())));
        UtilMessage.message(p, "Auto Craft", "You crafted a " + ChatColor.WHITE + ChatColor.BOLD + r.getName() + ChatColor.GRAY + " set");
    }

    private boolean remove(Player p, Material type, int amount){
        for(int i = 0; i < p.getInventory().getSize(); i++){
            ItemStack item = p.getInventory().getItem(i);
            if(item != null){
                if(item.getAmount() >= amount){
                    if(item.getType() == type){
                        if(item.getAmount() - amount <= 0){
                            p.getInventory().setItem(i, null);
                            return true;
                        }
                        item.setAmount(item.getAmount() - amount);
                        return true;
                    }
                }
            }
        }

        return false;
    }



    @Override
    public void help(Player player) {
    }

}