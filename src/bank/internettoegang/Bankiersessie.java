package bank.internettoegang;

import bank.bankieren.IBank;
import bank.bankieren.IRekening;
import bank.bankieren.Money;
import fontys.observer.BasicPublisher;
import fontys.observer.RemotePropertyListener;
import fontys.util.InvalidSessionException;
import fontys.util.NumberDoesntExistException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Bankiersessie extends UnicastRemoteObject implements
		IBankiersessie {

	private static final long serialVersionUID = 1L;
	private long laatsteAanroep;
	private int reknr;
	private IBank bank;
	private BasicPublisher basicPublisher;

	public Bankiersessie(int reknr, IBank bank) throws RemoteException {
		laatsteAanroep = System.currentTimeMillis();
		this.reknr = reknr;
		this.bank = bank;

		String[] prop = new String[1];
		prop[0] = "RekeningSessie";
		basicPublisher = new BasicPublisher(prop);
	}


	public boolean isGeldig() {
		return System.currentTimeMillis() - laatsteAanroep < GELDIGHEIDSDUUR;
	}

	@Override
	public boolean maakOver(int bestemming, Money bedrag)
			throws NumberDoesntExistException, InvalidSessionException,
			RemoteException {
		
		updateLaatsteAanroep();
		
		if (reknr == bestemming)
			throw new RuntimeException(
					"source and destination must be different");
		if (!bedrag.isPositive())
			throw new RuntimeException("amount must be positive");

		boolean succes = bank.maakOver(reknr, bestemming, bedrag);

		basicPublisher.inform(this, "RekeningSessie", null, reknr);

		return succes;
	}

	private void updateLaatsteAanroep() throws InvalidSessionException {
		if (!isGeldig()) {
			throw new InvalidSessionException("session has been expired");
		}
		
		laatsteAanroep = System.currentTimeMillis();
	}

	@Override
	public void update() {
		basicPublisher.inform(this, "RekeningSessie", null, reknr);
	}

	@Override
	public IRekening getRekening() throws InvalidSessionException,
			RemoteException {

		updateLaatsteAanroep();
		
		return bank.getRekening(reknr);
	}

	@Override
	public void logUit() throws RemoteException {
		UnicastRemoteObject.unexportObject(this, true);
	}

	@Override
	public void addListener(RemotePropertyListener remotePropertyListener, String s) throws RemoteException
	{
		basicPublisher.addListener(remotePropertyListener, s);
	}

	@Override
	public void removeListener(RemotePropertyListener remotePropertyListener, String s) throws RemoteException
	{
		basicPublisher.removeListener(remotePropertyListener, s);
	}
}
