package net.betterpvp.clans.clans.listeners;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.AdminClan;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.clans.ClanUtilities.ClanRelation;
import net.betterpvp.clans.clans.Pillage;
import net.betterpvp.clans.clans.events.ScoreboardUpdateEvent;
import net.betterpvp.core.framework.BPVPListener;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.fancymessage.FancyMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class MovementListener extends BPVPListener<Clans> {

    public MovementListener(Clans i) {
        super(i);
    }

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent e) {
        if (e.getFrom().getBlockX() != e.getTo().getBlockX() || e.getFrom().getBlockZ() != e.getTo().getBlockZ()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    Clan clanTo = ClanUtilities.getClan(e.getTo());
                    Clan clanFrom = ClanUtilities.getClan(e.getFrom());


                    if (clanTo == null && clanFrom == null) {

                        return;
                    }


                    if (clanFrom == null && clanTo != null
                            || clanFrom != null && clanTo == null
                            || clanFrom != null && clanTo != null
                            && !clanFrom.equals(clanTo)) {
                        displayOwner(e.getPlayer(), e.getTo());

                        new BukkitRunnable(){
                            @Override
                            public void run(){
                                Bukkit.getPluginManager().callEvent(new ScoreboardUpdateEvent(e.getPlayer()));
                            }
                        }.runTask(getInstance());

                    }
                }
            }.runTaskAsynchronously(getInstance());
        }

    }

    // TODO UNCOMMENT BEFORE RELEASE
    /*
    @EventHandler
    public void onUpdate(UpdateEvent e) {
        if (e.getType() == UpdateEvent.UpdateType.FASTER) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                Clan target = ClanUtilities.getClan(p.getLocation());
                if (target == null) {
                    if (p.getWorld().getName().equalsIgnoreCase("bossworld")) {
                        if (p.getGameMode() == GameMode.SURVIVAL) {

                            p.setGameMode(GameMode.ADVENTURE);

                        }
                    } else {
                        if (p.getGameMode() == GameMode.ADVENTURE) {
                            p.setGameMode(GameMode.SURVIVAL);
                        }
                    }
                } else {
                    if (target.getName().equalsIgnoreCase("Fields")) {
                        if (p.getGameMode() == GameMode.ADVENTURE) {
                            p.setGameMode(GameMode.SURVIVAL);
                        }
                    } else {
                        Clan c = ClanUtilities.getClan(p);

                        if (c != null) {


                            if (c == target) {
                                if (p.getGameMode() == GameMode.ADVENTURE) {
                                    p.setGameMode(GameMode.SURVIVAL);
                                }
                            } else {
                                if (Pillage.isPillaging(c, target)) {
                                    if (p.getGameMode() == GameMode.ADVENTURE) {
                                        p.setGameMode(GameMode.SURVIVAL);
                                    }
                                } else {
                                    if (p.getLocation().getY() < Clans.getOptions().getFarmingMaxY()
                                            && p.getLocation().getY() > Clans.getOptions().getFarmingMinY()) {
                                        if (p.getGameMode() == GameMode.ADVENTURE) {
                                            p.setGameMode(GameMode.SURVIVAL);
                                        }
                                    } else {
                                        if (p.getGameMode() == GameMode.SURVIVAL) {


                                            p.setGameMode(GameMode.ADVENTURE);


                                        }
                                    }
                                }
                            }
                        } else {
                            if (p.getGameMode() == GameMode.SURVIVAL) {
                                p.setGameMode(GameMode.ADVENTURE);
                            }
                        }
                    }
                }
            }


        }
    }

     */


    public void displayOwner(Player p, Location location) {

        String ownerString = ChatColor.YELLOW + "Wilderness";

        Clan clan = ClanUtilities.getClan(p);
        Clan target = ClanUtilities.getClan(location);


        String append = "";

        if (target != null) {
            ClanRelation relation = ClanUtilities.getRelation(clan, target);
            ownerString = ClanUtilities.getRelation(clan, target).getPrimary() + target.getName();

            if (target instanceof AdminClan) {
                AdminClan adminClan = (AdminClan) target;
                if (adminClan.isSafe()) {
                    ownerString = ChatColor.WHITE + target.getName();
                    append = ChatColor.WHITE + "(" + ChatColor.AQUA + "Safe" + ChatColor.WHITE + ")";
                }
            } else if (relation == ClanRelation.ALLY_TRUST) {
                append = ChatColor.GRAY + "(" + ChatColor.YELLOW + "Trusted" + ChatColor.GRAY + ")";

            } else if (relation == ClanRelation.ENEMY) {
                if (clan != null) {
                    append = clan.getDominanceString(target);
                }

            }
        }

        if (target != null) {
            if (target.getName().equals("Fields") || target.getName().equals("Lake")) {
                append = ChatColor.RED.toString() + ChatColor.BOLD + "                    Warning! " + ChatColor.GRAY.toString() + ChatColor.BOLD + "PvP Hotspot";
            }
            //
            new FancyMessage(ChatColor.BLUE + "Territory> " + ownerString + " " + append).tooltip(ClanUtilities.getClanTooltip(p, target)).send(p);
        } else {
            UtilMessage.message(p, "Territory", ownerString + " " + append);
        }


    }
}
