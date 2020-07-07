package net.betterpvp.clans.classes.commands;

import net.betterpvp.clans.classes.Role;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.BuildSkill;
import net.betterpvp.clans.skills.selector.RoleBuild;
import net.betterpvp.core.client.Rank;
import net.betterpvp.core.command.Command;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.fancymessage.FancyMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ScanCommand extends Command {

    private List<ScanRequest> requests;

    public ScanCommand() {
        super("scan", new String[]{"buildinfo"}, Rank.PLAYER);
        requests = new ArrayList<>();
    }

    @Override
    public void execute(Player player, String[] args) {
        if(args != null){
            if(args.length >= 2){
                String temp = "";
                for(int i = 1; i < args.length ; i++){
                    temp+= args[i] + " ";
                }

                String name = temp.trim();
                Gamer gamer = GamerManager.getOnlineGamer(name);
                if(gamer != null){
                    if(args[0].equalsIgnoreCase("request")){
                        if(!isRequesting(player.getUniqueId(), gamer.getUUID())){
                            requests.add(new ScanRequest(player.getUniqueId(), gamer.getUUID()));
                            Player target = Bukkit.getPlayer(gamer.getUUID());
                            if(target != null){
                                UtilMessage.message(target, "Class", ChatColor.GREEN + player.getName()
                                        + ChatColor.GRAY + " is requesting to view your current build.");

                                new FancyMessage(ChatColor.BLUE + "Class> " + ChatColor.GOLD.toString() + ChatColor.UNDERLINE + "Click Here")
                                        .command("/scan accept " + player.getName()).then(ChatColor.GRAY + " to accept!").send(target);
                                UtilMessage.message(player, "Class", "You requested to view " + ChatColor.GREEN + target.getName() + "'s"
                                        + ChatColor.GRAY + " current build.");
                            }
                        }else{
                            UtilMessage.message(player, "Class", "You are already requesting this players build!");
                        }
                    }else if(args[0].equalsIgnoreCase("accept")){
                        if(isRequesting(gamer.getUUID(), player.getUniqueId())){
                            requests.removeIf(s -> s.requester.toString().equalsIgnoreCase(gamer.getUUID().toString())
                                    && s.requestee.toString().equalsIgnoreCase(player.getUniqueId().toString()));
                            Player target = Bukkit.getPlayer(gamer.getUUID());
                            if(target != null){
                                UtilMessage.message(player, "Class", "You accepted " + ChatColor.GREEN +
                                        target.getName() + "'s " + ChatColor.GRAY + "scan request.");
                                Role r = Role.getRole(player);
                                if(r != null){


                                    RoleBuild b = GamerManager.getOnlineGamer(player).getActiveBuild(r.getName());
                                    if(b != null){

                                        UtilMessage.message(target, ChatColor.GREEN.toString() + ChatColor.BOLD + player.getName());

                                        BuildSkill sword = b.getBuildSkill(Types.SWORD);
                                        if(sword != null){
                                            UtilMessage.message(target, ChatColor.YELLOW + "Sword: " + ChatColor.GRAY + sword.getString());
                                        }

                                        BuildSkill axe = b.getBuildSkill(Types.AXE);
                                        if(axe != null){
                                            UtilMessage.message(target, ChatColor.YELLOW + "Axe: " + ChatColor.GRAY + axe.getString());
                                        }

                                        BuildSkill bow = b.getBuildSkill(Types.BOW);
                                        if(bow != null){
                                            UtilMessage.message(target, ChatColor.YELLOW + "Bow: " + ChatColor.GRAY + bow.getString());
                                        }

                                        BuildSkill passiveA = b.getBuildSkill(Types.PASSIVE_A);
                                        if(passiveA != null){
                                            UtilMessage.message(target, ChatColor.YELLOW + "Passive A: " + ChatColor.GRAY + passiveA.getString());
                                        }

                                        BuildSkill passiveB = b.getBuildSkill(Types.PASSIVE_B);
                                        if(passiveB != null){
                                            UtilMessage.message(target, ChatColor.YELLOW + "Passive B: " + ChatColor.GRAY + passiveB.getString());
                                        }

                                        BuildSkill global = b.getBuildSkill(Types.GLOBAL);
                                        if(global != null){
                                            UtilMessage.message(target, ChatColor.YELLOW + "Global: " + ChatColor.GRAY + global.getString());
                                        }
                                    }
                                }
                            }
                        }else{
                            UtilMessage.message(player, "Class", "This player is not requesting to view your build!");
                        }

                    }
                }

            }
        }
    }

    @Override
    public void help(Player player) {
    }

    private boolean isRequesting(UUID requester, UUID requestee){

        return requests.stream().anyMatch(s -> s.requestee.toString().equalsIgnoreCase(requestee.toString())
                && s.requester.toString().equalsIgnoreCase(requester.toString()));
    }

    public class ScanRequest{

        public UUID requester;
        public UUID requestee;

        public ScanRequest(UUID requester, UUID requestee){
            this.requestee = requestee;
            this.requester = requester;
        }
    }

}