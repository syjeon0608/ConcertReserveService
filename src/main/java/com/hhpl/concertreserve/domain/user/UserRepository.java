package com.hhpl.concertreserve.domain.user;

public interface UserRepository {
    Long findUserIdByUuid(String uuid);
}
