package bank.bankieren;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Nekkyou on 12-1-2016.
 */
public interface ICentraleBank extends Remote
{
    IRekening getIRekeningFromBank(IBank bank, int rekeningNummer) throws RemoteException;

    ArrayList<IBank> getBanken() throws RemoteException;

    boolean voegTransactieToe(IRekening rekeningVan, int rekeningNaar, Money bedrag) throws RemoteException;

    int getNextRekeningNummer() throws RemoteException;

    IBank getBank(String bankNaam) throws RemoteException;
    IBank getBank(int rekeningNummer) throws RemoteException;

    void addBank(IBank bank) throws RemoteException;

    HashMap<IRekening, IBank> getIRekening(int rekeningNummer) throws RemoteException;
}
