package org.chubby.github.cultcraft.content.ritual.targeting;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import java.util.Optional;
import java.util.UUID;

public class RitualTarget
{
    private final RitualTargetMode mode;
    private final int entityId;
    private final BlockPos position;
    private final double areaRadius;
    private final String targetName;

    private RitualTarget(RitualTargetMode mode, int entityId, BlockPos position, double areaRadius, String targetName)
    {
        this.mode = mode;
        this.entityId = entityId;
        this.position = position;
        this.areaRadius = areaRadius;
        this.targetName = targetName;
    }

    public static RitualTarget self()
    {
        return new RitualTarget(RitualTargetMode.SELF, 0, null, 0, null);
    }

    public static RitualTarget entity(Entity entity)
    {
        return new RitualTarget(RitualTargetMode.TARGET_ENTITY, entity.getId(), entity.blockPosition(), 0, entity.getName().getString());
    }

    public static RitualTarget area(BlockPos center, double radius)
    {
        return new RitualTarget(RitualTargetMode.TARGET_AREA, 0, center, radius, null);
    }

    public static RitualTarget location(BlockPos pos)
    {
        return new RitualTarget(RitualTargetMode.TARGET_LOCATION, 0, pos, 0, null);
    }

    public static RitualTarget bound(int entityId, String name)
    {
        return new RitualTarget(RitualTargetMode.TARGET_BOUND, entityId, null, 0, name);
    }

    public static RitualTarget effigy(String targetName)
    {
        return new RitualTarget(RitualTargetMode.TARGET_EFFIGY, 0, null, 0, targetName);
    }

    public static RitualTarget bloodLink(int entityId)
    {
        return new RitualTarget(RitualTargetMode.TARGET_BLOOD_LINK, entityId, null, 0, null);
    }

    public static RitualTarget global()
    {
        return new RitualTarget(RitualTargetMode.GLOBAL, 0, null, 0, null);
    }

    public RitualTargetMode getMode()
    {
        return mode;
    }

    public int getEntityId()
    {
        return entityId;
    }

    public Optional<BlockPos> getPosition()
    {
        return Optional.ofNullable(position);
    }

    public double getAreaRadius()
    {
        return areaRadius;
    }

    public Optional<String> getTargetName()
    {
        return Optional.ofNullable(targetName);
    }

    public Optional<Entity> resolveEntity(Level level)
    {
        return Optional.ofNullable(level.getEntity(entityId));
    }

    public boolean isValid(Level level)
    {
        return switch (mode)
        {
            case SELF, GLOBAL -> true;
            case TARGET_ENTITY, TARGET_BOUND, TARGET_BLOOD_LINK -> resolveEntity(level).isPresent();
            case TARGET_AREA, TARGET_LOCATION -> position != null;
            case TARGET_EFFIGY -> targetName != null && !targetName.isEmpty();
        };
    }
}