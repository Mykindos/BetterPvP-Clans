package net.betterpvp.clans.skills.selector.skills.assassin;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.effects.EffectManager;
import net.betterpvp.clans.effects.EffectType;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.InteractSkill;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.utility.*;
import net.betterpvp.core.utility.recharge.RechargeManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Set;
import java.util.WeakHashMap;

public class Blink extends Skill implements InteractSkill {

    private WeakHashMap<Player, Location> loc = new WeakHashMap<>();
    private WeakHashMap<Player, Long> blinkTime = new WeakHashMap<>();

    public Blink(Clans i) {
        super(i, "Blink", "Assassin", getAxes,
                rightClick, 5,
                true, true);

    }

    @Override
    public String[] getDescription(int level) {

        return new String[]{
                "Right click with Axe to Activate.",
                "",
                "Instantly teleport forwards 15 Blocks.",
                "Cannot be used while Slowed.",
                "",
                "Using again within 5 seconds De-Blinks,",
                "returning you to your original location.",
                "Cannot be used while Slowed.",
                "Cooldown: " + ChatColor.GREEN + getRecharge(level),
                "Energy: " + ChatColor.GREEN + getEnergy(level)};
    }

    @Override
    public Types getType() {

        return Types.AXE;
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
        if (e.getType() == UpdateEvent.UpdateType.TICK_2) {
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
        if (!RechargeManager.getInstance().isCooling(player.getName(), "Deblink") || force) {
            if (ClanUtilities.canCast(player)) {
                UtilMessage.message(player, getClassType(), "You used " + ChatColor.GREEN + "Deblink" + " " + getLevel(player) + ChatColor.GRAY + ".");


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
                        lastSmoke.getWorld().playEffect(lastSmoke.getLocation(), Effect.SMOKE, 4);
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


                player.getWorld().playEffect(player.getLocation(), Effect.BLAZE_SHOOT, 0, 15);
            }
        }
    }

    @Override
    public boolean usageCheck(Player player) {


        if (player.hasPotionEffect(PotionEffectType.SLOW)) {
            UtilMessage.message(player, getClassType(), "You cannot use " + getName() + " while Slowed.");
            return false;
        }

        if (UtilBlock.isInLiquid(player)) {
            UtilMessage.message(player, getClassType(), "You cannot use " + getName() + " in water.");
            return false;
        }

        if (player.getTargetBlock((Set<Material>) null, 4).getType() == Material.GLASS) {
            return false;
        }


        if ((loc.containsKey(player)) && (blinkTime.containsKey(player)) &&
                (!UtilTime.elapsed(blinkTime.get(player), 4000L))) {
            deblink(player, false);
            return false;
        }

        if (EffectManager.hasEffect(player, EffectType.STUN)) {
            UtilMessage.message(player, getClassType(), "You cannot use " + org.bukkit.ChatColor.GREEN
                    + getName(getLevel(player)) + ChatColor.GRAY + " while stunned.");
            return false;
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

        return 13 - ((level - 1));
    }

    @Override
    public float getEnergy(int level) {

        return (float) (level < 1 ? 35 : 35 - (1.5 * (level - 1)));
    }

    @Override
    public void activate(Player player, Gamer gamer) {
        Block lastSmoke = player.getLocation().getBlock();

        double maxRange = 16.0D;
        double curRange = 0.0D;
        while (curRange <= maxRange) {
            Location newTarget = player.getLocation().add(new Vector(0.0D, 0.2D, 0.0D))
                    .add(player.getLocation().getDirection().multiply(curRange));

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


                        UtilMessage.message(player, getClassType(), "You used " + ChatColor.GREEN + getName()
                                + " " + getLevel(player) + ChatColor.GRAY + ".");


                        player.getWorld().playEffect(player.getLocation(), Effect.BLAZE_SHOOT, 0);
                        return;
                    }
                }
            }
            curRange += 0.1D;


            if (!lastSmoke.equals(newTarget.getBlock())) {
                lastSmoke.getWorld().playEffect(lastSmoke.getLocation(), Effect.SMOKE, 4);
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

            player.leaveVehicle();
            player.teleport(loc);
        }


        player.setFallDistance(0.0F);


        UtilMessage.message(player, getClassType(), "You used " + ChatColor.GREEN + getName() + " " + getLevel(player) + ChatColor.GRAY + ".");
        RechargeManager.getInstance().add(player, "Deblink", 0.25, false);

        player.getWorld().playEffect(player.getLocation(), Effect.BLAZE_SHOOT, 0);

    }
}
