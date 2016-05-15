package service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import entity.ShareEntity;
import pl.spring.demo.clock.DateClock;
import pl.spring.demo.dao.StockDao;
import pl.spring.demo.mapper.ShareMapper;
import pl.spring.demo.service.BankInterface;
import pl.spring.demo.service.BrokerInterface;
import pl.spring.demo.service.TransactionValidatorInterface;
import pl.spring.demo.to.ShareTo;
import pl.spring.demo.to.TransactionTo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "CommonServiceTest-context.xml")
public class BrokerInterfaceImplementationTest {
	@Autowired
	private BrokerInterface brokerInterface;
	@Autowired
	private DateClock clock;
	@Autowired
	private StockDao stockDao;
	@Autowired
	private BankInterface bankInterface;
	@Autowired
	private TransactionValidatorInterface transactionValidatorInterface;
	
	private final static ShareEntity shareEntityOne = new ShareEntity(20130131,"WWW",12.34);
	private final static ShareEntity shareEntityTwo = new ShareEntity(20130131,"XXX",12.6);
	private final static ShareTo shareToOne = ShareMapper.map(shareEntityOne);
	private final static ShareTo shareToTwo = ShareMapper.map(shareEntityTwo);
	private final static ShareTo shareToNegotiate1 = new ShareTo(20130131,"WWW",12.34,20);
	private final static ShareTo shareToNegotiate2 = new ShareTo(20130131,"XXX",12.34,20);	
	private final static ShareTo shareToNegotiate3 = new ShareTo(20130131,"YYY",12.35,3);
	private final static ShareEntity shareEntityNegotiate1 = ShareMapper.map(shareToNegotiate1);
	private final static ShareEntity shareEntityNegotiate2 = ShareMapper.map(shareToNegotiate2);	
	
	
	@Before
	public void setUp() {
		Mockito.reset(clock);
		Mockito.reset(stockDao);
		Mockito.reset(bankInterface);
		Mockito.reset(transactionValidatorInterface);
	}
	
	@Test
	public void testShouldReturnStockHistory() {
		//given
		final List<ShareTo> sharesToReturn = Arrays.asList(shareToOne,shareToTwo);
		final List<ShareEntity> shareEntties = Arrays.asList(shareEntityOne,shareEntityTwo);
		final int date=20130202;
		final int minDate=20130103;
		final int maxDate=20130503;
		Mockito.when(clock.getDate()).thenReturn(date);
		Mockito.when(stockDao.getMinDate()).thenReturn(minDate);
		Mockito.when(stockDao.getMaxDate()).thenReturn(maxDate);
		Mockito.when(stockDao.getStockHistory(date)).thenReturn(shareEntties);
		//when
		List<ShareTo> result = brokerInterface.getStockHistory();
		//then
		assertEquals(sharesToReturn,result);
	}
	
	@Test(expected = Exception.class)
	public void testShouldThrowExceptionFordateOutOfRange() {
		//given
		final int date=20130202;
		final int minDate=20130203;
		final int maxDate=20130303;
		Mockito.when(clock.getDate()).thenReturn(date);
		Mockito.when(stockDao.getMinDate()).thenReturn(minDate);
		Mockito.when(stockDao.getMaxDate()).thenReturn(maxDate);
		//when
		brokerInterface.getStockHistory();
		//then
		fail("Test should throw exception for date out of range");
	}
	
	@Test(expected = Exception.class)
	public void testShouldThrowExceptionFordateOutOfRangeCurrent() {
		//given
		final int date=20130202;
		final int minDate=20130203;
		final int maxDate=20130303;
		Mockito.when(clock.getDate()).thenReturn(date);
		Mockito.when(stockDao.getMinDate()).thenReturn(minDate);
		Mockito.when(stockDao.getMaxDate()).thenReturn(maxDate);
		//when
		brokerInterface.getCurrentStockInfo();
		//then
		fail("Test should throw exception for date out of range");
	}
	
	@Test
	public void testShouldNegotiateBuy() {
		//given
		final Set<ShareTo> sharesToNegotiate = new HashSet<ShareTo>();
		sharesToNegotiate.add(shareToNegotiate1);
		final List<ShareEntity> shareEntities = Arrays.asList(shareEntityNegotiate1,shareEntityNegotiate2);
		final int date=20130131;
		Mockito.when(clock.getDate()).thenReturn(date);
		Mockito.when(stockDao.getSharesAtDate(date)).thenReturn(shareEntities);
		//when
		Set<ShareTo> negotiated = brokerInterface.negotiateBuy(sharesToNegotiate);
		//then
		assertFalse(negotiated.isEmpty());
		assertEquals(negotiated.size(),sharesToNegotiate.size());
		for(ShareTo share:negotiated) {
			assertTrue(share.getValue()>0);
			assertTrue(share.getNumber()>0);
			assertTrue(share.getValue()>shareToNegotiate1.getValue());
			assertTrue(share.getNumber()<=shareToNegotiate1.getNumber());
		}
	}
	
