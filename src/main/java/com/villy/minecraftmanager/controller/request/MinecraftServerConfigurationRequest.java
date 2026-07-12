package com.villy.minecraftmanager.controller.request;

public record MinecraftServerConfigurationRequest(
        String motd,
        Integer memoryMb,
        Integer maxPlayers,
        Integer viewDistance,
        Integer simulationDistance
) {
}
