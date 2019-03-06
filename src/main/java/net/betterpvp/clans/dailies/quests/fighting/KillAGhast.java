package net.betterpvp.clans.dailies.quests.fighting;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.dailies.progression.Progress;
import net.betterpvp.clans.dailies.progression.types.GeneralProgression;
import net.betterpvp.clans.dailies.quests.General;
import net.betterpvp.clans.gamer.combat.CombatLogs;
import net.betterpvp.clans.gamer.combat.LogManager;
import net.md_5.bungee.api.ChatColor;

public class KillAGhast extends General{

	public KillAGhast(Clans i) {
		super(i, "Kill 5 Ghasts", new String[]{
				ChatColor.GRAY + "Kill 5 ghasts."
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
				if(e.getEntity().getType() == EntityType.GHAST){
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


