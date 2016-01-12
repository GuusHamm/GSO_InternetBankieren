package bank.server;

import bank.bankieren.IBank;
import bank.bankieren.IRekening;

import java.util.ArrayList;

/**
 * Created by Nekkyou on 12-1-2016.
 */
public interface ICentraleBank
{
    IRekening getIRekeningFromBank(IBank bank, int rekeningNummer);

    ArrayList<IBank> getBanken();

    boolean voegTransactieToe(IRekening rekeningVan, int rekeningNaar, double bedrag);

    int getNextRekeningNummer();

    IBank getBank(String bankNaam);
}
