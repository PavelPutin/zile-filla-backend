package ru.vsu.pavel.zilefillabackend.dto;

import java.time.Instant;
import java.util.Date;

public record FileMetadata(
    long sizeBytes,
    Instant creation,
    Instant access,
    Instant modification
) {
}
