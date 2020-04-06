package net.betterpvp.clans.skills.selector.skills.ranger;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.classes.Role;
import net.betterpvp.clans.classes.roles.Ranger;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.clans.skills.selector.skills.data.ChargeData;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.framework.UpdateEvent.UpdateType;
import net.betterpvp.core.utility.UtilBlock;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilTime;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.Vector;

import java.util.*;

public class Barrage extends Skill {
    public List<ChargeData> data = new ArrayList<>();
    public static List<Arrow> arrows = new ArrayList<>();
    private WeakHashMap<Player, Long> charging = new WeakHashMap<>();

    public Barrage(Clans i) {
        super(i, "Barrage", "Ranger",
                getBow,
                rightClick, 5, true, false);
    }

    @Override
    public String[] getDescription(int level) {

        return new String[]{
                "Hold bow charge to channel.",
                "",
                "Shoot a barrage of arrows",
                "Shoots extra arrows based on charge time",
                "",
                "Maximum Arrows: " + ChatColor.GREEN + (4 + (level * 2)),
                "Charge Time: " + ChatColor.GREEN + ((getRecharge(level) / 100) / 10)
        };
    }

    @Override
    public void activateSkill(Player player) {

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if (charging.containsKey(e.getPlayer())) {
            charging.remove(e.getPlayer().getUniqueId());
        }
    }

    @Override
    public boolean usageCheck(Player player) {
        if (player.getLocation().getBlock().getType() == Material.WATER) {
            UtilMessage.message(player, "Skill", "You cannot use " + ChatColor.GREEN + getName() + " in water.");
            return false;
        }

        return false;
    }

    public ChargeData getData(UUID uuid) {
        for (ChargeData barrage : data) {
            if (barrage.getUUID() == barrage.getUUID()) {
                return barrage;
            }
        }
        return null;
    }

    @EventHandler
    public void onBarrageActivate(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (UtilBlock.usable(event.getClickedBlock())) {
                return;
            }
        }

        if (Arrays.asList(getActions()).contains(event.getAction())) {
            if (Arrays.asList(getMaterials()).contains(player.getInventory().getItemInMainHand().getType())) {
                if (hasSkill(player, this)) {
                    if (ClanUtilities.canCast(player)) {
                        if (player.getInventory().contains(Material.ARROW)) {
                            if (!charging.containsKey(player)) {
                                charging.put(player, System.currentTimeMillis());
                                new ChargeData(player.getUniqueId(), 4, (4 + (getLevel(player) * 2)));
                            }
                        }
                    }
                }
            }
        }
    }

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
                final ChargeData d = getData(player.getUniqueId());

                if (d != null) {
                    Bukkit.getScheduler().runTaskTimer(getInstance(), new Runnable() {
                        public void run() {
                            if (d.getCharge() <= 0) {
                                data.remove(d);
                                return;
                            }

                            Vector random = new Vector((Math.random() - 0.5D) / 10.0D, (Math.random() - 0.5D) / 10.0D, (Math.random() - 0.5D) / 10.0D);
                            Arrow arrow = player.launchProjectile(Arrow.class);
                            arrow.setVelocity(player.getLocation().getDirection().add(random).multiply(3));
                            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0F, 1.0F);
                            d.setCharge(d.getCharge() - 1);
                            arrows.add(arrow);
                        }
                    }, 0, 1L);
                }
            }
        }
    }

    @EventHandler
    public void onShoot(EntityShootBowEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            charging.remove(p);
        }
    }

    @EventHandler
    public void updateBarrage(UpdateEvent event) {
        if (event.getType() == UpdateType.TICK) {

            Iterator<ChargeData> iterator = data.iterator();
            while (iterator.hasNext()) {
                ChargeData data = iterator.next();
                Player player = Bukkit.getPlayer(data.getUUID());
                if (player == null) {
                    iterator.remove();
                    continue;
                }
                Role r = Role.getRole(player);
                if (r != null) {
                    if (r instanceof Ranger) {
                        if (player != null) {
                            if (!charging.containsKey(player)) {
                                iterator.remove();
                                continue;
                            }


                            if (player.getInventory().getItemInMainHand().getType() != Material.BOW) {
                                iterator.remove();
                                continue;
                            }

                            if (UtilBlock.isInLiquid(player)) {
                                iterator.remove();
                                continue;
                            }

                            if (!UtilTime.elapsed(charging.get(player), 2000)) {
                                return;
                            }

                            if (UtilTime.elapsed(data.getLastCharge(), (long) getRecharge(getLevel(player)))) {
                                if (data.getCharge() < data.getMaxCharge()) {
                                    data.addCharge();
                                    data.setLastCharge(System.currentTimeMillis() + 25);
                                    UtilMessage.message(player, getClassType(), getName() + ": " + ChatColor.YELLOW + "+ " + data.getCharge() + " Arrows");
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
