package entity;

import javax.persistence.*;

@Entity
public class ShareEntity implements Comparable<ShareEntity> {
	
	private Integer date;
	private String company;
	private Double value;
	
	public ShareEntity(Integer date, String company, Double value) {
		this.date = date;
		this.company = company;
		this.value = value;
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

	@Override
	public int compareTo(ShareEntity other) {
		int result = this.getDate() - other.getDate();
		return result;
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((company == null) ? 0 : company.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
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
		if (!(obj instanceof ShareEntity)) {
			return false;
		}
		ShareEntity other = (ShareEntity) obj;
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
