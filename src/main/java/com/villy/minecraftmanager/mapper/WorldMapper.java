package com.villy.minecraftmanager.mapper;

import com.villy.minecraftmanager.controller.request.WorldRequest;
import com.villy.minecraftmanager.controller.response.MinecraftServerConfigurationResponse;
import com.villy.minecraftmanager.controller.response.WorldResponse;
import com.villy.minecraftmanager.entity.World;
import org.springframework.beans.factory.annotation.Value;

public class WorldMapper {

    public static World toEntity(WorldRequest request) {
        return World.builder()
                .name(request.name())
                .motd(request.configuration().motd())
                .description(request.description())
                .gameMode(request.gameMode())
                .difficulty(request.difficulty())
                .seed(request.seed())
                .memoryMb(request.configuration().memoryMb())
                .maxPlayers(request.configuration().maxPlayers())
                .viewDistance(request.configuration().viewDistance())
                .simulationDistance(request.configuration().simulationDistance())
                .build();
    }

    public static WorldResponse toResponse(World world, String host) {
        String address = host + ":" + world.getPort();

        MinecraftServerConfigurationResponse configuration = new MinecraftServerConfigurationResponse(
                world.getMotd(),
                world.getMemoryMb(),
                world.getMaxPlayers(),
                world.getViewDistance(),
                world.getSimulationDistance()
        );

        return new WorldResponse(
                world.getId(),
                world.getName(),
                address,
                world.getDescription(),
                world.getContainerId(),
                world.getStatus(),
                world.getGameMode(),
                world.getDifficulty(),
                world.getSeed(),
                configuration,
                world.getCreatedAt(),
                world.getUpdatedAt()
        );
    }

}
