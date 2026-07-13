package com.villy.minecraftmanager.controller.request;

import com.villy.minecraftmanager.enums.Dimension;

public record LocationRequest(
        String name,
        String description,
        Dimension dimension,
        Double x,
        Double y,
        Double z,
        Float yaw,
        Float pitch
) {
}
