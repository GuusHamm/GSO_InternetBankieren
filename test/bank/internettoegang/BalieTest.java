package bank.internettoegang;

import bank.bankieren.Bank;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;

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
		String id = balie.openRekening("Test", "Bladel", "123456");

		Assert.assertThat(id,not(""));
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
		String password = "123456";
		String accountName = balie.openRekening("Test", "Bladel", password);

		Assert.assertNotNull(balie.logIn(accountName,password));
	}

	@Test
	public void testLogInInvalidUsername() throws Exception {
		String password = "123456";
		String accountName = balie.openRekening("Test", "Bladel", password);

		Assert.assertNull(balie.logIn(accountName+"er",password));
	}

	@Test
	public void testLogInInvalidPassword() throws Exception {
		String password = "123456";
		String accountName = balie.openRekening("Test", "Bladel", password);

		Assert.assertNull(balie.logIn(accountName,"123457"));
	}
}