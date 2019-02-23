package net.betterpvp.clans.clans.insurance;

import net.betterpvp.clans.clans.InsuranceType;
import org.bukkit.Location;
import org.bukkit.Material;

public class Insurance {
	
	private long time;
	private Material material;
	private byte data;
	private InsuranceType type;
	private Location loc;
	
	public Insurance(Location loc, Material mat, byte data, InsuranceType type, long time){
		this.material = mat;
		this.data = data;
		this.type = type;
		this.time = time;
		this.loc = loc;
	}
	
	public Location getLocation(){
		return loc;
	}
	
	public Material getMaterial(){
		return material;
	}
	
	public byte getData(){
		return data;
	}
	
	public InsuranceType getType(){
		return type;
	}
	
	public long getTime(){
		return time;
	}

}
