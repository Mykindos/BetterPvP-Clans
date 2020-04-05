package net.betterpvp.clans.koth;

import net.betterpvp.clans.weapon.weapons.SupplyCrateData;
import net.betterpvp.core.client.Rank;
import net.betterpvp.core.command.Command;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.restoration.BlockRestoreData;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class KOTHCommand extends Command {

    public KOTHCommand() {
        super("koth", new String[]{}, Rank.ADMIN);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void execute(Player player, String[] args) {
        if(args != null){
            if(args.length == 1){
                if(args[0].equalsIgnoreCase("start")){
                    if(KOTHManager.koth == null){
                        UtilMessage.broadcast("KOTH", "A KOTH has begun at Fields!");
                        UtilMessage.broadcast("KOTH", "The KOTH chest will land in 15 minutes!");
                        UtilMessage.broadcast("KOTH", "Win the event and earn great loot!");


                        Block b = player.getLocation().getBlock();
                        b.setType(Material.BEACON);
                        for(int x = -1 ; x <= 1; x++){
                            for(int z = -1; z <= 1; z++){
                                new BlockRestoreData(player.getLocation().add(x, -1, z).getBlock(), Material.IRON_BLOCK, (byte) 0, 1000000);
                            }
                        }
                        KOTHManager.koth = new SupplyCrateData(b.getLocation(), 900 );
                        KOTHManager.filledInventory = false;
                        KOTHManager.kothStart = System.currentTimeMillis() + 900000;
                    }


                }else if(args[0].equalsIgnoreCase("stop")){
                    UtilMessage.broadcast("KOTH", "The KOTH at Fields has ended!");
                    KOTHManager.koth = null;
                    KOTHManager.clanKills.clear();
                    KOTHManager.winner = null;
                    KOTHManager.broadcasted = false;
                    KOTHManager.finalWinner = "";
                }
            }
        }
    }

    @Override
    public void help(Player player) {
    }

}