package uni.miskolc.reservation.status.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"uni.miskolc.reservation.status.ws.controller", "uni.miskolc.reservation.status.data" })
public class ReservationStatusServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReservationStatusServiceApplication.class, args);
    }
}
