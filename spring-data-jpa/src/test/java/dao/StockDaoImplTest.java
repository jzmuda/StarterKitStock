package dao;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import entity.ShareEntity;
import pl.spring.demo.dao.impl.StockDaoImpl;


public class StockDaoImplTest {

	private StockDaoImpl stockDao;
	
	

	@Test
	public void testShouldOpenAndLoadDataFromFile() {
		// given
		final String file = "dane.csv";
		stockDao = new StockDaoImpl();
		stockDao.load(file);
		// when
		List<ShareEntity> shares=stockDao.getStockHistory(21000000);
		// then
		assertNotNull(shares);
		assertFalse(shares.isEmpty());

	}

	@Test(expected = Exception.class)
	public void testShouldThrowExceptionForNonExistentFile() {
		// given
		final String file = "awe.css";
		// when
		stockDao = new StockDaoImpl();
		stockDao.load(file);
		// then
		fail("test should throw Exception");
	}

	@Test(expected = Exception.class)
	public void testShouldThrowExceptionForFileWithLackedDataRecord() {
		// given
		final String file = "lackOfValue.csv";
		// when
		stockDao = new StockDaoImpl();
		stockDao.load(file);
		// then
		fail("test should throw Exception");
	}

	@Test(expected = Exception.class)
	public void testShouldThrowExceptionForFileWithCorruptDate() {
		// given
		final String file = "badDate.csv";
		// when
		stockDao = new StockDaoImpl();
		stockDao.load(file);
		// then
		fail("test should throw Exception");
	}
	
	@Test(expected = Exception.class)
	public void testShouldThrowExceptionForFileWithCorruptValue() {
		// given
		final String file = "badValue.csv";
		// when
		stockDao = new StockDaoImpl();
		stockDao.load(file);
		// then
		fail("test should throw Exception");
	}
	
	@Test
	public void testShouldAlwaysStoreSortedData() {
		// given
		final String file = "wrongOrder.csv";
		stockDao = new StockDaoImpl();
		stockDao.load(file);
		Integer previousDate=-10;
		// when
		List<ShareEntity> shares=stockDao.getStockHistory(21000000);
		// then
		for(ShareEntity share:shares) {
			Integer nextDate=share.getDate();
			assertTrue(nextDate>=previousDate);
			previousDate=nextDate;
		}
	}
	
	@Test
	public void testShouldGetMinDate() {
		// given
		final String file = "dane.csv";
		stockDao = new StockDaoImpl();
		stockDao.load(file);
		// when
		int min=stockDao.getMinDate();
		// then
		assertEquals(20130102,min);
	}
	
	@Test
	public void testShouldGetMaxDate() {
		// given
		final String file = "dane.csv";
		stockDao = new StockDaoImpl();
		stockDao.load(file);
		// when
		int max=stockDao.getMaxDate();
		// then
		assertEquals(20131230,max);
	}
	
	@Test
	public void testShouldGetSharesAtDate() {
		// given
		final String file = "dane.csv";
		final int date=20130108;
		stockDao = new StockDaoImpl();
		stockDao.load(file);
		// when
		List<ShareEntity> shares=stockDao.getSharesAtDate(date);
		// then
		assertNotNull(shares);
		assertFalse(shares.isEmpty());
		assertEquals(5,shares.size());
	}
	

}
