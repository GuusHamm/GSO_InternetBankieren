package bank.bankieren;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 * Created by guushamm on 6-1-16.
 */
public class BankTest {

	private Bank bank;
	private String name;
	private String place;

	@Before
	public void setUp() throws Exception {
		bank = new Bank("SNS");
		name = "Jan Janssen";
		place = "Helmond";
	}

	//	/**
//	 * creatie van een nieuwe bankrekening met een identificerend rekeningnummer;
//	 * alleen als de klant, geidentificeerd door naam en place, nog niet bestaat
//	 * wordt er ook een nieuwe klant aangemaakt
//	 *
//	 * @param naam
//	 *            van de eigenaar van de nieuwe bankrekening
//	 * @param place
//	 *            de woonplaats van de eigenaar van de nieuwe bankrekening
//	 * @return -1 zodra naam of place een lege string en anders het nummer van de
//	 *         gecreeerde bankrekening
//	 */
	@Test
	public void testOpenRekening() throws Exception {
		int rekeningNR = bank.openRekening(name, place);

		assertThat(rekeningNR, not(-1));
	}

	@Test
	public void testOpenRekeningNoName() throws Exception {
		int rekeningNR = bank.openRekening("", place);

		assertThat(rekeningNR, is(-1));


	}

	@Test
	public void testOpenRekeningNoPlace() throws Exception {
		int rekeningNR = bank.openRekening(name, "");

		assertThat(rekeningNR, is(-1));
	}

	@Test
	public void testOpenRekeningNoNameAndPlace() throws Exception {
		int rekeningNR = bank.openRekening("", "");

		assertThat(rekeningNR, is(-1));
	}

	//	/**
//	 * er wordt bedrag overgemaakt van de bankrekening met nummer bron naar de
//	 * bankrekening met nummer bestemming, mits het afschrijven van het bedrag
//	 * van de rekening met nr bron niet lager wordt dan de kredietlimiet van deze
//	 * rekening
//	 *
//	 * @param bron
//	 * @param bestemming
//	 *            ongelijk aan bron
//	 * @param bedrag
//	 *            is groter dan 0
//	 * @return <b>true</b> als de overmaking is gelukt, anders <b>false</b>
//	 * @throws NumberDoesntExistException
//	 *             als een van de twee bankrekeningnummers onbekend is
//	 */
	@Test
	public void testGetRekening() throws Exception {

	}

	//	/**
//	 * @param nr
//	 * @return de bankrekening met nummer nr mits bij deze bank bekend, anders null
//	 */
	@Test
	public void testMaakOver() throws Exception {

	}

	//	/**
//	 * @return de naam van deze bank
//	 */
	@Test
	public void testGetName() throws Exception {

	}
}