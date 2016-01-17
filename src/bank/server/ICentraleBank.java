package bank.server;

import bank.bankieren.IBank;
import bank.bankieren.IRekening;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by Nekkyou on 12-1-2016.
 */
public interface ICentraleBank extends Remote
{
    IRekening getIRekeningFromBank(IBank bank, int rekeningNummer) throws RemoteException;

    ArrayList<IBank> getBanken() throws RemoteException;

    boolean voegTransactieToe(IRekening rekeningVan, int rekeningNaar, double bedrag) throws RemoteException;

    int getNextRekeningNummer() throws RemoteException;

    IBank getBank(String bankNaam) throws RemoteException;
}
