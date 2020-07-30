package net.betterpvp.clans.clans.insurance;

import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.listeners.InsuranceListener;

import java.util.Collections;

public class InsuranceManager {

    public static void startRollback(Clan c) {
        c.getInsurance().sort((a, b) -> (int) b.getTime() - (int) a.getTime());
        InsuranceListener.rollback.addAll(c.getInsurance());
    }

}
