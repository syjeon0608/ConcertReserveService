package com.hhpl.concertreserve.interfaces.dto.payment;

import com.hhpl.concertreserve.domain.user.model.PointStatus;

public record PointChargeRequest(
        int amount,
        PointStatus status
) {
}
