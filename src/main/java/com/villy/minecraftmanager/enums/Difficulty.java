package com.villy.minecraftmanager.enums;

public enum Difficulty {
    PEACEFUL(0),
    EASY(1),
    NORMAL(2),
    HARD(3);

    private final int id;

    Difficulty(int id) {
        this.id = id;
    }

    public static Difficulty fromId(int id) {
        return switch (id) {
            case 0 -> PEACEFUL;
            case 1 -> EASY;
            case 2 -> NORMAL;
            case 3 -> HARD;
            default -> throw new IllegalArgumentException(
                    "Unknown difficulty id: " + id
            );
        };
    }
}
