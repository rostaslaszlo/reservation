package uni.miskolc.reservation.endws.boot;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.http.Http;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.client.ResponseErrorHandler;

import uni.miskolc.reservation.event.ws.api.ReservationEventServiceV1;
import uni.miskolc.reservation.event.ws.api.dto.ReservationEvent;
import uni.miskolc.reservation.event.ws.api.exception.ReservationEventException;

@Configuration
public class ReservationEventServiceV1Config {

    @Value("${integration-locations.reservation-event-service-v1}")
    private String reservationEventServiceV1BaseUrl;

    private ResponseErrorHandler errorHandler = new ResponseErrorHandler() {
        @Override
        public boolean hasError(ClientHttpResponse response) throws IOException {
            return response.getRawStatusCode() != 200;
        }
        
        @Override
        public void handleError(ClientHttpResponse response) throws IOException {
            throw new ReservationEventException();
        }
    };


    @Bean
    public IntegrationFlow ReservationEventServiceV1GatewayGetEventFlow() {
        return IntegrationFlows.from("reservationEventServiceV1GatewayGetLocationChannel").handle(
            Http.outboundGateway(reservationEventServiceV1BaseUrl + "/getEvent/{eventId}").httpMethod(HttpMethod.GET).expectedResponseType(ReservationEvent.class).errorHandler(errorHandler).uriVariable("eventId", "payload")
        ).get();
    }
    
    @Bean
    public IntegrationFlow ReservationEventServiceV1GatewayGetLocationFlow() {
        return IntegrationFlows.from("reservationEventServiceV1GatewayGetLocationChannel").handle(
            Http.outboundGateway(reservationEventServiceV1BaseUrl + "/getLocation/{eventId}").httpMethod(HttpMethod.GET).expectedResponseType(String.class).errorHandler(errorHandler).uriVariable("eventId", "payload")
        ).get();
    }

    @MessagingGateway
    public interface ReservationEventServiceV1Gateway extends ReservationEventServiceV1 {

        @Override
        @Gateway(requestChannel = "reservationEventServiceV1GatewayGetLocationChannel", replyTimeout = 2, requestTimeout = 200)
        ReservationEvent getEvent(@Payload String eventId);

        @Gateway(requestChannel = "reservationEventServiceV1GatewayGetLocationChannel", replyTimeout = 2, requestTimeout = 200)
        String getLocation(@Payload String locationId);
    }
}
