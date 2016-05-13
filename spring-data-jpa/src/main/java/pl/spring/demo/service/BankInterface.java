package pl.spring.demo.service;

import java.util.List;

import pl.spring.demo.to.TransactionTo;

public interface BankInterface {
	public void postTransaction(TransactionTo transaction);
	public List<TransactionTo> getTransaction(String from, String to);
}
