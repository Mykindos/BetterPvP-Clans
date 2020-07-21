package net.betterpvp.clans.clans.commands.subcommands;

import io.github.bananapuncher714.cartographer.core.Cartographer;
import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.AdminClan;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.clans.ClanUtilities.ClanRelation;
import net.betterpvp.clans.clans.commands.IClanCommand;
import net.betterpvp.clans.clans.map.MapListener;
import net.betterpvp.clans.utilities.UtilClans;
import net.betterpvp.core.utility.UtilItem;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedList;

public final class MapCommand implements IClanCommand {

    public int height = 6;
    public int width = 8;


    public void run(Player player, String[] args) {
        if (args.length == 1) {
            if (Clans.getOptions().isAdvancedMap()) {
                if (player.getInventory().contains(MapListener.map)) {
                    UtilMessage.message(player, "Map", "You already have a map in your inventory");
                    return;
                }
                player.getInventory().addItem(MapListener.map.clone());
                UtilMessage.message(player, "Map", "A map has been added to your inventory.");
            } else {
                UtilMessage.message(player, "Clans", "Clan Territory Map:");
                displayMap(player);
            }
        }
    }

    public String getMapIcon(Player player, Clan clan, Chunk found) {

        if (player.getLocation().getChunk() == found) {
            return "+";
        }


        if (ClanUtilities.getClan(found) == null) {
            return ChatColor.WHITE + "-";
        }

        if (ClanUtilities.getClan(found) != null) {
            Clan target = ClanUtilities.getClan(found);

            if (target instanceof AdminClan) {
                AdminClan ac = (AdminClan) target;
                if (ac.isSafe()) {
                    return ChatColor.DARK_GREEN + "#";
                } else {
                    return ChatColor.GOLD + "#";
                }
            }

            if (ClanUtilities.getRelation(clan, target).equals(ClanRelation.SELF)) {
                if (clan.getHome() != null) {
                    if (clan.getHome().getChunk().equals(found)) {
                        return ClanUtilities.getRelation(clan, ClanUtilities.getClan(found)).getPrimary() + "H";
                    }
                }

                return ClanUtilities.getRelation(clan, ClanUtilities.getClan(found)).getPrimary() + "#";
//            } else if (Clan.isSafeZone(target)) {
//                return ChatColor.AQUA + "S";
            } else {
                if (target.getHome() != null) {
                    if (target.getHome().getChunk().equals(found)) {
                        return ClanUtilities.getRelation(clan, ClanUtilities.getClan(found)).getPrimary() + "H";
                    }
                }

                return ClanUtilities.getRelation(clan, target).getPrimary() + "#";
            }
        }
        return null;
    }

    public LinkedList<String> mLocalMap(Player player, Chunk chunk) {
        if (chunk == null) {
            return null;
        }

        LinkedList<String> localMap = new LinkedList<>();
        for (int i = chunk.getX() - height; i <= chunk.getX() + height; i++) {
            String output = ChatColor.WHITE + "<";
            for (int j = chunk.getZ() + width; j >= chunk.getZ() - width; j--) {
                Chunk curChunk = player.getWorld().getChunkAt(i, j);

                if (i == chunk.getX() && j == chunk.getZ()) {
                    output = output + getMapIcon(player, ClanUtilities.getClan(player), curChunk);
                } else {
                    output = output + getMapIcon(player, ClanUtilities.getClan(player), curChunk);
                }
            }
            output = output + ChatColor.WHITE + ">";
            localMap.add(output);
        }
        return localMap;
    }

    public void displayMap(Player player) {
        LinkedList<String> local = mLocalMap(player, player.getLocation().getChunk());
        message(player, local);
    }

    public void message(Player client, LinkedList<String> messageList) {
        messageList(client, messageList);
    }

    public void messageList(Player client, LinkedList<String> messageList) {
        for (String curMessage : messageList) {
            UtilMessage.message(client, curMessage);
        }
    }

    @Override
    public String getName() {

        return "Map";
    }
}
