package pl.spring.demo.service;

import java.util.Set;

import pl.spring.demo.to.CurrencyTo;
/**
 * Exchange interface. Does two things:
 * provides current/mean/variation info on prices
 * and exchanges (depending on sign of currency ammounts either sells or buys
 * @author JZMUDA
 *
 */
public interface ExchangeInterface {
	public Set<CurrencyTo> askForPrices();
	public Set<CurrencyTo> exchange();
	public void tick();
}
