package pl.spring.demo.service.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import pl.spring.demo.service.ClientStrategy;
import pl.spring.demo.to.CurrencyTo;
import pl.spring.demo.to.ShareTo;
/**
 * Simple? strategy using mean and variation
 * You buy, when current price< mean - multiplier*variation
 * You split your money in equal portions on all companies when buying!
 * You sell always, when current price> mean+ multiplier*variation
 * @author JZMUDA
 *
 */
@Component
public class ClientStrategyImpl implements ClientStrategy{
/**
 * data on mean price value, price variation,
 * current prices and the number of days each
 * company is present on the market
 */
	private Map<String,Double> mean;
	private Map<String,Double> standardDeviation;
	private Map<String,Double> current;
	private Map<String,Integer> dayCount;
	private Integer currentDate;


	/**
	 * How much the price has to differ (in standard deviation units) from mean to buy shares?
	 */
	private double variationMultiplierBuy = 2.0;
	/**
	 * How much the price has to differ (in standard deviation units) from mean to sell shares?
	 */
	private double variationMultiplierSell = 2.0;
	
	/**
	 * what fraction of money are you willing to spend?
	 * Keep below 1.0, or you're in trouble
	 */
	private double singleBuyRatio = 0.9;
	/**
	 * the companies may appear or disappear
	 */
	
	public ClientStrategyImpl() {
	}


	@Override
	public Set<ShareTo> thinkBuyingShares(List<ShareTo> stockHistory, Map<String, Integer> possessedShares,
			double money) {
		init(stockHistory);
		return computeBuying(possessedShares, money);
	}

	/**
	 * Look into the wallet for money, 
	 * buy when the price is lower from mean by some fraction of standard deviation
	 * @param possessedShares
	 * @param money
	 * @return
	 */
	private Set<ShareTo> computeBuying(Map<String, Integer> possessedShares, double money) {
		Set<ShareTo> result=  new HashSet<ShareTo>();
		//are there any companies?
		if(current.keySet().size()>0) {
			double moneyToSpend = money*singleBuyRatio;
			for(String key:current.keySet()) {
				double currentPrice=current.get(key);
				if(currentPrice<mean.get(key)-variationMultiplierBuy*standardDeviation.get(key)) {
					int toBuy = (int) Math.round(moneyToSpend/currentPrice);
					if(toBuy>0) {
						result.add(new ShareTo(currentDate,key,currentPrice,toBuy));
						moneyToSpend-=currentPrice*toBuy;
					}
				}
			}
		}
		return result;
	}
/**
 * Sells all shares from company when the price is higher from...
 */
	@Override
	public Set<ShareTo> thinkSellingShares(List<ShareTo> stockHistory, Map<String, Integer> possessedShares, double money) {
		init(stockHistory);
		return computeSelling(possessedShares);
	}

	private Set<ShareTo> computeSelling(Map<String, Integer> possessedShares) {
		Set<ShareTo> result=  new HashSet<ShareTo>();
		//are there any shares?
		if(possessedShares.keySet().size()>0) {
			for(String key:current.keySet()) {
				double currentPrice=current.get(key);
				if(currentPrice>mean.get(key)+variationMultiplierSell*standardDeviation.get(key)) {
					if(possessedShares.containsKey(key)) {
						result.add(new ShareTo(currentDate,key,currentPrice,possessedShares.get(key)));
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * Left TODO, returns empty list
	 */

	@Override
	public Set<CurrencyTo> thinkExchanging(Set<CurrencyTo> pricesAndCurrencies,
			Map<String, Double> possessedCurrencies) {
		// TODO Auto-generated method stub
		return new HashSet<CurrencyTo>();
	}

	private void init(List<ShareTo> stockHistory) {
		this.mean=new HashMap<String,Double>();
		this.standardDeviation=new HashMap<String,Double>();
		this.current=new HashMap<String,Double>();
		this.dayCount=new HashMap<String,Integer>();
		computeMeans(stockHistory);
		computeStandardDeviation(stockHistory);
		computeCurrent(stockHistory);
	}


	/**
	 * Set up mean stock values for all companies plus the days on the market...
	 * @param stockHistory
	 */
	public void computeMeans(List<ShareTo> stockHistory) {
		//we do not know ad hoc, what are the companies. Map them!
		for(ShareTo share: stockHistory) {
			String key = share.getCompany();
			//we had this company already
			if(mean.containsKey(key)) {
				mean.put(key, mean.get(key)+share.getValue());
				dayCount.put(key, dayCount.get(key)+1);
			}
			else {
				mean.put(key,share.getValue());
				dayCount.put(key, 1);
			}

		}
		for(String key:mean.keySet())
			mean.put(key, mean.get(key)/dayCount.get(key));
	}
	/**
	 * Do NOT launch it before you set up mean values and day counts!!!!
	 * @param stockHistory
	 */
	public void computeStandardDeviation(List<ShareTo> stockHistory) {
		// TODO Auto-generated method stub
		for(ShareTo share: stockHistory) {
			String key = share.getCompany();
			//we had this company already
			if(standardDeviation.containsKey(key)) {
				standardDeviation.put(key,standardDeviation.get(key)+ Math.pow(mean.get(key)-share.getValue(), 2));
			}
			else {
				standardDeviation.put(key,Math.pow(mean.get(key)-share.getValue(), 2));
			}

		}
		for(String key:standardDeviation.keySet())
			standardDeviation.put(key, Math.sqrt(standardDeviation.get(key)/dayCount.get(key)));
	}
	/**
	 * Compute the map of CURRENT share prices (company may disappear)
	 * Again we use the fact of data sorting with respect to date!
	 * @param stockHistory
	 */
	private void computeCurrent(List<ShareTo> stockHistory) {
		int i = stockHistory.size()-1;
		ShareTo lastShare = stockHistory.get(i);
		currentDate=lastShare.getDate();
		current.put(lastShare.getCompany(), lastShare.getValue());
		i=i-1;
		while(currentDate.equals(stockHistory.get(i).getDate())) {
			current.put(stockHistory.get(i).getCompany(), stockHistory.get(i).getValue());
			i--;
		}
	}

	public Map<String, Double> getMean() {
		return mean;
	}
	public Map<String, Double> getStandardDeviation() {
		return standardDeviation;
	}

	public Map<String, Double> getCurrent() {
		return current;
	}
	
	public Integer getCurrentDate() {
		return currentDate;
	}

}
