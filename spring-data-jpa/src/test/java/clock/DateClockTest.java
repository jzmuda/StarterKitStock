package clock;

import static org.junit.Assert.*;

import org.junit.Test;

import pl.spring.demo.clock.DateClock;

public class DateClockTest {
	private static DateClock clock;
	private static int year=2013;
	private static int leapYear=2016;
	private static int month= 12;
	private static int february= 2;
	private static int day = 31;
	private static int otherDay = 24;
	private static int expectedDateAsInt = year*10000+month*100+day;
	private static int expectedIncreasedYearlyDate =20140101;
	private static int expectedIncreasedDate =20131225;
	private static int firstOfMarch =20130301;
	private static int firstOfMarchLeap =20160301;
	@Test
	public void testShouldSetAndReadDate() {
		//given
		clock = new DateClock(year,month,day);
		//when
		int date = clock.getDate();
		//then
		assertEquals(expectedDateAsInt,date);
	}
	
	@Test(expected = Exception.class)
	public void testShouldNotAcceptBadMonth() {
		//given
		int badMonth = month+13;
		//when
		clock = new DateClock(year,badMonth,day);
		//then
		fail("Test should throw exception");
	}
	
	@Test(expected = Exception.class)
	public void testShouldNotAcceptBadDay() {
		//given
		int badDay = 32;
		//when
		clock = new DateClock(year,month,badDay);
		//then
		fail("Test should throw exception");
	}
	
	@Test
	public void testShouldIncreaseNormalDate() {
		//given
		clock = new DateClock(year,month,otherDay);
		//when
		clock.tick();
		int date = clock.getDate();
		//then
		assertEquals(expectedIncreasedDate,date);
	}
	
	@Test
	public void testShouldIncreaseDateOnMonthChange() {
		//given
		clock = new DateClock(year,february,28);
		//when
		clock.tick();
		int date = clock.getDate();
		//then
		assertEquals(firstOfMarch,date);
	}
	
	@Test
	public void testShouldIncreaseDateOnMonthChangeLeapYear() {
		//given
		clock = new DateClock(leapYear,february,29);
		//when
		clock.tick();
		int date = clock.getDate();
		//then
		assertEquals(firstOfMarchLeap,date);
	}
	
	@Test
	public void testShouldIncreaseDateOnYearChange() {
		//given
		clock = new DateClock(year,month,day);
		//when
		clock.tick();
		int date = clock.getDate();
		//then
		assertEquals(expectedIncreasedYearlyDate,date);
	}

}
