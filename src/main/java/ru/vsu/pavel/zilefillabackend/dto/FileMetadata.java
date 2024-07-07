package ru.vsu.pavel.zilefillabackend.dto;

import java.time.Instant;

public record FileMetadata(
    long sizeBytes,
    Instant creation,
    Instant access,
    Instant modification
) {
}
