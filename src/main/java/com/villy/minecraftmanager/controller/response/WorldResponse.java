package com.villy.minecraftmanager.controller.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record WorldResponse(UUID id, String name, String description, LocalDateTime createdAt, LocalDateTime updatedAt) {
}
