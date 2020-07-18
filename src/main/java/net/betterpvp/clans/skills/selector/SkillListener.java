package net.betterpvp.clans.skills.selector;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.classes.Energy;
import net.betterpvp.clans.classes.Role;
import net.betterpvp.clans.effects.EffectManager;
import net.betterpvp.clans.effects.EffectType;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.clans.skills.selector.skills.InteractSkill;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.clans.skills.selector.skills.ToggleSkill;
import net.betterpvp.clans.skills.selector.skills.paladin.Polymorph;
import net.betterpvp.clans.weapon.ILegendary;
import net.betterpvp.clans.weapon.Weapon;
import net.betterpvp.clans.weapon.WeaponManager;
import net.betterpvp.core.framework.BPVPListener;
import net.betterpvp.core.utility.UtilBlock;
import net.betterpvp.core.utility.UtilItem;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.recharge.RechargeManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.Arrays;
import java.util.Optional;

public class SkillListener extends BPVPListener<Clans> {

    public SkillListener(Clans instance) {
        super(instance);
    }


    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {

        Player player = e.getPlayer();
        if (player.getInventory().getItemInMainHand() == null) {
            return;
        }

        if(Polymorph.polymorphed.containsKey(player)){
            return;
        }

        Role role = Role.getRole(player);
        if (role != null) {
            Gamer gamer = GamerManager.getOnlineGamer(player);
            gamer.setLastAction(System.currentTimeMillis());
            RoleBuild build = gamer.getActiveBuild(role.getName());
            if (build != null) {
                Optional<Skill> skillOptional = build.getActiveSkills().stream().filter(s -> {

                    if (s instanceof ToggleSkill) {
                        if (s.getClassType().equals(role.getName())) {
                            if (Arrays.asList(s.getMaterials()).contains(e.getItemDrop().getItemStack().getType())) {
                                return true;
                            }
                        }
                    }

                    return false;
                }).findFirst();

                if (skillOptional.isPresent()) {
                    e.setCancelled(true);
                    Skill skill = skillOptional.get();

                    if (!ClanUtilities.canCast(player)) {
                        return;
                    }

                    if (Arrays.asList(skill.getActions()).contains(Action.RIGHT_CLICK_BLOCK)) {
                        if (UtilItem.isBlockDoor(player)) {
                            return;
                        }
                    }

                    if (EffectManager.hasEffect(player, EffectType.SILENCE)) {
                        UtilMessage.message(player, "Effect", "You are silenced!");
                        return;
                    }
                    if (!skill.usageCheck(player)) {
                        return;
                    }

                    ToggleSkill toggleSkill = (ToggleSkill) skill;
                    toggleSkill.activateToggle(player, gamer);
                }
            }

        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onSkillActivate(PlayerInteractEvent event) {

        Player player = event.getPlayer();

        if (event.getHand() == EquipmentSlot.OFF_HAND) {
            return;
        }

        if (player.getInventory().getItemInMainHand() == null) {
            return;
        }

        if(Polymorph.polymorphed.containsKey(player)){
            return;
        }

        if (player.getInventory().getItemInMainHand().getType() != Material.BOW
                && player.getInventory().getItemInMainHand().getType() != Material.CROSSBOW) {
            if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                return;
            }
        }

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (UtilBlock.usable(event.getClickedBlock())) {
                return;
            }
        }


        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block b = event.getClickedBlock();
            if (b.getType() == Material.SPONGE) {
                // Only cancel if the sponge is below the player
                if (b.getLocation().getY() < player.getLocation().getY()) {
                    return;
                }
            } else if (b.getType() == Material.IRON_DOOR) {
                return;
            }
        }

        Weapon weapon = WeaponManager.getWeapon(player.getInventory().getItemInMainHand());
        if (weapon != null && weapon instanceof ILegendary) {
            return;
        }

        Role role = Role.getRole(player);
        if (role != null) {
            Gamer gamer = GamerManager.getOnlineGamer(player);
            gamer.setLastAction(System.currentTimeMillis());
            RoleBuild build = gamer.getActiveBuild(role.getName());
            if (build != null) {

                Optional<Skill> skillOptional = build.getActiveSkills().stream().filter(s -> {
                    if (s instanceof InteractSkill) {
                        if (s.getClassType().equals(role.getName())) {
                            if (Arrays.asList(s.getMaterials()).contains(player.getInventory().getItemInMainHand().getType())
                                    && (s.getActions() != null && Arrays.asList(s.getActions()).contains(event.getAction()))) {
                                return true;
                            }
                        }
                    }

                    return false;
                }).findFirst();

                if (skillOptional.isPresent()) {
                    if (!ClanUtilities.canCast(player)) {
                        return;
                    }
                    if (EffectManager.hasEffect(player, EffectType.SILENCE)) {
                        UtilMessage.message(player, "Effect", "You are silenced!");
                        return;
                    }

                    Skill skill = skillOptional.get();

                    if (!skill.useInteract()) {
                        return;
                    }

                    /*if (UtilItem.isBlockDoor(player)) {
                        return;
                    }*/

                    if (!skill.usageCheck(player)) {
                        return;
                    }

                    int level = skill.getLevel(player);
                    InteractSkill interactSkill = (InteractSkill) skill;
                    if (0.999 * (skill.getEnergy(skill.getLevel(player)) / 100) < Energy.getEnergy(player)) {
                        if (skill.getRecharge(level) == 0) {

                            interactSkill.activate(player, gamer);
                            return;
                        }
                        if (RechargeManager.getInstance().add(player, skill.getName(), skill.getRecharge(level),
                                skill.showRecharge(), true, skill.isCancellable())) {
                            if (Energy.use(player, skill.getName(), skill.getEnergy(level), true)) {
                                interactSkill.activate(player, gamer);

                            }
                        }
                    } else {
                        UtilMessage.message(player, "Energy", "You are too exhausted to use "
                                + ChatColor.GREEN + skill.getName(level) + ChatColor.GRAY + ".");
                    }

                }

            }
        }


    }
}
