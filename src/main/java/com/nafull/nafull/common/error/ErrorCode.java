package com.nafull.nafull.common.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND),
    ALREADY_REGISTERED(HttpStatus.CONFLICT),
    LETTER_NOT_FOUND(HttpStatus.NOT_FOUND),
    WRONG_ID_OR_PASSWORD(HttpStatus.UNAUTHORIZED),
    DISCORD_USER_NOT_FOUND(HttpStatus.NOT_FOUND),
    NOT_ENOUGH_WINGS(HttpStatus.CONFLICT);

    private final HttpStatus httpStatus;

    ErrorCode(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
