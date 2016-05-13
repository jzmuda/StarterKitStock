package pl.spring.demo.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import pl.spring.demo.to.CurrencyTo;
import pl.spring.demo.to.ShareTo;



/**
 * @author JZMUDA
 * Strategy interface for client. Three decisions are to be made
 */
public interface ClientStrategy {
	public Set<ShareTo> thinkBuyingShares(List<ShareTo> stockHistory, Map<String,Integer> possessedShares, double money);
	public Set<ShareTo> thinkSellingShares(List<ShareTo> stockHistory, Map<String,Integer> possessedShares, double money);
	public Set<CurrencyTo> thinkExchanging(Set<CurrencyTo> pricesAndCurrencies, Map<String,Double> possessedCurrencies);

}
