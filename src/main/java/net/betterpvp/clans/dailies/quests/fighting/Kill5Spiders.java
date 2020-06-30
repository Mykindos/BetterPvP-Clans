package net.betterpvp.clans.dailies.quests.fighting;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.combat.CombatLogs;
import net.betterpvp.clans.combat.LogManager;
import net.betterpvp.clans.dailies.progression.Progress;
import net.betterpvp.clans.dailies.progression.types.GeneralProgression;
import net.betterpvp.clans.dailies.quests.General;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

public class Kill5Spiders extends General{

	public Kill5Spiders(Clans i) {
		super(i, "Kill 5 Spiders", new String[]{
				ChatColor.GRAY + "Kill a total of " + ChatColor.GREEN + "5" + ChatColor.GRAY + " Spiders."
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
			CombatLogs killer = LogManager.getKiller(e.getEntity());
			if(killer!= null){
				if(killer.getDamager() instanceof Player){
					
					Player player = (Player) killer.getDamager();
					if(e.getEntity().getType() == EntityType.SPIDER){
						Progress p = getQuestProgression(player.getUniqueId(), getName());

						if(!p.isComplete()){
							if(p instanceof GeneralProgression){
								GeneralProgression gp = (GeneralProgression) p;
								gp.addCurrentAmount();

								if(gp.getCurrentAmount() >= gp.getRequiredAmount()){
									gp.onComplete(player.getUniqueId());
								}
							}
						}
					}
				}
			}
		}
	}

}


