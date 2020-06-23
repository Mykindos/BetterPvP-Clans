package net.betterpvp.clans.weapon.weapons;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.Energy;
import net.betterpvp.clans.weapon.Weapon;
import net.betterpvp.core.utility.UtilItem;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.recharge.RechargeManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class EnergyApple extends Weapon {

    public EnergyApple(Clans i) {
        super(i, Material.APPLE, (byte) 0, ChatColor.YELLOW + "Energy Apple", new String[]{
                ChatColor.GRAY + "Right Click: " + ChatColor.YELLOW + "Consume",
                ChatColor.GRAY + "  " + "Instantly restores 50 energy",
                "",
                ChatColor.GRAY + "Cooldown: " + ChatColor.GREEN + 10 + ChatColor.GRAY + " seconds"}, false, 0);

    }


    @EventHandler
    public void onAppleConsume(PlayerInteractEvent event) {
        if(event.getHand() == EquipmentSlot.OFF_HAND) return;
        final Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (player.getInventory().getItemInMainHand() == null) return;
            if (player.getInventory().getItemInMainHand().getType() != Material.APPLE) return;
            if (isThisWeapon(player)) {


                if (RechargeManager.getInstance().add(player, "Energy Apple", 10, true)) {

                    Energy.regenerateEnergy(player, 0.50);
                    UtilMessage.message(player, "Item", ChatColor.GRAY + "You consumed a " + getName());


                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (player.getInventory().getItemInMainHand() != null) {
                                if (player.getInventory().getItemInMainHand().getType() == Material.APPLE) {


                                    if (player.getInventory().getItemInMainHand().getAmount() != 1) {

                                        UtilItem.remove(player, Material.APPLE, 1);

                                        return;
                                    }
                                    player.getInventory().setItemInHand(new ItemStack(Material.AIR));

                                }
                            }

                        }
                    }.runTaskLater(getInstance(), 1);

                }


            }


        }
    }
}
