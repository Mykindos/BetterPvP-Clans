package net.betterpvp.clans.economy.shops.ignatius;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.economy.shops.ShopManager;
import net.betterpvp.clans.economy.shops.events.ShopTradeEvent;
import net.betterpvp.clans.economy.shops.events.TradeAction;
import net.betterpvp.clans.economy.shops.menu.buttons.LegendaryShopItem;
import net.betterpvp.clans.weapon.ILegendary;
import net.betterpvp.clans.weapon.Weapon;
import net.betterpvp.clans.weapon.WeaponManager;
import net.betterpvp.core.database.Log;
import net.betterpvp.core.framework.BPVPListener;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.utility.Titles;
import net.betterpvp.core.utility.UtilMath;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilTime;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.WanderingTrader;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * ADDED: 19/10/2020
 * Ignatius is a wandering vendor who spawns at fields on a 8-16 hour timer.
 * Player can purchase or sell things they may not usually be able to
 */
public class IgnatiusListener extends BPVPListener<Clans> {

    private static long lastIgnatiusSpawn;
    private long ignatiusRespawnCooldown;

    public IgnatiusListener(Clans instance) {
        super(instance);
        lastIgnatiusSpawn = System.currentTimeMillis();
        ignatiusRespawnCooldown = UtilMath.randomInt(8, 16) * 3_600_000;
    }

    @EventHandler
    public void onUpdate(UpdateEvent e){
        if(e.getType() == UpdateEvent.UpdateType.MIN_01){
            if(UtilTime.elapsed(lastIgnatiusSpawn, ignatiusRespawnCooldown)){

                Bukkit.getPluginManager().callEvent(new IgnatiusSpawnEvent());

            }
        }
    }

    @EventHandler
    public void onIgnatiusSpawn(IgnatiusSpawnEvent e){
        spawnIgnatius();

        new BukkitRunnable(){
            @Override
            public void run() {
                killIgnatius();
            }
        }.runTaskLater(getInstance(), 20 * 60 * 15);
    }

    private void spawnIgnatius(){
        lastIgnatiusSpawn = System.currentTimeMillis();
        ignatiusRespawnCooldown = UtilMath.randomInt(8, 16) * 3_600_000_000L;
        ShopManager.spawnShop(getInstance(), Clans.getOptions().getIgnatiusSpawnLocation(), "Ignatius");

        Bukkit.getOnlinePlayers().forEach(player -> {
            Titles.sendTitle(player, 20, 60, 20,
                ChatColor.GOLD + "Ignatius has entered the world", ChatColor.YELLOW + "You may find him roaming around Fields.");
            player.playSound(player.getLocation(), Sound.ENTITY_WANDERING_TRADER_REAPPEARED, 2f, 1f);

        });

        UtilMessage.broadcast("Shop", "Ignatius has entered the world.");
    }

    private void killIgnatius(){
        Clans.getOptions().getIgnatiusSpawnLocation().getWorld().getLivingEntities().forEach(ent -> {
            if(ent instanceof WanderingTrader){
                if(ent.getCustomName() != null && ent.getCustomName().contains("Ignatius")){
                    ent.setHealth(0D);
                    ent.remove();
                }
            }
        });

        UtilMessage.broadcast("Shop", "Ignatius has left the world.");
    }

    @EventHandler (priority = EventPriority.MONITOR)
    public void onBuyIgnatius(ShopTradeEvent e){
        if(e.isCancelled()){
            return;
        }
        if(e.getAction() == TradeAction.BUY_IGNATIUSITEM){
            if(e.getItem() instanceof LegendaryShopItem) {
                Weapon weapon = WeaponManager.getWeapon(e.getItem().getItemName());
                if(weapon != null){
                    if(weapon instanceof ILegendary){
                        UtilMessage.broadcast("Legendary", ChatColor.YELLOW + e.getPlayer().getName()
                                + ChatColor.GRAY + " purchased a " + weapon.getName() + ChatColor.GRAY + " from "
                                + ChatColor.GOLD + "Ignatius" + ChatColor.GRAY + "!");

                        Bukkit.getOnlinePlayers().forEach(player -> {
                            player.playSound(player.getLocation(), Sound.ENTITY_PILLAGER_CELEBRATE, 2f, 1f);
                        });

                        Log.write("Legendary", ChatColor.YELLOW + e.getPlayer().getName()
                                + ChatColor.GRAY + " purchased a " + weapon.getName() + ChatColor.GRAY + " from "
                                + ChatColor.GOLD + "Ignatius" + ChatColor.GRAY + "!");
                    }
                }
            }
        }
    }
}
