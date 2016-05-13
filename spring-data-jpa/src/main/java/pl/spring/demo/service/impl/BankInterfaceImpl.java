package pl.spring.demo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import pl.spring.demo.service.BankInterface;
import pl.spring.demo.to.TransactionTo;
@Component
public class BankInterfaceImpl implements BankInterface {
	
	private List<TransactionTo> transactions;
	
	public BankInterfaceImpl() {
		transactions=new ArrayList<TransactionTo>();
	}
	
	@Override
	public void postTransaction(TransactionTo transaction) {
		if(verifyTransaction(transaction))
			transactions.add(transaction);
		else throw new IllegalArgumentException("Corrupt Transaction!");
	}
	
	@Override
	public List<TransactionTo> getTransaction(String from, String to) {
		List<TransactionTo> result = new ArrayList<TransactionTo>();
		for(TransactionTo transaction:transactions) {
			if(transaction.getFrom().equals(from)&&transaction.getTo().equals(to)) {
				result.add(transaction);
			}
		}
		transactions.removeAll(result);
		return result;
	}
	
	public boolean verifyTransaction(TransactionTo transaction) {
		boolean result=!(transaction.getFrom()==null ||
				transaction.getTo()==null ||
				transaction.getTo().isEmpty() ||
				transaction.getFrom().isEmpty() ||
				transaction.getSum()==null ||
				transaction.getSum()<0);
		return result;
	}
	
}
