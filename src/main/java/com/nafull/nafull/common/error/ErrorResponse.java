package com.nafull.nafull.common.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public record ErrorResponse(String message, ErrorCode code) {}
