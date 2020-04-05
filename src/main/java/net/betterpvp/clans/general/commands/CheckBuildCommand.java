package net.betterpvp.clans.general.commands;

import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.BuildSkill;
import net.betterpvp.clans.skills.selector.RoleBuild;
import net.betterpvp.core.client.Rank;
import net.betterpvp.core.command.Command;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CheckBuildCommand extends Command {

    public CheckBuildCommand() {
        super("checkbuild", new String[]{"cbuild"}, Rank.ADMIN);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void execute(Player player, String[] args) {
        if(args != null){
            if(args.length == 2){
                Player p = Bukkit.getPlayer(args[0]);
                if(p != null){
                    Gamer gamer = GamerManager.getGamer(p);

                    RoleBuild b = gamer.getActiveBuild(args[1]);
                    if(b != null){

                        UtilMessage.message(player, ChatColor.GREEN.toString() + ChatColor.BOLD + p.getName());

                        BuildSkill sword = b.getBuildSkill(Types.SWORD);
                        if(sword != null){
                            UtilMessage.message(player, ChatColor.YELLOW + "Sword: " + ChatColor.GRAY + sword.getString());
                        }

                        BuildSkill axe = b.getBuildSkill(Types.AXE);
                        if(axe != null){
                            UtilMessage.message(player, ChatColor.YELLOW + "Axe: " + ChatColor.GRAY + axe.getString());
                        }

                        BuildSkill bow = b.getBuildSkill(Types.BOW);
                        if(bow != null){
                            UtilMessage.message(player, ChatColor.YELLOW + "bow: " + ChatColor.GRAY + bow.getString());
                        }

                        BuildSkill passiveA = b.getBuildSkill(Types.PASSIVE_A);
                        if(passiveA != null){
                            UtilMessage.message(player, ChatColor.YELLOW + "Passive A: " + ChatColor.GRAY + passiveA.getString());
                        }

                        BuildSkill passiveB = b.getBuildSkill(Types.PASSIVE_B);
                        if(passiveB != null){
                            UtilMessage.message(player, ChatColor.YELLOW + "Passive B: " + ChatColor.GRAY + passiveB.getString());
                        }

                        BuildSkill global = b.getBuildSkill(Types.GLOBAL);
                        if(global != null){
                            UtilMessage.message(player, ChatColor.YELLOW + "Global: " + ChatColor.GRAY + global.getString());
                        }
                    }
                }
            }
        }
    }

    @Override
    public void help(Player player) {
    }

}