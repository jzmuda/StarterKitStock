package pl.spring.demo.service;

import java.util.List;
import java.util.Set;

import pl.spring.demo.to.ShareTo;

public interface BrokerInterface {
	/*
	 * get stock history up to current date 
	 */
	public List<ShareTo> getStockHistory();
	/*
	 * get stock data for current date 
	 */
	public List<ShareTo> getCurrentStockInfo();
	/*
	 * "negotiation" of number of available shares and their price before client buys
	 */
	public Set<ShareTo> negotiateBuy(Set<ShareTo> toBuy);
	/*
	 * "negotiation" of number of shares and their price before client sells
	 */
	public Set<ShareTo> negotiateSell(Set<ShareTo> toSell);
	/*
	 * return the amount of shares to client
	 */
	public Set<ShareTo> getShares();
	/*
	 * get the shares from client
	 */
	public void postShares(Set<ShareTo> soldShares);
	/*
	 * add revenue to operations
	 */
	public double addRevenue(double price);
	/*
	 * ask for expected broker's income
	 */
	public double getBrokersPrice();
	/*
	 * ask for expected client's income
	 */
	public double getClientsIncome();
}
