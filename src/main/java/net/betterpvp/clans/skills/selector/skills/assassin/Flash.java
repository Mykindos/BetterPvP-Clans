package net.betterpvp.clans.skills.selector.skills.assassin;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.classes.Energy;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.effects.EffectManager;
import net.betterpvp.clans.effects.EffectType;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.events.SkillEquipEvent;
import net.betterpvp.clans.skills.selector.skills.InteractSkill;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.framework.UpdateEvent.UpdateType;
import net.betterpvp.core.particles.ParticleEffect;
import net.betterpvp.core.utility.*;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Set;
import java.util.WeakHashMap;

public class Flash extends Skill implements InteractSkill {

    private WeakHashMap<Player, Location> loc = new WeakHashMap<>();
    private WeakHashMap<Player, Integer> charges = new WeakHashMap<>();
    private WeakHashMap<Player, Long> lastRecharge = new WeakHashMap<>();
    private WeakHashMap<Player, Long> blinkTime = new WeakHashMap<>();

    public Flash(Clans i) {
        super(i, "Flash", "Assassin", getAxes,
                rightClick, 5,
                false, true);

    }

    @Override
    public String[] getDescription(int level) {

        return new String[]{
                "Right click with a axe to activate.",
                "",
                "Instantly teleport forwards 8 Blocks.",
                "Cannot be used while Slowed.",
                "",
                "Stores up to 3 charges.",
                "",
                "Cannot be used while Slowed.",
                "Recharge: 1 charge per " + ChatColor.GREEN + (11 - level) + ChatColor.GRAY + " seconds."
        };
    }

    @Override
    public Types getType() {

        return Types.AXE;
    }

    @EventHandler
    public void skillEquipEvent(SkillEquipEvent e) {
        if (e.getSkill().getName().equalsIgnoreCase(getName())) {
            if (!charges.containsKey(e.getPlayer())) {
                charges.put(e.getPlayer(), 0);
            }

            if (!lastRecharge.containsKey(e.getPlayer())) {
                lastRecharge.put(e.getPlayer(), System.currentTimeMillis());
            }
        }
    }

