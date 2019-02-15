package de.hhu.sharing;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import de.hhu.sharing.web.ProPayService;

@TestConfiguration
public class ProPayServiceTestConfiguration {
	@Bean
	public ProPayService pps() {
		return new ProPayService();
	}
}
