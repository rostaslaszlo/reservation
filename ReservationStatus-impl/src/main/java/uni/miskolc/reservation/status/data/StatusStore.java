package uni.miskolc.reservation.status.data;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import uni.miskolc.reservation.status.ws.api.dto.ReservationStatusType;

@Service
public class StatusStore {

    private Map<String, Map<String, ReservationStatusType>> status = new HashMap<>();

    public Map<String, ReservationStatusType> getStatus(String eventId) {
        /*HashMap<String, ReservationStatusType> response = new HashMap<String, ReservationStatusType>();
        response.put("e10j", ReservationStatusType.SOLD);
        response.put("e11j", ReservationStatusType.SOLD);
        response.put("e12j", ReservationStatusType.SOLD);
        response.put("m10k", ReservationStatusType.SOLD);
        response.put("m53k", ReservationStatusType.SOLD);
        response.put("h14b", ReservationStatusType.SOLD);*/

        Map<String, ReservationStatusType> event = this.status.get(eventId);

        if (event == null) {
            return new HashMap<String, ReservationStatusType>();
        }

        return this.status.get(eventId);
    }

    public ReservationStatusType getStatus(String eventId, String ticketId) {
        Map<String, ReservationStatusType> event = this.status.get(eventId);

        if (event == null) {
            return null;
        } else {
            event.get(eventId);
        }


        return ReservationStatusType.INCLUDED;
    }

    public void setStatus(String eventId, String ticketId, ReservationStatusType status) {
        Map<String, ReservationStatusType> event = this.status.get(eventId);

        if (event == null) {
            event = new HashMap<String, ReservationStatusType>();
        }

        event.put(ticketId, status);

        this.status.put(eventId, event);
    }

    public void removeStatus(String eventId, String ticketId) {
        Map<String, ReservationStatusType> event = this.status.get(eventId);

        if (event != null) {
            event.remove(ticketId);

            this.status.put(eventId, event);
        }
    }
}
