package net.betterpvp.clans.dailies.quests.fighting;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.dailies.progression.Progress;
import net.betterpvp.clans.dailies.progression.types.GeneralProgression;
import net.betterpvp.clans.dailies.quests.General;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

public class Kill20Pigmen extends General{

	public Kill20Pigmen(Clans i) {
		super(i, "Kill 20 Zombie Pigmen", new String[]{
				ChatColor.GRAY + "Kill a total of " + ChatColor.GREEN + "20" + ChatColor.GRAY + " Zombie Pigmen."
		});

	}

	@Override
	public int getRequiredAmount() {
		// TODO Auto-generated method stub
		return 20;
	}


	@EventHandler
	public void onKill(EntityDeathEvent e){
		if(isActive()){
			if(e.getEntity().getKiller() != null){
				if(e.getEntity().getType() == EntityType.ZOMBIFIED_PIGLIN){
					Progress p = getQuestProgression(e.getEntity().getKiller().getUniqueId(), getName());

					if(!p.isComplete()){
						if(p instanceof GeneralProgression){
							GeneralProgression gp = (GeneralProgression) p;
							gp.addCurrentAmount();

							if(gp.getCurrentAmount() >= gp.getRequiredAmount()){
								gp.onComplete(e.getEntity().getKiller().getUniqueId());
							}
						}
					}
				}
			}
		}
	}
	
}


