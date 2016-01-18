package bank.bankieren;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

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

	@Override
	public HashMap<IRekening, IBank> getIRekening(int rekeningnummer){
        IRekening rek;
        try
        {
            for (IBank b : getBanken())
            {
                if (b.getRekening(rekeningnummer) != null)
                {
					HashMap<IRekening,IBank> returnStuff = new HashMap<IRekening,IBank>();
					returnStuff.put(b.getRekening(rekeningnummer),b);
                    return returnStuff;
                }
            }
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
		return null;
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

	public IBank getBank(int rekeningnummer){
		HashMap<IRekening,IBank> result = getIRekening(rekeningnummer);

		for (IBank iBank : result.values()){
			return iBank;
		}
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
    public boolean voegTransactieToe(IRekening rekeningVan, int rekeningNaar, Money bedrag)    {
        IRekening iRekeningNaar = null;
        try
        {
            for (IBank bank : banken)
            {
                if (bank.getRekening(rekeningNaar) != null)
                {
                    iRekeningNaar = bank.getRekening(rekeningNaar);
					bank.muteer(iRekeningNaar,bedrag);
                    bank.getBalie().getSessie(rekeningNaar).update();
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
