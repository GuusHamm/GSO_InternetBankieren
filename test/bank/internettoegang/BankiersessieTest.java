package bank.internettoegang;

import bank.bankieren.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;

/**
 * Created by Nekkyou on 12-1-2016.
 */
public class BankiersessieTest  {
    private IBank iBank;
    private IKlant iKlant;
    private IKlant iKlant2;
    private IRekening iRekening;
    private IRekening iRekening2;
    private IBankiersessie iBankiersessie;
    private int rekeningNR;
    private int rekeningNR2;
    private String name;
	private Money money;

    @Before
    public void setUp() throws Exception    {
        name = "John Smith";
        String city = "Mississippi";

		money = new Money(2000,"€");

        iBank = new Bank("Rabobank");
        rekeningNR = iBank.openRekening(name,city);
        rekeningNR2 = iBank.openRekening("Jane Smith",city);

        iRekening = iBank.getRekening(rekeningNR);
        iRekening2= iBank.getRekening(rekeningNR2);

        iKlant = iRekening.getEigenaar();
        iKlant2 = iRekening2.getEigenaar();

        iBankiersessie = new Bankiersessie(rekeningNR,iBank);
    }

//    /**
//     * @returns true als de laatste aanroep van getRekening of maakOver voor deze
//     *          sessie minder dan GELDIGHEIDSDUUR geleden is
//     *          en er geen communicatiestoornis in de tussentijd is opgetreden,
//     *          anders false
//     */
    @Test
    public void testIsGeldig() throws Exception    {
        iBankiersessie = new Bankiersessie(rekeningNR,iBank);
		Assert.assertTrue(iBankiersessie.isGeldig());

		Thread.currentThread().sleep(10000);

		Assert.assertFalse(iBankiersessie.isGeldig());
    }

//	/**
//	 * er wordt bedrag overgemaakt van de bankrekening met het nummer bron naar
//	 * de bankrekening met nummer bestemming
//	 *
//	 * @param bestemming
//	 *            is ongelijk aan rekeningnummer van deze bankiersessie
//	 * @param bedrag
//	 *            is groter dan 0
//	 * @return <b>true</b> als de overmaking is gelukt, anders <b>false</b>
//	 * @throws NumberDoesntExistException
//	 *             als bestemming onbekend is
//	 * @throws InvalidSessionException
//	 *             als sessie niet meer geldig is
//	 */

    @Test
    public void testMaakOver() throws Exception    {
		long originalBalance = iRekening.getSaldo().getCents();
		long originalBalance2 = iRekening2.getSaldo().getCents();

		Assert.assertTrue(iBankiersessie.maakOver(rekeningNR2,money));

		long newBalance = iRekening.getSaldo().getCents();
		long newBalance2 = iRekening2.getSaldo().getCents();

		Assert.assertThat(originalBalance,not(newBalance));
		Assert.assertThat("Gaat fout", originalBalance2,not(newBalance2));
    }

	@Test (expected = RuntimeException.class)
	public void testMaakOverZelfdeRekeningNummer()	throws  Exception{
		iBankiersessie.maakOver(rekeningNR,money);
	}

	@Test (expected = RuntimeException.class)
	public void testMaakOverInvalidNumber()	throws  Exception{
		money = new Money(-1,"€");
		iBankiersessie.maakOver(rekeningNR,money);
	}

    @Test
    public void testGetRekening() throws Exception    {
		Assert.assertThat(iRekening.getEigenaar(),is(iBankiersessie.getRekening().getEigenaar()));
    }

    @Test
    public void testLogUit() throws Exception    {
		iBankiersessie.logUit();
    }
}