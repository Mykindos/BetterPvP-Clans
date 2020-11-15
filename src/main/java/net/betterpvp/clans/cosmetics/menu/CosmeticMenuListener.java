package net.betterpvp.clans.cosmetics.menu;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.cosmetics.CosmeticManager;
import net.betterpvp.clans.cosmetics.menu.buttons.CosmeticButton;
import net.betterpvp.clans.cosmetics.menu.menus.*;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.core.framework.BPVPListener;
import net.betterpvp.core.interfaces.events.ButtonClickEvent;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;

public class CosmeticMenuListener extends BPVPListener<Clans> {

    public CosmeticMenuListener(Clans instance) {
        super(instance);
    }

    @EventHandler
    public void onClick(ButtonClickEvent e) {
        if (e.getMenu() instanceof CosmeticMenu) {
            if (e.getButton().getName().contains("Death Effects")) {
                e.getPlayer().openInventory(new DeathEffectMenu(e.getPlayer()).getInventory());
            }else if(e.getButton().getName().contains("Kill Effects")){
                e.getPlayer().openInventory(new KillEffectMenu(e.getPlayer()).getInventory());
            }else if(e.getButton().getName().contains("Particle Effects")){
                e.getPlayer().openInventory(new ParticleEffectMenu(e.getPlayer()).getInventory());
            }else if(e.getButton().getName().contains("Wings")){
                e.getPlayer().openInventory(new WingEffectMenu(e.getPlayer()).getInventory());
            }
        }else if(e.getMenu() instanceof CosmeticSubMenu){
            if(e.getClickType() == ClickType.LEFT) {
                CosmeticSubMenu menu = (CosmeticSubMenu) e.getMenu();

                CosmeticButton cosmetic = (CosmeticButton) e.getButton();
                Gamer gamer = GamerManager.getOnlineGamer(e.getPlayer());
                if (gamer != null) {
                    if (gamer.getClient().hasDonation(cosmetic.getCosmetic().getName())) {
                        CosmeticManager.activateCosmetic(e.getPlayer(), cosmetic.getCosmetic());
                        // TODO something around saving idk yet
                        menu.fillPage();
                    }else{
                        UtilMessage.message(e.getPlayer(), "Cosmetic", "You do not own this cosmetic!");
                        e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0F, 0.6F);
                    }
                }
            }else if(e.getClickType() == ClickType.RIGHT){
                CosmeticSubMenu menu = (CosmeticSubMenu) e.getMenu();

                CosmeticButton cosmetic = (CosmeticButton) e.getButton();
                if(cosmetic.getCosmetic().getActive().contains(e.getPlayer().getUniqueId())){
                    cosmetic.getCosmetic().getActive().remove(e.getPlayer().getUniqueId());

                    // TODO something around saving idk yet
                    menu.fillPage();
                }
            }

        }
    }
}
