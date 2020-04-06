package net.betterpvp.clans.economy.shops;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.economy.shops.mysql.ShopKeeperRepository;
import net.betterpvp.clans.economy.shops.mysql.ShopRepository;
import net.betterpvp.core.client.ClientUtilities;
import net.betterpvp.core.client.Rank;
import net.betterpvp.core.command.Command;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilTime;
import net.betterpvp.core.utility.recharge.RechargeManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.UUID;


public class ShopCommand extends Command {

    private Clans i;
    private HashMap<UUID, Long> cd = new HashMap<>();

    public ShopCommand(Clans i) {
        super("shop", new String[]{"shops"}, Rank.PLAYER);
        this.i = i;

    }


 /*   @EventHandler
    public void onUpdate(UpdateEvent e) {
        if (e.getType() == UpdateEvent.UpdateType.SEC) {
            Iterator<Entry<UUID, Long>> it = cd.entrySet().iterator();
            while (it.hasNext()) {
                Entry<UUID, Long> next = it.next();
                if (UtilTime.elapsed(next.getValue(), 60000 * 5)) {
                    Player p = Bukkit.getPlayer(next.getKey());
                    if (p != null) {
                        RechargeManager.getInstance().add(p, "Portable Shop", 7200, true, false, false);
                        UtilMessage.message(p, "Shop", "Portable Shop is now on cooldown!");
                        it.remove();
                    }

                }
            }
        }
    }
*/

    @Override
    public void execute(final Player p, String[] args) {
        if (args != null) {
            if (ClientUtilities.getOnlineClient(p).hasRank(Rank.ADMIN, true)) {
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("reload")) {

                        p.sendMessage("Shops have been reloaded");
                        for (World w : Bukkit.getWorlds()) {
                            for (LivingEntity e : w.getLivingEntities()) {
                                if (e instanceof Player || e instanceof ArmorStand) continue;

                                for (Shop s : ShopManager.getShops()) {
                                    if (e.getCustomName() != null) {

                                        if (s.getName().equalsIgnoreCase(ChatColor.stripColor(e.getCustomName()))) {

                                            e.setHealth(0);
                                            e.remove();
                                        }
                                    }

                                }
                            }

                        }
                        ShopRepository.loadShops(i);
                        ShopKeeperRepository.loadShopKeepers(i);
                    }
                } else if (args.length > 0) {
                    if (ClientUtilities.getOnlineClient(p).hasRank(Rank.ADMIN, true)) {
                        if (args.length == 2) {
                            if (args[0].equalsIgnoreCase("create")) {
                                ShopManager.spawnShop(i, p.getLocation(), args[1]);
                                ShopKeeperRepository.addKeeper(args[1], p.getLocation());
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onUpdateSh(UpdateEvent ev) {
        if (ev.getType() == UpdateEvent.UpdateType.MIN_16) {
            for (World w : Bukkit.getWorlds()) {
                for (LivingEntity e : w.getLivingEntities()) {
                    if (e instanceof Player || e instanceof ArmorStand) continue;

                    for (Shop s : ShopManager.getShops()) {
                        //Bukkit.broadcastMessage(s.getName());
                        if (e.getCustomName() != null) {

                            if (s.getName().equalsIgnoreCase(ChatColor.stripColor(e.getCustomName()))) {

                                e.setHealth(0);
                                e.remove();
                            }
                        }
                    }
                }
            }
            ShopRepository.loadShops(i);
        }
    }


    @Override
    public void help(Player player) {


    }


}