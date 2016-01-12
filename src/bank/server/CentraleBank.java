package bank.server;

import bank.bankieren.Bank;
import bank.bankieren.IBank;
import bank.bankieren.IRekening;

import java.util.ArrayList;

/**
 * Created by guushamm on 12-1-16.
 */
public class CentraleBank implements ICentraleBank {
	private ArrayList<IBank> banken;
	private int nextRekeningNR;

    public CentraleBank() {
        //Constructor
		banken.add(new Bank("SNS"));
		banken.add(new Bank("RaboBank"));
		banken.add(new Bank("ING"));
		nextRekeningNR = 100000;
    }

    @Override
    public IRekening getIRekeningFromBank(IBank iBank,int rekeningNummer)    {
     	return iBank.getRekening(rekeningNummer);
    }

    public ArrayList<IBank> getBanken() {
        return banken;
    }

    public IBank getBank(String bankNaam){
		for (IBank iBank : banken){
			if (iBank.getName() == bankNaam){
				return iBank;
			}
		}
		return null;
	}

    @Override
    public boolean voegTransactieToe(IRekening rekeningVan, int rekeningNaar, double bedrag)    {
        IRekening iRekeningNaar;
        for (IBank bank : banken) {
            if (bank.getRekening(rekeningNaar) != null) {
                iRekeningNaar = bank.getRekening(rekeningNaar);
            }
        }



        return false;
    }

    @Override
    public synchronized int getNextRekeningNummer()    {
        return nextRekeningNR++;
    }
}