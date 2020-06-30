package net.betterpvp.clans.dailies.quests.fighting;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.Role;
import net.betterpvp.clans.combat.CombatLogs;
import net.betterpvp.clans.combat.LogManager;
import net.betterpvp.clans.dailies.progression.Progress;
import net.betterpvp.clans.dailies.progression.types.GeneralProgression;
import net.betterpvp.clans.dailies.quests.General;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

public class Kill3Knights extends General{

	public Kill3Knights(Clans i) {
		super(i, "Kill 3 Knights", new String[]{
				ChatColor.GRAY + "Kill a total of " + ChatColor.GREEN + "3" + ChatColor.GRAY + " Knights."
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
			CombatLogs cl = LogManager.getKiller(e.getEntity());
			if(cl != null){
				if(Role.getRole(e.getEntity()) != null && Role.getRole(e.getEntity()).getName().equals("Knight")){
					Progress p = getQuestProgression(cl.getDamager().getUniqueId(), getName());
					if(p != null) {
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

}
