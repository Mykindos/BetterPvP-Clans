package net.betterpvp.clans.skills.selector.button;

import net.betterpvp.clans.skills.selector.RoleBuild;
import net.betterpvp.core.interfaces.Button;
import org.bukkit.inventory.ItemStack;

public class BuildButton extends Button {

    private Action action;
    private RoleBuild build;
    private int id;
    private boolean locked;

    public BuildButton(int slot, RoleBuild build, Action action, int id, boolean locked, ItemStack item, String name, String[] lore) {
        super(slot, item, name, lore);
        this.build = build;
        this.action = action;
        this.id = id;
        this.locked = locked;
    }

    public RoleBuild getBuild() {
        return build;
    }

    public void setBuild(RoleBuild build) {
        this.build = build;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public enum Action {

        EDIT_BUILD, DELETE_BUILD, EQUIP_BUILD
    }

}