package pl.spring.demo.service;

import java.util.List;

import pl.spring.demo.to.TransactionTo;
/**
 * This is a simple bank interface. One can post or retrieve transactions
 * given the sender and recipient;
 * @author JZMUDA
 *
 */
public interface BankInterface {
	public void postTransaction(TransactionTo transaction);
	public List<TransactionTo> getTransaction(String from, String to);
}
