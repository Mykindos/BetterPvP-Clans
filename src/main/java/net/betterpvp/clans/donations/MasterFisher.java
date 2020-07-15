package net.betterpvp.clans.donations;

import net.betterpvp.core.donation.DonationExpiryTimes;
import net.betterpvp.core.donation.IDonation;

public class MasterFisher implements IDonation {
    @Override
    public String getName() {
        return "MasterFisher";
    }

    @Override
    public String getDisplayName() {
        return "Master Fisher";
    }

    @Override
    public long getExpiryTime() {
        return DonationExpiryTimes.DAY * 7 * 6;
    }
}
