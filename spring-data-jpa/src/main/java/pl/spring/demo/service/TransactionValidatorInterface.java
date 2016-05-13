package pl.spring.demo.service;
import pl.spring.demo.to.ShareTo;
import pl.spring.demo.to.TransactionTo;
import java.util.Collection;

public interface TransactionValidatorInterface {
	public void validateTransaction(Collection<TransactionTo> expected, Collection<TransactionTo> obtained);
	public void validateTransfer(Collection<ShareTo> expected, Collection<ShareTo> obtained);
}
