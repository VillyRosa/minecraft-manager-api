package com.villy.minecraftmanager.controller.request;

public record WorldImportRequest(
        String description,
        MinecraftServerConfigurationRequest configuration
) {
}
