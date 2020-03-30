package net.betterpvp.clans.fun;

import org.bukkit.entity.Item;
import org.bukkit.util.Vector;

public class BouncyBall {

    private Vector direction;
    private Item ball;
    public BouncyBall(Item ball, Vector direction) {
        this.direction = direction;
        this.ball = ball;
    }

    public void setDirection(Vector v) {
        this.direction = v;
    }

    public Vector getDirection() {
        return direction;
    }

    public Item getBall() {
        return ball;
    }

}