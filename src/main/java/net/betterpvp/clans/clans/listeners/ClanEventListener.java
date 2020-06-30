package net.betterpvp.clans.clans.listeners;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.*;
import net.betterpvp.clans.clans.events.*;
import net.betterpvp.clans.clans.mysql.AllianceRepository;
import net.betterpvp.clans.clans.mysql.ClanRepository;
import net.betterpvp.clans.clans.mysql.EnemyRepository;
import net.betterpvp.clans.clans.mysql.MemberRepository;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.core.client.Client;
import net.betterpvp.core.client.ClientUtilities;
import net.betterpvp.core.database.Log;
import net.betterpvp.core.framework.BPVPListener;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.recharge.RechargeManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.CreatureSpawnEvent;


public class ClanEventListener extends BPVPListener<Clans> {

    public ClanEventListener(Clans instance) {
        super(instance);
    }

    /**
     * Called whenever a Clan is created
     * Is cancellable
     *
     * @param e The event
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onClanCreate(ClanCreateEvent e) {

        if (e.isCancelled()) {
            return;
        }

        Player player = e.getPlayer();
        if (ClientUtilities.getOnlineClient(player).isAdministrating()) {
            AdminClan clan = new AdminClan(e.getClanName());

            clan.setLeader(player.getUniqueId());
            clan.getMembers().add(new ClanMember(player.getUniqueId(), ClanMember.Role.LEADER));
            // ScoreboardManager.addPlayer(player.getName());
            clan.setSafe(true);
            ClanUtilities.addClan(clan);
            ClanRepository.saveClan(clan);
            UtilMessage.broadcast("Clans", ChatColor.YELLOW + player.getName() + ChatColor.GRAY
                    + " formed " + ChatColor.YELLOW + "Admin Clan " + e.getClanName() + ChatColor.GRAY + ".");

        } else {
            if (RechargeManager.getInstance().add(player, "Create Clan", 120, true)) {
                Clan clan = new Clan(e.getClanName());
                clan.setLeader(player.getUniqueId());
                clan.getMembers().add(new ClanMember(player.getUniqueId(), ClanMember.Role.LEADER));
                ClanRepository.saveClan(clan);
                ClanUtilities.addClan(clan);
                // ScoreboardManager.addPlayer(player.getName());

                UtilMessage.broadcast("Clans", ChatColor.YELLOW + player.getName() + ChatColor.GRAY
                        + " formed " + ChatColor.YELLOW + "Clan " + e.getClanName() + ChatColor.GRAY + ".");
                Log.write("Clans", player.getName() + " formed Clan " + e.getClanName());
            }
        }
    }

    /**
     * Called whenever a Clan is deleted
     * Is cancellable
     *
     * @param e The event
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onClanDelete(ClanDeleteEvent e) {

        if (e.isCancelled()) {
            return;
        }

        if(e.getPlayer() != null){
            ClanUtilities.disbandClan(e.getPlayer(), e.getClan());
        }else{
            ClanUtilities.disbandClan(e.getClan());
        }

    }

    /**
     * Called whenever a player leaves a clan
     * Is cancellable
     *
     * @param e The event
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onMemberLeaveClan(MemberLeaveClanEvent e) {

        if (e.isCancelled()) {
            return;
        }

        Clan clan = e.getClan();

        MemberRepository.deleteMember(clan.getMember(e.getClient().getUUID()));
        clan.getMembers().remove(clan.getMember(e.getClient().getUUID()));


        Player player = Bukkit.getPlayer(e.getClient().getUUID());
        if (player != null) {
            Log.write("Clans", "[" + player.getName() + "] left [" + clan.getName() + "]");
            UtilMessage.message(player, "Clans", "You left " + ChatColor.YELLOW + "Clan " + clan.getName() + ChatColor.GRAY + ".");
            clan.messageClan(ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " left your Clan.", player.getUniqueId(), true);
        }
    }

    /**
     * Called whenever a player joins a clan
     * Is cancellable
     *
     * @param e The event
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onMemberJoinClan(MemberJoinClanEvent e) {

        if (e.isCancelled()) {
            return;
        }

        Player player = e.getPlayer();
        Clan clan = e.getClan();
        Gamer gamer = GamerManager.getOnlineGamer(player);
        InviteHandler.removeInvite(clan, gamer, "Invite");
        InviteHandler.removeInvite(gamer, clan, "Invite");
        // Make sure they aren't already in the clan, just incase they are for whatever reason.
        if (clan.getMember(player.getUniqueId()) == null) {

            clan.messageClan(ChatColor.YELLOW + player.getName() + ChatColor.GRAY
                    + " has joined your Clan.", player.getUniqueId(), true);

            clan.getMembers().add(new ClanMember(player.getUniqueId(), ClanMember.Role.RECRUIT));
            MemberRepository.saveMember(clan, new ClanMember(player.getUniqueId(), ClanMember.Role.RECRUIT));
            //  ScoreboardManager.addPlayer(player.getName());

            Log.write("Clans", "Added [" + player.getUniqueId() + "] to Clan [" + clan.getName() + "]");
            UtilMessage.message(player, "Clans", "You joined " + ChatColor.YELLOW + "Clan " + clan.getName() + ChatColor.GRAY + ".");



        }
    }

    /**
     * Called whenever a clan kicks a player
     * Is cancellable
     *
     * @param e The event
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onClanKickMember(ClanKickMemberEvent e) {

        if (e.isCancelled()) {
            return;
        }

        Player player = e.getPlayer();
        Client target = e.getTarget();
        Clan clan = e.getClan();


        // Check if player is online so we can tell them they were kicked
        Player temp = Bukkit.getPlayer(target.getUUID());
        if (temp != null) {
            UtilMessage.message(Bukkit.getPlayer(target.getUUID()), "Clans", ChatColor.YELLOW
                    + player.getName() + ChatColor.GRAY + " kicked you from "
                    + ChatColor.YELLOW + "Clan " + clan.getName() + ChatColor.GRAY + ".");

        }

        MemberRepository.deleteMember(clan.getMember(target.getUUID()));
        clan.getMembers().remove(clan.getMember(target.getUUID()));


        clan.messageClan(ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " kicked " + ChatColor.YELLOW
                + target.getName() + ChatColor.GRAY + " from your Clan.", player.getUniqueId(), true);
        UtilMessage.message(player, "Clans", "You kicked " + ChatColor.YELLOW + target.getName()
                + ChatColor.GRAY + " from your Clan.");
        Log.write("Clans", "[" + player.getName() + "] kicked [" + target.getName() + "] from [" + clan.getName() + "]");
    }

    /**
     * Called whenever a clan requests an alliance with another clan
     * Is cancellable
     *
     * @param e The event
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onClanAllyClan(ClanAllyClanEvent e) {

        if (e.isCancelled()) {
            return;
        }

        Player player = e.getPlayer();
        Clan clan = e.getClanA();
        Clan target = e.getClanB();

        clan.getAlliances().add(new Alliance(target, false));
        target.getAlliances().add(new Alliance(clan, false));
        AllianceRepository.saveAlly(clan, target, false);
        AllianceRepository.saveAlly(target, clan, false);
        // ScoreboardManager.updateRelation();

        Bukkit.getPluginManager().callEvent(new ClanRelationshipEvent(clan, target));

        InviteHandler.removeInvite(target, clan, "Ally");

        UtilMessage.message(player, "Clans", "You accepted alliance with " + ChatColor.YELLOW
                + "Clan " + target.getName() + ChatColor.GRAY + ".");
        clan.messageClan(ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " accepted alliance with "
                + ChatColor.YELLOW + "Clan " + target.getName() + ChatColor.GRAY + ".", player.getUniqueId(), true);
        target.messageClan(ChatColor.YELLOW + "Clan " + clan.getName() + ChatColor.GRAY +
                " has accepted alliance with you.", null, true);
        Log.write("Clans", "[" + clan.getName() + "] allied [" + target.getName() + "]");
    }

    /**
     * Called whenever a clan requests trust with an allied clan
     * Is cancellable
     *
     * @param e The event
     */
    @EventHandler (priority = EventPriority.MONITOR)
    public void onClanTrustClan(ClanTrustClanEvent e) {

        if (e.isCancelled()) {
            return;
        }

        Player player = e.getPlayer();
        Clan clan = e.getClanA();
        Clan target = e.getClanB();


        clan.getAlliance(target).setTrust(true);
        target.getAlliance(clan).setTrust(true);
        AllianceRepository.updateAlly(clan, target);
        AllianceRepository.updateAlly(target, clan);
        //ScoreboardManager.updateRelation();
        InviteHandler.removeInvite(clan, target, "Trust");

        Bukkit.getPluginManager().callEvent(new ClanRelationshipEvent(clan, target));

        UtilMessage.message(player, "Clans", "You accepted trust with " + ChatColor.YELLOW + "Clan " + target.getName() + ChatColor.GRAY + ".");
        clan.messageClan(ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " accepted trust with "
                + ChatColor.YELLOW + "Clan " + target.getName() + ChatColor.GRAY + ".", player.getUniqueId(), true);
        target.messageClan(ChatColor.YELLOW + "Clan " + clan.getName() + ChatColor.GRAY + " has accepted trust with you.", null, true);
        Log.write("Clans", "[" + clan.getName() + "] trusted [" + target.getName() + "]");
    }

