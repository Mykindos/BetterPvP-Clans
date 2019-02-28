package net.betterpvp.clans.clans;

public class Alliance {

    private Clan clan;
    private boolean trust;

    public Alliance(Clan clan, boolean trust) {
        this.clan = clan;
        this.trust = trust;
    }

    public Clan getClan() {
        return clan;
    }

    public void setClan(Clan clan) {
        this.clan = clan;
    }

    public boolean hasTrust() {
        return trust;
    }

    public void setTrust(boolean trust) {
        this.trust = trust;
    }
}
