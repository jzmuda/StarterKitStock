package pl.spring.demo.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.spring.demo.clock.DateClock;
import pl.spring.demo.service.impl.BrokerInterfaceImpl;
@Component
public class SimulationController {
	@Autowired
	private DateClock clock;
	
	//private BrokerInterfaceImpl broker;
	
	public SimulationController(int startYear, int startMonth, int startDay) {
		clock.setDate(startYear, startMonth, startDay);
		//broker = new BrokerInterfaceImpl(0, 0, 0, 0, 0.005, 5.0);
	}
	
	public void derp() {
		
	}
	
	public static void main(String[] args) {
        SimulationController controller = new SimulationController(2013, 2, 2);
        controller.derp();
    }

}
