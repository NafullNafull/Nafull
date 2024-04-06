package com.nafull.nafull.common.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class WebException extends RuntimeException {
    private final String message;
    private final ErrorCode code;
}
