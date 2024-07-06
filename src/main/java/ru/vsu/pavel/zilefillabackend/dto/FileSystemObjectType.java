package ru.vsu.pavel.zilefillabackend.dto;

public enum FileSystemObjectType {
    DIRECTORY("dir"), FILE("file");

    public final String dtoValue;
    FileSystemObjectType(final String dtoValue) {
        this.dtoValue = dtoValue;
    }
}
