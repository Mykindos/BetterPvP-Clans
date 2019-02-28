package net.betterpvp.clans.clans.insurance;

import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.listeners.InsuranceListener;

import java.util.Collections;
import java.util.Comparator;

public class InsuranceManager {

    public static void startRollback(Clan c) {
        Collections.sort(c.getInsurance(), new Comparator<Insurance>() {

            @Override
            public int compare(Insurance a, Insurance b) {
                // TODO Auto-generated method stub
                return (int) b.getTime() - (int) a.getTime();
            }

        });

        for (Insurance i : c.getInsurance()) {
            InsuranceListener.rollback.add(i);
        }
    }

}
