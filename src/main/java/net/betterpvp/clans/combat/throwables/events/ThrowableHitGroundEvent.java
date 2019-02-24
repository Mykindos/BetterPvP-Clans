package net.betterpvp.clans.combat.throwables.events;

import net.betterpvp.clans.combat.throwables.Throwables;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class ThrowableHitGroundEvent  extends Event {

    private static final HandlerList handlers = new HandlerList();
   
    private Throwables throwable;
    
    public ThrowableHitGroundEvent(Throwables t){
    	this.throwable = t;

    }
    
    public Throwables getThrowable(){
    	return throwable;
    }
    
    
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }


}
