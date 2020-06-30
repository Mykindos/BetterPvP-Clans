package net.betterpvp.clans.dailies.quests.fighting;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.Role;
import net.betterpvp.clans.combat.LogManager;
import net.betterpvp.clans.dailies.progression.Progress;
import net.betterpvp.clans.dailies.progression.types.GeneralProgression;
import net.betterpvp.clans.dailies.quests.General;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

public class Kill3Gladiators extends General{

	public Kill3Gladiators(Clans i) {
		super(i, "Kill 3 Gladiators", new String[]{
				ChatColor.GRAY + "Kill a total of " + ChatColor.GREEN + "3" + ChatColor.GRAY + " gladiators."
		});

	}

	@Override
	public int getRequiredAmount() {
		// TODO Auto-generated method stub
		return 3;
	}


	@EventHandler
	public void onKill(PlayerDeathEvent e){
		if(isActive()){
			if(LogManager.getKiller(e.getEntity()) != null){
				if(Role.getRole(e.getEntity()) != null && Role.getRole(e.getEntity()).getName().equals("Gladiator")){
					Progress p = getQuestProgression(LogManager.getKiller(e.getEntity()).getDamager().getUniqueId(), getName());
					if(p != null) {
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
