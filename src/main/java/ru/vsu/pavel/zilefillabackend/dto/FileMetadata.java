package ru.vsu.pavel.zilefillabackend.dto;

import java.util.Date;

public record FileMetadata(
    int sizeBytes,
    Date creation,
    Date access,
    Date modification
) {
}
