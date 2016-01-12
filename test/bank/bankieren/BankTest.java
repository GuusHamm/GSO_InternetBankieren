package bank.bankieren;

import fontys.util.NumberDoesntExistException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 * Created by guushamm on 6-1-16.
 */
public class BankTest {

	private Bank bank;
	private Bank raboBank;
	private String name;
	private String place;

	@Before
	public void setUp() throws Exception {
		bank = new Bank("SNS");
		raboBank = new Bank("Rabobank");
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
	public void testMaakOver() throws Exception {
		int rekeningSource = bank.openRekening(name, place);
		int rekeningDestination = bank.openRekening("Tim Daniels", "Casteren");

		Money m = new Money(5000, "€");
		assertTrue(bank.maakOver(rekeningSource ,rekeningDestination, m));
	}

	@Test (expected = RuntimeException.class)
	public void testMaakOverInvalidCurrency() throws Exception {
		new Money(5000, "$");
	}

	@Test (expected = RuntimeException.class)
	public void testMaakOverSourceEqualsDestination() throws Exception {
		Money m = new Money(5000, "€");
		//Source and destination are the same
		bank.maakOver(1, 1, m);
	}

	@Test (expected = RuntimeException.class)
	public void testMaakOverNegativeAmount() throws Exception {
		Money m = new Money(-5000, "€");
		bank.maakOver(1, 2, m);
	}

	@Test (expected = NumberDoesntExistException.class)
	public void testMaakOverInvalidNumber() throws Exception {
		Money m = new Money(5000, "€");
		bank.maakOver(987654213, 123456789, m);
	}

	//	/**
//	 * @return de naam van deze bank
//	 */
	@Test
	public void testGetName() throws Exception {
		String bankNaam = bank.getName();
		String bankNaam2 = raboBank.getName();

		assertEquals("The names aren't equal", bankNaam, "SNS");
		assertEquals("The names aren't equal", bankNaam2, "Rabobank");
	}
}