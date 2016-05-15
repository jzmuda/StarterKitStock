package pl.spring.demo.service.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import pl.spring.demo.service.ExchangeInterface;
import pl.spring.demo.to.CurrencyTo;
@Component
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

	//update prices, should be used by main controller
	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}

}
