package net.betterpvp.clans.skills.selector.skills;

import net.betterpvp.clans.Clans;
import org.bukkit.Material;
import org.bukkit.event.block.Action;

public abstract class ChannelSkill extends Skill{
    public ChannelSkill(Clans i, String name, String classType, Material[] items, Action[] actions, int maxLevel, boolean showRecharge, boolean useInteract) {
        super(i, name, classType, items, actions, maxLevel, showRecharge, useInteract);
    }
}
