package net.betterpvp.clans.skills.selector;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.Role;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.events.SkillDequipEvent;
import net.betterpvp.clans.skills.events.SkillEquipEvent;
import net.betterpvp.clans.skills.mysql.BuildRepository;
import net.betterpvp.clans.skills.selector.button.SkillButton;
import net.betterpvp.clans.skills.selector.page.BuildPage;
import net.betterpvp.clans.skills.selector.page.ClassSelectionPage;
import net.betterpvp.clans.skills.selector.page.SkillPage;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.clans.skills.selector.skills.assassin.*;
import net.betterpvp.clans.skills.selector.skills.gladiator.*;
import net.betterpvp.clans.skills.selector.skills.global.BreakFall;
import net.betterpvp.clans.skills.selector.skills.global.FastRecovery;
import net.betterpvp.clans.skills.selector.skills.global.Swim;
import net.betterpvp.clans.skills.selector.skills.knight.*;
import net.betterpvp.clans.skills.selector.skills.paladin.*;
import net.betterpvp.clans.skills.selector.skills.ranger.*;
import net.betterpvp.core.client.Client;
import net.betterpvp.core.client.ClientUtilities;
import net.betterpvp.core.framework.BPVPListener;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.interfaces.events.ButtonClickEvent;
import net.betterpvp.core.utility.UtilItem;
import net.betterpvp.core.utility.UtilPlayer;
import net.betterpvp.core.utility.recharge.Recharge;
import net.betterpvp.core.utility.recharge.RechargeManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class SelectorManager extends BPVPListener<Clans> {
    private static HashMap<String, Skill> skills = new HashMap<>();

    public SelectorManager(Clans i) {
        super(i);
        loadSkills(i);


    }

    public void loadSkills(Clans i) {
        /*
         * Global Skills
         */
        skills.put("Break Fall", new BreakFall(i));
        skills.put("Swim", new Swim(i));
        skills.put("Fast Recovery", new FastRecovery(i));

        /*
         * Paladin Skills
         */


        skills.put("Pestilence", new Pestilence(i));
        skills.put("Lightning Orb", new LightningOrb(i));
        skills.put("Blizzard", new Blizzard(i));
        skills.put("Molten Shield", new MoltenShield(i));
        skills.put("Polymorph", new Polymorph(i));
        //skills.put("Fireball Thing", new FireballThing(i));
        skills.put("Glacial Prison", new GlacialPrison(i));
        //skills.put("Displacement", new Displacement(i));
        skills.put("Void", new net.betterpvp.clans.skills.selector.skills.paladin.Void(i));
        //skills.put("Repel", new Repel(i));
        skills.put("Rooting Axe", new RootingAxe(i));
        skills.put("Null Blade", new NullBlade(i));
        skills.put("Rupture", new Rupture(i));
        skills.put("Immolate", new Immolate(i));
        skills.put("Arctic Armour", new ArcticArmour(i));
        skills.put("Magma Blade", new MagmaBlade(i));
        skills.put("Cyclone", new Cyclone(i));
        skills.put("Defensive Aura", new DefenseAura(i));
        skills.put("Molten Blast", new MoltenBlast(i));
        skills.put("Holy Light", new HolyLight(i));
        skills.put("Inferno", new Inferno(i));
        skills.put("Swarm", new Swarm(i));
        /*
         * Assassin Skills
         */

        skills.put("Flash", new Flash(i));
        //skills.put("Mirage", new Mirage(i));
        skills.put("Silencing Strikes", new SilencingStrikes(i));
        skills.put("Concussion", new Concussion(i));
        skills.put("Marked for Death", new MarkedForDeath(i));
        skills.put("Shocking Strikes", new ShockingStrikes(i));
        skills.put("Silencing Arrow", new SilencingArrow(i));
        skills.put("Toxic Arrow", new ToxicArrow(i));
        skills.put("Sever", new Sever(i));
        skills.put("Blink", new Blink(i));
        skills.put("Repeated Strikes", new RepeatedStrikes(i));
        skills.put("Evade", new Evade(i));
        skills.put("Backstab", new Backstab(i));
        //skills.put("No Knockback", new Knockback(i));
        skills.put("Feather falling", new FeatherFalling(i));
        skills.put("Leap", new Leap(i));
        skills.put("Recall", new Recall(i));
        skills.put("Smoke Bomb", new SmokeBomb(i));

        /*
         * Knight Skills
         */

        skills.put("Thorns", new Thorns(i));
        skills.put("Deflection", new Deflection(i));
        skills.put("Swordsmanship", new Swordsmanship(i));
        skills.put("Power Chop", new PowerChop(i));
        skills.put("Cleave", new Cleave(i));
        skills.put("Hilt Smash", new HiltSmash(i));
        skills.put("Hold Position", new HoldPosition(i));
        skills.put("Sacrifice", new Sacrifice(i));
        skills.put("Fortitude", new Fortitude(i));
        skills.put("Fury", new Fury(i));
        skills.put("Bulls Charge", new BullsCharge(i));
        skills.put("Riposte", new OldRiposte(i));
        skills.put("Defensive Stance", new DefensiveStance(i));

        /*
         * Ranger Skills
         */

        skills.put("Pin Down", new PinDown(i));
        skills.put("Stunning Shot", new StunningShot(i));
        //skills.put("Scout", new Scout(i));
        skills.put("Overcharge", new Overcharge(i));
        skills.put("Vitality Spores", new VitalitySpores(i));
        skills.put("Wolfs Fury", new WolfsFury(i));
        skills.put("Roped Arrow", new RopedArrow(i));
        skills.put("Agility", new Agility(i));
        skills.put("Precision", new Precision(i));
        skills.put("Sharpshooter", new Sharpshooter(i));
        skills.put("Entangle", new Entangle(i));
        skills.put("Volley", new Volley(i));
        skills.put("Disengage", new Disengage(i));
        skills.put("Barrage", new Barrage(i));
        skills.put("Hunters Thrill", new HuntersThrill(i));
        skills.put("Incendiary Shot", new IncendiaryShot(i));
        skills.put("Longshot", new Longshot(i));

        /*
         * Gladiator Skills
         */

        skills.put("Flesh Hook", new FleshHook(i));
        skills.put("Takedown", new Takedown(i));
        skills.put("Crippling Blow", new CripplingBlow(i));
        skills.put("Threatening Shout", new ThreateningShout(i));
        skills.put("Strength in Numbers", new StrengthInNumbers(i));
        skills.put("Spirit of the Bear", new SpiritOfTheBear(i));
        skills.put("Spirit of the Wolf", new SpiritOfTheWolf(i));
        skills.put("Overwhelm", new Overwhelm(i));
        skills.put("Stampede", new Stampede(i));
        skills.put("Seismic Slam", new SeismicSlam(i));
        skills.put("Battle Taunt", new BattleTaunt(i));
        skills.put("Colossus", new Colossus(i));
        skills.put("Resistance", new Resistance(i));
        skills.put("Bloodlust", new Bloodlust(i));

    }

    public static ArrayList<Skill> getSkillsFor(String role) {
        ArrayList<Skill> temp = new ArrayList<>();
        for (Skill key : skills.values()) {
            if (key.getClassType().equalsIgnoreCase(role) || key.getClassType().equals("Global")) {
                temp.add(key);
            }
        }

        return temp;
    }


    public static HashMap<String, Skill> getSkills() {
        return skills;
    }

    @EventHandler
    public void onBuildPick(ButtonClickEvent e) {
        if (e.getMenu() instanceof ClassSelectionPage) {
            if (e.getButton() != null) {
                String role = ChatColor.stripColor(e.getButton().getName()).split(" ")[0];
                Player p = e.getPlayer();
                p.openInventory(new BuildPage(getInstance(), p, role).getInventory());
            }
        }
    }

    @EventHandler
    public void onClassPick(ButtonClickEvent e) {

        if (e.getMenu() instanceof BuildPage) {
            if (e.getButton() != null) {
                final Player p = e.getPlayer();
                final Gamer g = GamerManager.getOnlineGamer(p);

                if (e.getButton().getName().contains("Back")) {

                    p.playSound(p.getLocation(), Sound.NOTE_PLING, 1.0F, 2.0F);
                    p.openInventory(new ClassSelectionPage(p).getInventory());
                    return;
                }

                if (e.getButton().getName().contains(" - ")) {
                    final int build = Integer.valueOf(ChatColor.stripColor(e.getButton().getName()).split(" - ")[1]);
                    final String role = ChatColor.stripColor(e.getMenu().getTitle().split(" ")[0]);

                    if (e.getButton().getName().contains("Edit & Save Build")) {

                        p.openInventory(new SkillPage(g, g.getBuild(role, build)).getInventory());
                        p.playSound(p.getLocation(), Sound.NOTE_PLING, 1.0F, 2.0F);
                    } else if (e.getButton().getName().contains("Apply Build")) {
                        if (RechargeManager.getInstance().add(p, "Apply Build", 5, true)) {

                            RoleBuild b = g.getActiveBuild(role);
                            b.setActive(false);

                            BuildRepository.updateBuild(p.getUniqueId(), b);

                            RoleBuild active = g.getBuild(role, build);
                            active.setActive(true);


                            p.playSound(p.getLocation(), Sound.NOTE_PLING, 1.0F, 2.0F);
                            p.openInventory(new BuildPage(getInstance(), p, role).getInventory());


                            BuildRepository.updateBuild(p.getUniqueId(), active);


                        }
                    } else if (e.getButton().getName().contains("Delete Build")) {
                        if (RechargeManager.getInstance().add(p, "Delete Build", 5, true)) {
                            g.getBuild(role, build).deleteBuild();
                            p.playSound(p.getLocation(), Sound.ITEM_BREAK, 1.0F, 0.6F);
                            BuildRepository.updateBuild(p.getUniqueId(), g.getBuild(role, build));

                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onSkillEquip(final ButtonClickEvent event) {
        if (event.getMenu() instanceof SkillPage) {
            if (event.getButton() instanceof SkillButton) {
                SkillPage page = (SkillPage) event.getMenu();
                final SkillButton button = (SkillButton) event.getButton();
                final RoleBuild build = page.getBuild();

                if (build == null) return;
                if (button.getSkill() == null) return;

                if (event.getClickType() == ClickType.LEFT) {
                    if (build.getPoints() > 0) {

                        BuildSkill buildSkill = build.getBuildSkill(button.getSkill().getType());
                        if (buildSkill == null) {
                            build.setSkill(button.getSkill().getType(), new BuildSkill(button.getSkill(), 1));
                            build.takePoint();
                            Bukkit.getServer().getPluginManager().callEvent(new SkillEquipEvent(event.getPlayer(), button.getSkill()));
                            BuildRepository.updateBuild(event.getPlayer().getUniqueId(), build);

                        } else {
                            if (buildSkill.getLevel() < button.getSkill().getMaxLevel()) {
                                if (build.getBuildSkill(button.getSkill().getType()).getSkill() == button.getSkill()) {
                                    build.takePoint();
                                    build.setSkill(button.getSkill().getType(), button.getSkill(), buildSkill.getLevel() + 1);
                                    BuildRepository.updateBuild(event.getPlayer().getUniqueId(), build);
                                }
                            }

                        }
                        event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.NOTE_PLING, 1.0F, 2.0F);
                    } else {
                        event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ITEM_BREAK, 1.0F, 0.6F);
                    }
                } else if (event.getClickType() == ClickType.RIGHT) {
                    if (build.getPoints() < 12) {
                        if (button.getSkill().getType() == null) return;
                        if (build.getBuildSkill(button.getSkill().getType()) == null) return;
                        if (build.getBuildSkill(button.getSkill().getType()).getSkill() == button.getSkill()) {

                            build.setSkill(button.getSkill().getType(), new BuildSkill(button.getSkill(), build.getBuildSkill(button.getSkill().getType()).getLevel() - 1));
                            event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.NOTE_PLING, 1.0F, 2.0F);
                            build.addPoint();

                            if (build.getBuildSkill(button.getSkill().getType()).getLevel() == 0) {
                                build.setSkill(button.getSkill().getType(), null);
                                Bukkit.getPluginManager().callEvent(new SkillDequipEvent(event.getPlayer(), button.getSkill()));


                            }
                            BuildRepository.updateBuild(event.getPlayer().getUniqueId(), build);
                        }


                    } else {
                        event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ITEM_BREAK, 1.0F, 0.6F);
                    }
                }

                page.buildPage(button.getSkill().getClassType());
                page.construct();
            }
        }

    }

    @EventHandler
    public void onUpdate(UpdateEvent e) {

        if (e.getType() == UpdateEvent.UpdateType.TICK) {
            for (Player p : Bukkit.getOnlinePlayers()) {

                if (p.getItemInHand() != null) {
                    Role role = Role.getRole(p);
                    if (role != null) {
                        new BukkitRunnable() {
                            @Override
                            public void run() {

                                Gamer gamer = GamerManager.getOnlineGamer(p);



                                if(gamer != null){
                                if (gamer.getClient().getSettingAsBoolean("RechargeBar")) {
                                    RoleBuild b = gamer.getActiveBuild(role.getName());

                                    if (b != null) {
                                        if (UtilItem.isAxe(p.getItemInHand().getType())) {
                                            BuildSkill skill = b.getBuildSkill(Types.AXE);
                                            if (skill != null) {

                                                if (!display(p, skill)) {
                                                    showPassiveB(p, b);
                                                }
                                            } else {
                                                showPassiveB(p, b);
                                            }


                                        } else if (UtilItem.isSword(p.getItemInHand().getType())) {
                                            BuildSkill skill = b.getBuildSkill(Types.SWORD);
                                            if (skill != null) {
                                                if (!display(p, skill)) {
                                                    showPassiveB(p, b);
                                                }
                                            } else {
                                                showPassiveB(p, b);
                                            }

                                        } else if (p.getItemInHand().getType() == Material.BOW) {
                                            BuildSkill skill = b.getBuildSkill(Types.BOW);
                                            if (skill != null) {
                                                display(p, skill);
                                            }
                                        }
                                    }
                                }
                                }
                            }
                        }.runTaskAsynchronously(getInstance());

                    }

                    if (p.getItemInHand().getType() == Material.ENDER_PEARL) {
                        display(p, "Ethereal Pearl");
                    } else if (p.getItemInHand().getType() == Material.APPLE) {
                        display(p, "Energy Apple");
                    } else if (p.getItemInHand().getType() == Material.HARD_CLAY && p.getItemInHand().getData().getData() == (byte) 15) {
                        display(p, "Gravity Bomb");
                    } else if (p.getItemInHand().getType() == Material.MAGMA_CREAM) {
                        display(p, "Incendiary Grenade");
                    } else if (p.getItemInHand().getType() == Material.EXP_BOTTLE) {
                        display(p, "Molotov");
                    } else if (p.getItemInHand().getType() == Material.WEB) {
                        display(p, "Throwing Web");
                    }
                }
            }
        }
    }

    private boolean display(Player p, BuildSkill s) {

        if (s.getSkill().showRecharge()) {


            Recharge r = RechargeManager.getInstance().getAbilityRecharge(p.getName(), s.getSkill().getName());
            if (r != null) {

                int red = (int) Math.ceil(((r.getRemaining() / (r.getSeconds() / 1000)) * 100 / 10));
                int green = 10 - red;

                if (r.getRemaining() < 0.15) {
                    green = 10;
                }

                String msg = "";

                for (int i = 0; i < green; i++) {
                    msg += ChatColor.GREEN.toString() + ChatColor.BOLD + "\u2588";
                }

                if (green != 10) {

                    for (int i = 0; i < red; i++) {
                        msg += ChatColor.RED.toString() + ChatColor.BOLD + "\u2588";
                    }
                }

                UtilPlayer.sendActionBar(p, ChatColor.GOLD.toString() + ChatColor.BOLD + s.getSkill().getName() + " "
                        + ChatColor.YELLOW + ChatColor.BOLD + "[" + msg + ChatColor.YELLOW + ChatColor.BOLD + "]");
                return true;
            }
        }

        return false;
    }

    private void display(Player p, String recharge) {


        Recharge r = RechargeManager.getInstance().getAbilityRecharge(p.getName(), recharge);
        if (r != null) {

            int red = (int) Math.ceil(((r.getRemaining() / (r.getSeconds() / 1000)) * 100 / 10));
            int green = 10 - red;

            if (r.getRemaining() < 0.1) {
                green = 10;
            }

            String msg = "";

            for (int i = 0; i < green; i++) {
                msg += ChatColor.GREEN.toString() + ChatColor.BOLD + "\u2588";
            }

            if (green != 10) {
                for (int i = 0; i < red; i++) {
                    msg += ChatColor.RED.toString() + ChatColor.BOLD + "\u2588";
                }
            }

            UtilPlayer.sendActionBar(p, ChatColor.GOLD.toString() + ChatColor.BOLD + recharge + " "
                    + ChatColor.YELLOW + ChatColor.BOLD + "[" + msg + ChatColor.YELLOW + ChatColor.BOLD + "]");
        }
    }


    private void showPassiveB(Player p, RoleBuild b) {
        BuildSkill skill2 = b.getBuildSkill(Types.PASSIVE_B);
        if (skill2 != null) {
            if (Arrays.asList(skill2.getSkill().getMaterials()).contains(p.getItemInHand().getType())) {
                display(p, skill2);
            }

        }
    }

}