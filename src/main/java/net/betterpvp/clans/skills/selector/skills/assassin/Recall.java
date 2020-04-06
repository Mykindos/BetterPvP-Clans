package net.betterpvp.clans.skills.selector.skills.assassin;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.AdminClan;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.classes.Role;
import net.betterpvp.clans.classes.events.RoleChangeEvent;
import net.betterpvp.clans.classes.roles.Assassin;
import net.betterpvp.clans.effects.EffectManager;
import net.betterpvp.clans.effects.EffectType;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.events.SkillEquipEvent;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.clans.skills.selector.skills.data.RecallData;
import net.betterpvp.clans.weapon.WeaponManager;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.framework.UpdateEvent.UpdateType;
import net.betterpvp.core.utility.UtilBlock;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilTime;
import net.betterpvp.core.utility.recharge.RechargeManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.InventoryView;

import java.util.Arrays;
import java.util.WeakHashMap;

public class Recall extends Skill {

    public WeakHashMap<Player, RecallData> data = new WeakHashMap<>();

    public Recall(Clans i) {
        super(i, "Recall", "Assassin",
                getSwordsAndAxes,
                noActions, 5, true, false);

    }


    @EventHandler
    public void onRoleChange(RoleChangeEvent e) {
        data.remove(e.getPlayer());
    }


    @EventHandler
    public void onRecall(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        InventoryView iv = player.getOpenInventory();


        if (iv.getType() != InventoryType.CRAFTING) {
            return;
        }

        if (WeaponManager.getWeapon(event.getItemDrop().getItemStack()) != null) {
            return;
        }


        if (!hasSkill(player, this)) {
            return;
        }


        if (Arrays.asList(getMaterials()).contains(event.getItemDrop().getItemStack().getType())) {
            event.setCancelled(true);
            if (usageCheck(player)) {
                if (data.containsKey(player)) {
                    if (data.get(player).getLocation() != null) {
                        if (player.getWorld() == data.get(player).getLocation().getWorld()) {
                            if (RechargeManager.getInstance().add(player, getName(), getRecharge(getLevel(player)), true)) {
                                RecallData d = data.get(player);
                                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_VILLAGER_CONVERTED, 2.0F, 2.0F);
                                player.teleport(d.getLocation());
                                player.setHealth(Math.min(player.getMaxHealth(), player.getHealth() + (d.getHealth() / 4)));

                                player.getWorld().playEffect(data.get(player).getLocation(), Effect.STEP_SOUND, Material.EMERALD_BLOCK);
                                UtilMessage.message(player, getName(), "You used " + ChatColor.GREEN + getName(getLevel(player)));

                            }
                        }
                    }
                }

            }
        }

    }


    @EventHandler
    public void updateRecallData(UpdateEvent event) {
        if (event.getType() == UpdateType.FAST) {

            for (Player p : Bukkit.getOnlinePlayers()) {
                Role r = Role.getRole(p);
                if (r != null && r instanceof Assassin) {
                    if (hasSkill(p, this)) {
                        if (data.containsKey(p)) {
                            RecallData rd = data.get(p);
                            if (UtilTime.elapsed(rd.getTime(), 1000)) {
                                rd.addLocation(p.getLocation(), p.getHealth());
                                rd.setTime(System.currentTimeMillis());
                            }
                        } else {
                            data.put(p, new RecallData());
                            data.get(p).addLocation(p.getLocation(), p.getHealth());
                        }
                    }
                }
            }
        }
    }


    @Override
    public void activateSkill(Player player) {

    }

    @Override
    public boolean usageCheck(Player player) {

        if (UtilBlock.isInLiquid(player)) {
            UtilMessage.message(player, getClassType(), "You cannot use " + ChatColor.GREEN + getName() + ChatColor.GRAY + " while in liquid!");
            return false;
        }


        if (EffectManager.hasEffect(player, EffectType.SILENCE)) {
            UtilMessage.message(player, getClassType(), "You cannot use " + ChatColor.GREEN + getName(getLevel(player)) + ChatColor.GRAY + " while silenced!");
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
        return true;
    }

    @Override
    public String[] getDescription(int level) {

        return new String[]{"Drop Sword / Axe to Activate",
                "",
                "Teleports you back to where you ",
                "were located 3 seconds ago",
                "Increases health by 1/4 of the health you had",
                "3 seconds ago",
                "",
                "Cooldown: " + ChatColor.GREEN + getRecharge(level),
                "Energy: " + ChatColor.GREEN + getEnergy(level)};
    }

    @EventHandler
    public void onEquip(SkillEquipEvent e) {
        if (e.getSkill() == this) {
            if (data.containsKey(e.getPlayer())) {
                data.get(e.getPlayer()).locs.clear();
            }
        }
    }

    @Override
    public Types getType() {

        return Types.PASSIVE_B;
    }

    @Override
    public double getRecharge(int level) {

        return 30 - ((level - 1) * 2);
    }

    @Override
    public float getEnergy(int level) {

        return (float) 80 - ((level - 1) * 5);
    }

}
