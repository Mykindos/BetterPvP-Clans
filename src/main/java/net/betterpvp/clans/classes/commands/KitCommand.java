package net.betterpvp.clans.classes.commands;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.Role;
import net.betterpvp.clans.classes.menu.KitMenu;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.core.client.ClientUtilities;
import net.betterpvp.core.client.Rank;
import net.betterpvp.core.command.Command;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.recharge.RechargeManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class KitCommand extends Command {

    public KitCommand() {
        super("kit", new String[]{"class"}, Rank.PLAYER);
    }

    @Override
    public void execute(Player player, String[] args) {
        Gamer gamer = GamerManager.getOnlineGamer(player);

        if(args != null) {
            if (args.length == 1) {

                Role role = Role.getRole(args[0]);
                if (role != null) {
                    giveKit(player, role);
                    return;
                } else {
                    UtilMessage.message(player, "Kit", args[0] + " is not a valid kit.");
                }


            } else if (args.length == 2) {

                Player p = Bukkit.getPlayer(args[1]);
                if (p != null) {
                    Role role = Role.getRole(args[0]);
                    if (role != null) {
                        ClientUtilities.messageStaff("Kit", ChatColor.GREEN + player.getName() + ChatColor.GRAY
                                + " gave " + ChatColor.GREEN + p.getName() + ChatColor.GRAY + " a " + ChatColor.YELLOW + role.getName() + ChatColor.GRAY + " kit");
                        giveKit(p, role);
                    } else {
                        UtilMessage.message(player, "Kit", args[0] + " is not a valid kit.");
                    }

                }

            }
        }else{
            player.openInventory(new KitMenu(player, false).getInventory());
        }
    }

    private void giveKit(Player p, Role role) {
        p.getEquipment().setHelmet(new ItemStack(role.getHelmet()));
        p.getEquipment().setChestplate(new ItemStack(role.getChestplate()));
        p.getEquipment().setLeggings(new ItemStack(role.getLeggings()));
        p.getEquipment().setBoots(new ItemStack(role.getBoots()));
        p.getInventory().addItem(new ItemStack(Material.IRON_SWORD));
        p.getInventory().addItem(new ItemStack(Material.IRON_AXE));
        p.getInventory().addItem(new ItemStack(Material.BOW));
        p.getInventory().addItem(new ItemStack(Material.ARROW, 32));
        p.getInventory().addItem(new ItemStack(Material.IRON_PICKAXE));
        p.getInventory().addItem(new ItemStack(Material.IRON_SPADE));
    }

    @Override
    public void help(Player player) {

    }

}