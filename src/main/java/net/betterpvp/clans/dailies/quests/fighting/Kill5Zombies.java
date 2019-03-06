package net.betterpvp.clans.dailies.quests.fighting;

import net.betterpvp.clans.combat.CombatLogs;
import net.betterpvp.clans.combat.LogManager;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.dailies.progression.Progress;
import net.betterpvp.clans.dailies.progression.types.GeneralProgression;
import net.betterpvp.clans.dailies.quests.General;

import net.md_5.bungee.api.ChatColor;

public class Kill5Zombies extends General{

	public Kill5Zombies(Clans i) {
		super(i, "Kill 5 Zombies", new String[]{
				ChatColor.GRAY + "Kill a total of " + ChatColor.GREEN + "5" + ChatColor.GRAY + " zombies."
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
			CombatLogs cl = LogManager.getKiller(e.getEntity());
			if(cl != null){
				if(e.getEntity().getType() == EntityType.ZOMBIE){
					Progress p = getQuestProgression(cl.getDamager().getUniqueId(), getName());

					if(!p.isComplete()){
						if(p instanceof GeneralProgression){
							GeneralProgression gp = (GeneralProgression) p;
							gp.addCurrentAmount();

							if(gp.getCurrentAmount() >= gp.getRequiredAmount()){
								gp.onComplete(cl.getDamager().getUniqueId());
							}
						}
					}
				}
			}
		}
	}
	
}


