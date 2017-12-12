package uni.miskolc.reservation.endws.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;

@SpringBootApplication
@EnableIntegration
@IntegrationComponentScan
@ComponentScan(
    {"uni.miskolc.reservation.endws.ws.controller", "uni.miskolc.reservation.endws.service.gateway", "uni.miskolc.reservation.endws.boot" }
)
public class ReservationWsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReservationWsApplication.class, args);
    }
}
