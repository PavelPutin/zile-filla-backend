package ru.vsu.pavel.zilefillabackend.dto;

import java.time.Instant;

public record FileMetadata(
    long sizeBytes,
    boolean sizeAccurate,
    boolean readable,
    Instant creation,
    Instant access,
    Instant modification
) {
}
