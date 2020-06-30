package net.betterpvp.clans.combat.combatlog;

import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.combat.combatlog.npc.NPC;
import net.betterpvp.clans.utilities.UtilClans;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CombatLog {

    public static List<CombatLog> loggers = new ArrayList<CombatLog>();

    private Player player;
    private NPC npc;
    private List<ItemStack> items;
    private Long time;

    public CombatLog(Player player) {
        this.player = player;
        this.npc = new NPC(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Right Click Me! " + ChatColor.GRAY + player.getName(), player.getLocation(), EntityType.SHEEP, 0.5, true);

        npc.getEntity().setRemoveWhenFarAway(false);
        npc.getEntity().setCustomNameVisible(true);
        this.items = new ArrayList<ItemStack>();




        if(UtilClans.hasValuables(player) || inEnemyLand()){
            this.time = System.currentTimeMillis() + System.currentTimeMillis();
        }else{
            this.time = System.currentTimeMillis() + 32000L;
        }


        for (ItemStack contents : player.getInventory().getContents()) {
            if(contents == null) continue;
            items.add(contents);
        }

        for (ItemStack armor : player.getInventory().getArmorContents()) {
            if(armor == null) continue;
            items.add(armor);
        }


        loggers.add(this);
    }

    public boolean inEnemyLand(){
        Clan c = ClanUtilities.getClan(getPlayer());
        Clan x = ClanUtilities.getClan(getPlayer().getLocation());
        if(c != null && x != null){
            if(c.isEnemy(x)){
                return true;
            }
        }
        return false;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public NPC getNPC() {
        return npc;
    }

    public void setNPC(NPC npc) {
        this.npc = npc;
    }

    public List<ItemStack> getItems() {
        return items;
    }

    public void setItemStack(List<ItemStack> items) {
        this.items = items;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public static CombatLog getCombatLog(Player player) {
        for (CombatLog log : loggers) {
            if (log.getPlayer().getUniqueId().equals(player.getUniqueId())) {
                return log;
            }
        }
        return null;
    }
}