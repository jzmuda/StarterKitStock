package pl.spring.demo.to;

public class ShareTo {

	private Integer date;
	private String company;
	private Double value;
	private Integer number;

	public ShareTo(Integer date, String company, Double value, Integer number) {
		this.date = date;
		this.company = company;
		this.value = value;
		this.number=number;
	}

	public Integer getDate() {
		return this.date;
	}

	public String getCompany() {
		return this.company;
	}

	public Double getValue() {
		return this.value;
	}
	
	public Integer getNumber() {
		return this.number;
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((company == null) ? 0 : company.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((number == null) ? 0 : number.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof ShareTo)) {
			return false;
		}
		ShareTo other = (ShareTo) obj;
		if (company == null) {
			if (other.company != null) {
				return false;
			}
		} else if (!company.equals(other.company)) {
			return false;
		}
		if (date == null) {
			if (other.date != null) {
				return false;
			}
		} else if (!date.equals(other.date)) {
			return false;
		}
		if (number == null) {
			if (other.number != null) {
				return false;
			}
		} else if (!number.equals(other.number)) {
			return false;
		}
		if (value == null) {
			if (other.value != null) {
				return false;
			}
		} else if (!value.equals(other.value)) {
			return false;
		}
		return true;
	}
	

}
