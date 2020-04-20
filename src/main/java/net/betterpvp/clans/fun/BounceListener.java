package net.betterpvp.clans.fun;

import net.betterpvp.clans.Clans;
import net.betterpvp.core.framework.BPVPListener;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.utility.UtilBlock;
import net.betterpvp.core.utility.UtilVelocity;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class BounceListener extends BPVPListener<Clans> {

    private List<BouncyBall> balls;

    public BounceListener(Clans instance) {
        super(instance);
        balls = new ArrayList<>();
    }

    @EventHandler
    public void onDropBounce(PlayerDropItemEvent e) {
        if(e.getItemDrop().getItemStack().getType() == Material.SLIME_BALL) {

            e.getItemDrop().setPickupDelay(Integer.MAX_VALUE);
            balls.add(new BouncyBall(e.getItemDrop(), e.getPlayer().getLocation().getDirection()));

        }
    }

    @EventHandler
    public void onDownball(PlayerInteractEvent e) {
        if(e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {

            BouncyBall b = balls.stream().filter(z -> z.getBall() != null
                    && z.getBall().getWorld().equals(e.getPlayer().getWorld()) && e.getPlayer().getLocation().distance(z.getBall().getLocation()) <= 2
                    || z.getBall() != null && e.getPlayer().getEyeLocation().distance(z.getBall().getLocation()) <= 2).findFirst().orElse(null);
            if(b != null) {

                b.getBall().setVelocity(e.getPlayer().getLocation().getDirection().multiply(1));
                b.setDirection(e.getPlayer().getLocation().getDirection());

            }
        }
    }

    @EventHandler
    public void onBounce(UpdateEvent e) {
        if(e.getType() == UpdateEvent.UpdateType.TICK) {

            ListIterator<BouncyBall> it = balls.listIterator();
            while(it.hasNext()) {
                BouncyBall b = it.next();

                if(b.getBall() == null || b.getBall().isDead()) {
                    it.remove();
                    continue;
                }

                if(UtilBlock.isGrounded(b.getBall())) {
                    UtilVelocity.velocity(b.getBall(), b.getDirection().clone(), 0.3, true, 0, 0.35, 1, false);
                }

                Vector newv = b.getDirection().clone();

                Location loc = b.getBall().getLocation();

                Location loc2 = new Location(b.getBall().getWorld(), loc.getX()
                        + newv.getX(), loc.getY() + newv.getY(), loc.getZ() + newv.getZ());
                if(!UtilBlock.airFoliage(loc2.getBlock())) {
                    BlockFace hitFace = loc.getBlock().getFace(loc2.getBlock());

                    if(hitFace != null) {

                        if(hitFace == BlockFace.NORTH) {
                            newv.setZ(0.2);
                        }else if(hitFace == BlockFace.SOUTH) {
                            newv.setZ(-0.2);
                        }else if(hitFace == BlockFace.EAST) {
                            newv.setX(-0.2);
                        }else if(hitFace == BlockFace.WEST) {
                            newv.setX(0.2);
                        }



                        b.setDirection(newv);

                    }
                }
            }

        }
    }

    @EventHandler
    public void onDownballPickup(PlayerPickupItemEvent e) {

        BouncyBall b = balls.stream().filter(z -> z.getBall() != null
                && z.getBall().getUniqueId().toString().equalsIgnoreCase(e.getItem().getUniqueId().toString()))
                .findFirst().orElse(null);
        if(b != null) {
            e.setCancelled(true);
        }
    }

}
