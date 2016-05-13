package service;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import pl.spring.demo.service.impl.TransactionValidatorImpl;
import pl.spring.demo.to.ShareTo;
import pl.spring.demo.to.TransactionTo;

public class TransactionValidatorImplTest {
	
	private TransactionValidatorImpl validator= new TransactionValidatorImpl();
	private final static TransactionTo clientToBrokerOne = new TransactionTo("client", "broker", 200.00);
	private final static TransactionTo clientToBrokerTwo = new TransactionTo("client", "broker", 200.01);
	private final static TransactionTo brokerToClientOne = new TransactionTo("broker", "client", 200.01);
	private final static ShareTo shareOne = new ShareTo(1234,"WWW",12.34,1234);
	private final static ShareTo shareTwo = new ShareTo(1235,"WWW",12.34,1234);
	private final static ShareTo shareThree = new ShareTo(1234,"WWW",12.34,1235);
	
	
	@Test
	public void testShouldNotThrowExceptionForTwoIdenticalShareSets() {
		//given
		Set<ShareTo> set1 = new HashSet<ShareTo>();
		set1.add(shareOne);
		set1.add(shareTwo);
		Set<ShareTo> set2 = new HashSet<ShareTo>();
		set2.add(shareTwo);
		set2.add(shareOne);
		//when
		try {
			validator.validateTransfer(set1, set2);
		}//then
		catch (Exception e) {
			fail("Share transfers are identical, no exceptions expected");
		}
	}
	
	@Test
	public void testShouldNotThrowExceptionForTwoIdenticalShareLists() {
		//given
		List<ShareTo> list1 = new ArrayList<ShareTo>();
		list1.add(shareOne);
		list1.add(shareTwo);
		List<ShareTo> list2 = new ArrayList<ShareTo>();
		list2.add(shareOne);
		list2.add(shareTwo);
		//when
		try {
			validator.validateTransfer(list1, list2);
		}//then
		catch (Exception e) {
			fail("Share transfers are identical, no exceptions expected");
		}
	}
	
	@Test(expected = Exception.class)
	public void testShouldThrowExceptionForTwoDifferentShareSets() {
		//given
		Set<ShareTo> set1 = new HashSet<ShareTo>();
		set1.add(shareOne);
		set1.add(shareTwo);
		Set<ShareTo> set2 = new HashSet<ShareTo>();
		set2.add(shareOne);
		set2.add(shareThree);
		//when
		validator.validateTransfer(set1, set2);
		//then
		fail("Share transfers are different, exception expected!");
	}
	
	@Test(expected = Exception.class)
	public void testShouldThrowExceptionForTwoDifferentShareLists() {
		//given
		List<ShareTo> list1 = new ArrayList<ShareTo>();
		list1.add(shareOne);
		list1.add(shareTwo);
		List<ShareTo> list2 = new ArrayList<ShareTo>();
		list2.add(shareOne);
		list2.add(shareThree);
		//when
		validator.validateTransfer(list1, list2);
		//then
		fail("Share transfers are different, exception expected!");
	}
	
	@Test
	public void testShouldNotThrowExceptionForTwoIdenticalTransactionSets() {
		//given
		Set<TransactionTo> set1 = new HashSet<TransactionTo>();
		set1.add(clientToBrokerOne);
		set1.add(clientToBrokerTwo);
		Set<TransactionTo> set2 = new HashSet<TransactionTo>();
		set2.add(clientToBrokerTwo);
		set2.add(clientToBrokerOne);
		//when
		try {
			validator.validateTransaction(set1, set2);
		}//then
		catch (Exception e) {
			fail("Transactions are identical, no exceptions expected");
		}
	}
	
	@Test
	public void testShouldNotThrowExceptionForTwoIdenticalTransactionLists() {
		//given
		List<TransactionTo> list1 = new ArrayList<TransactionTo>();
		list1.add(clientToBrokerOne);
		list1.add(clientToBrokerTwo);
		List<TransactionTo> list2 = new ArrayList<TransactionTo>();
		list2.add(clientToBrokerOne);
		list2.add(clientToBrokerTwo);
		//when
		try {
			validator.validateTransaction(list1, list2);
		}//then
		catch (Exception e) {
			fail("Transactions are identical, no exceptions expected");
		}
	}
	
	@Test(expected = Exception.class)
	public void testShouldThrowExceptionForTwoDifferentTransactionSets() {
		//given
		Set<TransactionTo> set1 = new HashSet<TransactionTo>();
		set1.add(clientToBrokerTwo);
		set1.add(clientToBrokerOne);
		Set<TransactionTo> set2 = new HashSet<TransactionTo>();
		set2.add(clientToBrokerOne);
		set2.add(brokerToClientOne);
		//when
		validator.validateTransaction(set1, set2);
		//then
		fail("Share transfers are different, exception expected!");
	}
	
	@Test(expected = Exception.class)
	public void testShouldThrowExceptionForTwoDifferentTransactionLists() {
		//given
		List<TransactionTo> list1 = new ArrayList<TransactionTo>();
		list1.add(clientToBrokerOne);
		list1.add(clientToBrokerTwo);
		List<TransactionTo> list2 = new ArrayList<TransactionTo>();
		list2.add(brokerToClientOne);
		list2.add(clientToBrokerTwo);
		//when
		validator.validateTransaction(list1, list2);
		//then
		fail("Share transfers are different, exception expected!");
	}

}
