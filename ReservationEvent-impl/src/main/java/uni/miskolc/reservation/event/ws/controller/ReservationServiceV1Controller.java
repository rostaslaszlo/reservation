package uni.miskolc.reservation.event.ws.controller;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import uni.miskolc.reservation.event.ws.api.ReservationEventServiceV1;
import uni.miskolc.reservation.event.ws.api.dto.ReservationEvent;

@Controller
@RequestMapping(value= {"/v1"})
public class ReservationServiceV1Controller implements ReservationEventServiceV1 {

    @RequestMapping(value= {"/getEvent/{id}"}, method = {RequestMethod.GET})
    @ResponseBody
    @Override
    public ReservationEvent getEvent(@PathVariable String id) {
        System.out.println("getEvent id:" + id);

        ReservationEvent reservationEvent = new ReservationEvent();
        reservationEvent.setEventId(id);
        reservationEvent.setEventLocation("Miskolc, Egyetemváros");
        reservationEvent.setEventName("Beadandó védése");
        reservationEvent.setEventStartDate(new Date());

        return reservationEvent;
    }

    @RequestMapping(value= {"/getLocation/{locationId}"}, method = {RequestMethod.GET})
    @ResponseBody
    @Override
    public String getLocation(@PathVariable String locationId) {
        System.out.println("getLocation locationId:" + locationId);

        throw new RuntimeException("óó jaj");
        //return new String("fdf" + locationId);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public String handleError(Exception exception) {
        return new String("Error: " + exception.toString());
    }
}
