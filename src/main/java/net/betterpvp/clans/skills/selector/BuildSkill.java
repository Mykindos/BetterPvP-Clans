package net.betterpvp.clans.skills.selector;

import net.betterpvp.clans.skills.selector.skills.Skill;

public class BuildSkill {

    private Skill skill;
    private int level;

    public BuildSkill(Skill skill, int level) {
        this.skill = skill;
        this.level = level;
    }

    public Skill getSkill() {
        return skill;
    }
    
    public String getString(){
    	return skill.getName() + " " + getLevel();
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
