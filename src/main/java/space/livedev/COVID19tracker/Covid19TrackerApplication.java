package space.livedev.COVID19tracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import space.livedev.COVID19tracker.services.CovidDataService;

@SpringBootApplication
public class Covid19TrackerApplication {

	public static void main(String[] args)
	{
		SpringApplication.run(Covid19TrackerApplication.class, args);

	}

}
