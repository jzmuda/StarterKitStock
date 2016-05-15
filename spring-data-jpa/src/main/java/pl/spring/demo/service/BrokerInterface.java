package pl.spring.demo.service;

import java.util.List;

/**
 * This interface defines our broker. It is responsible for stock data retrieval and
 * for share buy/sell with negotiations. Details depend on internal implementation
 * in BrokerInterfaceImpl.java.
 */

import java.util.Set;

import pl.spring.demo.to.BrokerParams;
import pl.spring.demo.to.ShareTo;

public interface BrokerInterface {
	/**
	 * initialize brokers parameters
	 * @param buySpread
	 * variation of number of shares sold to the client
	 * used in negotiation; default =20 percent
	 * @param sellSpread
	 * variation of number of shares bought from the client
	 * used in negotiation; default = 20 percent
	 * @param buyPriceSpread
	 * variation of offered price for shares sold to client
	 * used in negotiation; default = 2 percent
	 * @param sellPriceSpread
	 * variation of offered price for shares bought from client
	 * used in negotiation; default = 2 percent
	 * @param revenueWeight
	 * revenue per operation (default 0.005)
	 * @param revenueHeight
	 * minimal revenue, default =5 (PLN)
	 */
	public void init(BrokerParams params);
	/**
	 * returns stock history up to current date
	 * @return
	 * list of shares
	 */
	public List<ShareTo> getStockHistory();
	/**
	 * return stock info for the current day
	 * @return
	 */
	public List<ShareTo> getCurrentStockInfo();
	/**
	 * negotiation of number and prices of shares to sell
	 * @param toBuy
	 * shares client wants o buy
	 * @return
	 * number and prices of shares broker will sell
	 */
	public Set<ShareTo> negotiateBuy(Set<ShareTo> toBuy);
	/**
	 * negotiation of number and prices of shares to buy
	 * @param toSell
	 * shares client wants to sell
	 * @return
	 * number and prices of shares broker will buy
	 */
	public Set<ShareTo> negotiateSell(Set<ShareTo> toSell);
	/**
	 * give the shares to client
	 * @return
	 */
	public Set<ShareTo> getShares();
	/**
	 * get the shares from client
	 * @param soldShares
	 */
	public void postShares(Set<ShareTo> soldShares);
	/**
	 * ask for expected broker's income
	 */
	public double getBrokersPrice();
	/**
	 * ask for expected client's income
	 */
	public double getClientsIncome();
}
