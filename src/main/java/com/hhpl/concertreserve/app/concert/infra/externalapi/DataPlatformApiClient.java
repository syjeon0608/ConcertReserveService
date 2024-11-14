package com.hhpl.concertreserve.app.concert.infra.externalapi;

import com.hhpl.concertreserve.app.concert.domain.ReservationInfo;
import org.springframework.stereotype.Component;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@Component
public class DataPlatformApiClient {
    public void sendReservationInfo(ReservationInfo reservationInfo){
        log.info("SEND SUCCESS!!"+ reservationInfo);
    }
}