    /**
     * Called whenever a clan declares another clan as an enemy
     * Is cancellable
     *
     * @param e The event
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onClanEnemyClan(ClanEnemyClanEvent e) {

        if (e.isCancelled()) {
            return;
        }

        Player player = e.getPlayer();
        Clan clan = e.getClanA();
        Clan target = e.getClanB();

        if (clan.isAllied(target)) {
            clan.getAlliances().remove(clan.getAlliance(target));
            target.getAlliances().remove(target.getAlliance(clan));
            AllianceRepository.deleteAlly(clan, target);
            Log.write("Clans", "[" + clan.getName() + "] revoked alliance with [" + target.getName() + "]");
        }

        clan.getEnemies().add(new Dominance(clan, target, 0));
        target.getEnemies().add(new Dominance(target, clan, 0));

        EnemyRepository.saveDominance(new Dominance(target, clan, 0));
        EnemyRepository.saveDominance(new Dominance(clan, target, 0));

        Bukkit.getPluginManager().callEvent(new ClanRelationshipEvent(clan, target));
        // ScoreboardManager.updateRelation();


        Log.write("Clans", "[" + clan.getName() + "] waged war with [" + target.getName() + "]");
        UtilMessage.message(player, "Clans", "You waged war with " + ChatColor.YELLOW
                + "Clan " + target.getName() + ChatColor.GRAY + ".");
        clan.messageClan(ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " waged war with "
                + ChatColor.YELLOW + "Clan " + target.getName() + ChatColor.GRAY + ".", player.getUniqueId(), true);
        target.messageClan(ChatColor.YELLOW + "Clan " + clan.getName() + ChatColor.GRAY
                + " has waged war with your clan.", null, true);

    }

    /**
     * Called whenever a clan requests to be neutral with another clan
     * Is cancellable
     *
     * @param e The event
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onClanNeutralClan(ClanNeutralClanEvent e) {

        if (e.isCancelled()) {
            return;
        }

        Player player = e.getPlayer();
        Clan clan = e.getClanA();
        Clan target = e.getClanB();

        ClanUtilities.ClanRelation relation = ClanUtilities.getRelation(clan, target);
        if (relation == ClanUtilities.ClanRelation.ALLY || relation == ClanUtilities.ClanRelation.ALLY_TRUST) {
            UtilMessage.message(player, "Clans", "You revoked alliance with " + ChatColor.YELLOW + "Clan " + target.getName() + ChatColor.GRAY + ".");
            clan.messageClan(ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " revoked alliance with " + ChatColor.YELLOW + "Clan "
                    + target.getName() + ChatColor.GRAY + ".", player.getUniqueId(), true);
            target.messageClan(ChatColor.YELLOW + clan.getName() + ChatColor.GRAY + " has revoked alliance with you.", null, true);

            clan.getAlliances().remove(clan.getAlliance(target));
            target.getAlliances().remove(target.getAlliance(clan));
            AllianceRepository.deleteAlly(clan, target);

            Bukkit.getPluginManager().callEvent(new ClanRelationshipEvent(clan, target));

            Log.write("Clans", "[" + clan.getName() + "] revoked alliance with [" + target.getName() + "]");
        } else if (relation == ClanUtilities.ClanRelation.ENEMY) {



            if (InviteHandler.isInvited(clan, target, "Neutral")) {
                UtilMessage.message(player, "Clans", "You accepted neutral with " + ChatColor.YELLOW + "Clan " + target.getName() + ChatColor.GRAY + ".");
                clan.messageClan(ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " accepted neutral with "
                        + ChatColor.YELLOW + "Clan " + target.getName() + ChatColor.GRAY + ".", player.getUniqueId(), true);
                target.messageClan(ChatColor.YELLOW + "Clan " + clan.getName() + ChatColor.GRAY + " has accepted neutral with you.", null, true);


                Dominance dom = clan.getDominance(target);
                Dominance tarDom = target.getDominance(clan);
                Log.write("Clans", "[" + clan.getName() + "] neutralized [" + target.getName() + "] (" + dom.getPoints() + ":" + tarDom.getPoints() + ")");

                EnemyRepository.deleteEnemy(dom);
                clan.getEnemies().remove(dom);
                target.getEnemies().remove(tarDom);
                // ScoreboardManager.updateRelation();
                InviteHandler.removeInvite(clan, target, "Neutral");

                Bukkit.getPluginManager().callEvent(new ClanRelationshipEvent(clan, target));
                return;
            }




            UtilMessage.message(player, "Clans", "You requested neutral with " + ChatColor.YELLOW + "Clan " + target.getName() + ChatColor.GRAY + ".");
            clan.messageClan(ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " requested neutral with "
                    + ChatColor.YELLOW + "Clan " + target.getName() + ChatColor.GRAY + ".", player.getUniqueId(), true);

            target.messageClan(ChatColor.YELLOW + "Clan " + clan.getName() + ChatColor.GRAY + " has requested neutral with you.", null, true);
            InviteHandler.createInvite(clan, target,"Neutral", 10);


        }
    }

    /*
     * Disables mobs spawning in clan territory
     */
    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL
                || event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER
                || event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.BREEDING) {
            Clan clan = ClanUtilities.getClan(event.getEntity().getLocation());

            if (clan != null) {

                event.setCancelled(true);
            }

        }
    }
}
