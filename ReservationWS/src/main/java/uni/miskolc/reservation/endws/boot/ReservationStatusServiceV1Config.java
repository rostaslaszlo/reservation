package uni.miskolc.reservation.endws.boot;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.channel.NullChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.http.Http;
import org.springframework.integration.handler.LoggingHandler.Level;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.client.ResponseErrorHandler;

import uni.miskolc.reservation.status.ws.api.ReservationStatusServiceV1;
import uni.miskolc.reservation.status.ws.api.dto.ReservationStatusType;
import uni.miskolc.reservation.status.ws.api.exception.ReservationStatusException;

@Configuration
public class ReservationStatusServiceV1Config {

    @Value("${integration-locations.reservation-status-service-v1}")
    private String reservationStatusServiceV1BaseUrl;

    private ResponseErrorHandler errorHandler = new ResponseErrorHandler() {
        @Override
        public boolean hasError(ClientHttpResponse response) throws IOException {
            System.out.println("body: " + IOUtils.toString(response.getBody(), "UTF-8"));

            return response.getRawStatusCode() != 200;
        }

        @Override
        public void handleError(ClientHttpResponse response) throws IOException {
            throw new ReservationStatusException();
        }
    };

    @Bean
    MessageChannel nullChanel(){
        return new NullChannel();
    }

    @Bean
    public IntegrationFlow reservationStatusServiceV1GatewayGetReservationsFlow() {
        final Class<?> type = new HashMap<String, ReservationStatusType>().getClass();

        return IntegrationFlows.from("reservationStatusServiceV1GetReservationsChannel")
                .handle(Http.outboundGateway(reservationStatusServiceV1BaseUrl + "/getReservations/event/{eventId}")
                    .httpMethod(HttpMethod.GET)
                    .expectedResponseType(type)
                    .errorHandler(errorHandler)
                    .uriVariable("eventId", "payload"))
                .get();
    }

    @Bean
    public IntegrationFlow reservationStatusServiceV1GatewayPutReservationFlow() {
        return IntegrationFlows
                .from("reservationStatusServiceV1PutReservationChannel")
                .log(Level.INFO, "trace.http",
                    "'Request.\n'"+
                    ".concat('Headers : ').concat(headers.toString()).concat('\n')"+
                    ".concat('Payload : ').concat(payload.toString())"
                )
                .handle(
                    Http.outboundGateway(reservationStatusServiceV1BaseUrl + "/reservation/{statusType}/event/{eventId}/ticketId/{ticketId}")
                    .httpMethod(HttpMethod.GET)
                    .errorHandler(errorHandler)
                    .uriVariable("eventId", "payload[0]")
                    .uriVariable("ticketId", "payload[1]")
                    .uriVariable("statusType", "payload[2]")
                )
                .channel(nullChanel())
                .get();
    }

    @Bean
    public IntegrationFlow reservationStatusServiceV1GatewayDeleteReservationFlow() {
        return IntegrationFlows
                .from("reservationStatusServiceV1DeleteReservationChannel")
                .handle(Http.outboundGateway(reservationStatusServiceV1BaseUrl + "/reservation/event/{eventId}/ticketId/{ticketId}")
                    .httpMethod(HttpMethod.GET)
                    .expectedResponseType(Void.class)
                    .errorHandler(errorHandler)
                    .uriVariable("eventId", "payload[0]")
                    .uriVariable("ticketId", "payload[1]")
                )
                .channel(nullChanel())
                .get();
    }

    @MessagingGateway
    public interface ReservationStatusServiceV1Gateway extends ReservationStatusServiceV1 {

        @Override
        @Gateway(requestChannel = "reservationStatusServiceV1GetReservationsChannel", replyTimeout = 2, requestTimeout = 200)
        Map<String, ReservationStatusType> getReservations(@Payload String eventId);

        @Override
        @Gateway(requestChannel = "reservationStatusServiceV1PutReservationChannel", replyTimeout = 2, requestTimeout = 200)
        @Payload("T(java.util.Arrays).asList(#args[0],#args[1],#args[2])")
        void putReservation(String eventId, String ticketId, ReservationStatusType statusType);

        @Override
        @Gateway(requestChannel = "reservationStatusServiceV1DeleteReservationChannel", replyTimeout = 2, requestTimeout = 200)
        @Payload("T(java.util.Arrays).asList(#args[0],#args[1])")
        void deleteReservation(String eventId, String ticketId);
    }
}
