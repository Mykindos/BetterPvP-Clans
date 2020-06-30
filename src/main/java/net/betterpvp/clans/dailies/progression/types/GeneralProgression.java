package net.betterpvp.clans.dailies.progression.types;

import net.betterpvp.clans.dailies.progression.Progress;

import java.util.UUID;

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
