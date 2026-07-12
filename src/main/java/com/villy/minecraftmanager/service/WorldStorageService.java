package com.villy.minecraftmanager.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class WorldStorageService {

    @Value("${minecraft.worlds-directory}")
    private String worldsDirectory;

    public Path getWorldRootDirectory(UUID worldId) {
        return Paths.get(worldsDirectory, worldId.toString());
    }

    public Path getWorldDirectory(UUID worldId) {
        return Paths.get(worldsDirectory, worldId.toString(), "world");
    }

    public Path requireWorldDirectory(UUID worldId) {
        Path worldDir = getWorldDirectory(worldId);

        if (!Files.exists(worldDir)) {
            throw new IllegalArgumentException("World not found.");
        }

        return worldDir;
    }

    public Path moveWorld(Path source, UUID worldId) throws IOException {
        Path destination = getWorldDirectory(worldId);
        Files.createDirectories(destination.getParent());

        return Files.move(source, destination, StandardCopyOption.REPLACE_EXISTING);
    }

    public void deleteWorldDirectory(UUID worldId) throws IOException {
        Path worldDir = getWorldRootDirectory(worldId);

        if (!Files.exists(worldDir)) {
            return;
        }

        try (Stream<Path> paths = Files.walk(worldDir)) {
            paths.sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }
    }

}
