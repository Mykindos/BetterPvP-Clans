package net.betterpvp.clans.combat;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanMember;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.classes.Role;
import net.betterpvp.clans.classes.events.CustomDeathEvent;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.clans.utilities.UtilRating;
import net.betterpvp.core.database.Log;
import net.betterpvp.core.framework.BPVPListener;
import net.betterpvp.core.utility.UtilFormat;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.recharge.Recharge;
import net.betterpvp.core.utility.recharge.RechargeManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Iterator;


public class CombatManager extends BPVPListener<Clans> {


    public CombatManager(Clans i) {
        super(i);

    }


    @EventHandler
    public void mobDamage(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player) {

            LivingEntity ent = null;
            if (e.getDamager() instanceof Projectile) {
                Projectile proj = (Projectile) e.getDamager();
                if (proj.getShooter() instanceof LivingEntity) {
                    if (!(proj.getShooter() instanceof Player)) {
                        ent = (LivingEntity) proj.getShooter();
                    }
                }
            } else if (e.getDamager() instanceof LivingEntity) {
                if (!(e.getDamager() instanceof Player)) {
                    ent = (LivingEntity) e.getDamager();
                }
            }

            if (ent != null) {
                Player p = (Player) e.getEntity();
                //String name = ent.getCustomName() != null ? ent.getCustomName() : ChatColor.YELLOW + ent.getName();
                LogManager.addLog(p, ent, "");
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        e.setDeathMessage(null);
        if (e.getEntity() instanceof Player) {
            Player p = e.getEntity();
            Gamer g = GamerManager.getOnlineGamer(p);
            g.setLastDamaged(0);
            if (LogManager.getKiller(p) != null) {
                LivingEntity ent = LogManager.getKiller(p).getDamager();
                if (ent instanceof Player) {
                    Player dam = (Player) ent;
                    Gamer damGamer = GamerManager.getOnlineGamer(dam);
                    if (!ent.equals(p)) {

                        Bukkit.getPluginManager().callEvent(new CustomDeathEvent(p, dam));


                        if (RechargeManager.getInstance().add(dam, "Player-" + p.getName(), 300, false, false)) {
                            if (Role.getRole(p) != null) {
                                double fragments = 2;


                                damGamer.addFragments(fragments);

                                UtilMessage.message(dam, "Fragments", "You received " + ChatColor.GREEN +
                                        (fragments) + ChatColor.GRAY + " fragments.");
                            } else {
                                UtilMessage.message(dam, "Fragments", "You received no fragments as the player you killed was naked!");
                            }
                        }

                    }

                    CombatLogs killDetails = LogManager.getKiller(p);
                    String rolea = getRolePrefix(p);
                    String roleb = getRolePrefix(dam);
                    String prefix = "Death";
                    String length = "";
                    if (killDetails.getCause().equalsIgnoreCase("Longshot")) {
                        if (p.getLocation().distance(dam.getLocation()) > 40) {
                            length = ChatColor.GRAY + " from " + ChatColor.GREEN + (int) p.getLocation().distance(dam.getLocation()) + ChatColor.GRAY + " yards";
                        }
                    }


                    for (Player online : Bukkit.getOnlinePlayers()) {

                        Gamer onlineGamer = GamerManager.getOnlineGamer(online);

                        if (!onlineGamer.getClient().getSettingAsBoolean("General.Killfeed")
                                && !online.getName().equals(dam.getName()) && !online.getName().equals(p.getName())
                                && !ClanUtilities.isClanMember(dam, online) && !ClanUtilities.isClanMember(p, online)) {
                            continue;
                        }

                        Clan clanA = ClanUtilities.getClan(online);
                        String playerColour = ClanUtilities.getRelation(clanA, ClanUtilities.getClan(p)).getPrimary().toString();
                        String killerColour = ClanUtilities.getRelation(clanA, ClanUtilities.getClan(dam)).getPrimary().toString();

                        if (killDetails.getCause().equals("")) {
                           UtilMessage.message(online, prefix, rolea + playerColour + p.getName() + ChatColor.GRAY + " was killed by " + roleb + killerColour + dam.getName()
                                    + ChatColor.GRAY + " with " + ChatColor.GREEN + UtilFormat.cleanString(ChatColor.stripColor(getWeaponName(dam.getInventory().getItemInMainHand()))));
                        } else {
                           UtilMessage.message(online, prefix, rolea + playerColour + p.getName() + ChatColor.GRAY + " was killed by " + roleb + killerColour + dam.getName()
                                    + ChatColor.GRAY + " with " + ChatColor.GREEN + UtilFormat.cleanString(ChatColor.stripColor(killDetails.getCause())) + length);
                        }


                    }

                    g.setLastDamaged(0);


                    Role killedRole = Role.getRole(p);
                    Role killerRole = Role.getRole(dam);
                    if (killedRole != null) {
                        if(killerRole != null){
                            UtilRating.adjustRating(damGamer, killerRole, g, killedRole);
                        }


                        g.addDeath();
                        damGamer.addKill();

                    }

                    int tax = (int) (g.getCoins() * 0.10);

                    g.removeCoins(tax);
                    damGamer.addCoins(tax);


                    UtilMessage.message(dam, "Coins", "You collected " + ChatColor.YELLOW + "$"
                            + UtilFormat.formatNumber(tax) + ChatColor.GRAY + " Coins");
                    UtilMessage.message(p, "Coins", "You lost " + ChatColor.YELLOW + "$"
                            + UtilFormat.formatNumber(tax) + ChatColor.GRAY + " Coins");

                    Clan killerClan = ClanUtilities.getClan(dam);
                    Clan deadClan = ClanUtilities.getClan(p);


                    String dom = "";
                    if (killerClan != null && deadClan != null) {
                        if (killerClan.isOnCooldown() || deadClan.isOnCooldown()) {
                            if (killerClan.isOnCooldown()) {
                                UtilMessage.message(p, "Clans", "You did not lose any dominance as the enemy clan is on cooldown.");
                                UtilMessage.message(dam, "Clans", "You cannot lose or gain dominance while on cooldown.");
                            }

                            if (deadClan.isOnCooldown()) {
                                UtilMessage.message(p, "Clans", "You cannot lose or gain dominance while on cooldown.");
                                UtilMessage.message(dam, "Clans", "You gained no dominance as the enemy clan is on cooldown.");
                            }

                            return;
                        }


                        if (deadClan.isEnemy(killerClan)) {

                            killerClan.getDominance(deadClan).addPoint();
                            dom = " (+1 Dom)";
                            //ScoreboardManager.updateRelation(deadClan, killerClan);

                        }
                    }

                    Log.write("Kill", p.getName() + " was killed by " + dam.getName() + dom);


                } else {
                    CombatLogs killDetails = LogManager.getKiller(p);
                    String rolea = getRolePrefix(p);
                    String reason = killDetails.getName().equals("") ? killDetails.getDamager().getName() : killDetails.getName();
                    String cause = killDetails.getCause().equals("") ? "" : ChatColor.GRAY + " with " + ChatColor.GREEN + UtilFormat.cleanString(ChatColor.stripColor(killDetails.getCause()));


                    for (Player online : Bukkit.getOnlinePlayers()) {
                        String playerColour = ClanUtilities.getRelation(ClanUtilities.getClan(online), ClanUtilities.getClan(p)).getPrimary().toString();


                        UtilMessage.message(online, "Death", rolea + playerColour + p.getName() + ChatColor.GRAY + " was killed by " + ChatColor.YELLOW + reason + cause);


                    }

                }
            } else {

                Clan enemy = ClanUtilities.getClan(p.getLocation());
                Clan self = ClanUtilities.getClan(p);
                if (enemy != null && self != null) {

                    if (self.isEnemy(enemy)) {
                        if (enemy.isOnCooldown() || self.isOnCooldown()) {
                            if (enemy.isOnCooldown()) {
                                UtilMessage.message(p, "Clans", "You did not lose any dominance as the enemy clan is on cooldown.");

                            }

                            if (self.isOnCooldown()) {
                                UtilMessage.message(p, "Clans", "You cannot lose or gain dominance while on cooldown.");

                            }

                        } else {
                            if (isOnline(enemy)) {
                                if (self.getDominance(enemy).getPoints() < 15) {
                                    enemy.getDominance(self).addPoint();
                                    //ScoreboardManager.updateRelation(self, enemy);
                                    Log.write("Kill", p.getName() + " gave " + enemy.getName() + " 1 dom but was not killed by a player!");
                                }
                            }
                        }
                    }
                }
                String rolea = getRolePrefix(p);
                for (Player online : Bukkit.getOnlinePlayers()) {

                    String playerColour = ClanUtilities.getRelation(ClanUtilities.getClan(online), ClanUtilities.getClan(p)).getPrimary().toString();

                    if (p.getLastDamageCause() != null) {
                        UtilMessage.message(online, "Death", rolea + playerColour + p.getName() + ChatColor.GRAY + " was killed by " + getCause(p.getLastDamageCause().getCause()));
                    } else {
                        UtilMessage.message(online, "Death", rolea + playerColour + p.getName() + ChatColor.GRAY + " committed suicide.");
                    }
                }

            }
        }


    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {

        Gamer g = GamerManager.getOnlineGamer(e.getPlayer());

        if (g != null) {
            g.setLastDamaged(0);
            if (LogManager.getCombatLogs().containsKey(e.getPlayer())) {
                LogManager.getCombatLogs().get(e.getPlayer()).clear();
            }
        }

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void entDeath(EntityDeathEvent e) {
        if (LogManager.getCombatLogs().containsKey(e.getEntity())) {
            LogManager.getCombatLogs().remove(e.getEntity());
        }

        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (RechargeManager.getInstance().getRecharges().containsKey(p.getName())) {


                Iterator<Recharge> it = RechargeManager.getInstance().getRecharges(p).iterator();
                while (it.hasNext()) {
                    Recharge next = it.next();
                    if (next.isRemoveOnDeath()) {
                        it.remove();
                    }
                }
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

    private String getWeaponName(ItemStack d) {
        if (d.hasItemMeta()) {
            if (d.getItemMeta().getDisplayName() != null && !d.getItemMeta().getDisplayName().equals("")) {
                return d.getItemMeta().getDisplayName();
            }
        }

        return d.getType().name();
    }

    private String getRolePrefix(Player p) {
        if (Role.playerHasRole(p)) {
            return Role.getPrefix(p, true);
        }
        return "";
    }


    @EventHandler(priority = EventPriority.LOW)
    public void onHit(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof LivingEntity) {
            LivingEntity p = (LivingEntity) e.getEntity();
            LivingEntity dam = null;
            if (e.getDamager() instanceof LivingEntity) {

                dam = (LivingEntity) e.getDamager();
                LogManager.addLog(p, dam, null);

            } else if (e.getDamager() instanceof Projectile) {
                Projectile j = (Projectile) e.getDamager();
                if (j.getShooter() instanceof Player) {
                    dam = (Player) j.getShooter();

                    LogManager.addLog(p, dam, null);

                }
            }


        }
    }


    private String getCause(DamageCause cause) {
        String source = "?";
        if (cause == DamageCause.BLOCK_EXPLOSION) {
            source = "Explosion";
        } else if (cause == DamageCause.CONTACT) {
            source = "Cactus";
        } else if (cause == DamageCause.CUSTOM) {
            source = "Custom";
        } else if (cause == DamageCause.DROWNING) {
            source = "Water";
        } else if (cause == DamageCause.ENTITY_ATTACK) {
            source = "Suicide";
        } else if (cause == DamageCause.ENTITY_EXPLOSION) {
            source = "Explosion";
        } else if (cause == DamageCause.FALL) {
            source = "Fall";
        } else if (cause == DamageCause.FALLING_BLOCK) {
            source = "Falling Block";
        } else if (cause == DamageCause.FIRE) {
            source = "Fire";
        } else if (cause == DamageCause.FIRE_TICK) {
            source = "Fire";
        } else if (cause == DamageCause.LAVA) {
            source = "Lava";
        } else if (cause == DamageCause.LIGHTNING) {
            source = "Lightning";
        } else if (cause == DamageCause.MAGIC) {
            source = "Magic";
        } else if (cause == DamageCause.MELTING) {
            source = "Melting";
        } else if (cause == DamageCause.POISON) {
            source = "Poison";
        } else if (cause == DamageCause.PROJECTILE) {
            source = "Projectile";
        } else if (cause == DamageCause.STARVATION) {
            source = "Starvation";
        } else if (cause == DamageCause.SUFFOCATION) {
            source = "Suffocation";
        } else if (cause == DamageCause.SUICIDE) {
            source = "Suicide";
        } else if (cause == DamageCause.VOID) {
            source = "Suicide";
        } else if (cause == DamageCause.WITHER) {
            source = "Wither";
        }
        return source;
    }

}
