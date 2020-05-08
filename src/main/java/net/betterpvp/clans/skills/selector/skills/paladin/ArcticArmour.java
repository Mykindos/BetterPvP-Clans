package net.betterpvp.clans.skills.selector.skills.paladin;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.classes.Energy;
import net.betterpvp.clans.classes.Role;
import net.betterpvp.clans.effects.EffectManager;
import net.betterpvp.clans.effects.EffectType;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.ToggleSkill;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.core.utility.restoration.BlockRestoreData;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.framework.UpdateEvent.UpdateType;
import net.betterpvp.core.utility.UtilBlock;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;


public class ArcticArmour extends Skill implements ToggleSkill {

    private Set<UUID> active = new HashSet<>();

    public ArcticArmour(Clans i) {
        super(i, "Arctic Armour", "Paladin", getSwordsAndAxes,
                noActions, 5,
                false, false);

    }

    @Override
    public String[] getDescription(int level) {

        return new String[]{
                "Drop Axe/Sword to Toggle.",
                "",
                "Create a freezing area around you",
                "in a " + ChatColor.GREEN + (2 + level) + ChatColor.GRAY + " Block radius. Allies inside",
                "this area receive Protection I.",
                "Enemies inside this area receive",
                "Slowness I",
                "",
                "Energy / Second: " + ChatColor.GREEN + getEnergy(level)
        };
    }


    @EventHandler
    public void onSwap(PlayerSwapHandItemsEvent e) {
        Player p = e.getPlayer();
        Role role = Role.getRole(p);
        if (role != null && role.getName().equals(getClassType())) {
            if (hasSkill(p, this)) {
                if (Arrays.asList(getMaterials()).contains(e.getMainHandItem().getType())) {
                    activate(p);
                }
            }
        }
    }



    private void activate(Player p){

    }



    @EventHandler
    public void audio(UpdateEvent event) {
        if (event.getType() == UpdateType.SEC) {
            for (UUID uuid : active) {
                if (Bukkit.getPlayer(uuid) != null) {
                    Player cur = Bukkit.getPlayer(uuid);
                    cur.getWorld().playSound(cur.getLocation(), Sound.WEATHER_RAIN, 0.3F, 0.0F);
                }
            }
        }
    }

    @EventHandler
    public void SnowAura(UpdateEvent event) {
        if (event.getType() != UpdateType.FASTEST) {
            return;
        }
        Iterator<UUID> iterator = active.iterator();
        while (iterator.hasNext()) {
            UUID z = iterator.next();
            if (Bukkit.getPlayer(z) != null) {
                Player cur = Bukkit.getPlayer(z);
                Role role = Role.getRole(cur);

                if (!hasSkill(cur, this)) {

                    iterator.remove();


                } else if (!Energy.use(cur, getName(), getEnergy(getLevel(cur)) / 2, true)) {
                    iterator.remove();

                } else if (cur == null) {
                    iterator.remove();

                } else if (role == null || role != null && !role.getName().equals(getClassType())) {
                    iterator.remove();
                } else if (EffectManager.hasEffect(cur, EffectType.SILENCE)) {
                    iterator.remove();
                } else {

                    long duration = 2000;
                    int level = (2 + getLevel(cur));
                    HashMap<Block, Double> blocks = UtilBlock.getInRadius(cur.getLocation(), level);
                    for (Player p : UtilPlayer.getInRadius(cur.getLocation(), (level))) {
                        if (!ClanUtilities.canHurt(cur, p)) {
                            p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20, 0));
                            EffectManager.addEffect(p, EffectType.RESISTANCE, 1, 1000);
                        } else {
                            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20, 0));
                        }
                    }
                    for (Block block : blocks.keySet()) {
                        if (block.getLocation().getY() <= cur.getLocation().getY()) {
                            Block relDown = block.getRelative(BlockFace.DOWN);
                            if (relDown.getType() != Material.SNOW && UtilBlock.isGrounded(cur)
                                    && relDown.getType() != Material.AIR && relDown.getType() != Material.WATER && !UtilBlock.airFoliage(relDown)) {
                                if (block.getType() == Material.AIR) {
                                    new BlockRestoreData(block, Material.SNOW, (byte) 0, duration);
                                    block.setType(Material.SNOW);
                                }

                            }
                        }
                    }
                }
            } else {
                iterator.remove();
            }
        }

    }


    @Override
    public Types getType() {

        return Types.PASSIVE_B;
    }

    @Override
    public boolean usageCheck(Player player) {

        return true;
    }

    @Override
    public double getRecharge(int level) {

        return 0;
    }

    @Override
    public float getEnergy(int level) {

        return (float) (6 - ((level - 1) * 0.5));
    }


    @Override
    public void activateToggle(Player p, Gamer gamer) {
        if (active.contains(p.getUniqueId())) {
            active.remove(p.getUniqueId());
            UtilMessage.message(p, getClassType(), "Arctic Armour: " + ChatColor.RED + "Off");
        } else {
            if (ClanUtilities.canCast(p)) {
                active.add(p.getUniqueId());
                UtilMessage.message(p, getClassType(), "Arctic Armour: " + ChatColor.GREEN + "On");
            }
        }
    }
}
