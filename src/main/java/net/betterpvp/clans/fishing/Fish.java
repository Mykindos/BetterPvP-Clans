package net.betterpvp.clans.fishing;

public class Fish {

    private String player;
    private String name;
    private int size;
    private long systemTime;

    public Fish(String player, int size, String name, long systemTime){
        this.player = player;
        this.name = name;
        this.size = size;
        this.systemTime = systemTime;
    }

    public String getPlayerName(){
        return player;
    }

    public int getSize(){
        return size;
    }

    public String getFishName(){
        return name;
    }

    public long getSystemTime(){
        return systemTime;
    }
}
