package pl.spring.demo.to;

public class BrokerParams {
	
	int buySpread;
	int sellSpread;
	double buyPriceSpread;
	double sellPriceSpread;
	double revenueWeight;
	double revenueHeight;

	public BrokerParams(int buySpread, int sellSpread, double buyPriceSpread, double sellPriceSpread,
			double revenueWeight, double revenueHeight)
	{
	this.buySpread=buySpread;
	this.sellSpread=sellSpread;
	this.buyPriceSpread=buyPriceSpread;
	this.sellPriceSpread = sellPriceSpread;
	this.revenueWeight=revenueWeight;
	this.revenueHeight=revenueHeight;
	}

	public int getBuySpread() {
		return buySpread;
	}

	public int getSellSpread() {
		return sellSpread;
	}

	public double getBuyPriceSpread() {
		return buyPriceSpread;
	}

	public double getSellPriceSpread() {
		return sellPriceSpread;
	}

	public double getRevenueWeight() {
		return revenueWeight;
	}

	public double getRevenueHeight() {
		return revenueHeight;
	}
	
	
}
