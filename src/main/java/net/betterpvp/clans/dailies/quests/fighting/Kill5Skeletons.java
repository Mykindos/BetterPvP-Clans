package net.betterpvp.clans.dailies.quests.fighting;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.dailies.progression.Progress;
import net.betterpvp.clans.dailies.progression.types.GeneralProgression;
import net.betterpvp.clans.dailies.quests.General;
import net.betterpvp.clans.gamer.combat.LogManager;
import net.md_5.bungee.api.ChatColor;

public class Kill5Skeletons extends General{

	public Kill5Skeletons(Clans i) {
		super(i, "Kill 5 Skeletons", new String[]{
				ChatColor.GRAY + "Kill a total of " + ChatColor.GREEN + "5" + ChatColor.GRAY + " Skeletons."
		});

	}

	@Override
	public int getRequiredAmount() {
		// TODO Auto-generated method stub
		return 5;
	}


	@EventHandler
	public void onKill(EntityDeathEvent e){
		if(isActive()){
			if(LogManager.getKiller(e.getEntity()) != null){
				if(e.getEntity().getType() == EntityType.SKELETON){
					Progress p = getQuestProgression(LogManager.getKiller(e.getEntity()).getDamager().getUniqueId(), getName());
					if(p != null){
						if(!p.isComplete()){
							if(p instanceof GeneralProgression){
								GeneralProgression gp = (GeneralProgression) p;
								gp.addCurrentAmount();

								if(gp.getCurrentAmount() >= gp.getRequiredAmount()){
									gp.onComplete(LogManager.getKiller(e.getEntity()).getDamager().getUniqueId());
								}
							}
						}
					}
				}
			}
		}
	}

}


