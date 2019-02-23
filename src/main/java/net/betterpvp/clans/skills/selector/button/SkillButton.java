package net.betterpvp.clans.skills.selector.button;

import net.betterpvp.core.interfaces.Button;
import net.betterpvp.clans.skills.selector.skills.Skill;

public class SkillButton extends Button{

    private Button button;
    private Skill skill;
    public SkillButton(Button button, Skill s) {
        super(button.getSlot(), button.getItemStack(), button.getName(), button.getLore());
        
        this.button = button;
        this.skill = s;
     
    }
    
    public Skill getSkill(){
    	return skill;
    }
    
    public Button getButton() {
        return button;
    }
    
    public void setButton(Button button) {
        this.button = button;
    }
    
   
    
}