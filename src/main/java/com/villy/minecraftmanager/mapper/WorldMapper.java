package com.villy.minecraftmanager.mapper;

import com.villy.minecraftmanager.controller.request.WorldRequest;
import com.villy.minecraftmanager.controller.response.WorldResponse;
import com.villy.minecraftmanager.entity.World;

public class WorldMapper {

    public static World toEntity(WorldRequest request) {
        return World.builder()
                .name(request.name())
                .description(request.description())
                .gameMode(request.gameMode())
                .difficulty(request.difficulty())
                .seed(request.seed())
                .build();
    }

    public static WorldResponse toResponse(World carta) {
        return new WorldResponse(
                carta.getId(),
                carta.getName(),
                carta.getDescription(),
                carta.getContainerId(),
                carta.getStatus(),
                carta.getGameMode(),
                carta.getDifficulty(),
                carta.getSeed(),
                carta.getCreatedAt(),
                carta.getUpdatedAt()
        );
    }

}
