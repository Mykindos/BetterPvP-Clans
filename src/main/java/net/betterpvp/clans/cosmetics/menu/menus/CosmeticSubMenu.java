package net.betterpvp.clans.cosmetics.menu.menus;

import net.betterpvp.clans.cosmetics.CosmeticManager;
import net.betterpvp.clans.cosmetics.CosmeticType;
import net.betterpvp.clans.cosmetics.menu.buttons.CosmeticButton;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.core.interfaces.Button;
import net.betterpvp.core.interfaces.Menu;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.concurrent.atomic.AtomicInteger;

public class CosmeticSubMenu extends Menu {

    private CosmeticType type;

    public CosmeticSubMenu(Player player, int size, String title, CosmeticType type) {
        super(player, size, title, new Button[]{});
        this.type = type;
        fillPage();
    }

    public void fillPage() {
        getButtons().clear();
        Gamer gamer = GamerManager.getOnlineGamer(getPlayer());

        AtomicInteger count = new AtomicInteger();
        CosmeticManager.getCosmeticsByType(type).forEach(cosmetic -> {
            boolean hasCosmetic = gamer.getClient().hasDonation(cosmetic.getName());
            boolean isActive = cosmetic.getActive().contains(getPlayer().getUniqueId());

            addButton(new CosmeticButton(cosmetic, count.intValue(), hasCosmetic
                    ? ChatColor.GREEN + cosmetic.getDisplayName() : ChatColor.RED + cosmetic.getDisplayName(), isActive));
            count.getAndIncrement();
        });

        construct();
    }
}
