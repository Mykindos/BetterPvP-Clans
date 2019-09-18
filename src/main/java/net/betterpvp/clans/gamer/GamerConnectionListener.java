package net.betterpvp.clans.gamer;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.scoreboard.Scoreboard;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.mysql.BuildRepository;
import net.betterpvp.clans.skills.selector.RoleBuild;
import net.betterpvp.clans.skills.selector.SelectorManager;
import net.betterpvp.core.client.listeners.ClientLoginEvent;
import net.betterpvp.core.client.listeners.ClientQuitEvent;
import net.betterpvp.core.framework.BPVPListener;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class GamerConnectionListener extends BPVPListener<Clans> {

    public GamerConnectionListener(Clans instance) {
        super(instance);
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onClientLogin(ClientLoginEvent e) {
        Gamer gamer = GamerManager.getGamer(e.getClient().getUUID());
        if (gamer == null) {
            gamer = new Gamer(e.getClient().getUUID());


            GamerRepository.saveGamer(gamer);
            GamerManager.getGamers().add(gamer);
            GamerManager.addOnlineGamer(gamer);

            loadDefaults(gamer);
        } else {

            GamerManager.addOnlineGamer(gamer);
            BuildRepository.loadBuilds(getInstance(), gamer.getUUID());
        }

        gamer.setScoreboard(new Scoreboard(e.getClient().getPlayer()));
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onClientQuit(ClientQuitEvent e){
        Gamer g = GamerManager.getOnlineGamer(e.getClient().getUUID());
        if(g != null){
            g.setScoreboard(null);
        }
    }

    private void loadDefaults(Gamer c) {


        for (int d = 1; d < 5; d++) {

            RoleBuild a = new RoleBuild("Assassin", d);
            if (d == 1) {
                a.setActive(true);

            }
            a.setSkill(Types.SWORD, SelectorManager.getSkills().get("Sever"), 3);
            a.setSkill(Types.AXE, SelectorManager.getSkills().get("Leap"), 5);
            a.setSkill(Types.PASSIVE_A, SelectorManager.getSkills().get("Backstab"), 1);
            a.setSkill(Types.PASSIVE_B, SelectorManager.getSkills().get("Smoke Bomb"), 3);
            a.takePoints(12);

            RoleBuild g = new RoleBuild("Gladiator", d);
            if (d == 1) {
                g.setActive(true);
            }
            g.setSkill(Types.SWORD, SelectorManager.getSkills().get("Battle Taunt"), 5);
            g.setSkill(Types.AXE, SelectorManager.getSkills().get("Seismic Slam"), 3);
            g.setSkill(Types.PASSIVE_A, SelectorManager.getSkills().get("Colossus"), 1);
            g.setSkill(Types.PASSIVE_B, SelectorManager.getSkills().get("Stampede"), 3);
            g.takePoints(12);

            RoleBuild r = new RoleBuild("Ranger", d);
            if (d == 1) {
                r.setActive(true);
            }
            r.setSkill(Types.SWORD, SelectorManager.getSkills().get("Disengage"), 3);
            r.setSkill(Types.BOW, SelectorManager.getSkills().get("Incendiary Shot"), 5);
            r.setSkill(Types.PASSIVE_A, SelectorManager.getSkills().get("Longshot"), 3);
            r.setSkill(Types.GLOBAL, SelectorManager.getSkills().get("Swim"), 1);
            r.takePoints(12);

            RoleBuild p = new RoleBuild("Paladin", d);
            if (d == 1) {
                p.setActive(true);
            }
            p.setSkill(Types.SWORD, SelectorManager.getSkills().get("Inferno"), 5);
            p.setSkill(Types.AXE, SelectorManager.getSkills().get("Molten Blast"), 3);
            p.setSkill(Types.PASSIVE_A, SelectorManager.getSkills().get("Holy Light"), 2);
            p.setSkill(Types.PASSIVE_B, SelectorManager.getSkills().get("Immolate"), 2);
            p.takePoints(12);

            RoleBuild k = new RoleBuild("Knight", d);
            if (d == 1) {
                k.setActive(true);
            }
            k.setSkill(Types.SWORD, SelectorManager.getSkills().get("Riposte"), 3);
            k.setSkill(Types.AXE, SelectorManager.getSkills().get("Bulls Charge"), 5);
            k.setSkill(Types.PASSIVE_A, SelectorManager.getSkills().get("Fury"), 3);
            k.setSkill(Types.GLOBAL, SelectorManager.getSkills().get("Swim"), 1);
            k.takePoints(12);


            c.getBuilds().add(k);
            c.getBuilds().add(r);
            c.getBuilds().add(g);
            c.getBuilds().add(p);
            c.getBuilds().add(a);

            BuildRepository.saveBuild(c.getUUID(), a);
            BuildRepository.saveBuild(c.getUUID(), p);
            BuildRepository.saveBuild(c.getUUID(), r);
            BuildRepository.saveBuild(c.getUUID(), g);
            BuildRepository.saveBuild(c.getUUID(), k);
        }
    }
}
