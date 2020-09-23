package net.betterpvp.clans.combat.ratings;

public class Rating {

    private int rating;
    private long lastKill;

    public Rating(int rating, long lastKill) {
        this.rating = rating;
        this.lastKill = lastKill;
    }

    public long getLastKill() {
        return lastKill;
    }

    public void setLastKill(long lastKill) {
        this.lastKill = lastKill;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
