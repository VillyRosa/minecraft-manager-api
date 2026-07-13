package com.villy.minecraftmanager.controller.response;

import com.villy.minecraftmanager.enums.Dimension;

import java.time.LocalDateTime;
import java.util.UUID;

public record LocationResponse(
        UUID id,
        String name,
        String description,
        Dimension dimension,
        Double x,
        Double y,
        Double z,
        Float yaw,
        Float pitch,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
