package net.betterpvp.clans.skills.selector.skills.necromancer;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.combat.LogManager;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.InteractSkill;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.clans.skills.selector.skills.data.CustomArmorStand;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.utility.UtilBlock;
import net.betterpvp.core.utility.UtilMath;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilVelocity;
import net.minecraft.server.v1_15_R1.EntityTypes;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.*;

public class Grasp extends Skill implements InteractSkill {

    private WeakHashMap<Player, ArrayList<LivingEntity>> cooldownJump = new WeakHashMap<>();
    private HashMap<ArmorStand, Long> stands = new HashMap<>();

    public Grasp(Clans i) {
        super(i, "Grasp", "Necromancer", getSwords, rightClick, 5, true, true);
    }


    @Override
    public void activate(Player player, Gamer gamer) {
        int level = getLevel(player);
        UtilMessage.message(player, getClassType(), "You used " + ChatColor.GREEN + getName(level));
        Block block = player.getTargetBlock(null, (20 + (level * 10) / 2));
        Location startPos = player.getLocation();
        if (block.getType() != Material.AIR) {
            final Vector v = player.getLocation().toVector().subtract(block.getLocation().toVector()).normalize().multiply(0.2);
            v.setY(0);
            final Location loc = block.getLocation().add(v);

            cooldownJump.put(player, new ArrayList<>());
            final BukkitTask runnable = new BukkitRunnable() {

                @SuppressWarnings("deprecation")
                @Override
                public void run() {

                    if ((loc.getBlock().getType() != Material.AIR)
                            && UtilBlock.solid(loc.getBlock())) {

                        loc.add(0.0D, 1.0D, 0.0D);
                        if ((loc.getBlock().getType() != Material.AIR)
                                && UtilBlock.solid(loc.getBlock())) {
                            Bukkit.broadcastMessage("Cancelling cuz " + loc.getBlock().getType().name());
                            cancel();
                            return;
                        }

                    }

                    Location compare = loc.clone();
                    compare.setY(startPos.getY());
                    if (compare.distance(startPos) < 1) {
                        cancel();
                        return;
                    }


                    if ((loc.clone().add(0.0D, -1.0D, 0.0D).getBlock().getType() == Material.AIR)) {
                        loc.add(0.0D, -1.0D, 0.0D);
                    }

                    for (int i = 0; i < 10; i++) {
                        loc.add(v);
                        Location tempLoc = new Location(player.getWorld(), loc.getX() + UtilMath.randDouble(-2D, 2.0D), loc.getY() + UtilMath.randDouble(0.0D, 0.5D) - 0.50,
                                loc.getZ() + UtilMath.randDouble(-2.0D, 2.0D));

                        createArmourStand(player, tempLoc.clone(), level);
                        createArmourStand(player, tempLoc.clone().add(0, 1, 0), level);
                        createArmourStand(player, tempLoc.clone().add(0, 2, 0), level);

                        if (i % 2 == 0) {
                            player.getWorld().playSound(tempLoc, Sound.ENTITY_VEX_DEATH, 0.3f, 0.3f);
                        }
                    }

                }

            }.runTaskTimer(getInstance(), 0, 2);

            new BukkitRunnable() {

                @Override
                public void run() {
                    runnable.cancel();
                    cooldownJump.get(player).clear();

                }

            }.runTaskLater(getInstance(), 40);
        }
    }

    private void createArmourStand(Player player, Location loc, int level) {
        CustomArmorStand as = new CustomArmorStand(EntityTypes.ARMOR_STAND, ((CraftWorld) loc.getWorld()).getHandle());
        ArmorStand test = as.spawn(loc);
        // ArmorStand test = (ArmorStand) p.getWorld().spawnEntity(tempLoc, EntityType.ARMOR_STAND);
        test.getEquipment().setHelmet(new ItemStack(Material.WITHER_SKELETON_SKULL));
        test.setGravity(false);
        test.setVisible(false);
        test.setSmall(true);
        test.setHeadPose(new EulerAngle(UtilMath.randomInt(360), UtilMath.randomInt(360), UtilMath.randomInt(360)));

        stands.put(test, System.currentTimeMillis() + 300);

        for (Entity ent : test.getNearbyEntities(0.5D, 0.5D, 0.5D)) {

            if (ent instanceof LivingEntity) {

                LivingEntity ed = (LivingEntity) ent;

                if (ed instanceof Player) {
                    if (!ClanUtilities.canHurt(player, (Player) ed)) {
                        continue;
                    }

                }
                if (!cooldownJump.get(player).contains(ed)) {

                    LogManager.addLog(ed, player, "Grasp");
                    Bukkit.getPluginManager().callEvent(new CustomDamageEvent(ed, player, null, EntityDamageEvent.DamageCause.CUSTOM, 2 + level, false));
                    cooldownJump.get(player).add(ed);
                }
                if(ent.getLocation().distance(player.getLocation()) < 3) continue;
                Location target = player.getLocation();
                target.add(target.getDirection().normalize().multiply(2));
                UtilVelocity.velocity(ent, UtilVelocity.getTrajectory(ent.getLocation(), target), 1.0, false, 0, 0.5, 1, true);


            }
        }
    }



    @EventHandler
    public void onUpdate(UpdateEvent e) {
        if (e.getType() == UpdateEvent.UpdateType.TICK) {
            Iterator<Map.Entry<ArmorStand, Long>> it = stands.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<ArmorStand, Long> next = it.next();
                if (next.getValue() - System.currentTimeMillis() <= 0) {
                    next.getKey().remove();
                    it.remove();
                }
            }
        }
    }

    @Override
    public String[] getDescription(int level) {

        return new String[]{
                "Right click with Sword to Activate.",
                "",
                "Create a wall of skulls closes in on you from a distance",
                "and brings all enemies with it.",
                "",
                "Cooldown: " + ChatColor.GREEN + getRecharge(level),
                "Max range: " + ChatColor.GREEN + 20 + ((level * 10) / 2),
                "Damage: " + ChatColor.GREEN + 2 + level

        };
    }

    @Override
    public Types getType() {
        return Types.SWORD;
    }

    @Override
    public double getRecharge(int level) {
        return 20 - (level * 1.5);
    }

    @Override
    public float getEnergy(int level) {
        return 0;
    }

    @Override
    public boolean usageCheck(Player p) {
        int level = getLevel(p);
        Block block = p.getTargetBlock(null, (20 + (level * 10) / 2));
        if(block.getLocation().distance(p.getLocation()) < 3){
            UtilMessage.message(p, "Skill", "You cannot use " + ChatColor.GREEN + getName() + ChatColor.GRAY + " this close.");
            return false;
        }

        if(block.getType() == Material.AIR){
            UtilMessage.message(p, "Skill", "Could not find location within " + ChatColor.GREEN + (20 + (level * 10) / 2) + ChatColor.GRAY + " blocks.");
            return false;
        }
        return true;
    }
}
