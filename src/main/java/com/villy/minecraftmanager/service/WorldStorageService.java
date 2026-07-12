package com.villy.minecraftmanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorldStorageService {

    @Value("${minecraft.worlds-directory}")
    private String worldsDirectory;

    private final FileStorageService fileStorageService;

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
        return fileStorageService.move(source, getWorldRootDirectory(worldId));
    }

    public void deleteWorldDirectory(UUID worldId) throws IOException {
        fileStorageService.deleteDirectory(getWorldRootDirectory(worldId));
    }

}
