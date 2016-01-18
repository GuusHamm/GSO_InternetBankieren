package bank.internettoegang;

import bank.bankieren.IBank;
import fontys.observer.BasicPublisher;
import fontys.observer.RemotePropertyListener;
import fontys.util.InvalidSessionException;

import java.beans.PropertyChangeEvent;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class Balie extends UnicastRemoteObject implements IBalie {

	private static final long serialVersionUID = -4194975069137290780L;
	private IBank bank;
	private HashMap<String, ILoginAccount> loginaccounts;
	private Collection<IBankiersessie> sessions;
	private java.util.Random random;

	//Basic publisher
	private BasicPublisher basicPublisher;

	public Balie(IBank bank) throws RemoteException {
		this.bank = bank;
		loginaccounts = new HashMap<String, ILoginAccount>();
		sessions = new HashSet<IBankiersessie>();
		random = new Random();

		//Basic publisher stuff
		String[] props = new String[1];
		props[0] = "Rekening";
		basicPublisher = new BasicPublisher(props);
	}

	public String openRekening(String naam, String plaats, String wachtwoord) {
		if (naam.equals(""))
			return null;
		if (plaats.equals(""))
			return null;

		if (wachtwoord.length() < 4 ||	 wachtwoord.length() > 8)
			return null;

		int nr = 0;
		try {
			nr = bank.openRekening(naam, plaats);
		}
		catch (RemoteException e)
		{
			e.printStackTrace();
		}

		//-1 will only occur when name or city is empty, which is already caught in the lines above.
//		if (nr == -1)
//			return null;

		String accountname = generateId(8);
		while (loginaccounts.containsKey(accountname))
			accountname = generateId(8);
		loginaccounts.put(accountname, new LoginAccount(accountname,
				wachtwoord, nr));

		return accountname;
	}

	public IBankiersessie logIn(String accountnaam, String wachtwoord)
			throws RemoteException {
		ILoginAccount loginaccount = loginaccounts.get(accountnaam);
		if (loginaccount == null)
			return null;
		if (loginaccount.checkWachtwoord(wachtwoord)) {
			IBankiersessie sessie = new Bankiersessie(loginaccount
					.getReknr(), bank);

			sessions.add(sessie);
		 	sessie.addListener(this, "RekeningSessie");
			return sessie;
		}
		else return null;
	}

	private static final String CHARS = "abcdefghijklmnopqrstuvwxyz0123456789";

	private String generateId(int x) {
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < x; i++) {
			int rand = random.nextInt(36);
			s.append(CHARS.charAt(rand));
		}
		return s.toString();
	}

	@Override
	public IBankiersessie getSessie(int rekeningNr) {
		for (IBankiersessie iBankiersessie : sessions) {
			try
			{
				if (iBankiersessie.getRekening().getNr() == rekeningNr) {
                    return iBankiersessie;
                }
			}
			catch (InvalidSessionException e)
			{
				e.printStackTrace();
			}
			catch (RemoteException e)
			{
				e.printStackTrace();
			}
		}
		return null;
	}


	@Override
	public void propertyChange(PropertyChangeEvent propertyChangeEvent) throws RemoteException
	{
		basicPublisher.inform(this, "Rekening", null, propertyChangeEvent.getNewValue());
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
