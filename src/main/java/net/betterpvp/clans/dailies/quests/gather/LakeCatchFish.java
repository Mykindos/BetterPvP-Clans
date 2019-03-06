package net.betterpvp.clans.dailies.quests.gather;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.dailies.progression.Progress;
import net.betterpvp.clans.dailies.progression.types.GeneralProgression;
import net.betterpvp.clans.dailies.quests.General;
import net.md_5.bungee.api.ChatColor;

public class LakeCatchFish extends General{

	public LakeCatchFish(Clans i) {
		super(i, "Catch 25 Fish", new String[]{
				ChatColor.GRAY + "Catch " + ChatColor.GREEN + "25 " + ChatColor.GRAY + "fish from the lake.",
		});

	}

	@Override
	public int getRequiredAmount() {
		// TODO Auto-generated method stub
		return 25;
	}

	@EventHandler
	public void onBreak(PlayerFishEvent e){
		if(isActive()){
			if(e.getCaught() != null && e.getCaught().getType() == EntityType.DROPPED_ITEM){
				if(ClanUtilities.getClan(e.getHook().getLocation()) != null){
					if(ClanUtilities.getClan(e.getHook().getLocation()).getName().equals("Lake")
							|| ClanUtilities.getClan(e.getHook().getLocation()).getName().equals("Fields")){
							Progress p = getQuestProgression(e.getPlayer().getUniqueId(), getName());

							if(!p.isComplete()){
								if(p instanceof GeneralProgression){
									GeneralProgression gp = (GeneralProgression) p;
									gp.addCurrentAmount();

									if(gp.getCurrentAmount() >= gp.getRequiredAmount()){
										gp.onComplete(e.getPlayer().getUniqueId());
									}
								}
							}

						}
					}
				}
			}
		}
	}

