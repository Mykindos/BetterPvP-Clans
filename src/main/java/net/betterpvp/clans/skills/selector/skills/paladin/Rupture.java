package net.betterpvp.clans.skills.selector.skills.paladin;

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
import net.betterpvp.core.framework.UpdateEvent.UpdateType;
import net.betterpvp.core.utility.*;
import net.minecraft.server.v1_15_R1.*;
import org.bukkit.*;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.entity.*;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.WeakHashMap;


public class Rupture extends Skill implements InteractSkill {


    private WeakHashMap<Player, ArrayList<LivingEntity>> cooldownJump = new WeakHashMap<>();
    private HashMap<ArmorStand, Long> stands = new HashMap<>();
    //private WeakHashMap<Player, ArrayList<ArmorStand>> fakeArmorStands = new WeakHashMap<>();

    public Rupture(Clans i) {
        super(i, "Rupture", "Paladin", getAxes, rightClick, 5,
                true, true);

    }

    @Override
    public String[] getDescription(int level) {

        return new String[]{
                "Right click with Axe to Activate.",
                "",
                "Rupture the earth in the direction",
                "you are facing, damaging, knocking up",
                "and slowing any enemies hit.",
                "",
                "Cooldown: " + ChatColor.GREEN + getRecharge(level),
                "Energy: " + ChatColor.GREEN + getEnergy(level)

        };
    }

    @Override
    public Types getType() {

        return Types.AXE;
    }



    @EventHandler
    public void onUpdate(UpdateEvent e) {
        if (e.getType() == UpdateType.TICK) {
            Iterator<Entry<ArmorStand, Long>> it = stands.entrySet().iterator();
            while (it.hasNext()) {
                Entry<ArmorStand, Long> next = it.next();
                if (next.getValue() - System.currentTimeMillis() <= 0) {
                    next.getKey().remove();
                    it.remove();
                }
            }
        }
    }

    @Override
    public boolean usageCheck(Player player) {
        if(UtilBlock.isInLiquid(player)){
            UtilMessage.message(player, "Skill", "You cannot use " + net.md_5.bungee.api.ChatColor.GREEN + getName() + net.md_5.bungee.api.ChatColor.GRAY + " in water.");
            return false;
        }

        return ClanUtilities.canCast(player);
    }

    @Override
    public double getRecharge(int level) {

        return 14 - ((level - 1) * 1);
    }

    @Override
    public float getEnergy(int level) {

        return 30 - ((level - 1) * 2);
    }

    @Override
    public void activate(Player p, Gamer gamer) {
        UtilMessage.message(p, getClassType(), "You used " + ChatColor.GREEN + getName(getLevel(p)));
        final Vector v = p.getLocation().getDirection().normalize().multiply(0.3D);
        v.setY(0);
        final Location loc = p.getLocation().subtract(0.0D, 1.0D, 0.0D).add(v);
        cooldownJump.put(p, new ArrayList<>());
        final BukkitTask runnable = new BukkitRunnable() {

            @SuppressWarnings("deprecation")
            @Override
            public void run() {

                if ((loc.getBlock().getType() != Material.AIR)
                        && UtilBlock.solid(loc.getBlock())) {

                    loc.add(0.0D, 1.0D, 0.0D);
                    if ((loc.getBlock().getType() != Material.AIR)
                            && UtilBlock.solid(loc.getBlock())) {

                        cancel();
                        return;
                    }

                }
                if ((loc.clone().add(0.0D, -1.0D, 0.0D).getBlock().getType() == Material.AIR)) {
                    loc.add(0.0D, -1.0D, 0.0D);
                }

                for (int i = 0; i < 3; i++) {
                    loc.add(v);
                    Location tempLoc = new Location(p.getWorld(), loc.getX() + UtilMath.randDouble(-1.5D, 1.5D), loc.getY() + UtilMath.randDouble(0.0D, 0.5D) - 0.75,
                            loc.getZ() + UtilMath.randDouble(-1.5D, 1.5D));

                    CustomArmorStand as = new CustomArmorStand(EntityTypes.ARMOR_STAND, ((CraftWorld) loc.getWorld()).getHandle());
                    ArmorStand test = as.spawn(tempLoc);
                   // ArmorStand test = (ArmorStand) p.getWorld().spawnEntity(tempLoc, EntityType.ARMOR_STAND);
                    test.getEquipment().setHelmet(new ItemStack(Material.PACKED_ICE));
                    test.setGravity(false);
                    test.setVisible(false);
                    test.setSmall(true);
                    test.setHeadPose(new EulerAngle(UtilMath.randomInt(360), UtilMath.randomInt(360), UtilMath.randomInt(360)));


                    p.getWorld().playEffect(loc, Effect.STEP_SOUND, Material.PACKED_ICE);


                    stands.put(test, System.currentTimeMillis() + 4000);

                    for (Entity ent : test.getNearbyEntities(0.5D, 0.5D, 0.5D)) {

                        if (ent instanceof LivingEntity) {

                            LivingEntity ed = (LivingEntity) ent;

                            if (ed instanceof Player) {
                                if (!ClanUtilities.canHurt(p, (Player) ed)) {
                                    continue;
                                }

                            }
                            if (!cooldownJump.get(p).contains(ed)) {

                                UtilVelocity.velocity(ed, 0.5, 1, 2.0, false);
                                ed.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 30, 2));
                                LogManager.addLog(ed, p, "Rupture");
                                Bukkit.getPluginManager().callEvent(new CustomDamageEvent(ed, p, null, DamageCause.CUSTOM, 8.0, false));

                                cooldownJump.get(p).add(ed);
                            }
                        }
                    }


                }

            }

        }.runTaskTimer(getInstance(), 0, 2);

        new BukkitRunnable() {

            @Override
            public void run() {
                runnable.cancel();
                cooldownJump.get(p).clear();

            }

        }.runTaskLater(getInstance(), 40);

    }
}
