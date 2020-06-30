package net.betterpvp.clans.dailies;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.dailies.progression.types.GeneralProgression;
import net.betterpvp.clans.dailies.quests.General;
import net.betterpvp.clans.dailies.quests.fighting.*;
import net.betterpvp.clans.dailies.quests.gather.*;
import net.betterpvp.core.configs.Configs;
import net.betterpvp.core.utility.UtilMath;
import net.betterpvp.core.utility.UtilTime;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;


public class QuestManager {

	private static List<Quest> quests = new ArrayList<>();

	public QuestManager(Clans i){
		
		/* 
		 * Gathering
		 */
		addQuest(new FieldsIronOre(i));
		addQuest(new FieldsCoalOre(i));
		addQuest(new FieldsGoldOre(i));
		addQuest(new FieldsEmeraldOre(i));
		addQuest(new FieldsDiamondOre(i));
		addQuest(new FieldsRedstoneOre(i));
		addQuest(new LakeCatchFish(i));
		addQuest(new Harvest30Carrot(i));
		addQuest(new Harvest30Potato(i));
		addQuest(new Harvest30Wheat(i));
		addQuest(new Harvest30Netherwarts(i));
		
		/*
		 * Killing Players
		 */
		
		addQuest(new Kill3Assassins(i));
		addQuest(new Kill3Knights(i));
		addQuest(new Kill3Paladins(i));
		addQuest(new Kill3Rangers(i));
		addQuest(new Kill3Gladiators(i));
		addQuest(new KillPlayers15(i));
		addQuest(new KillsAsAssassin(i));
		addQuest(new KillsAsKnight(i));
		addQuest(new KillsAsPaladin(i));
		addQuest(new KillsAsGladiator(i));
		addQuest(new KillsAsRanger(i));

		/*
		 * Killing Mobs
		 */
		
		addQuest(new Kill5Zombies(i));
		addQuest(new Kill5Skeletons(i));
		addQuest(new Kill5Spiders(i));

		
		
		loadQuests();
		
		new BukkitRunnable() {
			@Override
			public void run() {
				if(UtilTime.elapsed(Clans.getOptions().getDailyResetTime(), 86400000)) {
					loadQuests();
					Clans.getOptions().setDailyResetTime();
					i.getConfigManager().get(Configs.MAIN).set("Daily-Reset-Time", System.currentTimeMillis());
					
				}
			}
		}.runTaskTimer(i, 1000, 1000);
		
	}
	
	public static void loadQuests(){
		for(Quest p : getQuests()){
			p.setActive(false);
			p.getProgression().clear();
		}
		int x = 0;
		while (x < 5){
			Quest q = getQuests().get(UtilMath.randomInt(getQuests().size() -1));
			if(q != null){
				if(!q.isActive()){
					q.setActive(true);
					x++;
					for(Player p : Bukkit.getOnlinePlayers()){
						if(q instanceof General){
							General g = (General) q;
							g.getProgression().add(new GeneralProgression(p.getUniqueId(), q.getName(), g.getRequiredAmount()));
						}
					}
				}
			}
		}
	}

	public static List<Quest> getQuests(){
		return quests;
	}

	public void addQuest(Quest q){
		if(!getQuests().contains(q)){
			getQuests().add(q);
		}
	}
	
	public static Quest getQuest(String name){
		for(Quest q : getQuests()){
			if(q.getName().equalsIgnoreCase(name)){
				return q;
			}
		}
		return null;
	}

}