    @EventHandler
    public void recharge(UpdateEvent e) {
        if (e.getType() == UpdateType.TICK_2) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (hasSkill(p, this)) {
                    if (!charges.containsKey(p)) {
                        charges.put(p, 0);
                    }


                    if (!lastRecharge.containsKey(p)) {
                        lastRecharge.put(p, System.currentTimeMillis());
                    }

                    if (charges.get(p) == 3) {
                        lastRecharge.put(p, System.currentTimeMillis());
                        continue;
                    }

                    if (UtilTime.elapsed(lastRecharge.get(p), (11000 - (getLevel(p) * 1000)))) {
                        charges.put(p, Math.min(3, charges.get(p) + 1));
                        UtilMessage.message(p, getClassType(), "Flash Charges: " + ChatColor.GREEN + charges.get(p));
                        lastRecharge.put(p, System.currentTimeMillis());
                    }
                }
            }
        }
    }

    @EventHandler
    public void onCustomDamage(CustomDamageEvent e) {
        if (e.getCause() == DamageCause.SUFFOCATION) {
            if (e.getDamager() == null) {
                if (e.getDamagee() instanceof Player) {
                    Player player = (Player) e.getDamagee();
                    if (blinkTime.containsKey(player)) {
                        if (!UtilTime.elapsed(blinkTime.get(player), 500)) {
                            deblink(player, true);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDetectGlass(UpdateEvent e) {
        if (e.getType() == UpdateType.TICK_2) {
            for (Player p : blinkTime.keySet()) {
                if (!UtilTime.elapsed(blinkTime.get(p), 250)) {
                    for (double x = -0.3; x <= 0.3; x += 0.3) {
                        for (double z = -0.3; z <= 0.3; z += 0.3) {
                            Location loc = new Location(p.getWorld(), Math.floor(p.getLocation().getX() + x),
                                    p.getLocation().getY(), Math.floor(p.getLocation().getZ() + z));

                            if (loc.getBlock().getType().name().contains("GLASS") || loc.getBlock().getType().name().contains("DOOR")) {

                                deblink(p, true);
                            }
                        }
                    }

                }
            }
        }
    }

    public void deblink(Player player, boolean force) {

        if (ClanUtilities.canCast(player)) {


            Block lastSmoke = player.getLocation().getBlock();

            double curRange = 0.0D;

            Location target = this.loc.remove(player);

            boolean done = false;
            while (!done) {
                Vector vec = UtilVelocity.getTrajectory(player.getLocation(),
                        new Location(player.getWorld(), target.getX(), target.getY(), target.getZ()));

                Location newTarget = player.getLocation().add(vec.multiply(curRange));


                curRange += 0.2D;


                if (!lastSmoke.equals(newTarget.getBlock())) {
                    //lastSmoke.getWorld().playEffect(lastSmoke.getLocation(), Effect.SMOKE, 4);
                }
                lastSmoke = newTarget.getBlock();

                if (UtilMath.offset(newTarget, target) < 0.4D) {
                    done = true;
                }
                if (curRange > 24.0D) {
                    done = true;
                }
                if (curRange > 24.0D) {
                    done = true;
                }
            }

            player.teleport(target);

            player.setFallDistance(0.0F);


            //player.getWorld().playEffect(player.getLocation(), Effect.BLAZE_SHOOT, 0, 15);
        }

    }

    @Override
    public boolean usageCheck(Player player) {

        if (!hasSkill(player, this)) {
            return false;
        }

        if (player.hasPotionEffect(PotionEffectType.SLOW)) {
            UtilMessage.message(player, getClassType(), "You cannot use " + getName() + " while Slowed.");
            return false;
        }

        if (UtilBlock.isInLiquid(player)) {
            UtilMessage.message(player, getClassType(), "You cannot use " + getName() + ChatColor.GRAY + " in water.");
            return false;
        }

        if (player.getTargetBlock((Set<Material>) null, 4).getType() == Material.GLASS) {
            return false;
        }

        if (EffectManager.hasEffect(player, EffectType.STUN)) {
            UtilMessage.message(player, getClassType(), "You cannot use " + org.bukkit.ChatColor.GREEN
                    + getName(getLevel(player)) + ChatColor.GRAY + " while stunned.");
            return false;
        }

        if (charges.containsKey(player)) {
            if (charges.get(player) == 0) {
                UtilMessage.message(player, getClassType(), "You don't have any " + ChatColor.GREEN + getName() + ChatColor.GRAY + " charges.");
                return false;
            }
        }


        return true;
    }

    private boolean isWall(Block b) {
        return !UtilBlock.airFoliage(b) || !UtilBlock.airFoliage(b.getRelative(BlockFace.DOWN))
                || (!UtilBlock.airFoliage(b.getRelative(BlockFace.WEST)) && !UtilBlock.airFoliage(b.getRelative(BlockFace.NORTH)))
                || (!UtilBlock.airFoliage(b.getRelative(BlockFace.EAST)) && !UtilBlock.airFoliage(b.getRelative(BlockFace.NORTH)))
                || (!UtilBlock.airFoliage(b.getRelative(BlockFace.EAST)) && !UtilBlock.airFoliage(b.getRelative(BlockFace.SOUTH)))
                || (!UtilBlock.airFoliage(b.getRelative(BlockFace.WEST)) && !UtilBlock.airFoliage(b.getRelative(BlockFace.SOUTH)));

    }

    @Override
    public double getRecharge(int level) {

        return 0;
    }

    @Override
    public float getEnergy(int level) {

        return 0;
    }

    @Override
    public void activate(Player player, Gamer gamer) {
        Block lastSmoke = player.getLocation().getBlock();

        Location oldPos = player.getLocation().clone();
        Location newPos = null;

        if (charges.containsKey(player)) {
            if (charges.get(player) > 0) {

                if (Energy.use(player, getName(), getEnergy(getLevel(player)), true)) {
                    charges.put(player, charges.get(player) - 1);
                    double maxRange = 8.0D;
                    double curRange = 0.0D;
                    while (curRange <= maxRange) {
                        Location newTarget = player.getLocation().add(new Vector(0.0D, 0.2D, 0.0D)).add(player.getLocation().getDirection().multiply(curRange));

                        if (newTarget.getBlock().getType() == Material.LEGACY_IRON_DOOR_BLOCK || newTarget.getBlock().getType() == Material.IRON_DOOR) {
                            player.setVelocity(player.getLocation().getDirection().multiply(-0.25).add(new Vector(0, 0.1, 0)));

                            break;
                        }

                        if (newTarget.getBlock().getType() == Material.GLASS || newTarget.getBlock().getType().name().contains("STAINED_GLASS")) {
                            player.setVelocity(player.getLocation().getDirection().multiply(-0.25).add(new Vector(0, 0.1, 0)));

                            break;
                        }

                        if ((!UtilBlock.airFoliage(newTarget.getBlock())) || (!UtilBlock.airFoliage(newTarget.getBlock().getRelative(BlockFace.UP)))) {

                            break;
                        }


                        if (isWall(newTarget.getBlock().getRelative(BlockFace.UP))) {
                            player.setVelocity(player.getLocation().getDirection().multiply(-1).add(new Vector(0, 0.1, 0)).multiply(0.25));
                            break;
                        }


                        for (Player cur : player.getWorld().getPlayers()) {
                            if (!cur.equals(player)) {

                                if (UtilMath.offset(newTarget, cur.getLocation()) <= 0.5D) {


                                    Location target = cur.getLocation().add(player.getLocation().subtract(cur.getLocation()).toVector().normalize());
                                    player.teleport(UtilWorld.locMerge(player.getLocation(), target));


                                    UtilMessage.message(player, getClassType(), "You used " + ChatColor.GREEN + getName() + " " + getLevel(player) + ChatColor.GRAY + ".");


                                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_WITHER_SHOOT, 0.4F, 1.2F);
                                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_SILVERFISH_DEATH, 1.0F, 1.6F);
                                    return;
                                }
                            }
                        }
                        curRange += 0.1D;


                        if (!lastSmoke.equals(newTarget.getBlock())) {
                            //lastSmoke.getWorld().playEffect(lastSmoke.getLocation(), Effect.SMOKE, 4);
                        }
                        lastSmoke = newTarget.getBlock();
                    }


                    curRange -= 1D;
                    if (curRange < 0.0D) {
                        curRange = 0.0D;
                    }

                    Location loc = player.getLocation().add(player.getLocation().getDirection().multiply(curRange).add(new Vector(0.0D, 0.4D, 0.0D)));
                    this.loc.put(player, player.getLocation());


                    if (curRange > 0.0D) {
                        blinkTime.put(player, System.currentTimeMillis());
                        for (int i = 0; i < curRange; i++) {
                            Location particle = oldPos.add(player.getLocation().getDirection());
                            for (int x = 0; x < 3; x++) {
                                ParticleEffect.FIREWORKS_SPARK.display(particle);
                            }
                        }
                        player.leaveVehicle();
                        player.teleport(loc);
                        newPos = loc.clone();


                    }


                    player.setFallDistance(0.0F);


                    UtilMessage.message(player, getClassType(), "You used " + ChatColor.GREEN + getName() + " " + getLevel(player) + ChatColor.GRAY + ".");


                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_WITHER_SHOOT, 0.4F, 1.2F);
                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_SILVERFISH_DEATH, 1.0F, 1.6F);

                }
            }
        }

    }
}
