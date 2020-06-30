package net.betterpvp.clans.donations;

import net.betterpvp.core.donation.DonationExpiryTimes;
import net.betterpvp.core.donation.IDonation;

public class ConveniencePackage implements IDonation {
    @Override
    public String getName() {
        return "ConveniencePackage";
    }

    @Override
    public String getDisplayName() {
        return "Convenience Package";
    }

    @Override
    public long getExpiryTime() {
        return DonationExpiryTimes.DAY * 60;
    }
}
