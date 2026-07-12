package com.villy.minecraftmanager.controller.response;

import com.villy.minecraftmanager.enums.ContainerStatus;
import com.villy.minecraftmanager.enums.Difficulty;
import com.villy.minecraftmanager.enums.GameMode;

import java.time.LocalDateTime;
import java.util.UUID;

public record WorldResponse(
        UUID id,
        String name,
        String address,
        String description,
        String containerId,
        ContainerStatus status,
        GameMode gameMode,
        Difficulty difficulty,
        String seed,
        MinecraftServerConfigurationResponse configuration,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
