package pl.spring.demo.to;

public class ConfigTo {
	private DateTO start;
	private DateTO finish;
	private double pln;
	private double eur;
	private BrokerParams brokerParams;
	public ConfigTo(DateTO start, DateTO finish, double pln, double eur, BrokerParams brokerParams) {
		this.start = start;
		this.finish = finish;
		this.pln = pln;
		this.eur = eur;
		this.brokerParams = brokerParams;
	}
	public DateTO getStart() {
		return start;
	}
	public DateTO getFinish() {
		return finish;
	}
	public double getPln() {
		return pln;
	}
	public double getEur() {
		return eur;
	}
	public BrokerParams getBrokerParams() {
		return brokerParams;
	}
	

}
