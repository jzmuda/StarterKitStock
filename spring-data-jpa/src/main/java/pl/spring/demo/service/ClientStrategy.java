package pl.spring.demo.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import pl.spring.demo.to.CurrencyTo;
import pl.spring.demo.to.ShareTo;



/**
 * @author JZMUDA
 * Strategy interface for client. Three decisions are to be made regarding shares and currency speculation
 * Share operations are divided into buy and sell phase due to the client-broker negotiation requirement
 * and intermediate bank transaction.
 * Currency exchange planning can be done in one step (negative number means sell, positive-buy)
 */
public interface ClientStrategy {
	public Set<ShareTo> thinkBuyingShares(List<ShareTo> stockHistory, Map<String,Integer> possessedShares, double money);
	public Set<ShareTo> thinkSellingShares(List<ShareTo> stockHistory, Map<String,Integer> possessedShares, double money);
	public Set<CurrencyTo> thinkExchanging(Set<CurrencyTo> pricesAndCurrencies, Map<String,Double> possessedCurrencies);

}
