package com.hhpl.concertreserve.app.user.api.dto;

import com.hhpl.concertreserve.app.user.domain.PointStatus;

public record PointChargeRequest(
        int amount,
        PointStatus status
) {
}
