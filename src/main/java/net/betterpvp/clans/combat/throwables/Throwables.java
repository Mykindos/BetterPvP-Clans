package net.betterpvp.clans.combat.throwables;

import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;

public class Throwables {

    private Item item;
    private LivingEntity ent;
    private String skillName;
    private long expire;
    private List<LivingEntity> immune;
    private boolean checkHead;

    public Throwables(Item item, LivingEntity ent, String skillName, long expire) {
        this.item = item;
        this.ent = ent;
        this.skillName = skillName;
        this.expire = System.currentTimeMillis() + expire;
        item.setPickupDelay((int) System.currentTimeMillis());
        immune = new ArrayList<>();
    }

    public Item getItem() {
        return item;
    }

    public void setCheckHead(boolean bool) {
        this.checkHead = bool;
    }

    public boolean isCheckingHead() {
        return checkHead;
    }

    public List<LivingEntity> getImmunes() {
        return immune;
    }


    public LivingEntity getThrower() {
        return ent;
    }

    public String getSkillName() {
        return skillName;
    }

    public long getExpireTime() {
        return expire;
    }


}
