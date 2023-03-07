package io.github.mineria_mc.mineria.common.enchantments;

public enum ElementType {
    NONE(0, 11184810),
    FIRE(1, 14759181),
    WATER(2, 1784051),
    AIR(3, 7526298),
    GROUND(4, 7752762);

    final int id;
    final int color;

    ElementType(int id, int color) {
        this.id = id;
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public int getId() {
        return id;
    }

    public static ElementType byId(int id) {
        return switch (id) {
            case 1 -> FIRE;
            case 2 -> WATER;
            case 3 -> AIR;
            case 4 -> GROUND;
            default -> NONE;
        };
    }
}
