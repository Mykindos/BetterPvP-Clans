package net.betterpvp.clans.weapon.weapons;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.effects.EffectManager;
import net.betterpvp.clans.effects.EffectType;
import net.betterpvp.clans.weapon.Weapon;
import net.betterpvp.core.utility.UtilItem;
import net.betterpvp.core.utility.UtilMessage;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;


public class EnderPearl extends Weapon {

    public EnderPearl(Clans i) {
        super(i, Material.ENDER_PEARL, (byte) 0,
                ChatColor.YELLOW + "Purifying Capsule", new String[]{

                        ChatColor.GRAY
                                + "Right-Click: " + ChatColor.YELLOW
                                + "Consume",
                        ChatColor.GRAY + "  " + "Removes Negative Effects", "",
                        ChatColor.GRAY + "Included Effects:",
                        ChatColor.GRAY + "Slows, Silences, Stuns, Poisons",
                        ChatColor.GRAY + "Shock, Vulnerability, Blindness"},
                false, 0);
    }

    @EventHandler
    public void onPearlUse(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        if(event.getHand() == EquipmentSlot.OFF_HAND) return;
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK
                || event.getAction() == Action.RIGHT_CLICK_AIR) {
            if (player.getInventory().getItemInMainHand() == null)
                return;
            if (player.getInventory().getItemInMainHand().getType() != Material.ENDER_PEARL)
                return;
            event.setCancelled(true);
            UtilMessage
                    .message(player, "Ethereal Pearl", "You removed all negative effects!");
            player.getWorld()
                    .playSound(player.getLocation(), Sound.ENTITY_GENERIC_EAT, 2F, 1F);
            EffectManager.addEffect(player, EffectType.IMMUNETOEFFECTS, 5000);

            /*for (PotionEffect pot : player.getActivePotionEffects()) {
                if (pot.getType() == PotionEffectType.SLOW
                        || pot.getType() == PotionEffectType.CONFUSION
                        || pot.getType() == PotionEffectType.POISON
                        || pot.getType() == PotionEffectType.BLINDNESS
                        || pot.getType() == PotionEffectType.WITHER) {
                    player.removePotionEffect(pot.getType());
                }
            }

            EffectManager.removeEffect(player, EffectType.SHOCK);
            EffectManager.removeEffect(player, EffectType.SILENCE);
            EffectManager.removeEffect(player, EffectType.STUN);
            EffectManager.removeEffect(player, EffectType.VULNERABILITY);*/

            new BukkitRunnable() {
                @Override
                public void run() {
                    if (player.getInventory().getItemInMainHand() != null) {
                        if (player.getInventory().getItemInMainHand()
                                .getType() == Material.ENDER_PEARL) {

                            if (player.getInventory().getItemInMainHand().getAmount() != 1) {

                                UtilItem.remove(player, Material.ENDER_PEARL, 1);

                                return;
                            }

                            player.getInventory()
                                    .setItemInHand(new ItemStack(Material.AIR));
                        }
                    }

                }
            }.runTaskLater(getInstance(), 1);
        }

    }

    @EventHandler
    public void Teleport(PlayerTeleportEvent event) {
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            event.setCancelled(true);
        }
    }

}
