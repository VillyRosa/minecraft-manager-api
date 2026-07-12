package com.villy.minecraftmanager.domain;

import com.villy.minecraftmanager.enums.Difficulty;
import com.villy.minecraftmanager.enums.GameMode;

import java.nio.file.Path;

public record ImportedWorld(
        Path worldPath,
        String name,
        String seed,
        GameMode gameMode,
        Difficulty difficulty,
        String minecraftVersion
) {
}
