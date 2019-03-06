package net.betterpvp.clans.dailies.progression.types;

import java.util.UUID;

import net.betterpvp.clans.dailies.progression.Progress;

public class GeneralProgression extends Progress{
	
	private int currentAmount = 0;
	private int requiredAmount;
	
	

	public GeneralProgression(UUID uuid, String questName, int requiredAmount) {
		super(uuid, questName);
		this.requiredAmount = requiredAmount;
	}
	
	public int getCurrentAmount(){
		return currentAmount;
	
	}
	
	public void setCurrentAmount(int i){
		this.currentAmount = i;
	}
	
	public void addCurrentAmount(){
		this.currentAmount++;
	}
	
	public int getRequiredAmount(){
		return requiredAmount;
	}
	

}
