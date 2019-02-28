package net.betterpvp.clans.clans.map.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.map.MapCursor;

public class MinimapPlayerCursorEvent
        extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Player viewer;
    private Player viewed;
    private MapCursor.Type type;
    private boolean display;

    public MinimapPlayerCursorEvent(Player viewer, Player viewed, boolean canSee, MapCursor.Type type) {
        this.viewed = viewed;
        this.viewer = viewer;
        this.display = canSee;


        setType(type);
    }

    public Player getViewer() {
        return this.viewer;
    }

    public Player getViewed() {
        return this.viewed;
    }

    public MapCursor.Type getType() {
        return this.type;
    }

    public void setType(MapCursor.Type type) {
        this.type = type;
    }

    public boolean isCursorShown() {
        return this.display;
    }

    public void setCursorShown(boolean display) {
        this.display = display;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}