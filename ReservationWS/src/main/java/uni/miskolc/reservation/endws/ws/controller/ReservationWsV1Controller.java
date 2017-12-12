package uni.miskolc.reservation.endws.ws.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import uni.miskolc.reservation.endws.ws.api.ReservationWsV1;
import uni.miskolc.reservation.event.ws.api.ReservationEventServiceV1;
import uni.miskolc.reservation.event.ws.api.dto.ReservationEvent;
import uni.miskolc.reservation.status.ws.api.ReservationStatusServiceV1;
import uni.miskolc.reservation.status.ws.api.dto.ReservationStatusType;

@Controller
@RequestMapping(value= {"/v1"})
public class ReservationWsV1Controller implements ReservationWsV1 {

    @Autowired
    ReservationEventServiceV1 reservationServiceV1;

    @Autowired
    ReservationStatusServiceV1 reservationStatusServiceV1;

    @RequestMapping(value= {"/getEvent/{id}"}, method = {RequestMethod.GET})
    @ResponseBody
    public String getEvent(@PathVariable String id) {

        ReservationEvent content = reservationServiceV1.getEvent(id);

        try {
            System.out.println(reservationServiceV1.getLocation(id));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println(e.getMessage());
        }

        System.out.println(content.getEventName());


        reservationStatusServiceV1.putReservation(id, "abc1223", ReservationStatusType.SELECTED);

        //System.out.println(reservationStatusServiceV1.getReservations(id));

        return content.getEventName();
    }
}
