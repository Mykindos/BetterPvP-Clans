package net.betterpvp.clans.clans.listeners;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.Invitable;
import net.betterpvp.core.framework.BPVPListener;
import net.betterpvp.core.framework.UpdateEvent;
import org.bukkit.event.EventHandler;

import java.util.ArrayList;
import java.util.List;

public class InviteHandler extends BPVPListener<Clans> {

    private static List<Invite> invites = new ArrayList<>();

    public InviteHandler(Clans instance) {
        super(instance);
    }


    /**
     * Create a valid invite for x minutes
     *
     * @param inviter The inviter
     * @param invitee The invitee
     * @param expiry  Time in minutes until invite is no longer valid
     * @return true if invite does not already exist
     */
    public static boolean createInvite(Invitable inviter, Invitable invitee, String type, int expiry) {

        Invite invite = new Invite(inviter, invitee, type, expiry);
        if (!invites.contains(invite)) {
            invites.add(invite);
            return true;
        }

        return false;
    }


    /**
     * @param inviter
     * @param invitee
     * @return Returns true if the invite was found and valid
     */
    public static boolean removeInvite(Invitable inviter, Invitable invitee, String type) {
        return invites.removeIf(i -> i.inviter.equals(inviter) && i.invitee.equals(invitee) && i.type.equals(type));
    }

    /**
     * Check if there is a valid invite between two Invitable
     *
     * @param invitee The invitee
     * @param inviter The inviter
     * @return Returns true if a valid invite exists
     */
    public static boolean isInvited(Invitable invitee, Invitable inviter, String type) {
        return invites.stream().filter(i -> i.invitee.equals(invitee) && i.inviter.equals(inviter) && i.type.equals(type)).findAny().isPresent();
    }


    @EventHandler
    public void onUpdate(UpdateEvent e) {
        if (e.getType() == UpdateEvent.UpdateType.SLOWEST) {
            invites.removeIf(i -> i.expiry <= System.currentTimeMillis());
        }
    }


}

class Invite {

    public Invitable inviter;
    public Invitable invitee;
    public String type;
    public long requestTime;
    public long expiry;

    public Invite(Invitable inviter, Invitable invitee, String type, int expiry) {
        this.inviter = inviter;
        this.invitee = invitee;
        this.type = type;
        this.requestTime = System.currentTimeMillis();
        this.expiry = this.requestTime + (expiry * 60000);
    }

    @Override
    public boolean equals(Object i) {
        if (!(i instanceof Invite)) return false;

        Invite invite = (Invite) i;
        return invite.inviter.equals(inviter) && invite.invitee.equals(invitee)
                && invite.expiry == expiry && type.equals(invite.type);
    }

}