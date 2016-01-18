package bank.server;

import bank.bankieren.Bank;
import bank.bankieren.IBank;
import bank.bankieren.IRekening;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * Created by guushamm on 12-1-16.
 */
public class CentraleBank extends UnicastRemoteObject implements ICentraleBank {
	private ArrayList<IBank> banken;
	private int nextRekeningNR;

    public CentraleBank() throws RemoteException {
        //Constructor
        banken = new ArrayList<>();

		nextRekeningNR = 100000;
    }

    @Override
    public IRekening getIRekeningFromBank(IBank iBank,int rekeningNummer)    {
     	try {
            return iBank.getRekening(rekeningNummer);
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
            return null;
        }

    }

    public ArrayList<IBank> getBanken() {
        return banken;
    }

    public IBank getBank(String bankNaam){
        try {
            System.out.println("Banken size: " + banken.size());
            System.out.println("Banknaam: " + bankNaam);
            for (IBank iBank : banken){
                System.out.println("Bankname: " + iBank.getName());
                System.out.println("Comparing to: " + bankNaam);
                if (iBank.getName().matches(bankNaam)){
                    System.out.println("Found bank");
                    return iBank;
                }
            }
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        System.out.println("Returning null");
        return null;
	}

    @Override
    public void addBank(IBank bank) throws RemoteException
    {
        //Check if the bank already exists
        for (IBank b : banken) {
            if (b.getName().matches(bank.getName())) {
                //If the bank's name is found in the list return.
                return;
            }
        }

        banken.add(bank);
    }

    @Override
    public boolean voegTransactieToe(IRekening rekeningVan, int rekeningNaar, double bedrag)    {
        IRekening iRekeningNaar;
        try
        {
            for (IBank bank : banken)
            {
                if (bank.getRekening(rekeningNaar) != null)
                {
                    iRekeningNaar = bank.getRekening(rekeningNaar);
                }
            }
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }


        return false;
    }

    @Override
    public synchronized int getNextRekeningNummer()    {
        return nextRekeningNR++;
    }
}
