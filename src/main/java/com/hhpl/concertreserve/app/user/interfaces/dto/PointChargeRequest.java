package com.hhpl.concertreserve.app.user.interfaces.dto;

import com.hhpl.concertreserve.app.user.domain.PointStatus;

public record PointChargeRequest(
        int amount,
        PointStatus status
) {
}
