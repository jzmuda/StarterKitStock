package service;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import pl.spring.demo.service.impl.ClientStrategyImpl;
import pl.spring.demo.to.ShareTo;
/**
 * Quite strategy implementation-dependent one
 * Author knows how it works and
 * needs to know whether it works
 * @author JZMUDA
 *
 */
public class ClientStrategyImplTest {
	private ClientStrategyImpl strategy= new ClientStrategyImpl();
	private final String COMPANY1="XXX";
	private final String COMPANY2="YYY";
	private final ShareTo SHAREX = new ShareTo(11, COMPANY1, 4.0, 1);
	private final ShareTo SHAREY = new ShareTo(11, COMPANY2, 1.0, 1);
	private List<ShareTo> HISTORY1;
	private Map<String,Integer> POSSESSED1;
	private Double MONEY=1000.00;
	private final double MEAN11 = 1.5;
	private final double DEV11 = 0.5;
	private final double MEAN12 = 2.5;
	private final double DEV12 = 0.5;
	private final double EPS = 0.00001;
	private List<ShareTo> HISTORY2;

	@Before
	public void setup() {
		HISTORY1=new ArrayList<ShareTo>();
		for( int i =0; i<10 ;i++ ) {
			HISTORY1.add(new ShareTo(i+1, COMPANY1, 1.0 + i%2, 1));
			HISTORY1.add(new ShareTo(i+1, COMPANY2, 2.0 + (i+1)%2, 1));
		}
		HISTORY2=new ArrayList<ShareTo>();
		HISTORY2.addAll(HISTORY1);
		HISTORY2.add(SHAREX);
		HISTORY2.add(SHAREY);
		POSSESSED1=new HashMap<String,Integer>();
		POSSESSED1.put(COMPANY1,10);
	}

	@Test
	public void testShouldComputeMeanStockValues() {
		//given
		//when
		strategy.thinkBuyingShares(HISTORY1, POSSESSED1, MONEY);
		Map<String,Double> means = strategy.getMean();
		Map<String,Double> variations = strategy.getStandardDeviation();
		Map<String,Double> current = strategy.getCurrent();
		//then (it's double, so there is some precision problem)
		assertTrue(means.keySet().contains(COMPANY1));
		assertTrue(means.keySet().contains(COMPANY2));
		double meanDiff1=Math.abs(MEAN11-means.get(COMPANY1));
		assertTrue(meanDiff1<EPS);	
		double meanDiff2=Math.abs(MEAN12-means.get(COMPANY2));
		assertTrue(meanDiff2<EPS);
	}

	@Test
	public void testShouldComputeCurrentStockValues() {
		//given
		//when
		strategy.thinkBuyingShares(HISTORY1, POSSESSED1, MONEY);
		Map<String,Double> current = strategy.getCurrent();
		//then (it's double, so there is some precision problem)
		assertEquals(2,current.size());
		assertTrue(current.containsKey(COMPANY1));
		assertTrue(current.containsKey(COMPANY2));
		assertEquals(HISTORY1.get(HISTORY1.size()-1).getValue(),current.get(COMPANY2));
		assertEquals(HISTORY1.get(HISTORY1.size()-2).getValue(),current.get(COMPANY1));
	}

	@Test
	public void testShouldComputeStockValueVariation() {
		//given
		//when
		strategy.thinkBuyingShares(HISTORY1, POSSESSED1, MONEY);
		Map<String,Double> variations = strategy.getStandardDeviation();
		//then (it's double, so there is some precision problem)
		double varDiff1=Math.abs(DEV11-variations.get(COMPANY1));
		assertTrue(varDiff1<EPS);
		double varDiff2=Math.abs(DEV12-variations.get(COMPANY2));
		assertTrue(varDiff2<EPS);
	}
	
	@Test
	public void testShouldReturnSharesToBuyFromCompany2() {
		//given
		//when
		Set<ShareTo> result =strategy.thinkBuyingShares(HISTORY2, POSSESSED1, MONEY);
		//then
		assertFalse(result.isEmpty());
		for(ShareTo share:result) {
			assertEquals(COMPANY2, share.getCompany());
			assertTrue(share.getNumber()>1);
			assertEquals(COMPANY2,share.getCompany());
			assertEquals(strategy.getCurrentDate(), share.getDate());
		}
	}
	@Test
	public void testShouldSellSharesFromCompany1() {
		//given
		//when
		Set<ShareTo> result =strategy.thinkSellingShares(HISTORY2, POSSESSED1, MONEY);
		//then
		assertFalse(result.isEmpty());
		for(ShareTo share:result) {
			assertEquals(COMPANY1, share.getCompany());
			assertEquals(POSSESSED1.get(share.getCompany()),share.getNumber());
			assertEquals(strategy.getCurrentDate(), share.getDate());
		}
	}
}


