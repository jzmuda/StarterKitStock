package pl.spring.demo.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.spring.demo.service.BankInterface;
import pl.spring.demo.service.BrokerInterface;
import pl.spring.demo.service.ClientStrategy;
import pl.spring.demo.service.ExchangeInterface;
import pl.spring.demo.service.TransactionValidatorInterface;
import pl.spring.demo.to.CurrencyTo;
import pl.spring.demo.to.ShareTo;
import pl.spring.demo.to.TransactionTo;
@Component
public class Client {

	private final String PLN="PLN";
	private final String EUR="EUR";
	private final String CLIENT="CLIENT";
	private final String BROKER="BROKER";
	private final String dateOutOfRangeError="Date out of data range";
	//these guys are supposed to be singletons, hence autowired beans
	@Autowired
	private BrokerInterface brokerInterface;
	@Autowired
	private ExchangeInterface exchangeInterface;
	@Autowired
	private ClientStrategy strategy;
	@Autowired
	private BankInterface bankInterface;
	@Autowired
	private TransactionValidatorInterface transactionValidatorInterface;

	//this is something only the client possesses and needs to get parameterized
	private ClientWallet wallet;

	//so we do not need to ask DAO every single time for full history;
	private List<ShareTo> stockHistory;
	private Set<ShareTo> sharesToBuy;
	private Set<ShareTo> sharesToSell;
	//Compact notation: negative sum means we sell, positive: we buy
	private Set<CurrencyTo> currencyToExchange;

	public Client() {
	}

	public void init(double zloty, double ojro) throws Exception {
		initWallet(zloty, ojro);
		initStockHistory();
	}

	private String report() {
		String result =""+ stockHistory.get(stockHistory.size()-1).getDate();
		result += printShares();
		result += printCurrencies();
		return result;
	}

	private String printCurrencies() {
		Map<String,Double> currencies = wallet.checkMoney();
		String result="";
		if(currencies.size()>0) {
			for(String currency:currencies.keySet())
				result+=";"+currency+","+currencies.get(currency);
		}
		return result;
	}

	private String printShares() {
		Map<String,Integer> shares = wallet.checkShares();
		String result="";
		if(shares.size()>0) {
			for(String company:shares.keySet())
				result+=";"+company+","+shares.get(company);
		}
		return result;
	}
	
	public String finalReport() {
		String result = "";
		result+=printCurrencies();
		result+=printShareValue();
		return result;
	}
	
	

	private String printShareValue() {
		Map<String,Integer> shares = wallet.checkShares();
		String result="";
		if(shares.size()>0) {
			for(String company:shares.keySet())
				result+=";"+company+" approx. "+evaluateShare(company,shares.get(company))+" PLN";
		}
		return result;	}

	private Double evaluateShare(String company, Integer number) {
		for(int i=stockHistory.size()-1;i>0;i--) {
			if(stockHistory.get(i).getCompany().equals(company))
				return stockHistory.get(i).getValue()*number;
		}
		return 0.0;
	}

	private void initStockHistory() throws Exception {
		stockHistory = new ArrayList<ShareTo>();
		stockHistory.addAll(brokerInterface.getStockHistory());
		try{

		} catch (Exception e) {
			if(e.getMessage().equals(dateOutOfRangeError))
				throw new IllegalArgumentException(dateOutOfRangeError);
			else throw e;
		}
	}

	private void initWallet(double zloty, double ojro) {
		wallet = new ClientWallet();
		Map<String,Double> initialMoney = new HashMap<String,Double>();
		if(zloty>0) {
			initialMoney.put(PLN, zloty);
		}
		if(ojro>0) {
			initialMoney.put(EUR, ojro);
		}
		if(initialMoney.size()>0)
		{
			wallet.putMoney(initialMoney);
		}
	}

	public Client(ClientStrategy strategy, BrokerInterface brokerInterface, ExchangeInterface exchangeInterface, ClientWallet wallet) {
		this.strategy = strategy;
		this.wallet = wallet;
		this.brokerInterface = brokerInterface;
		this.exchangeInterface = exchangeInterface;
	}


