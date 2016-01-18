package bank.internettoegang;

import bank.bankieren.IRekening;
import bank.bankieren.Money;
import fontys.observer.RemotePublisher;
import fontys.util.InvalidSessionException;
import fontys.util.NumberDoesntExistException;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IBankiersessie extends Remote, RemotePublisher{

	//TODO Change this back
	long GELDIGHEIDSDUUR = 60000000;
	/**
	 * @returns true als de laatste aanroep van getRekening of maakOver voor deze
	 *          sessie minder dan GELDIGHEIDSDUUR geleden is
	 *          en er geen communicatiestoornis in de tussentijd is opgetreden, 
	 *          anders false
	 */
	boolean isGeldig() throws RemoteException; 

	/**
	 * er wordt bedrag overgemaakt van de bankrekening met het nummer bron naar
	 * de bankrekening met nummer bestemming
	 *
	 * @param bestemming
	 *            is ongelijk aan rekeningnummer van deze bankiersessie
	 * @param bedrag
	 *            is groter dan 0
	 * @return <b>true</b> als de overmaking is gelukt, anders <b>false</b>
	 * @throws NumberDoesntExistException
	 *             als bestemming onbekend is
	 * @throws InvalidSessionException
	 *             als sessie niet meer geldig is 
	 */
	boolean maakOver(int bestemming, Money bedrag)
			throws NumberDoesntExistException, InvalidSessionException,
			RemoteException;

	/**
	 * sessie wordt beeindigd
	 */
	void logUit() throws RemoteException;

	void update() throws RemoteException;

	/**
	 * @return de rekeninggegevens die horen bij deze sessie
	 * @throws InvalidSessionException
	 *             als de sessieId niet geldig of verlopen is
	 * @throws RemoteException
	 */
	IRekening getRekening() throws InvalidSessionException, RemoteException;
}
