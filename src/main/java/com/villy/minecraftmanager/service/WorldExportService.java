package com.villy.minecraftmanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@RequiredArgsConstructor
public class WorldExportService {

    private final WorldStorageService worldStorageService;

    public Path exportWorld(UUID worldId) throws IOException {
        Path worldPath = worldStorageService.requireWorldDirectory(worldId);
        Path zipFile = createTemporaryZip(worldId);

        zipDirectory(worldPath, zipFile);

        return zipFile;
    }

    private Path createTemporaryZip(UUID worldId) throws IOException {
        return Files.createTempFile(
                "world-" + worldId,
                ".zip"
        );
    }

    private void zipDirectory(Path source, Path destination) throws IOException {

        try (ZipOutputStream zipOut = new ZipOutputStream(Files.newOutputStream(destination))) {
            Files.walk(source)
                    .filter(Files::isRegularFile)
                    .forEach(file -> addFileToZip(source, file, zipOut));
        }
    }

    private void addFileToZip(Path source, Path file, ZipOutputStream zipOut) {
        try {
            Path relative = source.relativize(file);
            zipOut.putNextEntry(new ZipEntry(relative.toString()));
            Files.copy(file, zipOut);
            zipOut.closeEntry();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
