package pl.spring.demo.service.impl;

import java.util.Collection;

import pl.spring.demo.service.TransactionValidatorInterface;
import pl.spring.demo.to.ShareTo;
import pl.spring.demo.to.TransactionTo;

public class TransactionValidatorImpl implements TransactionValidatorInterface{

	@Override
	public void validateTransaction(Collection<TransactionTo> expected, Collection<TransactionTo> obtained) {
		if(!expected.equals(obtained))
			throw new RuntimeException("Invalid money transaction!");
	}

	@Override
	public void validateTransfer(Collection<ShareTo> expected, Collection<ShareTo> obtained) {
		if(!expected.equals(obtained))
			throw new RuntimeException("Invalid share transaction!");
	}

}
