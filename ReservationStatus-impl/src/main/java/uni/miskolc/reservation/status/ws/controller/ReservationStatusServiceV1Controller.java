package uni.miskolc.reservation.status.ws.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import uni.miskolc.reservation.status.data.StatusStore;
import uni.miskolc.reservation.status.ws.api.ReservationStatusServiceV1;
import uni.miskolc.reservation.status.ws.api.dto.ReservationStatusType;

@Controller
@RequestMapping(value= {"/v1"}, method = {RequestMethod.PUT, RequestMethod.GET, RequestMethod.DELETE})
public class ReservationStatusServiceV1Controller implements ReservationStatusServiceV1 {

    @Autowired
    private StatusStore statusStore;
    
    @RequestMapping(value= {"/getReservations/event/{eventId}"}, method = {RequestMethod.GET})
    @ResponseBody
    @Override
    public Map<String, ReservationStatusType> getReservations(@PathVariable String eventId) {
        return statusStore.getStatus(eventId);
    }

    @RequestMapping(value= {"/reservation/{statusType}/event/{eventId}/ticketId/{ticketId}"}, method = {RequestMethod.GET})
    @ResponseBody
    @Override
    public void putReservation(@PathVariable String eventId, @PathVariable String ticketId, @PathVariable ReservationStatusType statusType) {
        statusStore.setStatus(eventId, ticketId, statusType);
    }
    
    @RequestMapping(value= {"/reservation/event/{eventId}/ticketId/{ticketId}"}, method = {RequestMethod.DELETE})
    @ResponseBody
    @Override
    public void deleteReservation(@PathVariable String eventId, @PathVariable String ticketId) {
        statusStore.removeStatus(eventId, ticketId);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public String handleError(Exception exception) {
        return new String("Error: " + exception.toString());
    }
}
