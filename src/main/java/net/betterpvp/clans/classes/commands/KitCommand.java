package net.betterpvp.clans.classes.commands;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.Role;
import net.betterpvp.clans.classes.menu.KitMenu;
import net.betterpvp.clans.classes.roles.Assassin;
import net.betterpvp.clans.classes.roles.Ranger;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.clans.utilities.UtilClans;
import net.betterpvp.core.client.ClientUtilities;
import net.betterpvp.core.client.Rank;
import net.betterpvp.core.command.Command;
import net.betterpvp.core.utility.UtilItem;
import net.betterpvp.core.utility.UtilMessage;
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

        if (gamer.getClient().hasRank(Rank.ADMIN, false)) {
            if (args != null) {
                if (args.length == 1) {

                    Role role = Role.getRole(args[0]);
                    if (role != null) {
                        giveKit(player, role);
                        return;
                    } else {
                        UtilMessage.message(player, "Kit", args[0] + " is not a valid kit.");
                    }
                    return;

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
                    return;
                }
            } else {
                player.openInventory(new KitMenu(player, false).getInventory());
            }
        }else if(gamer.getClient().hasRank(Rank.PLAYER, false)){
            if(Clans.getOptions().isHub()){
                UtilMessage.message(player, "You cannot claim your starter kit in the Hub!");
                return;
            }
            if(gamer.getClient().isDiscordLinked()){
                if(!gamer.isStarterKitClaimed()) {
                    player.openInventory(new KitMenu(player, false).getInventory());
                }else{
                    UtilMessage.message(player, "Kit", "You have already claimed your starter kit for this season!");
                }
            }else{
                UtilMessage.message(player, "Kit", "You need to link your discord account to claim your starter kit. Type"
                        + ChatColor.GREEN + "/link " + ChatColor.GRAY + " for more information.");
            }
        }


    }

    public static void giveKit(Player p, Role role) {
        if (p.getEquipment().getHelmet() == null) {
            p.getEquipment().setHelmet(UtilClans.updateNames(new ItemStack(role.getHelmet())));
        } else {
            UtilItem.insert(p, UtilClans.updateNames(new ItemStack(role.getHelmet())));
        }

        if (p.getEquipment().getChestplate() == null) {
            p.getEquipment().setChestplate(UtilClans.updateNames(new ItemStack(role.getChestplate())));
        } else {
            UtilItem.insert(p, UtilClans.updateNames(new ItemStack(role.getChestplate())));
        }

        if (p.getEquipment().getLeggings() == null) {
            p.getEquipment().setLeggings(UtilClans.updateNames(new ItemStack(role.getLeggings())));

        } else {
            UtilItem.insert(p, UtilClans.updateNames(new ItemStack(role.getLeggings())));
        }

        if (p.getEquipment().getBoots() == null) {
            p.getEquipment().setBoots(UtilClans.updateNames(new ItemStack(role.getBoots())));
        } else {
            UtilItem.insert(p, UtilClans.updateNames(new ItemStack(role.getBoots())));
        }


        p.getInventory().addItem(UtilClans.updateNames(new ItemStack(Material.IRON_SWORD)));
        p.getInventory().addItem(UtilClans.updateNames(new ItemStack(Material.IRON_AXE)));
        if (role instanceof Assassin || role instanceof Ranger) {
            p.getInventory().addItem(UtilClans.updateNames(new ItemStack(Material.BOW)));
            p.getInventory().addItem(UtilClans.updateNames(new ItemStack(Material.CROSSBOW)));
            p.getInventory().addItem(UtilClans.updateNames(new ItemStack(Material.ARROW, 64)));
        }

        p.getInventory().addItem(UtilClans.updateNames(new ItemStack(Material.IRON_PICKAXE)));
        p.getInventory().addItem(UtilClans.updateNames(new ItemStack(Material.IRON_SHOVEL)));
    }


    @Override
    public void help(Player player) {

    }

}