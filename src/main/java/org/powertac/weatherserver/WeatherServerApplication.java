package org.powertac.weatherserver;

import org.powertac.weatherserver.data.DataSeeder;
import org.springframework.beans.BeansException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

@SpringBootApplication
public class WeatherServerApplication implements ApplicationRunner, ApplicationContextAware {

	private ApplicationContext context;

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(WeatherServerApplication.class);
		app.setBannerMode(Banner.Mode.OFF);
		app.setLogStartupInfo(false);
		app.run(args);
	}

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		this.context = context;
	}

	@Override
	public void run(ApplicationArguments args) {
		DataSeeder seeder = context.getBean(DataSeeder.class);
		seeder.seed();
	}

}
