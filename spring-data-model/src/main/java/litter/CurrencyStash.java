package litter;

public class CurrencyStash {
	private String currency;
	private Double amount;
	
	public CurrencyStash(String currency, Double amount) {
		this.currency=currency;
		this.amount=amount;
	}
	
	public String getCurrency() {
		return currency;
	}
	
	public Double getAmount() {
		return amount;
	}
	
	public void modify(CurrencyStash other) {
		if(currency.equals(other.getCurrency()))
			amount+=other.amount;
		else throw new IllegalArgumentException("Currency type mismatch");
	}

}
