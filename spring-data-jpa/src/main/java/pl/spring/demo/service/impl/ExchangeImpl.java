package pl.spring.demo.service.impl;

import java.util.HashSet;
import java.util.Set;

import pl.spring.demo.service.ExchangeInterface;
import pl.spring.demo.to.CurrencyTo;

public class ExchangeImpl implements ExchangeInterface{

	@Override
	public Set<CurrencyTo> askForPrices() {
		// TODO Auto-generated method stub
		return new HashSet<CurrencyTo>();
	}

	@Override
	public Set<CurrencyTo> exchange() {
		// TODO Auto-generated method stub
		return new HashSet<CurrencyTo>();
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}

}
