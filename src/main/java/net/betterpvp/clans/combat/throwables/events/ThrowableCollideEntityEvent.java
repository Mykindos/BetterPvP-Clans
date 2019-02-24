package net.betterpvp.clans.combat.throwables.events;

import net.betterpvp.clans.combat.throwables.Throwables;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;



public class ThrowableCollideEntityEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
   
    private Throwables throwable;
    private LivingEntity collide;
    
    public ThrowableCollideEntityEvent(Throwables t, LivingEntity collide){
    	this.throwable = t;
    	this.collide = collide;
    }
    
    public Throwables getThrowable(){
    	return throwable;
    }
    
    public LivingEntity getCollision(){
    	return collide;
    }
    
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
