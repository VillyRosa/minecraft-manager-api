package com.villy.minecraftmanager.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class WorldExportService {

    @Value("${minecraft.worlds-directory}")
    private String worldsDirectory;

    public Path exportWorld(UUID worldId) throws IOException {
        Path worldPath = findWorldDirectory(worldId);
        Path zipFile = createTemporaryZip(worldId);

        zipDirectory(worldPath, zipFile);

        return zipFile;
    }

    private Path findWorldDirectory(UUID worldId) {
        Path worldPath = Paths.get(worldsDirectory, worldId.toString(), "world");

        if (!Files.exists(worldPath)) {
            throw new IllegalArgumentException("World not found.");
        }

        return worldPath;
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
