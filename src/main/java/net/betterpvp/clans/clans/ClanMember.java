package net.betterpvp.clans.clans;

import org.bukkit.ChatColor;

import java.util.UUID;

public class ClanMember {

    private UUID uuid;
    private Role role;


    public ClanMember(UUID uuid, Role role) {
        this.uuid = uuid;
        this.role = role;

    }

    public UUID getUUID() {
        return uuid;
    }


    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public enum Role {

        LEADER(4), ADMIN(3), MEMBER(2), RECRUIT(1), NONE(0);

        private int i;

        Role(int value) {
            this.i = value;
        }

        public int toInt() {
            return i;
        }
    }

    public boolean hasRole(Role role) {
        return getRole().toInt() >= role.toInt();

    }

    public String getRoleIcon() {
        return ChatColor.YELLOW + getRole().toString().substring(0, 1) + ".";
    }
}
