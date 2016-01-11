package bank.internettoegang;

import bank.bankieren.Bank;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by guushamm on 5-1-16.
 */
public class BalieTest extends TestCase {
	private Balie balie;
	private Bank bank;

	@Before
	public void setUp() throws Exception {
		bank = new Bank("SNS");
		balie = new Balie(bank);
	}

	@Test
	public void testOpenRekening() throws Exception {

	}

	@Test
	public void testOpenRekeningNaamNull() throws Exception {
		assertNull(balie.openRekening("", "Bladel", "123456"));
	}

	@Test
	public void testOpenRekeningPlaatsNull() throws Exception {
		assertNull(balie.openRekening("Test", "", "123456"));
	}

	@Test
	public void testOpenRekeningSmallPass() throws Exception {
		assertNull(balie.openRekening("Test", "Bladel", "abc"));
	}

	@Test
	public void testOpenRekeningBigPass() throws Exception {
		assertNull(balie.openRekening("Test", "Bladel", "abcdefghijk"));
	}

	@Test
	public void testLogIn() throws Exception {

	}
}