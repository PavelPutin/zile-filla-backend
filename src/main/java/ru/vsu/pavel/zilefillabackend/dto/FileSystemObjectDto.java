package ru.vsu.pavel.zilefillabackend.dto;

public record FileSystemObjectDto(
        FileSystemObjectType type,
        String name,
        FileMetadata metadata
) {
}