	public String tick() {
		try{
			checkDataPhase();
			buySharesPhase();
			sellSharesPhase();
			exchangeMoneyPhase();
			return report();
		}
		catch(Exception e) {
			return e.getMessage();
		}
	}


	private void checkDataPhase() {
		List<ShareTo> current = brokerInterface.getCurrentStockInfo();
		stockHistory.addAll(current);
	}

	private void buySharesPhase() {
		sharesToBuy = new HashSet<ShareTo>();
		sharesToBuy = strategy.thinkBuyingShares(stockHistory, wallet.checkShares(), wallet.checkMoney().get(PLN));

		sharesToBuy = brokerInterface.negotiateBuy(sharesToBuy);
		if(sharesToBuy.size()>0){
			negotiateBuyPhase();
		}

	}


	private void negotiateBuyPhase() {
		Double price = brokerInterface.getBrokersPrice();
		Map<String,Double> needsToPay = new HashMap<String,Double>();
		needsToPay.put(PLN,price);
		Map<String,Double> takeOut = wallet.getMoney(needsToPay);
		//do we have enough money?
		if(Math.abs(takeOut.get(PLN)-price)<0.0001) {
			buyTransactionPhase(price);
		}
		else
		{
			discardBuyTransactionPhase(takeOut);
		}
	}


	private void discardBuyTransactionPhase(Map<String, Double> takeOut) {
		wallet.putMoney(takeOut);
//		throw new IllegalStateException("Strategy failure: Not Enough Money");
	}


	private void buyTransactionPhase(Double price) {
		bankInterface.postTransaction(new TransactionTo(CLIENT, BROKER, price));
		Set<ShareTo> obtained=brokerInterface.getShares();
		transactionValidatorInterface.validateTransfer(sharesToBuy, obtained);
		wallet.putShares(map(obtained));
	}

	private Map<String, Integer> map(Set<ShareTo> obtained) {
		Map <String,Integer> result = new HashMap<String,Integer>();
		for(ShareTo share: obtained) {
			result.put(share.getCompany(), share.getNumber());
		}
		return result;
	}

	private void sellSharesPhase() {
		sharesToSell = new HashSet<ShareTo>();
		sharesToSell = strategy.thinkSellingShares(stockHistory, wallet.checkShares(), wallet.checkMoney().get(PLN));
		sharesToSell = brokerInterface.negotiateSell(sharesToSell);
		if(sharesToSell.size()>0){
			negotiateSellPhase();
		}
	}


	private void negotiateSellPhase() {
		Double income= brokerInterface.getClientsIncome();
		Map<String,Integer> toExtract= map(sharesToSell);
		Map<String,Integer> extracted=wallet.getShares(toExtract);
		//in case we want to sell share we don't have
		if(extracted.equals(toExtract)) {
			sellTransactionPhase(income);
		}
		else {
			discardSelltransactionPhase(extracted);
		}
	}


	private void discardSelltransactionPhase(Map<String, Integer> extracted) {
		wallet.putShares(extracted);
		throw new IllegalStateException("Strategy failure: bad share number evaluation");
	}


	private void sellTransactionPhase(Double income) {
		System.out.println("We sell shares for "+income);
		brokerInterface.postShares(sharesToSell);
		List<TransactionTo> expected= Arrays.asList(new TransactionTo(BROKER, CLIENT, income));
		List<TransactionTo> obtained=bankInterface.getTransaction(BROKER, CLIENT);
		transactionValidatorInterface.validateTransaction(expected, obtained);
		Map<String,Double> obtainedMoney=new HashMap<String,Double>();
		obtainedMoney.put(PLN, obtained.get(0).getSum());
		wallet.putMoney(obtainedMoney);
	}

	/**
	 * Left TODO. Talks with empty lists, does nothing
	 */

	private void exchangeMoneyPhase() {
		// TODO Auto-generated method stub
		currencyToExchange = new HashSet<CurrencyTo>();
		currencyToExchange = strategy.thinkExchanging(exchangeInterface.askForPrices(), wallet.checkMoney());
		//etc..
	}

}
