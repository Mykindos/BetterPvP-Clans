package net.betterpvp.clans.skills.selector.skills.ranger;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.classes.Role;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.combat.LogManager;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.clans.skills.selector.skills.data.OverChargeData;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.framework.UpdateEvent.UpdateType;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilTime;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

public class Overcharge extends Skill {

    private List<OverChargeData> data = new ArrayList<OverChargeData>();
    public static List<Arrow> arrows = new ArrayList<Arrow>();
    private Set<UUID> charging = new HashSet<>();


    public Overcharge(Clans i) {
        super(i, "Overcharge", "Ranger",
                getBow,
                rightClick, 5, true, false);
    }

    @Override
    public String[] getDescription(int level) {

        return new String[]{
                "Draw back harder on your bow, giving",
                "2 bonus damage per 0.8 seconds",
                "",
                "Maximum Damage: " + ChatColor.GREEN + (2 + level)
        };
    }

    @Override
    public void activateSkill(Player player) {

    }

    @Override
    public boolean usageCheck(Player player) {
        if (player.getLocation().getBlock().getType() == Material.WATER ) {
            UtilMessage.message(player, "Skill", "You cannot use " + ChatColor.GREEN + getName() + " in water.");
            return false;
        }

        return false;
    }

    public OverChargeData getData(UUID uuid) {
        for (OverChargeData barrage : data) {
            if (barrage.getUUID() == barrage.getUUID()) {
                return barrage;
            }
        }
        return null;
    }

    @EventHandler
    public void onBarrageActivate(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (Arrays.asList(getActions()).contains(event.getAction())) {
            if (Arrays.asList(getMaterials()).contains(player.getInventory().getItemInMainHand().getType())) {
                Role r = Role.getRole(player);
                if (r != null && r.getName().equals(getClassType())) {
                    if (hasSkill(player, this)) {
                        if (ClanUtilities.canCast(player)) {
                            if (player.getInventory().contains(Material.ARROW)) {
                                if (!charging.contains(player.getUniqueId())) {
                                    data.add(new OverChargeData(player.getUniqueId(), 2, (1 + getLevel(player))));
                                    charging.add(player.getUniqueId());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        charging.remove(e.getPlayer().getUniqueId());
    }

    private WeakHashMap<Arrow, Integer> bonus = new WeakHashMap<>();

    @EventHandler
    public void onPlayerShoot(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        if (!(event.getProjectile() instanceof Arrow)) {
            return;
        }

        final Player player = (Player) event.getEntity();
        Role r = Role.getRole(player);
        if (r != null && r.getName().equals(getClassType())) {
            if (hasSkill(player, this)) {
                final OverChargeData data = getData(player.getUniqueId());

                if (data != null) {
                    bonus.put((Arrow) event.getProjectile(), data.getCharge());
                }
            }
        }
    }

    @EventHandler
    public void onDamage(CustomDamageEvent e) {
        if (e.getProjectile() != null) {
            if (e.getProjectile() instanceof Arrow) {
                if (e.getDamager() instanceof Player) {
                    Arrow a = (Arrow) e.getProjectile();

                    if (bonus.containsKey(a)) {


                        LogManager.addLog(e.getDamagee(), ((Player) a.getShooter()), "Overcharge: " + bonus.get(a));


                        e.setDamage(e.getDamage() + bonus.get(a));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onShoot(EntityShootBowEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            charging.remove(p.getUniqueId());
        }
    }


    @EventHandler
    public void updateOvercharge(UpdateEvent event) {
        if (event.getType() == UpdateType.TICK) {

            Iterator<OverChargeData> iterator = data.iterator();
            while (iterator.hasNext()) {
                OverChargeData data = iterator.next();
                Player player = Bukkit.getPlayer(data.getUUID());
                if (player != null) {
                    Role r = Role.getRole(player);
                    if (r != null && r.getName().equals(getClassType())) {
                        if (player != null) {
                            if (!charging.contains(player.getUniqueId())) {
                                iterator.remove();
                                continue;
                            }

                            if (player.getInventory().getItemInMainHand().getType() != Material.BOW) {
                                iterator.remove();
                                continue;
                            }

                            if (player.getLocation().getBlock().isLiquid()) {
                                iterator.remove();
                                continue;
                            }

                            if (UtilTime.elapsed(data.getLastCharge(), 800)) {
                                if (data.getCharge() < data.getMaxCharge()) {
                                    data.addCharge();
                                    data.setLastCharge(System.currentTimeMillis());
                                    UtilMessage.message(player, getClassType(), getName() + ": " + ChatColor.YELLOW + "+ " + data.getCharge() + " Bonus Damage");
                                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.4F, 1.0F + 0.05F * data.getCharge());
                                }
                            }
                        }
                    } else {
                    }

                    Iterator<Arrow> arrowIterator = arrows.iterator();
                    while (arrowIterator.hasNext()) {
                        Arrow arrow = arrowIterator.next();
                        if (arrow.isOnGround() || !arrow.isValid() || arrow.isInsideVehicle()) {
                            arrowIterator.remove();
                        }
                    }
                }
            }
        }
    }


    @Override
    public Types getType() {

        return Types.BOW;
    }

    @Override
    public double getRecharge(int level) {

        return 1200 - ((level - 1) * 100);
    }

    @Override
    public float getEnergy(int level) {

        return 40;
    }

}
