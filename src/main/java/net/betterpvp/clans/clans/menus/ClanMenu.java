package net.betterpvp.clans.clans.menus;

import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanMember;
import net.betterpvp.clans.clans.ClanMember.Role;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.clans.menus.buttons.ClaimButton;
import net.betterpvp.clans.clans.menus.buttons.EnergyMenuButton;
import net.betterpvp.clans.clans.menus.buttons.LeaveButton;
import net.betterpvp.clans.clans.menus.buttons.MemberButton;
import net.betterpvp.core.client.Client;
import net.betterpvp.core.client.ClientUtilities;
import net.betterpvp.core.interfaces.Button;
import net.betterpvp.core.interfaces.Menu;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class ClanMenu extends Menu {

    private Clan clan;
    private Player player;

    public ClanMenu(Player player, Clan clan) {
        super(player, 54, ChatColor.YELLOW + "Clan Menu", new Button[]{});
        this.clan = clan;
        this.player = player;
        fillPage();
        construct();
    }

    public void fillPage() {

        ClanMember member = clan.getMember(player.getUniqueId());


        addButton(new EnergyMenuButton(4, new ItemStack(Material.BLAZE_ROD),
                ChatColor.GREEN.toString() + ChatColor.BOLD + "Clan Energy",
                ChatColor.GRAY + "Clan Energy is needed to keep your clan from being disbanded",
                "",
                ChatColor.GREEN + "Your Energy: " + ChatColor.YELLOW + (int) clan.getEnergy(),
                ChatColor.GREEN + "Disbands in: " + ChatColor.YELLOW
                        + ClanUtilities.getHoursOfEnergy(clan) + " hours",
                "",
                ChatColor.GREEN + "Left Click: " + ChatColor.GRAY + "Buy Energy"));


        if (member.hasRole(Role.LEADER)) {

            addButton(new LeaveButton(clan, 6, new ItemStack(Material.DARK_OAK_DOOR),
                    ChatColor.GREEN.toString() + ChatColor.BOLD + "Leave Clan",
                    ChatColor.GREEN + "Shift Left-Click: " + ChatColor.GRAY + "Leave Clan",
                    ChatColor.GREEN + "Shift Right-Click: " + ChatColor.GRAY + "Disband Clan"));
        } else {

            addButton(new LeaveButton(clan, 6, new ItemStack(Material.DARK_OAK_DOOR),
                    ChatColor.GREEN.toString() + ChatColor.BOLD + "Leave Clan",
                    ChatColor.GREEN + "Shift Left-Click: " + ChatColor.GRAY + "Leave Clan"));
        }

        if (member.hasRole(Role.ADMIN)) {
            addButton(new ClaimButton(clan, 2, new ItemStack(Material.GRASS),
                    ChatColor.GREEN.toString() + ChatColor.BOLD + "Claimed Land",
                    ChatColor.GRAY + "Your clan has claimed " + ChatColor.GREEN + clan.getTerritory().size()
                            + ChatColor.GRAY + " chunks",
                    ChatColor.GRAY + "Your clan can claim " + ChatColor.GREEN
                            + ((clan.getMembers().size() + 3) - clan.getTerritory().size())
                            + ChatColor.GRAY + " more chunks",
                    "",
                    ChatColor.GREEN + "Shift Left-Click: " + ChatColor.GRAY + "Claim current chunk",
                    ChatColor.GREEN + "Shift Right-Click: " + ChatColor.GRAY + "Unclaim current chunk"));
        } else {
            addButton(new ClaimButton(clan, 2, new ItemStack(Material.GRASS),
                    ChatColor.GREEN.toString() + ChatColor.BOLD + "Claimed Land",
                    ChatColor.GRAY + "Your clan has claimed " + ChatColor.GREEN + clan.getTerritory().size()
                            + ChatColor.GRAY + " chunks",
                    ChatColor.GRAY + "Your clan can claim " + ChatColor.GREEN
                            + ((clan.getMembers().size() + 3) - clan.getTerritory().size())
                            + ChatColor.GRAY + " more chunks"));
        }


        int count = 18;
        for (ClanMember m : clan.getMembers()) {


            ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1, (short) SkullType.PLAYER.ordinal());
            SkullMeta headMeta = (SkullMeta) head.getItemMeta();
            Client client = ClientUtilities.getClient(m.getUUID());
            Player p = Bukkit.getPlayer(m.getUUID());

            if (client != null) {

                headMeta.setOwner(client.getName());
                head.setItemMeta(headMeta);

                String role = m.getRole().name().toLowerCase().substring(0, 1).toUpperCase() + m.getRole().name().substring(1).toUpperCase();
                if (!member.hasRole(Role.ADMIN)) {
                    addButton(new MemberButton(clan, count, head, p != null ? ChatColor.GREEN + p.getName() : ChatColor.RED + client.getName(),
                            "", ChatColor.GREEN + "Role: " + ChatColor.GRAY + role));
                } else {
                    addButton(new MemberButton(clan, count, head, p != null ? ChatColor.GREEN + p.getName() : ChatColor.RED + client.getName(),
                            "", ChatColor.GREEN + "Role: " + ChatColor.GRAY + role,
                            ChatColor.GREEN + "",
                            ChatColor.GREEN + "Shift Left-Click: " + ChatColor.GRAY + "Promote",
                            ChatColor.GREEN + "",
                            ChatColor.GREEN + "Right Click: " + ChatColor.GRAY + "Demote",
                            ChatColor.GREEN + "Shift Right-Click: " + ChatColor.GRAY + "Kick"));
                }
                count++;
            }
        }

    }

}
