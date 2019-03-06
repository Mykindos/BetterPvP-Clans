package net.betterpvp.clans.economy;


import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;
import org.bukkit.OfflinePlayer;

import java.util.List;

/**
 * Vault implementation
 */
public class BetterPvP_Economy implements Economy {

    @Override
    public EconomyResponse bankBalance(String arg0) {

        return null;
    }

    @Override
    public EconomyResponse bankDeposit(String arg0, double arg1) {

        return null;
    }

    @Override
    public EconomyResponse bankHas(String arg0, double arg1) {

        return null;
    }

    @Override
    public EconomyResponse bankWithdraw(String arg0, double arg1) {

        return null;
    }

    @Override
    public EconomyResponse createBank(String arg0, String arg1) {

        return null;
    }

    @Override
    public EconomyResponse createBank(String arg0, OfflinePlayer arg1) {

        return null;
    }

    @Override
    public boolean createPlayerAccount(String arg0) {

        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer arg0) {

        return false;
    }

    @Override
    public boolean createPlayerAccount(String arg0, String arg1) {

        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer arg0, String arg1) {

        return false;
    }

    @Override
    public String currencyNamePlural() {

        return null;
    }

    @Override
    public String currencyNameSingular() {

        return null;
    }

    @Override
    public EconomyResponse deleteBank(String arg0) {

        return null;
    }

    @Override
    public EconomyResponse depositPlayer(String arg0, double arg1) {
        Gamer gamer = GamerManager.getGamer(arg0);

        if (gamer != null) {
            gamer.addCoins(arg1);
            return new EconomyResponse(arg1, gamer.getCoins(), ResponseType.SUCCESS, "");
        }
        return new EconomyResponse(0, 0, ResponseType.FAILURE, "Could not locate client");
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer arg0, double arg1) {

        return depositPlayer(arg0.getName(), arg1);
    }

    @Override
    public EconomyResponse depositPlayer(String arg0, String arg1,
                                         double arg2) {

        return depositPlayer(arg0, arg2);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer arg0, String arg1,
                                         double arg2) {

        return depositPlayer(arg0, arg2);
    }

    @Override
    public String format(double arg0) {

        return null;
    }

    @Override
    public int fractionalDigits() {

        return 0;
    }

    @Override
    public double getBalance(String arg0) {
        Gamer gamer = GamerManager.getGamer(arg0);
        if (gamer != null) {
            return gamer.getCoins();
        }
        return 0;
    }

    @Override
    public double getBalance(OfflinePlayer arg0) {
        return getBalance(arg0.getName());
    }

    @Override
    public double getBalance(String arg0, String arg1) {

        return getBalance(arg0);
    }

    @Override
    public double getBalance(OfflinePlayer arg0, String arg1) {

        return getBalance(arg0.getName(), "world");
    }

    @Override
    public List<String> getBanks() {

        return null;
    }

    @Override
    public String getName() {

        return "Coins";
    }

    @Override
    public boolean has(String arg0, double arg1) {
        Gamer gamer = GamerManager.getGamer(arg0);
        if (gamer != null) {
            return gamer.hasCoins((int) arg1);
        }

        return false;

    }

    @Override
    public boolean has(OfflinePlayer arg0, double arg1) {

        return has(arg0.getName(), arg1);
    }

    @Override
    public boolean has(String arg0, String arg1, double arg2) {

        return has(arg0, arg2);
    }

    @Override
    public boolean has(OfflinePlayer arg0, String arg1, double arg2) {

        return has(arg0.getName(), arg2);
    }

    @Override
    public boolean hasAccount(String arg0) {
        Gamer gamer = GamerManager.getOnlineGamer(arg0);

        return gamer != null;
    }

    @Override
    public boolean hasAccount(OfflinePlayer arg0) {

        return hasAccount(arg0.getName());
    }

    @Override
    public boolean hasAccount(String arg0, String arg1) {

        return hasAccount(arg0);
    }

    @Override
    public boolean hasAccount(OfflinePlayer arg0, String arg1) {

        return hasAccount(arg0.getName());
    }

    @Override
    public boolean hasBankSupport() {

        return false;
    }

    @Override
    public EconomyResponse isBankMember(String arg0, String arg1) {

        return null;
    }

    @Override
    public EconomyResponse isBankMember(String arg0, OfflinePlayer arg1) {

        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String arg0, String arg1) {

        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String arg0, OfflinePlayer arg1) {

        return null;
    }

    @Override
    public boolean isEnabled() {

        return true;
    }

    @Override
    public EconomyResponse withdrawPlayer(String arg0, double arg1) {
        Gamer gamer = GamerManager.getGamer(arg0);
        if (gamer != null) {
            gamer.removeCoins(arg1);
            return new EconomyResponse(arg1, gamer.getCoins(), ResponseType.SUCCESS, "");
        }
        return new EconomyResponse(0, 0, ResponseType.FAILURE, "Could not locate client");
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer arg0, double arg1) {

        return withdrawPlayer(arg0.getName(), arg1);
    }

    @Override
    public EconomyResponse withdrawPlayer(String arg0, String arg1,
                                          double arg2) {

        return withdrawPlayer(arg0, arg2);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer arg0, String arg1,
                                          double arg2) {

        return withdrawPlayer(arg0.getName(), arg2);
    }

}
