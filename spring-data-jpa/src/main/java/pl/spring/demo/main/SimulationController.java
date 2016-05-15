package pl.spring.demo.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.spring.demo.client.Client;
import pl.spring.demo.clock.DateClock;
import pl.spring.demo.service.BrokerInterface;
import pl.spring.demo.to.ConfigTo;
/**
 * Main simulation controller
 * @author JZMUDA
 *
 */

@Component
public class SimulationController {
	@Autowired
	private DateClock clock;
	@Autowired
	private BrokerInterface brokerInterface;
	@Autowired
	private Client client;
	
	private Integer finishDate;
	
	public SimulationController() {

	}
	
	/**
	 * Sets up required parameters
	 * @param config
	 * contains start and finish dates, initial euro and pln for client
	 * as well as broker negotiation/revenue paras
	 * @throws Exception
	 */

	public void setup(ConfigTo config) throws Exception {
		clock.setDate(config.getStart().getYear(), config.getStart().getMonth(), config.getStart().getDay());
		this.finishDate=clock.convertDateToInt(config.getFinish().getYear(), config.getFinish().getMonth(), config.getFinish().getDay());
		brokerInterface.init(config.getBrokerParams());
		client.init(config.getPln(),config.getEur());
	}
	
	/**
	 * runs the simulation and reports the results in console
	 */
	public void run() {
		while(clock.getDate()<=finishDate) {
			System.out.println(client.tick());
			clock.tick();
		}
		System.out.println("Finished");
		System.out.println(client.finalReport());
	}
	

}
