package pl.spring.demo.to;

public class TransactionTo {
	private String from;
	private String to;
	private Double sum;
	
	public TransactionTo(String from, String to, Double sum) {
		this.from=from;
		this.to=to;
		this.sum=sum;
	}
	
	public String getTo(){
		return to;
	}
	
	public String getFrom() {
		return from;
	}
	
	public Double getSum() {
		return sum;
	}
	
	@Override
	public boolean equals(Object other){
	    if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof TransactionTo))return false;
	    TransactionTo otherTransaction = (TransactionTo)other;
	    return (this.getFrom().equals(otherTransaction.getFrom()) &&
	    		this.getTo().equals(otherTransaction.getTo()) &&
	    		this.getSum().equals(otherTransaction.getSum()));
	}

}
