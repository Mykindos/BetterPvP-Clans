package net.betterpvp.clans.clans;

public class AdminClan extends Clan {
    
    private boolean safe;

    public AdminClan(String name) {
        super(name);
        this.safe = false;
    }
    
    public boolean isSafe() {
        return safe;
    }
    
    public void setSafe(boolean safe) {
        this.safe = safe;
    }
    
}
