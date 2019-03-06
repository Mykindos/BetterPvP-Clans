package net.betterpvp.clans.dailies.perks;

public abstract class QuestPerk {
	
	private String name;
	
	public QuestPerk(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public abstract String[] getDescription(int price);

}
