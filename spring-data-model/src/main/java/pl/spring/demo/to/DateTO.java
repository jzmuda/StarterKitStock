package pl.spring.demo.to;

public class DateTO {
	int year;
	int month;
	int day;
	
	public DateTO(int year, int month, int day) {
		this.year = year;
		this.month = month;
		this.day = day;
	}

	public int getYear() {
		return year;
	}

	public int getMonth() {
		return month;
	}

	public int getDay() {
		return day;
	}
	
}
