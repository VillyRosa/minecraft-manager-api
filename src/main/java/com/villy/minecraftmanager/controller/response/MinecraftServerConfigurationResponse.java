package com.villy.minecraftmanager.controller.response;

public record MinecraftServerConfigurationResponse(
        String motd,
        Integer memoryMb,
        Integer maxPlayers,
        Integer viewDistance,
        Integer simulationDistance
) {
}
