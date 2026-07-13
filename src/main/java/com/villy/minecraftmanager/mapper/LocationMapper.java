package com.villy.minecraftmanager.mapper;

import com.villy.minecraftmanager.controller.request.LocationRequest;
import com.villy.minecraftmanager.controller.response.LocationDetailsResponse;
import com.villy.minecraftmanager.controller.response.LocationResponse;
import com.villy.minecraftmanager.controller.response.WorldSummaryResponse;
import com.villy.minecraftmanager.entity.Location;
import com.villy.minecraftmanager.entity.World;

public class LocationMapper {

    public static Location toEntity(LocationRequest request, World world) {
        return Location.builder()
                .name(request.name())
                .world(world)
                .description(request.description())
                .dimension(request.dimension())
                .x(request.x())
                .y(request.y())
                .z(request.z())
                .yaw(request.yaw())
                .pitch(request.pitch())
                .build();
    }

    public static LocationResponse toResponse(Location location) {
        return new LocationResponse(
                location.getId(),
                location.getName(),
                location.getDescription(),
                location.getDimension(),
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getYaw(),
                location.getPitch(),
                location.getCreatedAt(),
                location.getUpdatedAt()
        );
    }

    public static LocationDetailsResponse toDetailResponse(Location location) {
        WorldSummaryResponse world = new WorldSummaryResponse(
                location.getWorld().getId(),
                location.getWorld().getName()
        );

        return new LocationDetailsResponse(
                location.getId(),
                world,
                location.getName(),
                location.getDescription(),
                location.getDimension(),
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getYaw(),
                location.getPitch(),
                location.getCreatedAt(),
                location.getUpdatedAt()
        );
    }

}
