package net.betterpvp.clans.skills;

import org.bukkit.Material;
import org.bukkit.event.block.Action;

public interface ISkill {

     Material[] getSwords = {Material.IRON_SWORD, Material.GOLDEN_SWORD, Material.DIAMOND_SWORD, Material.NETHERITE_SWORD};

     Material[] getBow = {Material.BOW, Material.CROSSBOW};

     Material[] getAxes = {Material.IRON_AXE, Material.GOLDEN_AXE, Material.DIAMOND_AXE, Material.NETHERITE_AXE};

     Material[] getSwordsAndAxes = {Material.IRON_SWORD, Material.GOLDEN_SWORD, Material.DIAMOND_SWORD, Material.NETHERITE_SWORD,
            Material.IRON_AXE, Material.GOLDEN_AXE, Material.DIAMOND_AXE, Material.NETHERITE_AXE};

     Material[] noMaterials = {};

     Action[] noActions = {};

     Action[] rightClick = {Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK};

     Action[] leftClick = {Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK};
}
