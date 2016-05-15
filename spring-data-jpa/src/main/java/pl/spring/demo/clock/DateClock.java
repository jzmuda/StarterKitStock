package pl.spring.demo.clock;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
@Service
public class DateClock {
	private final int yearMultiplier=10000;
	private final int monthMultiplier=100;
	private int year;
	private int month;
	private int day;

	private static Map<Integer, Integer> monthDayMap= new HashMap<Integer, Integer>();
	static {
		monthDayMap.put(1,31);
		monthDayMap.put(2,28);
		monthDayMap.put(3,31);
		monthDayMap.put(4,30);
		monthDayMap.put(5,31);
		monthDayMap.put(6,30);
		monthDayMap.put(7,31);
		monthDayMap.put(8,31);
		monthDayMap.put(9,30);
		monthDayMap.put(10,31);
		monthDayMap.put(11,30);
		monthDayMap.put(12,31);
	}
	
	public DateClock() {
	}

	public DateClock(int year, int month, int day) {
		setDate(year, month, day);
	}

	public void setDate(int year, int month, int day) {
		this.year=year;
		this.month=month;
		if(validMonth()) {
			setFebInLeap();	
			this.day=day;
			if(!validDay()) throw new IllegalArgumentException("Date value out of range [1,"+monthDayMap.get(month)+"]");
		}
		else throw new IllegalArgumentException("Month value out of range [1,12]");
	}

	public int getDate() {
		return year*yearMultiplier + month*monthMultiplier + day;
	}
	
	public int convertDateToInt(int year, int month, int day) {
		return year*yearMultiplier + month*monthMultiplier + day;
	}

	public void tick() {
		day++;
		if(!validDay()) {
			day=1;
			month++;
			if(!validMonth()){
				month=1;
				year++;
			}
		}
	}

	private void setFebInLeap() {
		if (leapYear())
			monthDayMap.put(2, 29);
		else
			monthDayMap.put(2, 28);
	}

	private boolean leapYear() {
		return ((year%400==0) || (year%4==0 && year%25!=0));
	}

	private boolean validDay() {
		return (day<=monthDayMap.get(month) && day>0);
	}

	private boolean validMonth() {
		return monthDayMap.containsKey(month);
	}


}
