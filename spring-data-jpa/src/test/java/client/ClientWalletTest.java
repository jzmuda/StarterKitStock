package client;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import pl.spring.demo.client.ClientWallet;

public class ClientWalletTest {
	private static final Double eur=1500.00;
	private static final Double pln=10000.00;
	private static final Double fraction=0.3;
	private static final Double toGet=fraction*pln;
	private static final Double remaining=pln-fraction*pln;
	private final static Double toPut = 100.0;
	private final static Double afterPut = toPut+eur;

	private static final Integer kghm=100;
	private static final Integer ibm=120;
	private static final Integer sharesToGet=kghm-30;
	private static final Integer remainingShares=kghm-sharesToGet;
	private static final Integer sharesPut=10;
	private final static Integer sharesafterPut = sharesPut+kghm;

	private static ClientWallet clientWallet;
	private static Map<String,Double> initialMoney;
	private static Map<String,Integer> initialShares;

	@Before
	public void setUp() {
		initialMoney=new HashMap<String,Double>();
		initialMoney.put("PLN", pln);
		initialMoney.put("EUR", eur);
		initialShares=new HashMap<String,Integer>();
		initialShares.put("KGHM", kghm);
		clientWallet= new ClientWallet(initialMoney);
		clientWallet.putShares(initialShares);
	}

	@Test
	public void testShouldReturnInitialMoneyState() {
		//given
		Map<String,Double> money;
		//when
		money= clientWallet.checkMoney();
		//then
		assertEquals(initialMoney,money);
	}


	@Test
	public void testShouldGetMoneyAndReduceAmount() {
		//given
		Map<String,Double> moneyToGet=new HashMap<String,Double>();

		moneyToGet.put("PLN",toGet);
		//when
		Map<String,Double> money= clientWallet.getMoney(moneyToGet);
		//then
		assertEquals(toGet,money.get("PLN"));
		assertEquals(remaining,clientWallet.checkMoney().get("PLN"));
	}

	@Test
	public void testShouldNotGetMoreMoneyThanItHas() {
		//given
		Map<String,Double> moneyToGet=new HashMap<String,Double>();
		moneyToGet.put("PLN",10*pln);
		//when
		Map<String,Double> money= clientWallet.getMoney(moneyToGet);
		//then
		assertEquals(pln,money.get("PLN"));
		assertFalse(clientWallet.checkMoney().containsKey("PLN"));
	}

	@Test
	public void testShouldNotGetCurrencyThatWasNotThereInTheFirstPlace() {
		//given
		Map<String,Double> moneyToGet=new HashMap<String,Double>();
		moneyToGet.put("USD",10340.0);
		//when
		Map<String,Double> money= clientWallet.getMoney(moneyToGet);
		//then
		assertTrue(money.isEmpty());
	}


	@Test
	public void testShouldPutMoreMoney() {
		//given
		Map<String,Double> moneyToPut=new HashMap<String,Double>();
		moneyToPut.put("EUR",toPut);
		//when
		clientWallet.putMoney(moneyToPut);
		//then
		assertEquals(afterPut,clientWallet.checkMoney().get("EUR"));
	}

	@Test(expected = Exception.class)
	public void testShouldNotPutNegativeAmountOfMoney() {
		//given
		Map<String,Double> moneyToPut=new HashMap<String,Double>();
		moneyToPut.put("EUR",-toPut);
		//when
		clientWallet.putMoney(moneyToPut);
		//then
		fail("test should throw Exception");
	}
	
	@Test(expected = Exception.class)
	public void testShouldNotGetNegativeAmountOfMoney() {
		//given
		Map<String,Double> moneyToGet=new HashMap<String,Double>();
		moneyToGet.put("EUR",-toPut);
		//when
		clientWallet.getMoney(moneyToGet);
		//then
		fail("test should throw Exception");
	}
	@Test(expected = Exception.class)
	public void testShouldNotPutZeroMoney() {
		//given
		Map<String,Double> moneyToPut=new HashMap<String,Double>();
		moneyToPut.put("EUR",0.00);
		//when
		clientWallet.putMoney(moneyToPut);
		//then
		fail("test should throw Exception");
	}
	
