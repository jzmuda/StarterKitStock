package pl.spring.demo.main;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.stereotype.Component;

import pl.spring.demo.to.BrokerParams;
import pl.spring.demo.to.ConfigTo;
import pl.spring.demo.to.DateTO;

@Component
public class Main {
	@Autowired
	SimulationController controller;
	
	public SimulationController getController() {
		return controller;
	}

	public static void main(String[] args) {
		Path currentRelativePath = Paths.get("");
		String openFileName = currentRelativePath.toAbsolutePath().toString();
		System.out.println(openFileName);
		openFileName+="\\src\\main\\resources\\spring\\spring-context.xml";
		final ApplicationContext context = 
				new FileSystemXmlApplicationContext(openFileName);
		final Main main = 
				context.getBean(Main.class);
		DateTO start= new DateTO(2013,2,3);
		DateTO finish= new DateTO(2013,12,30);
		BrokerParams params = new BrokerParams(0,0,0,0,0.005,5.0);
		ConfigTo config = new ConfigTo(start, finish, 10000.00, 0.00, params);
		try {
			main.getController().setup(config);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("oops");
		}
		main.getController().run();
	}
	

}
