package com.hhpl.concertreserve.app.waitingqueue.domain;

import com.hhpl.concertreserve.app.common.error.CoreException;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

import static com.hhpl.concertreserve.app.common.error.ErrorType.UUID_MISSING;
import static com.hhpl.concertreserve.app.common.error.ErrorType.UUID_VALIDATION_FAILED;

@Component
public class WaitingQueueValidator {

    private static final String UUID_REGEX = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
    private static final Pattern UUID_PATTERN = Pattern.compile(UUID_REGEX);

    public void validateUserUuid(String uuid) {
        if (uuid == null || uuid.trim().isEmpty()) {
            throw new CoreException(UUID_MISSING);
        }
        if (!isValidUUID(uuid)){
            throw new CoreException(UUID_VALIDATION_FAILED);
        }
    }

    private  boolean isValidUUID(String uuid) {
        if (uuid == null || uuid.isEmpty()) {
            return false;
        }
        return UUID_PATTERN.matcher(uuid).matches();
    }

}
