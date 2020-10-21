package net.betterpvp.clans.combat;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.AdminClan;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.clans.utilities.UtilClans;
import net.betterpvp.clans.utilities.UtilGamer;
import net.betterpvp.core.utility.UtilPlayer;
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

    public static void addLog(LivingEntity damagee, LivingEntity damager, String name, String cause, double damage) {
        if (damagee instanceof Player && damager instanceof Player) {
            if (!ClanUtilities.canHurt((Player) damager, (Player) damagee)) {
                return;
            }
        }

        if (!getCombatLogs().containsKey(damagee)) {
            getCombatLogs().put(damagee, new ArrayList<CombatLogs>());
        }

        getCombatLogs().get(damagee).add(new CombatLogs(damager, name, cause, damage));
    }

    public static synchronized void addLog(LivingEntity damagee, LivingEntity damager, String cause, double damage) {
        addLog(damagee, damager, "", cause, damage);
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

    public static HashMap<UUID, Integer> getDamageBreakdown(Player killed){
        HashMap<UUID, Integer> breakdown = new HashMap<>();
        if(getCombatLogs().containsKey(killed)){
          List<CombatLogs> logs = getCombatLogs().get(killed);
          logs.stream().forEach(c -> {
              if(c.getDamager() instanceof Player){
                  Player dam = (Player) c.getDamager();
                  if(breakdown.containsKey(dam.getUniqueId())){
                      breakdown.put(dam.getUniqueId(), breakdown.get(dam.getUniqueId()) + (int) c.getDamage());
                  }else{
                      breakdown.put(dam.getUniqueId(), (int) c.getDamage());
                  }
              }
          });
        }
        return sortByValue(breakdown);
    }

    public static <K, V extends Comparable<? super V>> HashMap<K, V> sortByValue(HashMap<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());

        HashMap<K, V> result = new LinkedHashMap<>();
        for (HashMap.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
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

            if (p.getGameMode() == GameMode.SPECTATOR || p.getGameMode() == GameMode.CREATIVE) {
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
