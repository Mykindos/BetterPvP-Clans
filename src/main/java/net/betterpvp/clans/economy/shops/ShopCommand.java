package net.betterpvp.clans.economy.shops;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.economy.shops.ignatius.IgnatiusSpawnEvent;
import net.betterpvp.clans.economy.shops.mysql.ShopKeeperRepository;
import net.betterpvp.clans.economy.shops.mysql.ShopRepository;
import net.betterpvp.core.client.ClientUtilities;
import net.betterpvp.core.client.Rank;
import net.betterpvp.core.command.Command;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.utility.UtilMessage;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.WanderingTrader;
import org.bukkit.event.EventHandler;

import java.util.HashMap;
import java.util.UUID;


public class ShopCommand extends Command {

    private Clans i;
    private HashMap<UUID, Long> cd = new HashMap<>();

    public ShopCommand(Clans i) {
        super("shop", new String[]{"shops"}, Rank.PLAYER);
        this.i = i;

    }

    @Override
    public void execute(final Player p, String[] args) {
        if (args != null) {
            if (ClientUtilities.getOnlineClient(p).hasRank(Rank.ADMIN, true)) {
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("reload")) {

                        p.sendMessage("Shops have been reloaded");
                        ShopRepository.loadShops(i);
                        ShopKeeperRepository.loadShopKeepers(i);
                    } else if (args[0].equalsIgnoreCase("ignatius")) {
                        UtilMessage.message(p, "Shop", "Spawned Ignatius");
                        Bukkit.getPluginManager().callEvent(new IgnatiusSpawnEvent());
                    }
                } else if (args.length > 0) {
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

    @EventHandler
    public void onUpdateSh(UpdateEvent ev) {
        if (ev.getType() == UpdateEvent.UpdateType.MIN_16) {
            for (World w : Bukkit.getWorlds()) {
                for (LivingEntity e : w.getLivingEntities()) {
                    if (e instanceof Player || e instanceof ArmorStand || e instanceof WanderingTrader) continue;

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