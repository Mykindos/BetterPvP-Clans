package net.betterpvp.clans.worldevents.commands;

import net.betterpvp.clans.clans.events.ScoreboardUpdateEvent;
import net.betterpvp.clans.worldevents.WEManager;
import net.betterpvp.clans.worldevents.WorldEvent;
import net.betterpvp.clans.worldevents.types.Boss;
import net.betterpvp.clans.worldevents.types.Environmental;
import net.betterpvp.clans.worldevents.types.Timed;
import net.betterpvp.clans.worldevents.types.WorldEventMinion;
import net.betterpvp.clans.worldevents.types.bosses.SlimeKing;
import net.betterpvp.clans.worldevents.types.bosses.ads.SlimeBase;
import net.betterpvp.core.client.Rank;
import net.betterpvp.core.command.Command;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class BossCommand extends Command {

    public BossCommand() {
        super("boss", new String[]{}, Rank.ADMIN);
    }


    @Override
    public void execute(Player player, String[] args) {


        if(args.length > 1){
            if(args[0].equalsIgnoreCase("spawn")){
                System.out.println("Spawning: " + args[1].toLowerCase());
                switch(args[1].toLowerCase()){
                    case "broodmother":
                        Boss b = (Boss) WEManager.getWorldEvent("Broodmother");
                        b.spawn();
                        b.setActive(true);
                        WEManager.announce();
                        break;
                    case "undeadcamp":
                        Timed undeadcamp = (Timed) WEManager.getWorldEvent("UndeadCamp");
                        undeadcamp.spawn();
                        undeadcamp.setActive(true);
                        break;
                    case "skeletonking":
                        Boss a = (Boss) WEManager.getWorldEvent("skeletonking");
                        a.spawn();
                        a.setActive(true);
                        WEManager.announce();
                        break;
                    case "slimeking":
                        Boss s = (Boss) WEManager.getWorldEvent("slimeking");
                        s.spawn();
                        s.setActive(true);
                        WEManager.announce();
                        break;
                    case "witherton":
                        Boss w = (Boss) WEManager.getWorldEvent("witherton");
                        w.spawn();
                        w.setActive(true);
                        WEManager.announce();
                        break;
                    case "borisdoris":
                        Boss bd = (Boss) WEManager.getWorldEvent("Boris & Doris");
                        bd.spawn();
                        bd.setActive(true);
                        WEManager.announce();
                        break;
                    case "miningmadness":
                        Environmental enviro = (Environmental) WEManager.getWorldEvent("miningmadness");
                        enviro.spawn();
                        enviro.setActive(true);
                        WEManager.announce();
                        break;
                    case "fishingfrenzy":
                        Environmental enviro2 = (Environmental) WEManager.getWorldEvent("fishingfrenzy");
                        enviro2.spawn();
                        enviro2.setActive(true);
                        WEManager.announce();
                        break;
                }
            }
        }else if(args.length == 1) {
            if(args[0].equalsIgnoreCase("tp")) {
                for(WorldEvent we : WEManager.getWorldEvents()) {
                    if(we.isActive()) {

                        if(we instanceof Boss) {
                            Boss boss = (Boss) we;
                            UtilMessage.message(player, "World Event", "Teleported " + ChatColor.YELLOW + boss.getName() + ChatColor.GRAY + " to you.");
                            if(boss.getBoss() != null && !boss.getBoss().isDead()) {
                                boss.getBoss().teleport(player);

                                for(WorldEventMinion wem : boss.getMinions()) {
                                    if(wem.getEntity() != null && !wem.getEntity().isDead()) {
                                        wem.getEntity().teleport(player);
                                    }
                                }

                                if(boss instanceof SlimeKing) {
                                    SlimeKing slimeKing = (SlimeKing) boss;

                                    for(SlimeBase sb : slimeKing.allSlimes) {
                                        if(sb.getEntity() != null && !sb.getEntity().isDead()) {
                                            sb.getEntity().teleport(player);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }else if(args[0].equalsIgnoreCase("reset")){
                for(WorldEvent we : WEManager.getWorldEvents()){
                    we.setActive(false);
                }

                for(Player p : Bukkit.getOnlinePlayers()){
                    Bukkit.getPluginManager().callEvent(new ScoreboardUpdateEvent(p));
                }
            }
        }
    }



    @Override
    public void help(Player player) {
        // TODO Auto-generated method stub

    }

}