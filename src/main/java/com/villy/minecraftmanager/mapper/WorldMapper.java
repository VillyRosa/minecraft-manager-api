package com.villy.minecraftmanager.mapper;

import com.villy.minecraftmanager.controller.request.WorldRequest;
import com.villy.minecraftmanager.controller.response.WorldResponse;
import com.villy.minecraftmanager.entity.World;

public class WorldMapper {

    public static World toEntity(WorldRequest request) {
        return World.builder()
                .name(request.name())
                .description(request.description())
                .build();
    }

    public static WorldResponse toResponse(World carta) {
        return new WorldResponse(
                carta.getId(),
                carta.getName(),
                carta.getDescription(),
                carta.getCreatedAt(),
                carta.getUpdatedAt()
        );
    }

}
