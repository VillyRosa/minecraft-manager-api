package com.villy.minecraftmanager.controller.request;

import com.villy.minecraftmanager.enums.Difficulty;
import com.villy.minecraftmanager.enums.GameMode;

public record WorldRequest(
        String name,
        String description,
        GameMode gameMode,
        Difficulty difficulty,
        String seed
) {
}
