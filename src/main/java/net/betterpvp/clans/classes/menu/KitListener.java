package net.betterpvp.clans.classes.menu;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.Role;
import net.betterpvp.clans.classes.roles.Ranger;
import net.betterpvp.clans.utilities.UtilClans;
import net.betterpvp.core.framework.BPVPListener;
import net.betterpvp.core.interfaces.events.ButtonClickEvent;
import net.betterpvp.core.utility.UtilItem;
import net.betterpvp.core.utility.UtilPlayer;
import net.betterpvp.core.utility.recharge.RechargeManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class KitListener extends BPVPListener<Clans> {

    public KitListener(Clans i){
        super(i);

    }

    private List<String> claimed = new ArrayList<>();

    @EventHandler
    public void onClick(ButtonClickEvent e){
        if(e.getMenu() instanceof KitMenu){
            if(e.getButton() instanceof KitButton){
                KitButton kb = (KitButton) e.getButton();
                Player p = e.getPlayer();
                Role r = Role.getRole(p);



                if(r == null) {
                    p.getInventory().setHelmet(UtilClans.updateNames(new ItemStack(kb.getRole().getHelmet())));
                }else {
                    UtilItem.insert(p, UtilClans.updateNames(new ItemStack(kb.getRole().getHelmet())));
                }

                if(r == null) {
                    p.getInventory().setChestplate(UtilClans.updateNames(new ItemStack(kb.getRole().getChestplate())));

                }else {
                    UtilItem.insert(p, UtilClans.updateNames(new ItemStack(kb.getRole().getChestplate())));
                }

                if(r == null) {
                    p.getInventory().setLeggings(UtilClans.updateNames(new ItemStack(kb.getRole().getLeggings())));

                }else {
                    UtilItem.insert(p, UtilClans.updateNames(new ItemStack(kb.getRole().getLeggings())));
                }

                if(r == null) {
                    p.getInventory().setBoots(UtilClans.updateNames(new ItemStack(kb.getRole().getBoots())));

                }else {
                    UtilItem.insert(p, UtilClans.updateNames(new ItemStack(kb.getRole().getBoots())));
                }

                ItemStack sword = UtilPlayer.createItem(Material.IRON_SWORD, 1, ChatColor.LIGHT_PURPLE + "Starter Sword", ChatColor.WHITE + "Right click to cast an ability.",
                        ChatColor.GRAY + "Note: You must be in an unsafe zone to cast abilities.");
                ItemStack axe = UtilPlayer.createItem(Material.IRON_AXE, 1, ChatColor.LIGHT_PURPLE + "Starter Axe", ChatColor.WHITE + "Right click to cast an ability.",
                        ChatColor.GRAY + "Note: You must be in an unsafe zone to cast abilities.");
                ItemStack bow = UtilPlayer.createItem(Material.BOW, 1, ChatColor.LIGHT_PURPLE + "Starter Bow", ChatColor.WHITE + "Left click with a bow to prepare an ability.",
                        ChatColor.GRAY + "Note: You must be in an unsafe zone to cast abilities.",
                        ChatColor.GRAY + "Note: This will not work if you do not have a bow ability selected,",
                        ChatColor.GRAY + "right click an enchanting table to modify your class!");
                ItemStack arrows = UtilClans.updateNames(new ItemStack(Material.ARROW, 32));

                UtilItem.insert(p, sword);
                UtilItem.insert(p, axe);

                if(kb.getRole() instanceof Ranger) {
                    UtilItem.insert(p, bow);
                    UtilItem.insert(p, arrows);
                }

                UtilItem.insert(p, new ItemStack(Material.DIAMOND_SHOVEL));
                UtilItem.insert(p, new ItemStack(Material.DIAMOND_PICKAXE));
                p.closeInventory();



            }

        }
    }


}