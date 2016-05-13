package litter;

public class ShareStash {
	
	private String company;
	private Integer number;
	
	public ShareStash(String company, Integer number) {
		this.company=company;
		this.number=number;
	}
	
	public String getCompany() {
		return company;
	}
	
	public Integer getNumber() {
		return number;
	}
	
	public void modify(ShareStash other) {
		if(company.equals(other.getCompany()))
			number+=other.number;
		else throw new IllegalArgumentException("Share type mismatch");
	}

}
