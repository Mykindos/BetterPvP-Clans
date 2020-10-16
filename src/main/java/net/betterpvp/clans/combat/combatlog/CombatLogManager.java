package net.betterpvp.clans.combat.combatlog;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanMember;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.combat.LogManager;
import net.betterpvp.clans.combat.combatlog.npc.events.PlayerInteractNPCEvent;
import net.betterpvp.clans.combat.ratings.Rating;
import net.betterpvp.clans.combat.ratings.RatingRepository;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.core.framework.BPVPListener;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.Iterator;

public class CombatLogManager extends BPVPListener<Clans> {

    public CombatLogManager(Clans i) {
        super(i);
    }


    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {

        if (Clans.getOptions().isHub()) {
            return;
        }

        Player player = event.getPlayer();
        if (LogManager.isSafe(player)) {
            return;
        } else {

            for (Entity entity : player.getNearbyEntities(18, 18, 18)) {
                if (entity instanceof Player) {
                    Player near = (Player) entity;

                    if (!ClanUtilities.canHurt(player, near)) {
                        continue;
                    }

                    CombatLog log = new CombatLog(player);
                    Sheep sheep = (Sheep) log.getNPC().getEntity();
                    sheep.setColor(DyeColor.CYAN);
                    return;
                }
            }

            CombatLog log = new CombatLog(player);
            Sheep sheep = (Sheep) log.getNPC().getEntity();
            sheep.setColor(DyeColor.CYAN);
        }


    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (CombatLog.getCombatLog(player) != null) {
            CombatLog log = CombatLog.getCombatLog(player);
            log.getNPC().remove();
            CombatLog.loggers.remove(log);
        }
    }

    @EventHandler
    public void onCombatNPCClick(PlayerInteractNPCEvent event) {
        Iterator<CombatLog> iterator = CombatLog.loggers.iterator();
        while (iterator.hasNext()) {
            CombatLog logger = iterator.next();
            if (event.getNPC().equals(logger.getNPC())) {
                for (ItemStack stack : logger.getItems()) {
                    if (stack == null || stack.getType() == Material.AIR) {
                        continue;
                    }

                    logger.getNPC().getLocation().getWorld().dropItemNaturally(logger.getNPC().getLocation(), stack);
                }

				/*
				if(ClanUtilities.canHurt(event.getPlayer(), logger.getPlayer())){
					if(BountyManager.hasBounty(logger.getPlayer().getUniqueId())){
						Bounty b = BountyManager.getBounty(logger.getPlayer().getUniqueId());
						ClientUtilities.getClient(event.getPlayer()).getGamer().addCoins(b.getAmount());
						UtilMessage.message(event.getPlayer(), "Bounty", "You killed " +
								ChatColor.YELLOW + logger.getPlayer().getName() + ChatColor.GRAY + " for a bounty of $" + ChatColor.YELLOW + b.getAmount() + ChatColor.GRAY + ".");
						UtilMessage.broadcast("Bounty", ChatColor.YELLOW  + event.getPlayer().getName() + ChatColor.GRAY +  " claimed a bounty of $" + ChatColor.YELLOW
								+ b.getAmount() + ChatColor.GRAY + " from " +ChatColor.YELLOW +  logger.getPlayer().getName() + ChatColor.GRAY + "!");
						BountyManager.removeBounty(logger.getPlayer().getUniqueId());
					}
				}
				 */

                Clan enemy2 = ClanUtilities.getClan(logger.getNPC().getLocation());


                Clan enemy = ClanUtilities.getClan(event.getPlayer());
                Clan self = ClanUtilities.getClan(logger.getPlayer());
                boolean doIt = true;
                if (enemy2 != null && self != null) {
                    if (self.isEnemy(enemy2)) {
                        if (isOnline(enemy2)) {
                            enemy2.getDominance(self).addPoint();
                            doIt = false;
                        }
                    }
                }

                if (doIt) {
                    if (enemy != null && self != null) {
                        if (self.isEnemy(enemy)) {
                            if (isOnline(enemy)) {
                                enemy.getDominance(self).addPoint();
                            }
                        }
                    }
                }

                if (logger.getRole() != null) {
                    Gamer gamer = GamerManager.getGamer(logger.getPlayer().getUniqueId());
                    if (gamer != null) {
                        Rating rating = gamer.getRatings().get(logger.getRole().getName());
                        rating.setRating(rating.getRating() - 15);
                        gamer.getRatings().put(logger.getRole().getName(), rating);
                        RatingRepository.updateRating(gamer, logger.getRole().getName());
                    }
                }


                UtilMessage.broadcast("Log", ChatColor.YELLOW + logger.getPlayer().getName() + ChatColor.GRAY
                        + " dropped inventory for combat logging.");
                File f = new File("world/playerdata", logger.getPlayer().getUniqueId() + ".dat");
                if (f.exists()) {
                    f.delete();
                }

                logger.getNPC().remove();
                iterator.remove();
            }
        }
    }

    private boolean isOnline(Clan c) {
        for (ClanMember d : c.getMembers()) {
            if (Bukkit.getPlayer(d.getUUID()) != null) {
                return true;
            }
        }

        return false;
    }

    @EventHandler
    public void updateCombat(UpdateEvent event) {
        if (event.getType() == UpdateEvent.UpdateType.FASTER) {

            Iterator<CombatTag> iterator = CombatTag.tagged.iterator();
            while (iterator.hasNext()) {
                CombatTag tag = iterator.next();

                if (System.currentTimeMillis() >= tag.getTime()) {
                    iterator.remove();
                }
            }

            Iterator<CombatLog> logIterator = CombatLog.loggers.iterator();
            while (logIterator.hasNext()) {
                CombatLog log = logIterator.next();

                if (System.currentTimeMillis() >= log.getTime()) {
                    if (log.getNPC() != null) {
                        log.getNPC().remove();
                    }
                    logIterator.remove();
                }
            }
        }
    }
}