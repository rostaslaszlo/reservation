package uni.miskolc.reservation.event.ws.api;

import uni.miskolc.reservation.event.ws.api.dto.ReservationEvent;

public interface ReservationEventServiceV1 {

    ReservationEvent getEvent(String eventId);

    String getLocation(String locationId);
}
