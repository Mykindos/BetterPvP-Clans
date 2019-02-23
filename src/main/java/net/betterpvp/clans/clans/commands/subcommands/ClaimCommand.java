package net.betterpvp.clans.clans.commands.subcommands;

import net.betterpvp.clans.clans.AdminClan;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanMember.Role;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.clans.commands.IClanCommand;
import net.betterpvp.clans.clans.events.ChunkClaimEvent;
import net.betterpvp.clans.clans.mysql.ClanRepository;

import net.betterpvp.core.client.ClientUtilities;
import net.betterpvp.core.database.Log;
import net.betterpvp.core.utility.UtilFormat;
import net.betterpvp.core.utility.UtilLocation;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public final class ClaimCommand implements IClanCommand{



    public ClaimCommand() {
       
        
        
    }

    public void run(Player player, String[] args) {
    	if(player.getWorld().getName().equalsIgnoreCase("tutorial")) return;
    	if(player.getWorld().getName().equalsIgnoreCase("bossworld2")) return;
    	
        Clan clan = ClanUtilities.getClan(player);
        if (clan == null) {
            UtilMessage.message(player, "Clans", "You are not in a Clan.");
            return;
        }
        
        
        if (!clan.getMember(player.getUniqueId()).hasRole(Role.ADMIN)) {
			UtilMessage.message(player, "Clans", "You need to be a clan admin to claim land");
			return;
		}
        
        if(player.getWorld().getEnvironment().equals(Environment.NETHER) && !ClientUtilities.getClient(player).isAdministrating()){
        	UtilMessage.message(player, "Clans", "You cannot claim land in the nether.");
        	return;
        }

        if (!(clan instanceof AdminClan)) {
            if (clan.getTerritory().size() >= clan.getMembers().size() + 3) { // Previously 
                UtilMessage.message(player, "Clans", "Your Clan cannot claim more Territory.");
                return;
            }
        }

        /*
        for(SupplyCrateData s : SupplyCrate.crates){
        	Chunk c = s.getLocation().getChunk();
        	if(c == player.getLocation().getChunk()){
        		UtilMessage.message(player, "Clans", "You cannot claim land containing a supply drop!");
        		return;
        	}
        }
        */

        for (Clan others : ClanUtilities.clans) {
            if (!clan.getTerritory().contains(UtilFormat.chunkToFile(player.getLocation().getChunk()))) {
                if (others.getTerritory().contains(UtilFormat.chunkToFile(player.getLocation().getChunk()))) {
                    UtilMessage.message(player, "Clans", "This Territory is owned by " + ChatColor.YELLOW + "Clan "
                            + ClanUtilities.getClan(player.getLocation()).getName() + ChatColor.GRAY + ".");
                    return;
                }
            }
        }

        if (clan.getTerritory().contains(UtilFormat.chunkToFile(player.getLocation().getChunk()))) {
            UtilMessage.message(player, "Clans", "This Territory is owned by " + ChatColor.YELLOW + "Clan "
                    + ClanUtilities.getClan(player.getLocation()).getName() + ChatColor.GRAY + ".");
            return;
        }

        Chunk chunk = player.getLocation().getChunk();
        World world = player.getWorld();
        if (chunk.getEntities() != null) {
            for (Entity entitys : chunk.getEntities()) {
                if (entitys instanceof Player) {
                    if (entitys.equals(player)) {
                        continue;
                    }

                    UtilMessage.message(player, "Clans", "You cannot claim Territory containing enemies.");
                    break;
                }
            }
        }

        if (!(clan instanceof AdminClan)) {
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    Chunk testedChunk = world.getChunkAt(chunk.getX() + x, chunk.getZ() + z);
                    for (Clan clans : ClanUtilities.clans) {
                        if (clans.getTerritory().contains(UtilFormat.chunkToFile(testedChunk))) {
                            if (clans.equals(clan)) {
                                continue;
                            }

                            UtilMessage.message(player, "Clans", "You cannot claim next to enemy territory.");
                            return;
                        }
                    }
                }
            }
        }
        
        if(clan instanceof AdminClan){
        	 UtilMessage.message(player, "Clans", "You claimed Territory " + ChatColor.YELLOW
                     + UtilLocation.chunkToString(player.getLocation().getChunk()) + ChatColor.GRAY + ".");

             clan.messageClan(ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " claimed Territory " + ChatColor.YELLOW
                     + UtilLocation.chunkToString(player.getLocation().getChunk()) + ChatColor.GRAY + ".", player.getUniqueId(), true);
              clan.getTerritory().add(UtilFormat.chunkToFile(player.getLocation().getChunk()));
             UtilLocation.outlineChunk(player.getLocation().getChunk());
             Log.write("Clans", "[" + player.getName() + "] claimed territory [" + UtilFormat.chunkToFile(player.getLocation().getChunk()) + "]");
             ClanRepository.updateClaims(clan);
             Bukkit.getPluginManager().callEvent(new ChunkClaimEvent(player.getLocation().getChunk()));
            // ClanRepository.updateDynmap(i, clan);
             return;
        }

        if (clan.getTerritory().size() > 0) {
        	
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    Chunk testedChunk = world.getChunkAt(chunk.getX() + x, chunk.getZ() + z);
                    for (Clan clans : ClanUtilities.clans) {
                        if (clans.getTerritory().contains(UtilFormat.chunkToFile(testedChunk))) {
			
                            clan.getTerritory().add(UtilFormat.chunkToFile(player.getLocation().getChunk()));
                            UtilLocation.outlineChunk(player.getLocation().getChunk());
                            Bukkit.getPluginManager().callEvent(new ChunkClaimEvent(player.getLocation().getChunk()));

                            UtilMessage.message(player, "Clans", "You claimed Territory " + ChatColor.YELLOW
                                    + UtilLocation.chunkToString(player.getLocation().getChunk()) + ChatColor.GRAY + ".");

                            clan.messageClan(ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " claimed Territory " + ChatColor.YELLOW
                                    + UtilLocation.chunkToString(player.getLocation().getChunk()) + ChatColor.GRAY + ".", player.getUniqueId(), true);

                            Log.write("Clans", "[" + player.getName() + "] claimed territory [" + UtilFormat.chunkToFile(player.getLocation().getChunk()) + "]");
                            ClanRepository.updateClaims(clan);
                            //ClanRepository.updateDynmap(i, clan);
                            return;
                            
                        }
                    }
                }
            }
            
        } else {

            /*
             If clan has no territory let them claim without allign territory checks
             */
            UtilMessage.message(player, "Clans", "You claimed Territory " + ChatColor.YELLOW
                    + UtilLocation.chunkToString(player.getLocation().getChunk()) + ChatColor.GRAY + ".");

            clan.messageClan(ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " claimed Territory " + ChatColor.YELLOW
                    + UtilLocation.chunkToString(player.getLocation().getChunk()) + ChatColor.GRAY + ".", player.getUniqueId(), true);
             clan.getTerritory().add(UtilFormat.chunkToFile(player.getLocation().getChunk()));
            UtilLocation.outlineChunk(player.getLocation().getChunk());
            Log.write("Clans", "[" + player.getName() + "] claimed territory [" + UtilFormat.chunkToFile(player.getLocation().getChunk()) + "]");
            ClanRepository.updateClaims(clan);
            Bukkit.getPluginManager().callEvent(new ChunkClaimEvent(player.getLocation().getChunk()));
            //ClanRepository.updateDynmap(i, clan);
            return;
        }

       UtilMessage.message(player, "Clans", "You need to claim next to your territory.");
    }

	@Override
	public String getName() {
		
		return "Claim";
	}
}