	@Test(expected = Exception.class)
	public void testShouldNotGetZeroMoney() {
		//given
		Map<String,Double> moneyToGet=new HashMap<String,Double>();
		moneyToGet.put("EUR",0.00);
		//when
		clientWallet.getMoney(moneyToGet);
		//then
		fail("test should throw Exception");
	}

	@Test
	public void testShouldPutNewCurrency() {
		//given
		Map<String,Double> moneyToPut=new HashMap<String,Double>();
		moneyToPut.put("USD",toPut);
		//when
		clientWallet.putMoney(moneyToPut);
		//then
		assertEquals(toPut,clientWallet.checkMoney().get("USD"));
	}

	@Test
	public void testShouldCheckShares() {
		//given
		Map<String,Integer> shares;
		//when
		shares=clientWallet.checkShares();
		//then
		assertEquals(initialShares,shares);
	}

	@Test
	public void testShouldPutNewShares() {
		//given
		Map<String,Integer> sharesToPut=new HashMap<String,Integer>();
		sharesToPut.put("IBM",ibm);
		//when
		clientWallet.putShares(sharesToPut);
		//then
		assertTrue(clientWallet.checkShares().containsKey("IBM"));
		assertEquals(ibm,clientWallet.checkShares().get("IBM"));
	}

	@Test(expected = Exception.class)
	public void testShouldNotPutNegativeShares() {
		//given
		Map<String,Integer> sharesToPut=new HashMap<String,Integer>();
		sharesToPut.put("IBM",-ibm);
		//when
		clientWallet.putShares(sharesToPut);
		//then
		fail("test should throw Exception");
	}
	
	@Test(expected = Exception.class)
	public void testShouldNotGetNegativeShares() {
		//given
		Map<String,Integer> sharesToGet=new HashMap<String,Integer>();
		sharesToGet.put("KGHM",-kghm);
		//when
		clientWallet.getShares(sharesToGet);
		//then
		fail("test should throw Exception");
	}
	
	@Test(expected = Exception.class)
	public void testShouldNotPutZeroShares() {
		//given
		Map<String,Integer> sharesToPut=new HashMap<String,Integer>();
		sharesToPut.put("IBM",0);
		//when
		clientWallet.putShares(sharesToPut);
		//then
		fail("test should throw Exception");
	}
	
	@Test(expected = Exception.class)
	public void testShouldNotGetZeroShares() {
		//given
		Map<String,Integer> sharesToGet=new HashMap<String,Integer>();
		sharesToGet.put("KGHM",0);
		//when
		clientWallet.getShares(sharesToGet);
		//then
		fail("test should throw Exception");
	}

	@Test
	public void testShouldGetSharesAndReduceThemInWallet() {
		//given
		Map<String,Integer> sharesToGetFrom=new HashMap<String,Integer>();
		sharesToGetFrom.put("KGHM",sharesToGet);
		//when
		Map<String,Integer> obtainedShares=clientWallet.getShares(sharesToGetFrom);
		//then
		assertEquals(sharesToGet,obtainedShares.get("KGHM"));
		assertEquals(remainingShares, clientWallet.checkShares().get("KGHM"));
	}

	@Test
	public void testShouldNotGetMoreSharesThanItHas() {
		//given
		Map<String,Integer> sharesToGetFrom=new HashMap<String,Integer>();
		sharesToGetFrom.put("KGHM",10*kghm);
		//when
		Map<String,Integer> obtainedShares=clientWallet.getShares(sharesToGetFrom);
		//then
		assertEquals(kghm,obtainedShares.get("KGHM"));
		assertFalse(clientWallet.checkShares().containsKey("KGHM"));
	}

	@Test
	public void testShouldAddMoreShares() {
		//given
		Map<String,Integer> sharesToPuta=new HashMap<String,Integer>();
		sharesToPuta.put("KGHM",sharesPut);
		//when
		clientWallet.putShares(sharesToPuta);
		//then
		assertEquals(sharesafterPut,clientWallet.checkShares().get("KGHM"));
	}

	@Test
	public void testShouldNotGetShareItDidNotHaveInTheFirstPlace() {
		//given
		Map<String,Integer> sharesToGetFrom=new HashMap<String,Integer>();
		sharesToGetFrom.put("BBW",sharesToGet);
		//when
		Map<String,Integer> obtainedShares=clientWallet.getShares(sharesToGetFrom);
		//then
		assertTrue(obtainedShares.isEmpty());
	}

}
