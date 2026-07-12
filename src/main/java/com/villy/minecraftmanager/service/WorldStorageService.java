package com.villy.minecraftmanager.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class WorldStorageService {

    @Value("${minecraft.worlds-directory}")
    private String worldsDirectory;

    public void deleteWorldDirectory(UUID worldId) throws IOException {
        Path worldDir = Paths.get(worldsDirectory, worldId.toString());

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
