package pl.spring.demo.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import pl.spring.demo.service.BankInterface;
import pl.spring.demo.service.BrokerInterface;
import pl.spring.demo.service.ClientStrategy;
import pl.spring.demo.service.ExchangeInterface;
import pl.spring.demo.service.TransactionValidatorInterface;
import pl.spring.demo.to.CurrencyTo;
import pl.spring.demo.to.ShareTo;
import pl.spring.demo.to.TransactionTo;

public class Client {
	
	private final String PLN="PLN";
	private final String CLIENT="CLIENT";
	private final String BROKER="BROKER";
	private final String dateOutOfRangeError="Date out of data range";
	//this guys go through constructor, since they need parameters
	private BrokerInterface brokerInterface;
	private ExchangeInterface exchangeInterface;
	//this is something only the client possesses and needs to get parameterized
	private ClientWallet wallet;
	private ClientStrategy strategy;
	//these guys are supposed to be singletons, hence autowired beans
	@Autowired
	private BankInterface bankInterface;

	@Autowired
	private TransactionValidatorInterface transactionValidatorInterface;

	//so we do not need to ask DAO every single time for full history;
	private List<ShareTo> stockHistory;
	private Set<ShareTo> sharesToBuy;
	private Set<ShareTo> sharesToSell;
	//Compact notation: negative sum means we sell, positive: we buy
	private Set<CurrencyTo> currencyToExchange;

	public Client(ClientStrategy strategy, BrokerInterface brokerInterface, ExchangeInterface exchangeInterface, ClientWallet wallet) {
		this.strategy = strategy;
		this.wallet = wallet;
		this.brokerInterface = brokerInterface;
		this.exchangeInterface = exchangeInterface;
		stockHistory = new ArrayList<ShareTo>();
		try{
			stockHistory.addAll(brokerInterface.getStockHistory());
		} catch (Exception e) {
			throw new IllegalArgumentException(dateOutOfRangeError);
		}

	}


	public void tick() {
		//is there a stock session?
		if(checkDataPhase()) {
			buySharesPhase();
			sellSharesPhase();
			exchangeMoneyPhase();
		}
	}







	private boolean checkDataPhase() {
		try {
			List<ShareTo> current = brokerInterface.getCurrentStockInfo();
			stockHistory.addAll(current);
			return true;
		} catch(Exception e) { //well, you have to stop if you run out of the DAO data range
			if(e.getMessage().equals(dateOutOfRangeError))
				throw e;
			return false;
		}
	}

	private void buySharesPhase() {
		sharesToBuy = new HashSet<ShareTo>();
		sharesToBuy = strategy.thinkBuyingShares(stockHistory, wallet.checkShares(), wallet.checkMoney().get(PLN));
		sharesToBuy = brokerInterface.negotiateBuy(sharesToBuy);
		if(sharesToBuy.size()>0){
			Double price = brokerInterface.getBrokersPrice();
			Map<String,Double> needsToPay = new HashMap<String,Double>();
			needsToPay.put(PLN,price);
			Map<String,Double> takeOut = wallet.getMoney(needsToPay);
			//do we have enough money?
			if(takeOut.get(PLN).equals(price)) {
				bankInterface.postTransaction(new TransactionTo(CLIENT, BROKER, price));
				Set<ShareTo> obtained=brokerInterface.getShares();
				transactionValidatorInterface.validateTransfer(sharesToBuy, obtained);
				wallet.putShares(map(obtained));
			}
			else
			{
				wallet.putMoney(takeOut);
				throw new IllegalStateException("Strategy failure: Not Enough Money");
			}
		}

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
			Double income= brokerInterface.getClientsIncome();
			Map<String,Integer> toExtract= map(sharesToSell);
			Map<String,Integer> extracted=wallet.getShares(toExtract);
			//in case we want to sell share we don't have
			if(extracted.equals(toExtract)) {
				brokerInterface.postShares(sharesToSell);
				List<TransactionTo> expected= Arrays.asList(new TransactionTo(BROKER, CLIENT, income));
				List<TransactionTo> obtained=bankInterface.getTransaction(BROKER, CLIENT);
				transactionValidatorInterface.validateTransaction(expected, obtained);
				Map<String,Double> obtainedMoney=new HashMap<String,Double>();
				obtainedMoney.put(PLN, obtained.get(0).getSum());
			}
			else {
				wallet.putShares(extracted);
				throw new IllegalStateException("Strategy failure: bad share number evaluation");
			}
		}
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
