package ru.vsu.pavel.zilefillabackend.dto;

public record MoveCopyDto(
        MoveCopy actionType,
        String target
) {
}
