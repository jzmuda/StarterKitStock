package pl.spring.demo.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import pl.spring.demo.clock.DateClock;
import pl.spring.demo.dao.StockDao;
import pl.spring.demo.mapper.ShareMapper;
import pl.spring.demo.service.BankInterface;
import pl.spring.demo.service.BrokerInterface;
import pl.spring.demo.service.TransactionValidatorInterface;
import pl.spring.demo.to.ShareTo;
import pl.spring.demo.to.TransactionTo;

public class BrokerInterfaceImpl implements BrokerInterface{
	
	@Autowired
	private StockDao stockDao;
	
	@Autowired
	private DateClock clock;
	
	@Autowired
	private BankInterface bankInterface;
	
	@Autowired
	private TransactionValidatorInterface transactionValidatorInterface;
	
	
	
	/*
	 * buy/sell random price and amount spreads in% (uniform random distributions)
	 * plus the revenue in % and minimal revenue per operation in PLN
	 */	
	private int buySpread=20;
	private int sellSpread=20;
	private double buyPriceSpread=2;
	private double sellPriceSpread=2;
	private final int hundredPercent=100;
	private final String CLIENT="CLIENT";
	private final String BROKER="BROKER";
	
	private double revenueWeight=0.005;
	private double revenueHeight=5.0;
	/*
	 * random number generator
	 */
	private Random ran;
	//expected shares to be bought by client after negotiation
	private Set<ShareTo> clientWillBuy;
	//expected payment
	private double clientWillPay;

	private Set<ShareTo> clientWillSell;

	private double clientWillGet;
	
	public BrokerInterfaceImpl() {
		super();
		ran = new Random();
	}
	
	

	public BrokerInterfaceImpl(int buySpread, int sellSpread, double buyPriceSpread, double sellPriceSpread,
			double revenueWeight, double revenueHeight) {
		super();
		this.buySpread = buySpread;
		this.sellSpread = sellSpread;
		this.buyPriceSpread = buyPriceSpread;
		this.sellPriceSpread = sellPriceSpread;
		this.revenueWeight = revenueWeight;
		this.revenueHeight = revenueHeight;
		this.ran = new Random();
	}

	@Override
	public List<ShareTo> getStockHistory() {
		int date= clock.getDate();
		if(date>stockDao.getMaxDate() || date< stockDao.getMinDate())
			throw new IllegalArgumentException("Date out of data range");
		return ShareMapper.map2To(stockDao.getStockHistory(date));
	}
	

	@Override
	public List<ShareTo> getCurrentStockInfo() {
		int date= clock.getDate();
		if(date>stockDao.getMaxDate() || date< stockDao.getMinDate())
			throw new IllegalArgumentException("Date out of data range");
		return ShareMapper.map2To(stockDao.getSharesAtDate(date));
	}

	@Override
	public Set<ShareTo> negotiateBuy(Set<ShareTo> toBuy) {
		clientWillBuy = new HashSet<ShareTo>();
		List <ShareTo> comparator = ShareMapper.map2To(stockDao.getSharesAtDate(clock.getDate()));
		if(comparator.isEmpty())
			throw new IllegalArgumentException("Non-session day");
		for(ShareTo share:toBuy) {
			double price=getPrice(comparator,share.getCompany());
			clientWillBuy.add(new
					ShareTo(share.getDate(),share.getCompany(),
							randomizeBuyPrice(price),
							randomizeBuyNumber(share.getNumber())));
		}
		evaluatePayment();
		return clientWillBuy;
	}

	private double getPrice(List<ShareTo> comparator, String company) {
		double result = -10;
		for(ShareTo share:comparator) {
			if(share.getCompany().equals(company))
				result= share.getValue();
		}
		if(result<0)
			throw new IllegalArgumentException("Company "+company+" does not exist!");
		return result;
	}

	private Double randomizeBuyPrice(Double double1) {
		return 0.01*(ran.nextDouble()*buyPriceSpread+hundredPercent)*double1;
	}
	
	private Integer randomizeBuyNumber(Integer number) {
		return (int) Math.round(0.01*(ran.nextInt(buySpread+1)+hundredPercent-buySpread)*number);
	}
	
	public double evaluatePayment() {
		clientWillPay = 0;
		for(ShareTo share: clientWillBuy) {
			double price= share.getValue()*share.getNumber();
			clientWillPay+=addRevenue(price);
		}
		round2(clientWillPay);
		return clientWillPay;
	}
	
	/**
	 * Broker asks for payment for each operation. Client should be able to ask how much it is.
	 * @param price
	 * @return
	 */
	@Override
	public double addRevenue(double price) {
		return  price*revenueWeight >revenueHeight ? round2(price*(1.0+revenueWeight)) : round2(price + revenueHeight);
	}
	/**
	 * 
	 * @param a
	 * gets rounded to 2 decimal places. (could go with epsilon=0.001,
	 * but after multiplication could get some unexpected stuff!
	 * @return
	 * rounded a
	 */
	public Double round2(Double a){
		return Math.round(a * 100.0) / 100.0;
	}
	

	@Override
	public Set<ShareTo> negotiateSell(Set<ShareTo> toSell) {
		clientWillSell = new HashSet<ShareTo>();
		List <ShareTo> comparator = ShareMapper.map2To(stockDao.getSharesAtDate(clock.getDate()));
		if(comparator.isEmpty())
			throw new IllegalArgumentException("Non-session day");
		for(ShareTo share:toSell) {
			double price=getPrice(comparator,share.getCompany());
			clientWillSell.add(new
					ShareTo(share.getDate(),share.getCompany(),
							randomizeSellPrice(price),
							randomizeSellNumber(share.getNumber())));
		}
		evaluateClientsIncome();
		return clientWillSell;
	}
	
	private double evaluateClientsIncome() {
		clientWillGet = 0;
		for(ShareTo share: clientWillSell) {
			double price= share.getValue()*share.getNumber();
			clientWillGet-=addRevenue(price);
		}
		round2(clientWillGet);
		return clientWillGet;
	}
	
	private Double randomizeSellPrice(Double double1) {
		return 0.01*(hundredPercent-ran.nextDouble()*sellPriceSpread)*double1;
	}
	
	private Integer randomizeSellNumber(Integer number) {
		return (int) Math.round(0.01*(ran.nextInt(sellSpread+1)+hundredPercent-sellSpread)*number);
	}

	@Override
	public Set<ShareTo> getShares() {
		List<TransactionTo> expected = new ArrayList<TransactionTo>();
				expected.add(new TransactionTo(CLIENT, BROKER, clientWillPay));
		List<TransactionTo> obtained = bankInterface.getTransaction(CLIENT, BROKER);
		transactionValidatorInterface.validateTransaction(expected,  obtained);
		return clientWillBuy;
	}
	
	@Override
	public double getBrokersPrice() {
		return clientWillPay;
	}


/**
 * broker gets the shares (should be equal to negotiated quote) and pays the client
 * @param soldShares
 */

	@Override
	public void postShares(Set<ShareTo> soldShares) {
		transactionValidatorInterface.validateTransfer(clientWillSell, soldShares);
		bankInterface.postTransaction(new TransactionTo(BROKER, CLIENT, clientWillGet));
	}



@Override
public double getClientsIncome() {
	return clientWillGet;
}




}
