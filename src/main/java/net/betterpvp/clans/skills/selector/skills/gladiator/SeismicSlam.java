package net.betterpvp.clans.skills.selector.skills.gladiator;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.AdminClan;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.combat.LogManager;
import net.betterpvp.clans.effects.EffectManager;
import net.betterpvp.clans.effects.EffectType;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.framework.UpdateEvent.UpdateType;
import net.betterpvp.core.utility.*;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class SeismicSlam extends Skill {

    private Set<UUID> active = new HashSet<>();
    private List<FallingBlock> fallingBlocks = new ArrayList<>();
    private WeakHashMap<Player, Double> height = new WeakHashMap<>();
    private WeakHashMap<Player, List<LivingEntity>> immune = new WeakHashMap<>();
    public HashMap<Chunk, Long> lastSlam = new HashMap<>();

    public SeismicSlam(Clans i) {
        super(i, "Seismic Slam", "Gladiator",
                getAxes,
                rightClick, 5, true, true);
    }

    @Override
    public String[] getDescription(int level) {
        // TODO Auto-generated method stub
        return new String[]{
                "Right click with Axe to Activate",
                "",
                "Jump and slam the ground, knocking up all opponents",
                "within a small radius",
                "",
                "Cooldown: " + ChatColor.GREEN + getRecharge(level),
                "Energy: " + ChatColor.GREEN + getEnergy(level)
        };
    }

    @Override
    public void activateSkill(final Player player) {
        player.setVelocity(new Vector(0, 1.3, 0));
        player.getWorld().playSound(player.getLocation(), Sound.ZOMBIE_WOOD, 1.5F, 0.2F);
        UtilMessage.message(player, getClassType(), "You used " + ChatColor.GREEN + getName() + " " + getLevel(player) + ChatColor.GRAY + ".");

        new BukkitRunnable() {

            @Override
            public void run() {
                player.setVelocity(player.getLocation().getDirection().multiply(1).add(new Vector(0, -0.5, 0)));
                //player.setVelocity(new Vector(0, -1.5, 0));
                EffectManager.addEffect(player, EffectType.NOFALL, 5000);
                height.put(player, player.getLocation().getY());
                active.add(player.getUniqueId());
                immune.put(player, new ArrayList<LivingEntity>());
            }

        }.runTaskLater(getInstance(), 15);


    }

    @EventHandler
    public void onUpdate(UpdateEvent e) {
        if (e.getType() == UpdateType.TICK) {
            Iterator<UUID> iterator = active.iterator();
            while (iterator.hasNext()) {
                UUID uuid = iterator.next();

                if (Bukkit.getPlayer(uuid) != null) {
                    final Player p = Bukkit.getPlayer(uuid);
                    if (p.isDead()) {
                        iterator.remove();
                        continue;
                    }
                    if (UtilBlock.isGrounded(p) || p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR) {
                        slam(p);

                        iterator.remove();
                    }
                } else {
                    iterator.remove();
                }
            }
        }
    }

    @EventHandler
    public void onUnload(ChunkUnloadEvent e) {

        if (lastSlam.containsKey(e.getChunk())) {
            if (!UtilTime.elapsed(lastSlam.get(e.getChunk()), 60000)) {
                for (Entity ent : e.getChunk().getEntities()) {
                    if (ent instanceof FallingBlock) {

                        ent.remove();
                    }
                }
                e.setCancelled(true);
            }
        }
    }

    @SuppressWarnings("deprecation")
    public void slam(final Player p) {
        lastSlam.put(p.getLocation().getChunk(), System.currentTimeMillis());
        new BukkitRunnable() {
            int i = 0;

            List<Player> hit = new ArrayList<>();
            List<Location> blocks = new ArrayList<>();

            @Override
            public void run() {
                if (i == 5) {
                    cancel();
                }

                final Location loc = p.getLocation();
                loc.getWorld().playSound(loc, Sound.EXPLODE, 2.0F, 1.0F);


                for (Block b : UtilBlock.getInRadius(p.getLocation().add(0, -1, 0), i, 3).keySet()) {
                    if (!blocks.contains(b.getLocation())) {
                        if ((b.getLocation().getBlockY() == loc.getBlockY() - 1) &&
                                (b.getType() != Material.AIR) && (b.getType() != Material.SIGN_POST) && (b.getType() != Material.CHEST) && (b.getType() != Material.DROPPER) && (b.getType() != Material.TRAPPED_CHEST) && (b.getType() != Material.STONE_PLATE) && (b.getType() != Material.WOOD_PLATE)
                                && (b.getType() != Material.WALL_SIGN) && (b.getType() != Material.WALL_BANNER) && (b.getType() != Material.STANDING_BANNER) && (b.getType() != Material.CROPS) && (b.getType() != Material.LONG_GRASS)
                                && (b.getType() != Material.SAPLING) && (b.getType() != Material.DEAD_BUSH) && (!b.getType().name().toLowerCase().contains("fence")) && (b.getType() != Material.RED_ROSE) && (b.getType() != Material.RED_MUSHROOM) && (b.getType() != Material.BROWN_MUSHROOM) && (b.getType() != Material.TORCH) && (b.getType() != Material.LADDER) && (b.getType() != Material.VINE) && (b.getType() != Material.DOUBLE_PLANT) && (b.getType() != Material.PORTAL) && (b.getType() != Material.CACTUS) && (b.getType() != Material.WATER) && (b.getType() != Material.STATIONARY_WATER) && (b.getType() != Material.LAVA) && (b.getType() != Material.STATIONARY_LAVA &&
                                (b.getType().getId() != 43) && (b.getType().getId() != 44) && (b.getRelative(BlockFace.UP).getType() == Material.AIR) && !b.isLiquid())) {
                            FallingBlock fb = loc.getWorld().spawnFallingBlock(b.getLocation().clone().add(0.0D, 1.1, 0.0D), b.getType(), b.getData());
                            blocks.add(b.getLocation());
                            lastSlam.put(b.getLocation().getChunk(), System.currentTimeMillis());
                            fb.setVelocity(new Vector(0.0F, 0.3F, 0.0F));
                            fb.setDropItem(false);
                            fallingBlocks.add(fb);
                        }
                    }
                }


                for (LivingEntity ent : UtilPlayer.getAllInRadius(p.getLocation(), 4)) {
                    if (ent.equals(p)) continue;
                    if (immune.get(p).contains(ent)) continue;
                    if (ent instanceof Player) {
                        if (hit.contains(p)) continue;
                        Player dam = (Player) ent;
                        if (!ClanUtilities.canHurt(p, dam)) {
                            continue;
                        }
                        hit.add(dam);

                    }

                    immune.get(p).add(ent);
                    LogManager.addLog(ent, p, "Seismic Slam");
                    UtilVelocity.velocity(ent, 0.3 * ((height.get(p) - ent.getLocation().getY()) * 0.1), 1, 3, true);
                    Bukkit.getPluginManager().callEvent(new CustomDamageEvent(ent, p, null, DamageCause.CUSTOM, 3.0, false));
                }
                i++;
            }

        }.runTaskTimer(getInstance(), 0, 2);

    }


    @EventHandler
    public void onBlockChangeState(EntityChangeBlockEvent event) {
        if (fallingBlocks.contains(event.getEntity())) {
            event.setCancelled(true);
            fallingBlocks.remove(event.getEntity());
            FallingBlock fb = (FallingBlock) event.getEntity();
            fb.getWorld().playSound(fb.getLocation(), Sound.STEP_STONE, 1.0F, 1.0F);
            event.getEntity().remove();
        }
    }

    @Override
    public boolean usageCheck(Player player) {

        if (!hasSkill(player, this)) {

            return false;
        }
        Clan clan = ClanUtilities.getClan(player.getLocation());
        if (clan != null) {
            if (clan instanceof AdminClan) {
                AdminClan adminClan = (AdminClan) clan;

                if (adminClan.isSafe()) {
                    UtilMessage.message(player, getClassType(), "You cannot use " + ChatColor.GREEN + getName() + ChatColor.GRAY + " in Safe Zones.");
                    return false;
                }
            }
        }

        if (player.getLocation().getBlock().getType() == Material.WATER || player.getLocation().getBlock().getType() == Material.STATIONARY_WATER) {
            UtilMessage.message(player, "Skill", "You cannot use " + ChatColor.GREEN + getName() + " in water.");
            return false;
        }

        return true;
    }


    @Override
    public Types getType() {
        // TODO Auto-generated method stub
        return Types.AXE;
    }

    @Override
    public double getRecharge(int level) {
        // TODO Auto-generated method stub
        return 25 - ((level - 1) * 2);
    }

    @Override
    public float getEnergy(int level) {
        // TODO Auto-generated method stub
        return 50 - ((level - 1) * 5);
    }

    @Override
    public boolean requiresShield() {
        // TODO Auto-generated method stub
        return false;
    }

}
