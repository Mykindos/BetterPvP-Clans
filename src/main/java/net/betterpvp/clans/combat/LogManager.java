package net.betterpvp.clans.combat;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.AdminClan;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.clans.utilities.UtilClans;
import net.betterpvp.core.utility.UtilItem;
import net.betterpvp.core.utility.UtilTime;
import org.bukkit.GameMode;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.*;


public class LogManager {

    private static WeakHashMap<LivingEntity, List<CombatLogs>> logs = new WeakHashMap<>();

    public static WeakHashMap<LivingEntity, List<CombatLogs>> getCombatLogs() {
        return logs;
    }

    public static void addLog(LivingEntity damagee, LivingEntity damager, String name, String cause) {
        if (damagee instanceof Player && damager instanceof Player) {
            if (!ClanUtilities.canHurt((Player) damager, (Player) damagee)) {
                return;
            }
        }

        if (!getCombatLogs().containsKey(damagee)) {
            getCombatLogs().put(damagee, new ArrayList<CombatLogs>());
        }

        getCombatLogs().get(damagee).add(new CombatLogs(damager, name, cause));
    }

    public static void addLog(LivingEntity damagee, LivingEntity damager, String cause) {
        addLog(damagee, damager, "", cause);
    }


    public static synchronized void processLogs() {
        Iterator<LivingEntity> eit = logs.keySet().iterator();
        while (eit.hasNext()) {
            LivingEntity ent = eit.next();
            if (ent != null) {
                ListIterator<CombatLogs> it = logs.get(ent).listIterator();
                while (it.hasNext()) {
                    CombatLogs next = it.next();
                    if (next == null) {
                        it.remove();
                        continue;
                    }
                    if (UtilTime.elapsed(next.getExpiry(), 10000)) {
                        it.remove();
                    }
                }
            }
        }
    }


    public static CombatLogs getKiller(LivingEntity p) {
        if (getCombatLogs().containsKey(p)) {
            if (getCombatLogs().get(p).size() <= 0) {
                return null;
            }

            return getCombatLogs().get(p).get(getCombatLogs().get(p).size() - 1);
        }
        return null;
    }

    public static boolean isSafe(Player p) {


        Gamer c = GamerManager.getOnlineGamer(p);
        if (c != null) {

            if (p.isDead()) {
                return true;
            }

            if (c.getClient().isAdministrating()) {
                return true;
            }

            if (p.getGameMode() != GameMode.SURVIVAL && p.getGameMode() != GameMode.ADVENTURE) {
                return true;
            }

            if (Clans.getOptions().isFNG()) {
                return true;
            }

            if (p.getWorld().getName().equals("tutorial")) {
                return true;
            }

           if (UtilClans.hasValuables(p)) {
               return false;
           }

            Clan pc = ClanUtilities.getClan(p);
            if (pc != null) {
                Clan ec = ClanUtilities.getClan(p.getLocation());

                if (ec != null && ec == pc) {
                    if (System.currentTimeMillis() < pc.getLastTnted()) {
                        return false;
                    }

                }

                if (ec != null) {
                    if (pc.isEnemy(ec)) {
                        return false;
                    }
                }
            }


            if (c.safeLogged()) {
                return true;
            }

            if (!UtilTime.elapsed(c.getLastDamaged(), 15000)) {
                return false;
            }


        }


        Clan locClan = ClanUtilities.getClan(p.getLocation());
        if (locClan != null) {
            if (locClan instanceof AdminClan) {
                if (((AdminClan) locClan).isSafe()) {
                    return true;
                }
            }


            if (ClanUtilities.getClan(p) != locClan) {
                return false;
            }
        }


        if (getKiller(p) != null) {
            return false;
        }


        return true;
    }

}
