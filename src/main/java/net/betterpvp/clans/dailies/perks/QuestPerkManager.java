package net.betterpvp.clans.dailies.perks;

import java.util.ArrayList;
import java.util.List;

import net.betterpvp.clans.gamer.GamerManager;
import org.bukkit.entity.Player;

import net.betterpvp.clans.dailies.mysql.QuestRepository;

public class QuestPerkManager {
	
	private static List<QuestPerk> perks = new ArrayList<>();
	
	public QuestPerkManager(){
		addPerk(new BaseFishing());
		//addPerk(new Swim());
		addPerk(new ShopDiscount5());
	}
	
	public static void addPerk(QuestPerk p){
		perks.add(p);
		
	}
	
	public static QuestPerk getPerk(String name){
		for(QuestPerk qp : getPerks()){
	
			if(qp.getName().equalsIgnoreCase(name)){

				return qp;
			}
		}
		return null;
	}
	
	public static List<QuestPerk> getPerks(){
		return perks;
	}
	
	public static boolean hasPerk(Player p, String name){
		for(QuestPerk q : getPerks()){
			if(q.getName().equalsIgnoreCase(name)){
				if(GamerManager.getOnlineGamer(p).getQuestPerks().contains(q)){
					return true;
				}
			}
		}
		return false;
	}
	
	public static void addPerk(Player p, String name){
		for(QuestPerk z : getPerks()){
			if(z.getName().equalsIgnoreCase(name)){
				GamerManager.getOnlineGamer(p).getQuestPerks().add(z);
				QuestRepository.saveQuestPerk(p.getUniqueId(), z);
				
			}
		}
	}

}
