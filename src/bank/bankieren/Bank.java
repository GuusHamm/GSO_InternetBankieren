package bank.bankieren;

import bank.internettoegang.IBalie;
import bank.internettoegang.IBankiersessie;
import fontys.util.NumberDoesntExistException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Bank extends UnicastRemoteObject implements IBank {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8728841131739353765L;
	private Map<Integer,IRekeningTbvBank> accounts;
	private Collection<IKlant> clients;
	private int nieuwReknr;
	private String name;
	private ICentraleBank centraleBank;
	private IBalie balie;

	public Bank(String name) throws RemoteException {
		accounts = new HashMap<Integer,IRekeningTbvBank>();
		clients = new ArrayList<IKlant>();
		nieuwReknr = 100000000;	
		this.name = name;	
	}

	public Bank(String name, ICentraleBank cb) throws RemoteException {
		accounts = new HashMap<Integer,IRekeningTbvBank>();
		clients = new ArrayList<IKlant>();
		this.name = name;
		nieuwReknr = 1000;
		if (name == "ING") {
			nieuwReknr = 2000;
		}
		else if (name == "SNS") {
			nieuwReknr = 3000;
		}
		else if (name == "ABN AMRO") {
			nieuwReknr = 4000;
		}
		else if (name == "ASN") {
			nieuwReknr = 5000;
		}

		centraleBank = cb;
	}

	public int openRekening(String name, String city) {
		if (name.equals("") || city.equals(""))
			return -1;

		IKlant klant = getKlant(name, city);
		IRekeningTbvBank account = new Rekening(nieuwReknr, klant, Money.EURO);
		accounts.put(nieuwReknr,account);
		nieuwReknr++;
		return nieuwReknr-1;
	}

	private IKlant getKlant(String name, String city) {
		for (IKlant k : clients) {
			if (k.getNaam().equals(name) && k.getPlaats().equals(city))
				return k;
		}
		IKlant klant = new Klant(name, city);
		clients.add(klant);
		return klant;
	}

	public IRekening getRekening(int nr) {
		return accounts.get(nr);
	}

	public boolean maakOver(int source, int destination, Money money)
			throws NumberDoesntExistException {
		if (source == destination)
			throw new RuntimeException(
					"cannot transfer money to your own account");
		if (!money.isPositive())
			throw new RuntimeException("money must be positive");

		IRekeningTbvBank source_account = (IRekeningTbvBank) getRekening(source);
		if (source_account == null)
			throw new NumberDoesntExistException("account " + source
					+ " unknown at " + name);

		Money negative = Money.difference(new Money(0, money.getCurrency()),
				money);
		boolean success = source_account.muteer(negative);
		if (!success)
			return false;



//		if (centraleBank != null){
//			try {
//				centraleBank.voegTransactieToe(source_account,destination,money);
//			} catch (RemoteException e) {
//				e.printStackTrace();
//			}
//			return true;
//		}

//		else{
//			dest_account = (IRekeningTbvBank) getRekening(destination);
//		}

		//Haal de rekening op
		IRekeningTbvBank dest_account = (IRekeningTbvBank) getRekening(destination);
		if (dest_account == null)
		{
			Bank bank = null;

			//Get the bank of the destination
			try
			{
				bank = (Bank)centraleBank.getBank(destination);
			}
			catch(RemoteException ex)
			{
				System.out.println("RemoteException: " + ex.getMessage());
			}

			//Check if the bank exists
			if(bank == null) {
				source_account.muteer(money);
				throw new NumberDoesntExistException("account " + source
															 + " unknown at every bank");
			}
			//If it exists, try to transfer the money
			else {
				if(!bank.maakOverAndereBank(destination, money)) {
					//If it failed, reset the money
					source_account.muteer(money);
					return false;
				}

				try
				{
					IBankiersessie ibs = bank.getBalie().getSessie(destination);
					if (ibs != null)
					{
						ibs.update();
					}
				}
				catch (RemoteException ex)
				{
					System.out.println("RemoteException: " + ex.getMessage());
				}

				return true;
			}

		}

		success = dest_account.muteer(money);

		if (!success) // rollback
			source_account.muteer(money);
		else
		{
			//balie.updateBankiersessie(destination, money);
			//balie.updateBankiersessie(source, money);

			try
			{
				IBankiersessie ibs = balie.getSessie(destination);
				if (ibs != null)
				{
					ibs.update();
				}
			}
			catch (RemoteException ex)
			{
				System.out.println("RemoteException: " + ex.getMessage());
			}
		}
		//Change the saldo of the opposite Account.
		success = dest_account.muteer(money);

		if (!success) // rollback
			source_account.muteer(money);
		return success;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public IBalie getBalie() throws RemoteException
	{
		return balie;
	}

	@Override
	public void setBalie(IBalie balie) throws RemoteException
	{
		this.balie = balie;
	}

	public boolean maakOverAndereBank(int destination, Money money){
		Money negative = Money.difference(new Money(0, money.getCurrency()),
										  money);
		boolean success;

		IRekeningTbvBank dest_account = (IRekeningTbvBank) getRekening(destination);

		success = dest_account.muteer(money);

		return success;
	}

	@Override
	public void muteer(IRekening iRekening, Money money){
		IRekeningTbvBank iRekeningTbvBank = (IRekeningTbvBank) iRekening;
		iRekeningTbvBank.muteer(money);
	}

}
