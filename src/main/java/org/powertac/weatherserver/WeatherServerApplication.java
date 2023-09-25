package org.powertac.weatherserver;

import org.apache.logging.log4j.LogManager;
import org.powertac.weatherserver.data.DataSeeder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.persistence.EntityManager;

@SpringBootApplication
public class WeatherServerApplication implements ApplicationRunner, ApplicationContextAware {

	private ApplicationContext context;

	@Value("${spring.datasource.url}")
	String datasource;

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
		context.getBean(DataSeeder.class).seed();
		context.getBean(EntityManager.class).clear();
	}

}
