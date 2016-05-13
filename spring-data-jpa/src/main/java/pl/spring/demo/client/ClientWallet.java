package pl.spring.demo.client;

import java.util.HashMap;
import java.util.Map;

public class ClientWallet {
	//wallet contents
	private Map<String,Integer> shares;
	private Map<String,Double> money;

	//constructor 
	public ClientWallet() {
		this.shares = new HashMap<String,Integer>();
		this.money = new HashMap<String,Double>();
	}

	//constructor with Map(currency,amount)
	public ClientWallet(Map<String,Double> initial) {
		this.shares = new HashMap<String,Integer>();
		this.money = new HashMap<String,Double>();
		putMoney(initial);
	}

	//round to 2 decimal places (we talk about normal money!)
	public Double round2(Double a){
		return Math.round(a * 100.0) / 100.0;
	}

	//put money methods. Can't put non-positive amount, always rounds up to 2 decimal
	public void putMoney(Map<String, Double> moneyToAdd) {
		for(String currency: moneyToAdd.keySet()) {
			double howMuch=round2(moneyToAdd.get(currency));
			if(howMuch>0) {
				if( money.containsKey(currency)) {
					money.put(currency, money.get(currency) + howMuch);
				}
				else
					money.put(currency,howMuch);
			}
			else throw new IllegalArgumentException("Illegal operation: put non-positive amount of money");
		}

	}
	//put money
	public void putMoney(String currency, Double amount) {
		amount=round2(amount);
		if(amount>0) {
			if( money.containsKey(currency)) {
				money.put(currency, money.get(currency) + amount);
			}
			else
				money.put(currency,amount);
		}
		else throw new IllegalArgumentException("Illegal operation: put non-positive amount of money");
	}

	//get money method. You can get only what you have, deletes currency if dropped to zero
	public Map<String,Double> getMoney(Map<String,Double> moneyToGet) {
		Map<String,Double> result = new HashMap<String,Double>();
		for(String currency: moneyToGet.keySet()) {
			if( money.containsKey(currency)) {
				Double howMuch = moneyToGet.get(currency);
				if(howMuch>0) {
					double diff=round2(money.get(currency) - moneyToGet.get(currency));
					if(diff>0) {
						money.put(currency,diff);
						result.put(currency, moneyToGet.get(currency));
					}
					else {
						result.put(currency,money.get(currency));
						money.remove(currency);
					}
				}
				else throw new IllegalArgumentException("Illegal operation: get non-positive amount of money");
			}
		}
		return result;
	}

	//how much of given currency we have
	public Double checkGivenCurrency(String currency) {
		double result=0;
		if (hasCurrency(currency)){
			result=money.get(currency);
		}
		return result;
	}

	//do we have the given currency
	public boolean hasCurrency(String currency) {
		return money.containsKey(currency);
	}

	//all currencies
	public Map<String, Double> checkMoney() {
		return money;
	}

	//put shares- only positive values!	
	public void putShares(Map<String, Integer> sharesToAdd) {
		for(String company: sharesToAdd.keySet()) {
			Integer howMany=sharesToAdd.get(company);
			if(howMany>0) {
				if( shares.containsKey(company)) {
					shares.put(company, shares.get(company) + howMany);
				}
				else
					shares.put(company,howMany);
			}
			else throw new IllegalArgumentException("Illegal operation: put non-positive amount of shares");
		}
	}

	//get shares method. You can get only what you have, deletes currency if dropped to zero
	public Map<String,Integer> getShares(Map<String,Integer> sharesToGet) {
		Map<String,Integer> result = new HashMap<String,Integer>();
		for(String company: sharesToGet.keySet()) {
			if( shares.containsKey(company)) {
				Integer howMany = sharesToGet.get(company);
				if(howMany>0) {
					Integer diff=shares.get(company) - howMany;
					if(diff>0) {
						shares.put(company, diff);
						result.put(company, howMany);
					}
					else {
						result.put(company,shares.get(company));
						shares.remove(company);
					}
				}
				else throw new IllegalArgumentException("Illegal operation: get non-positive amount of shares");
			}
		}
		return result;
	}
	//checks how much shares there are
	public Map<String,Integer> checkShares() {
		return shares;
	}


}
