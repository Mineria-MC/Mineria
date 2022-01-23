package com.mineria.mod.util;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nullable;
import java.util.function.ToDoubleFunction;

/**
 * De-obfuscated version of {@link RandomPositionGenerator}
 */
public class RandomPositionGeneratorUtil
{
    @Nullable
    public static Vector3d getPos(CreatureEntity entity, int horizontalMovement, int verticalMovement)
    {
        return RandomPositionGenerator.getPos(entity, horizontalMovement, verticalMovement);
    }

    @Nullable
    public static Vector3d getAirPos(CreatureEntity entity, int horizontalMovement, int verticalMovement, int additionalY, @Nullable Vector3d direction, double maxRotAngle)
    {
        return RandomPositionGenerator.getAirPos(entity, horizontalMovement, verticalMovement, additionalY, direction, maxRotAngle);
    }

    @Nullable
    public static Vector3d getLandPos(CreatureEntity entity, int horizontalMovement, int verticalMovement)
    {
        return RandomPositionGenerator.getLandPos(entity, horizontalMovement, verticalMovement);
    }

    @Nullable
    public static Vector3d getLandPos(CreatureEntity entity, int horizontalMovement, int verticalMovement, ToDoubleFunction<BlockPos> walkTargetGetter)
    {
        return RandomPositionGenerator.getLandPos(entity, horizontalMovement, verticalMovement, walkTargetGetter);
    }

    @Nullable
    public static Vector3d getAboveLandPos(CreatureEntity entity, int horizontalMovement, int verticalMovement, Vector3d direction, float maxRotAngle, int aboveSolidMax, int aboveSolidMin)
    {
        return RandomPositionGenerator.getAboveLandPos(entity, horizontalMovement, verticalMovement, direction, maxRotAngle, aboveSolidMax, aboveSolidMin);
    }

    @Nullable
    public static Vector3d getLandPosTowards(CreatureEntity entity, int horizontalMovement, int verticalMovement, Vector3d direction)
    {
        return RandomPositionGenerator.getLandPosTowards(entity, horizontalMovement, verticalMovement, direction);
    }

    @Nullable
    public static Vector3d getPosTowards(CreatureEntity entity, int horizontalMovement, int verticalMovement, Vector3d direction)
    {
        return RandomPositionGenerator.getPosTowards(entity, horizontalMovement, verticalMovement, direction);
    }

    @Nullable
    public static Vector3d getPosTowards(CreatureEntity entity, int horizontalMovement, int verticalMovement, Vector3d direction, double maxRotAngle)
    {
        return RandomPositionGenerator.getPosTowards(entity, horizontalMovement, verticalMovement, direction, maxRotAngle);
    }

    @Nullable
    public static Vector3d getAirPosTowards(CreatureEntity entity, int horizontalMovement, int verticalMovement, int additionalY, Vector3d direction, double maxRotAngle)
    {
        return RandomPositionGenerator.getAirPosTowards(entity, horizontalMovement, verticalMovement, additionalY, direction, maxRotAngle);
    }

    @Nullable
    public static Vector3d getPosAvoid(CreatureEntity entity, int horizontalMovement, int verticalMovement, Vector3d direction)
    {
        return RandomPositionGenerator.getPosAvoid(entity, horizontalMovement, verticalMovement, direction);
    }

    @Nullable
    public static Vector3d getLandPosAvoid(CreatureEntity entity, int horizontalMovement, int verticalMovement, Vector3d direction)
    {
        return RandomPositionGenerator.getLandPosAvoid(entity, horizontalMovement, verticalMovement, direction);
    }

    /*
     * generateRandomPos(CreatureEntity entity, int horizontalMovement, int verticalMovement, int additionalY, @Nullable Vector3d direction, boolean airOnly, double maxRotAngle, ToDoubleFunction<BlockPos> walkTargetGetter, boolean moveUpToAboveSolid, int aboveSolidMin, int aboveSolidMax, boolean stable)
     */
}