	@Test(expected = Exception.class)
	public void testBuyNegotiationsShouldThrowErrorForNonexistentCompanyNegotiation() {
		//given
		final Set<ShareTo> sharesToNegotiate = new HashSet<ShareTo>();
		sharesToNegotiate.add(shareToNegotiate1);
		sharesToNegotiate.add(shareToNegotiate3);
		final List<ShareEntity> shareEntities = Arrays.asList(shareEntityNegotiate1,shareEntityNegotiate2);
		final int date=20130131;
		Mockito.when(clock.getDate()).thenReturn(date);
		Mockito.when(stockDao.getSharesAtDate(date)).thenReturn(shareEntities);
		//when
		brokerInterface.negotiateBuy(sharesToNegotiate);
		//then
		fail("Test should throw an exception for nonexistent company");
	}
	
	@Test(expected = Exception.class)
	public void testBuyNegotiationsShouldThrowErrorForNoSessionDay() {
		//given
		final Set<ShareTo> sharesToNegotiate = new HashSet<ShareTo>();
		sharesToNegotiate.add(shareToNegotiate1);
		sharesToNegotiate.add(shareToNegotiate3);
		List<ShareEntity> empty = new ArrayList<ShareEntity>();
		final int date=20130131;
		Mockito.when(clock.getDate()).thenReturn(date);
		Mockito.when(stockDao.getSharesAtDate(date)).thenReturn(empty);
		//when
		brokerInterface.negotiateBuy(sharesToNegotiate);
		//then
		fail("Test should throw an exception for No-Session day");
	}
	
	@Test
	public void testShouldNegotiateSell() {
		//given
		final Set<ShareTo> sharesToNegotiate = new HashSet<ShareTo>();
		sharesToNegotiate.add(shareToNegotiate1);
		final List<ShareEntity> shareEntities = Arrays.asList(shareEntityNegotiate1,shareEntityNegotiate2);
		final int date=20130131;
		Mockito.when(clock.getDate()).thenReturn(date);
		Mockito.when(stockDao.getSharesAtDate(date)).thenReturn(shareEntities);
		//when
		Set<ShareTo> negotiated = brokerInterface.negotiateSell(sharesToNegotiate);
		//then
		assertFalse(negotiated.isEmpty());
		assertEquals(negotiated.size(),sharesToNegotiate.size());
		for(ShareTo share:negotiated) {
			assertTrue(share.getValue()>0);
			assertTrue(share.getNumber()>0);
			assertTrue(share.getValue()<shareToNegotiate1.getValue());
			assertTrue(share.getNumber()<=shareToNegotiate1.getNumber());
		}
	}
	
	@Test(expected = Exception.class)
	public void testSellNegotiationsShouldThrowErrorForNonexistentCompanyNegotiation() {
		//given
		final Set<ShareTo> sharesToNegotiate = new HashSet<ShareTo>();
		sharesToNegotiate.add(shareToNegotiate1);
		sharesToNegotiate.add(shareToNegotiate3);
		final List<ShareEntity> shareEntities = Arrays.asList(shareEntityNegotiate1,shareEntityNegotiate2);
		final int date=20130131;
		Mockito.when(clock.getDate()).thenReturn(date);
		Mockito.when(stockDao.getSharesAtDate(date)).thenReturn(shareEntities);
		//when
		brokerInterface.negotiateSell(sharesToNegotiate);
		//then
		fail("Test should throw an exception for nonexistent company");
	}
	
	@Test(expected = Exception.class)
	public void testSellNegotiationsShouldThrowErrorForNoSessionDay() {
		//given
		final Set<ShareTo> sharesToNegotiate = new HashSet<ShareTo>();
		sharesToNegotiate.add(shareToNegotiate1);
		sharesToNegotiate.add(shareToNegotiate3);
		List<ShareEntity> empty = new ArrayList<ShareEntity>();
		final int date=20130131;
		Mockito.when(clock.getDate()).thenReturn(date);
		Mockito.when(stockDao.getSharesAtDate(date)).thenReturn(empty);
		//when
		brokerInterface.negotiateSell(sharesToNegotiate);
		//then
		fail("Test should throw an exception for No-Session day");
	}
		
