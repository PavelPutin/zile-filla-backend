package ru.vsu.pavel.zilefillabackend.dto;

public record FileSystemObjectDto(
        String type,
        String name,
        FileMetadata metadata
) {
}
