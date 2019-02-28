package net.betterpvp.clans.skills.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SkillReloadEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        // TODO Auto-generated method stub
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
