package net.betterpvp.clans.clans.commands.subcommands;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.clans.Pillage;
import net.betterpvp.clans.clans.commands.IClanCommand;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.core.framework.BPVPListener;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.framework.UpdateEvent.UpdateType;
import net.betterpvp.core.utility.Titles;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilTime;
import net.betterpvp.core.utility.UtilTime.TimeUnit;
import net.betterpvp.core.utility.recharge.RechargeManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.WeakHashMap;


public class HomeCommand extends BPVPListener<Clans> implements IClanCommand {

    public HomeCommand(Clans instance) {
        super(instance);

    }


    private WeakHashMap<Player, Long> teleports = new WeakHashMap<>();


    public void run(Player player, String[] args) {
        Clan clan = ClanUtilities.getClan(player);
        if (clan == null) {
            UtilMessage.message(player, "Clans", "You are not in a Clan.");
            return;
        }

        if (RechargeManager.getInstance().add(player, "Clan-Home-Command", 30, true)) {


            if (player.getWorld().getName().equalsIgnoreCase("bossworld")) {
                UtilMessage.message(player, "Clans", "You cant teleport home from here");
                return;
            }

            if (clan.getHome() == null) {
                UtilMessage.message(player, "Clans", "Your Clan has not set a Home.");
                return;
            }

            Gamer gamer = GamerManager.getOnlineGamer(player);

            Clan locationClan = ClanUtilities.getClan(player.getLocation());
            if (locationClan != null) {
                if (gamer.getClient().isAdministrating()) {
                    UtilMessage.message(player, "Clans", "You teleported to your Clan Home.");
                    player.teleport(clan.getHome());
                    return;
                }

                if (locationClan.getName().equalsIgnoreCase("Blue Spawn")
                        || locationClan.getName().equalsIgnoreCase("Red Spawn")) {
                    if (!UtilTime.elapsed(gamer.getLastDamaged(), 15000)) {
                        UtilMessage.message(player, "Clans", "Cannot teleport from spawn while in combat");
                        return;
                    }

                    if (player.getLocation().getY() > 110) {
                        Clan homeClan = ClanUtilities.getClan(clan.getHome());
                        if (homeClan != null && homeClan.equals(ClanUtilities.getClan(player))) {
                            if(Pillage.pillages.stream().anyMatch(pillage -> pillage.getPillaged().equals(homeClan))){
                                if(!RechargeManager.getInstance().add(player, "Clan Home", 30.0D, true)){
                                    return;
                                }
                            }
                            UtilMessage.message(player, "Clans", "You teleported to your Clan Home.");
                            player.teleport(clan.getHome());
                        }
                    } else {

                        UtilMessage.message(player, "Clans", "You can only teleport home from spawn or in the wilderness.");

                    }

                } else {
                    UtilMessage.message(player, "Clans", "You can only teleport home from spawn or in the wilderness.");
                }
            } else {
                if (!Clans.getOptions().canClanHomeFromWilderness()) {
                    UtilMessage.message(player, "Clans", "You can only teleport home from spawn or in the wilderness.");
                    return;
                }


                if (teleports.containsKey(player)) {
                    UtilMessage.message(player, "Clans", "You already have a home teleport in progress.");
                    return;
                }

                teleports.put(player, System.currentTimeMillis());

            }
        }
    }

    @EventHandler
    public void onUodate(UpdateEvent e) {
        if (e.getType() == UpdateType.FASTEST) {
            if (teleports.isEmpty()) return;
            Iterator<Entry<Player, Long>> it = teleports.entrySet().iterator();
            while (it.hasNext()) {
                Entry<Player, Long> next = it.next();

                Titles.sendTitle(next.getKey(), 0, 20, 20, "", ChatColor.YELLOW + "Teleporting in " + ChatColor.GREEN +
                        Math.max(0, UtilTime.convert((next.getValue() + 30000) - System.currentTimeMillis(), TimeUnit.BEST, 1))
                        + " " + UtilTime.getTimeUnit2(next.getValue() + 30000 - System.currentTimeMillis()));

                if ((next.getValue() + 30000) - System.currentTimeMillis() <= 0) {
                    UtilMessage.message(next.getKey(), "Clans", "You teleported to your Clan Home.");
                    next.getKey().teleport(ClanUtilities.getClan(next.getKey()).getHome());
                    it.remove();


                }
            }
        }
    }

    @EventHandler
    public void onDamage(CustomDamageEvent e) {
        if (e.getDamagee() instanceof Player) {
            Player player = (Player) e.getDamagee();
            if (teleports.containsKey(player)) {
                teleports.remove(player);
                Titles.sendTitle(player, 0, 20, 20, "", ChatColor.RED + "Teleport cancelled");
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (teleports.containsKey(e.getPlayer())) {
            if (e.getFrom().getBlockX() != e.getTo().getBlockX() || e.getFrom().getBlockY() != e.getTo().getBlockY()
                    || e.getFrom().getBlockZ() != e.getTo().getBlockZ()) {
                teleports.remove(e.getPlayer());
                Titles.sendTitle(e.getPlayer(), 0, 20, 20, "", ChatColor.RED + "Teleport cancelled");
            }
        }
    }


    @Override
    public String getName() {

        return "Home";
    }

}
