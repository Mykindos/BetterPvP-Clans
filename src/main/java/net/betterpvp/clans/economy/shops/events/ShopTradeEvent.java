package net.betterpvp.clans.economy.shops.events;


import net.betterpvp.clans.economy.shops.menu.buttons.ShopItem;
import net.betterpvp.clans.gamer.Gamer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ShopTradeEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }


    private Gamer gamer;
    private final Player p;
    private final ShopItem item;
    private final TradeAction action;
    private final TradeCurrency currency;
    private boolean shift;
    private boolean cancelled;
    private String cancelReason;

    private boolean giveItem = true;

    public ShopTradeEvent(Gamer gamer, Player p, ShopItem item, TradeAction action, TradeCurrency currency, boolean shift) {
        this.gamer = gamer;
        this.p = p;
        this.item = item;
        this.action = action;
        this.currency = currency;
        this.shift = shift;
    }

    public Player getPlayer() {
        return p;
    }


    public TradeAction getAction() {
        return action;
    }

    public ShopItem getItem() {
        return item;
    }

    public boolean isCancelled(){
        return cancelled;
    }

    public void setCancelled(String reason){
        cancelled = true;
        cancelReason = reason;
    }

    public boolean isShift() {
        return shift;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public boolean isGiveItem() {
        return giveItem;
    }

    public void setGiveItem(boolean giveItem) {
        this.giveItem = giveItem;
    }

    public Gamer getGamer() {
        return gamer;
    }

    public TradeCurrency getCurrency() {
        return currency;
    }
}
