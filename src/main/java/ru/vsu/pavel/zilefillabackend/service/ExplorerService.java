package ru.vsu.pavel.zilefillabackend.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.vsu.pavel.zilefillabackend.dto.*;
import ru.vsu.pavel.zilefillabackend.errors.*;
import ru.vsu.pavel.zilefillabackend.util.CopyVisitor;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import static ru.vsu.pavel.zilefillabackend.util.FileSystemUtils.*;

@Service
@AllArgsConstructor
@Slf4j
public class ExplorerService {

    private final FileSystemAccessService fileSystemAccessService;

    public List<FileSystemObjectDto> changeDirectory(Path path) {
        log.debug("FileSystemService.changeDirectory({})", path);
        // TODO: убрать дублирование кода
        var pathInSubTree = fileSystemAccessService.getPathInSubtree(path);
        log.debug("Change directory path in subtree: {}", pathInSubTree);

        // TODO: убрать дублирование кода
        if (!Files.exists(pathInSubTree)) {
            log.warn("'{}' does not exist", path);
            throw new NoSuchFileResponseException(HttpStatus.NOT_FOUND, new NoSuchFileException(path.toString()));
        }

        if (!Files.isDirectory(pathInSubTree)) {
            log.warn("Try get not directory '{}'", path);
            throw new NotDirectoryResponseException(HttpStatus.BAD_REQUEST, new NotDirectoryException(path.toString()));
        }

        var result = new ArrayList<FileSystemObjectDto>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(pathInSubTree)) {
            for (Path file : stream) {
                log.debug("File: {}", file);
                log.debug("File '{file}' readable status {}", Files.isReadable(file));
                var attr = Files.readAttributes(file, BasicFileAttributes.class);
                var sizeData = getDirectorySizeBytes(file);
                var metadata = new FileMetadata(
                        sizeData.value(),
                        sizeData.accurate(),
                        Files.isReadable(file),
                        attr.creationTime().toInstant(),
                        attr.lastAccessTime().toInstant(),
                        attr.lastModifiedTime().toInstant()
                );
                result.add(new FileSystemObjectDto(
                        attr.isDirectory() ? FileSystemObjectType.DIRECTORY.dtoValue : FileSystemObjectType.FILE.dtoValue,
                        file.getFileName().toString(),
                        metadata
                ));
            }
        } catch (AccessDeniedException e) {
            log.warn(e.getMessage(), e);
            throw new FileAccessDeniedResponseException(HttpStatus.FORBIDDEN, path.toString());
        } catch (IOException e) {
            log.warn(e.getMessage(), e);
            throw new IOExceptionResponseException(HttpStatus.INTERNAL_SERVER_ERROR, path.toString());
        }
        log.debug("Change directory result: {}", result);
        result.sort((a, b) -> {

            if (a.type().equals(FileSystemObjectType.DIRECTORY.dtoValue) && b.type().equals(FileSystemObjectType.FILE.dtoValue)) {
                return -1;
            }

            if (a.type().equals(FileSystemObjectType.FILE.dtoValue) && b.type().equals(FileSystemObjectType.DIRECTORY.dtoValue)) {
                return 1;
            }

            return a.name().compareTo(b.name());
        });
        return result;
    }

    public void rename(Path source, RenameDto renameDto) {
        log.debug("FileSystemService.rename({})", source);

        // TODO: убрать дублирование кода
        var pathInSubTree = fileSystemAccessService.getPathInSubtree(source);
        log.debug("Rename source path in subtree '{}'", pathInSubTree);

        // TODO: убрать дублирование кода
        if (!Files.exists(pathInSubTree)) {
            log.warn("'{}' does not exist", source);
            throw new NoSuchFileResponseException(HttpStatus.NOT_FOUND, new NoSuchFileException(source.toString()));
        }

        var target = pathInSubTree.resolveSibling(renameDto.newName());
        if (Files.exists(target)) {
            log.warn("'{}' already exists", target);
            throw new FileAlreadyExistsResponseException(HttpStatus.CONFLICT, source.toString());
        }
        log.debug("Rename '{}' to '{}'", pathInSubTree, target);
        try {
            Files.move(pathInSubTree, target, StandardCopyOption.ATOMIC_MOVE);
        } catch (NoSuchFileException e) {
            log.warn("'{}' doesn't exist", source, e);
            throw new NoSuchFileResponseException(HttpStatus.NOT_FOUND, e);
        } catch (AccessDeniedException e) {
            log.warn("'{}' is not accessible", source, e);
            throw new FileAccessDeniedResponseException(HttpStatus.FORBIDDEN, source.toString());
        } catch (DirectoryNotEmptyException e) {
            log.warn("'{}' is not an empty directory", source, e);
            throw new DirectoryNotEmptyResponseException(HttpStatus.CONFLICT, source.toString());
        } catch (IOException e) {
            log.warn("'{}' is not readable", source, e);
            throw new IOExceptionResponseException(HttpStatus.INTERNAL_SERVER_ERROR, source.toString());
        }
    }

    public void delete(Path path) {
        log.debug("FileSystemService.delete({})", path);

        var pathInSubTree = fileSystemAccessService.getPathInSubtree(path);
        if (fileSystemAccessService.isRoot(pathInSubTree)) {
            throw new FileAccessDeniedResponseException(HttpStatus.FORBIDDEN, "");
        }

        try {
            if (!checkAccessForDelete(pathInSubTree)) {
                log.warn("'{}' has not accessible file", path);
                throw new AccessDeniedException("");
            }
            deleteDirectory(pathInSubTree);
        } catch (NoSuchFileException e) {
            log.warn("'{}' does not exist", path);
            throw new NoSuchFileResponseException(HttpStatus.NOT_FOUND, new NoSuchFileException(path.toString()));
        } catch (AccessDeniedException | SecurityException e) {
            log.warn("'{}' is not accessible", path, e);
            throw new FileAccessDeniedResponseException(HttpStatus.FORBIDDEN, path.toString());
        } catch (DirectoryNotEmptyException e) {
            log.warn("'{}' is not an empty directory", path, e);
            throw new DirectoryNotEmptyResponseException(HttpStatus.CONFLICT, path.toString());
        } catch (IOException e) {
            log.warn("'{}' is not readable", path, e);
            throw new IOExceptionResponseException(HttpStatus.INTERNAL_SERVER_ERROR, path.toString());
        }
    }

    public void copy(Path source, Path target) {
        log.debug("FileSystemService.copy({})", source);

        // TODO: убрать дублирование кода
        var pathInSubTree = fileSystemAccessService.getPathInSubtree(source);
        if (fileSystemAccessService.isRoot(pathInSubTree)) {
            throw new FileAccessDeniedResponseException(HttpStatus.FORBIDDEN, "");
        }
        log.debug("Move source path in subtree '{}'", pathInSubTree);

        // TODO: убрать дублирование кода
        if (!Files.exists(pathInSubTree)) {
            log.warn("'{}' does not exist", source);
            throw new NoSuchFileResponseException(HttpStatus.NOT_FOUND, new NoSuchFileException(source.toString()));
        }

        var targetInSubTree = fileSystemAccessService.getPathInSubtree(target);
        log.debug("Move target path in subtree '{}'", targetInSubTree);

        // TODO: убрать дублирование кода
        if (!Files.exists(targetInSubTree)) {
            log.warn("'{}' does not exist", target);
            throw new NoSuchFileResponseException(HttpStatus.NOT_FOUND, new NoSuchFileException(target.toString()));
        }

        if (!Files.isDirectory(targetInSubTree)) {
            log.warn("'{}' is not a directory", target);
            throw new NotDirectoryResponseException(HttpStatus.BAD_REQUEST, target.toString());
        }

        try {
            if (!Files.isDirectory(pathInSubTree)) {
                log.debug("Copy file '{}' to '{}'", pathInSubTree, targetInSubTree);
                targetInSubTree = targetInSubTree.resolve(pathInSubTree.getFileName());
                Files.copy(pathInSubTree, targetInSubTree, StandardCopyOption.REPLACE_EXISTING);
            } else {
                log.debug("Copy directory '{}' to '{}'", pathInSubTree, targetInSubTree);
                EnumSet<FileVisitOption> options
                        = EnumSet.of(FileVisitOption.FOLLOW_LINKS);
                targetInSubTree = targetInSubTree.resolve(pathInSubTree.getName(pathInSubTree.getNameCount() - 1));
                var copier = new CopyVisitor(pathInSubTree, targetInSubTree);
                Files.walkFileTree(pathInSubTree, options, Integer.MAX_VALUE, copier);
            }
        } catch (NoSuchFileException e) {
            log.warn("'{}' doesn't exist", source, e);
            throw new NoSuchFileResponseException(HttpStatus.NOT_FOUND, e);
        } catch (AccessDeniedException e) {
            log.warn("'{}' is not accessible", source, e);
            throw new FileAccessDeniedResponseException(HttpStatus.FORBIDDEN, source.toString());
        } catch (DirectoryNotEmptyException e) {
            log.warn("'{}' is not an empty directory", source, e);
            throw new DirectoryNotEmptyResponseException(HttpStatus.CONFLICT, source.toString());
        } catch (IOException e) {
            log.warn("'{}' is not readable", source, e);
            throw new IOExceptionResponseException(HttpStatus.INTERNAL_SERVER_ERROR, source.toString());
        }
    }

    public void move(Path actualPath, Path target) {
    }
}
