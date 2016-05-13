package service;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import pl.spring.demo.service.impl.BankInterfaceImpl;
import pl.spring.demo.to.TransactionTo;

public class BankInterfaceImplementationTest {
	
	private static BankInterfaceImpl bankInterface;
	private static TransactionTo transactionOne = new TransactionTo("client", "broker", 12.34);
	private static TransactionTo transactionTwo = new TransactionTo("broker", "client", 12.34);
	private TransactionTo transactionBad = new TransactionTo("", "", -12.34);
	
	@BeforeClass
	public static void setUp() {
		bankInterface = new BankInterfaceImpl();
		bankInterface.postTransaction(transactionOne);
		bankInterface.postTransaction(transactionTwo);
	}
	
	@Test
	public void testShouldRecognizeBadTransaction() {
		// given
		boolean state;
		// when
		state=bankInterface.verifyTransaction(transactionBad);
		// then
		assertFalse(state);
	}
	
	@Test
	public void testShouldRecognizeGoodTransaction() {
		// given
		boolean state;
		// when
		state=bankInterface.verifyTransaction(transactionOne);
		// then
		assertTrue(state);
	}
	
	@Test(expected = Exception.class)
	public void testShouldThrowExceptionForBadTransactionPost() {
		// given
		// when
		bankInterface.postTransaction(transactionBad);
		// then
		fail("test should throw Exception");
	}
	
	@Test
	public void testShouldAcceptAndGetTransaction() {
		// given
		List<TransactionTo> one;
		List<TransactionTo> two;
		List<TransactionTo> emptyOne;
		List<TransactionTo> emptyTwo;
		// when
		one = bankInterface.getTransaction(transactionOne.getFrom(), transactionOne.getTo());
		two = bankInterface.getTransaction(transactionTwo.getFrom(), transactionTwo.getTo());
		emptyOne = bankInterface.getTransaction(transactionOne.getFrom(), transactionOne.getTo());
		emptyTwo = bankInterface.getTransaction(transactionTwo.getFrom(), transactionTwo.getTo());
		// then
		assertNotNull(one);
		assertTrue(one.contains(transactionOne));
		assertTrue(one.size()==1);
		assertNotNull(two);
		assertTrue(two.contains(transactionTwo));
		assertTrue(two.size()==1);
		assertNotNull(emptyOne);
		assertTrue(emptyOne.isEmpty());
		assertNotNull(emptyTwo);
		assertTrue(emptyTwo.isEmpty());
	}

}
