package pl.spring.demo.service;

import java.util.Set;

import pl.spring.demo.to.CurrencyTo;

public interface ExchangeInterface {
	public Set<CurrencyTo> askForPrices();
	public Set<CurrencyTo> exchange();
	public void tick();
}
