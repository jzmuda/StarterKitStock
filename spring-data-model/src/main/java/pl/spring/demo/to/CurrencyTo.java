package pl.spring.demo.to;

public class CurrencyTo {
	
	private String currency;
	private Double meanPrice;
	private Double variation;
	private Double currentPrice;
	private Double amount;
	
	public CurrencyTo(String currency, Double meanPrice, Double variation, Double currentPrice, Double amount) {
		super();
		this.currency = currency;
		this.meanPrice = meanPrice;
		this.variation = variation;
		this.currentPrice = currentPrice;
		this.amount = Math.round(amount * 100.0) / 100.0;
	}

	public String getCurrency() {
		return currency;
	}

	public Double getMeanPrice() {
		return meanPrice;
	}

	public Double getVariation() {
		return variation;
	}

	public Double getCurrentPrice() {
		return currentPrice;
	}

	public Double getAmount() {
		return amount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result + ((currency == null) ? 0 : currency.hashCode());
		result = prime * result + ((currentPrice == null) ? 0 : currentPrice.hashCode());
		result = prime * result + ((meanPrice == null) ? 0 : meanPrice.hashCode());
		result = prime * result + ((variation == null) ? 0 : variation.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof CurrencyTo)) {
			return false;
		}
		CurrencyTo other = (CurrencyTo) obj;
		if (amount == null) {
			if (other.amount != null) {
				return false;
			}
		} else if (!amount.equals(other.amount)) {
			return false;
		}
		if (currency == null) {
			if (other.currency != null) {
				return false;
			}
		} else if (!currency.equals(other.currency)) {
			return false;
		}
		if (currentPrice == null) {
			if (other.currentPrice != null) {
				return false;
			}
		} else if (!currentPrice.equals(other.currentPrice)) {
			return false;
		}
		if (meanPrice == null) {
			if (other.meanPrice != null) {
				return false;
			}
		} else if (!meanPrice.equals(other.meanPrice)) {
			return false;
		}
		if (variation == null) {
			if (other.variation != null) {
				return false;
			}
		} else if (!variation.equals(other.variation)) {
			return false;
		}
		return true;
	}

	

}
