package client;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pl.spring.demo.client.Client;
import pl.spring.demo.client.ClientWallet;
import pl.spring.demo.clock.DateClock;
import pl.spring.demo.dao.StockDao;
import pl.spring.demo.service.impl.BrokerInterfaceImpl;
import pl.spring.demo.service.impl.ClientStrategyImpl;
import pl.spring.demo.service.impl.ExchangeImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "ClientTest-context.xml")
public class ClientTest {
	
	private Client client;
	@Autowired
	DateClock clock;
	@Autowired
	StockDao stockDao;
	
	@Before
	public void setUp(){
		Map<String,Double> initial= new HashMap<String,Double>();
		initial.put("PLN", 10000.00);
		//client= new Client(new ClientStrategyImpl(), new BrokerInterfaceImpl(0, 0, 0, 0, 0.005, 5), new ExchangeImpl(), new ClientWallet(initial));
		
	}
	
	@Test
	public void testShouldDoSth() throws InterruptedException {
		System.out.println(clock.getDate());
		Thread.sleep(2000);
	}

}
