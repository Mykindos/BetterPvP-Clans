package net.betterpvp.clans.skills.selector.skills.paladin;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.Role;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.core.utility.UtilBlock;
import net.betterpvp.core.utility.UtilVelocity;
import net.betterpvp.core.utility.recharge.RechargeManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Arrays;

public class RootingAxe extends Skill {

    public RootingAxe(Clans i) {
        super(i, "Rooting Axe", "Paladin", getAxes, noActions, 3, false, false);

    }


    @Override
    public String[] getDescription(int level) {

        return new String[]{
                "Your axe rips players downward into",
                "the earth disrupting their movement,",
                "and stops them from jumping for 2 seconds",
                "",
                "Internal Cooldown: " + ChatColor.GREEN + getRecharge(level)


        };
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onDamage(CustomDamageEvent e) {
        if (e.getDamager() instanceof Player) {

            Player p = (Player) e.getDamager();
            if (Role.getRole(p) != null && Role.getRole(p).getName().equals(getClassType())) {
                if (hasSkill(p, this)) {
                    if (Arrays.asList(getMaterials()).contains(p.getInventory().getItemInMainHand().getType())) {
                        if (e.getCause() == DamageCause.ENTITY_ATTACK) {

                            if (e.getDamagee() instanceof Wither) {
                                return;
                            }


                            Block b1 = e.getDamagee().getLocation().getBlock().getRelative(0, -1, 0);

                            if (p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().name().contains("_SLAB")
                                    || b1.getType().name().contains("_STEP") || b1.getType() == Material.OAK_SLAB
                                    || b1.getType().name().contains("STAIR") ) {
                                return;
                            }

                            Block b2 = e.getDamagee().getLocation().getBlock().getRelative(0, -2, 0);
                            if (b2.getType().name().contains("LADDER") || b2.getType().name().contains("GATE") || !UtilBlock.solid(b2.getType())) {
                                return;
                            }
                            Block b3 = e.getDamagee().getEyeLocation().getBlock().getRelative(0, -1, 0);
                            if (UtilBlock.airFoliage(b3)) {
                                if (!UtilBlock.airFoliage(b2)) {
                                    if (!UtilBlock.airFoliage(b1)) {
                                        if (!b1.isLiquid()) {
                                            if (!b2.isLiquid()) {
                                                if (UtilBlock.isGrounded(e.getDamagee())) {
                                                    if (RechargeManager.getInstance().add(p, getName(), 11 - (getLevel(p) * 1.5), false)) {
                                                        e.getDamagee().teleport(e.getDamagee().getLocation().add(0, -0.9, 0));
                                                        e.getDamagee().getWorld().playEffect(e.getDamagee().getLocation(), Effect.STEP_SOUND, e.getDamagee().getLocation().getBlock().getType());
                                                        ((LivingEntity) e.getDamagee()).addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 40, -5));
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                            }
                        } else {
                            UtilVelocity.velocity(e.getDamagee(), new Vector(0, -1, 0), 0.5, false, 0, 0, 10, false);

                        }
                    }
                }


            }
        }
    }


    @Override
    public Types getType() {

        return Types.PASSIVE_A;
    }

    @Override
    public double getRecharge(int level) {

        return 8 - ((level - 1) * 2);
    }

    @Override
    public float getEnergy(int level) {

        return 0;
    }

    @Override
    public boolean usageCheck(Player player) {

        return false;
    }


}
