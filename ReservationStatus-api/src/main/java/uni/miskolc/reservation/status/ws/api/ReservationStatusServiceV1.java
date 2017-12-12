package uni.miskolc.reservation.status.ws.api;

import java.util.Map;

import uni.miskolc.reservation.status.ws.api.dto.ReservationStatusType;

public interface ReservationStatusServiceV1 {

    Map<String, ReservationStatusType> getReservations(String eventId);

    void putReservation(String eventId, String ticketId, ReservationStatusType statusType);

    void deleteReservation(String eventId, String ticketId);
}