	@Test
	public void testShouldNegotiateAndGetShares() {
		//given
		final Set<ShareTo> sharesToNegotiate = new HashSet<ShareTo>();
		sharesToNegotiate.add(shareToNegotiate1);
		sharesToNegotiate.add(shareToNegotiate2);
		final List<ShareEntity> shareEntities = Arrays.asList(shareEntityNegotiate1,shareEntityNegotiate2);
		final int date=20130131;
		Mockito.when(clock.getDate()).thenReturn(date);
		Mockito.when(stockDao.getSharesAtDate(date)).thenReturn(shareEntities);
		Set<ShareTo> negotiated = brokerInterface.negotiateBuy(sharesToNegotiate);
		TransactionTo payment = new TransactionTo("client", "broker", brokerInterface.getBrokersPrice());
		Mockito.when(bankInterface.getTransaction("client", "broker")).thenReturn(Arrays.asList(payment));
		Mockito.doNothing().when(transactionValidatorInterface).validateTransaction(Arrays.asList(payment),Arrays.asList( payment));
		//when
		Set<ShareTo> obtained= brokerInterface.getShares();
		//then
		assertEquals(negotiated, obtained);
	}
	
	@Test(expected = Exception.class)
	public void testShouldThrowErrorForWrongPayment() {
		//given
		final Set<ShareTo> sharesToNegotiate = new HashSet<ShareTo>();
		sharesToNegotiate.add(shareToNegotiate1);
		sharesToNegotiate.add(shareToNegotiate2);
		final List<ShareEntity> shareEntities = Arrays.asList(shareEntityNegotiate1,shareEntityNegotiate2);
		final int date=20130131;
		Mockito.when(clock.getDate()).thenReturn(date);
		Mockito.when(stockDao.getSharesAtDate(date)).thenReturn(shareEntities);
		brokerInterface.negotiateBuy(sharesToNegotiate);
		TransactionTo payment = new TransactionTo("client", "broker", brokerInterface.getBrokersPrice());
		TransactionTo underpayment = new TransactionTo("client", "broker", brokerInterface.getBrokersPrice()*0.9);
		Mockito.when(bankInterface.getTransaction("client", "broker")).thenReturn(Arrays.asList(underpayment));
		Mockito.doThrow(new IllegalStateException()).when(transactionValidatorInterface).validateTransaction(Arrays.asList(payment),Arrays.asList(underpayment));
		//when
		brokerInterface.getShares();
		//then
		fail("Broker should throw exception for failed transaction");
	}
	
	@Test
	public void testShouldNegotiateAndPostShares() {
		//given
		final Set<ShareTo> sharesToNegotiate = new HashSet<ShareTo>();
		sharesToNegotiate.add(shareToNegotiate1);
		sharesToNegotiate.add(shareToNegotiate2);
		final List<ShareEntity> shareEntities = Arrays.asList(shareEntityNegotiate1,shareEntityNegotiate2);
		final int date=20130131;
		Mockito.when(clock.getDate()).thenReturn(date);
		Mockito.when(stockDao.getSharesAtDate(date)).thenReturn(shareEntities);
		Set<ShareTo> negotiated = brokerInterface.negotiateSell(sharesToNegotiate);
		Mockito.doNothing().when(transactionValidatorInterface).validateTransfer(negotiated, negotiated);
		final TransactionTo brokersPayment = new TransactionTo("broker", "client", brokerInterface.getClientsIncome());
		Mockito.doNothing().when(bankInterface).postTransaction(brokersPayment);
		//when
		try {
			brokerInterface.postShares(negotiated);
			//then
			Mockito.verify(transactionValidatorInterface).validateTransfer(negotiated, negotiated);
			Mockito.verify(bankInterface).postTransaction(brokersPayment);
		} catch (Exception e) {
			
			fail("Broker should accept valid share transfer and post bank transaction");
		}
		
		
	}
	
	@Test
	public void testShouldThrowExceptionWhenPostUnnegotiatedShares() {
		//given
		final Set<ShareTo> sharesToNegotiate = new HashSet<ShareTo>();
		sharesToNegotiate.add(shareToNegotiate1);
		sharesToNegotiate.add(shareToNegotiate2);
		final List<ShareEntity> shareEntities = Arrays.asList(shareEntityNegotiate1,shareEntityNegotiate2);
		final int date=20130131;
		Mockito.when(clock.getDate()).thenReturn(date);
		Mockito.when(stockDao.getSharesAtDate(date)).thenReturn(shareEntities);
		Set<ShareTo> negotiated = brokerInterface.negotiateSell(sharesToNegotiate);
		Mockito.doThrow(new IllegalStateException()).when(transactionValidatorInterface).validateTransfer(negotiated, negotiated);
		Mockito.doNothing().when(bankInterface).postTransaction(new TransactionTo("broker", "client", brokerInterface.getClientsIncome()));
		boolean exceptionCaught=false;
		//when
		try {
			brokerInterface.postShares(negotiated);
		} catch (Exception e) {
			exceptionCaught=true;
		}		
		//then
		assertTrue(exceptionCaught);
		Mockito.verify(transactionValidatorInterface).validateTransfer(Mockito.anyObject(),Mockito.anyObject());
	}

}
