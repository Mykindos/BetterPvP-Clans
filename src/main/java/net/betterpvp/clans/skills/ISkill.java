package net.betterpvp.clans.skills;

import org.bukkit.Material;
import org.bukkit.event.block.Action;

public interface ISkill {

    public Material[] getSwords = {Material.IRON_SWORD, Material.GOLD_SWORD, Material.DIAMOND_SWORD};

    public Material[] getBow = {Material.BOW};

    public Material[] getAxes = {Material.IRON_AXE, Material.GOLD_AXE, Material.DIAMOND_AXE};

    public Material[] getSwordsAndAxes = {Material.IRON_SWORD, Material.GOLD_SWORD, Material.DIAMOND_SWORD,
            Material.IRON_AXE, Material.GOLD_AXE, Material.DIAMOND_AXE};

    public Material[] noMaterials = {};

    public Action[] noActions = {};

    public Action[] rightClick = {Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK};

    public Action[] leftClick = {Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK};
}
