package org.chubby.github.cultcraft.content.ritual.targeting;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.ClipContext;
import java.util.Optional;
import java.util.List;
import java.util.Comparator;

public interface ITargetingMethod
{
    Optional<RitualTarget> acquireTarget(Level level, BlockPos ritualPos, Player performer);

    String getDescription();

    class LineOfSightTargeting implements ITargetingMethod
    {
        private final double maxDistance;

        public LineOfSightTargeting(double maxDistance)
        {
            this.maxDistance = maxDistance;
        }

        @Override
        public Optional<RitualTarget> acquireTarget(Level level, BlockPos ritualPos, Player performer)
        {
            Vec3 eyePos = performer.getEyePosition();
            Vec3 lookVec = performer.getLookAngle();
            Vec3 endVec = eyePos.add(lookVec.scale(maxDistance));

            AABB searchBox = new AABB(eyePos, endVec).inflate(2.0);
            List<Entity> entities = level.getEntities(performer, searchBox);

            return entities.stream()
                    .filter(e -> e instanceof LivingEntity)
                    .min(Comparator.comparingDouble(e -> e.distanceToSqr(eyePos)))
                    .map(RitualTarget::entity);
        }

        @Override
        public String getDescription()
        {
            return "Target entity in line of sight (max " + maxDistance + " blocks)";
        }
    }

    class ProximityTargeting implements ITargetingMethod
    {
        private final double radius;
        private final boolean playersOnly;

        public ProximityTargeting(double radius, boolean playersOnly)
        {
            this.radius = radius;
            this.playersOnly = playersOnly;
        }

        @Override
        public Optional<RitualTarget> acquireTarget(Level level, BlockPos ritualPos, Player performer)
        {
            AABB searchBox = new AABB(ritualPos).inflate(radius);
            Vec3 center = Vec3.atCenterOf(ritualPos);

            if (playersOnly)
            {
                List<Player> players = level.getEntitiesOfClass(
                        Player.class,
                        searchBox,
                        p -> !p.equals(performer)
                );

                return players.stream()
                        .min(Comparator.comparingDouble(p -> p.distanceToSqr(center)))
                        .map(RitualTarget::entity);
            }
            else
            {
                List<LivingEntity> entities = level.getEntitiesOfClass(
                        LivingEntity.class,
                        searchBox,
                        e -> !e.equals(performer)
                );

                return entities.stream()
                        .min(Comparator.comparingDouble(e -> e.distanceToSqr(center)))
                        .map(RitualTarget::entity);
            }
        }


        @Override
        public String getDescription()
        {
            return "Target nearest " + (playersOnly ? "player" : "entity") + " within " + radius + " blocks";
        }
    }

    class BloodLinkTargeting implements ITargetingMethod
    {
        @Override
        public Optional<RitualTarget> acquireTarget(Level level, BlockPos ritualPos, Player performer)
        {
            return Optional.empty();
        }

        @Override
        public String getDescription()
        {
            return "Target via blood link (requires blood sample catalyst)";
        }
    }

    class EffigyTargeting implements ITargetingMethod
    {
        @Override
        public Optional<RitualTarget> acquireTarget(Level level, BlockPos ritualPos, Player performer)
        {
            return Optional.empty();
        }

        @Override
        public String getDescription()
        {
            return "Target via effigy (requires personal effigy catalyst)";
        }
    }

    class NamedTargeting implements ITargetingMethod
    {
        private final String targetName;

        public NamedTargeting(String name)
        {
            this.targetName = name;
        }

        @Override
        public Optional<RitualTarget> acquireTarget(Level level, BlockPos ritualPos, Player performer)
        {
            return level.players().stream()
                    .filter(p -> p.getName().getString().equalsIgnoreCase(targetName))
                    .findFirst()
                    .map(RitualTarget::entity);
        }

        @Override
        public String getDescription()
        {
            return "Target player named '" + targetName + "'";
        }
    }

    class CircleBoundTargeting implements ITargetingMethod
    {
        private final double circleRadius;

        public CircleBoundTargeting(double radius)
        {
            this.circleRadius = radius;
        }

        @Override
        public Optional<RitualTarget> acquireTarget(Level level, BlockPos ritualPos, Player performer)
        {
            AABB circleBox = new AABB(ritualPos).inflate(circleRadius, 1.0, circleRadius);
            List<LivingEntity> entities = level.getEntitiesOfClass(
                    LivingEntity.class,
                    circleBox,
                    e -> !e.equals(performer)
            );

            return entities.stream()
                    .filter(e -> {
                        double dist = Math.sqrt(
                                Math.pow(e.getX() - ritualPos.getX(), 2) +
                                        Math.pow(e.getZ() - ritualPos.getZ(), 2)
                        );
                        return dist <= circleRadius;
                    })
                    .findFirst()
                    .map(RitualTarget::entity);
        }

        @Override
        public String getDescription()
        {
            return "Target entity standing within ritual circle";
        }
    }

    class SelfTargeting implements ITargetingMethod
    {
        @Override
        public Optional<RitualTarget> acquireTarget(Level level, BlockPos ritualPos, Player performer)
        {
            return Optional.of(RitualTarget.self());
        }

        @Override
        public String getDescription()
        {
            return "Target yourself";
        }
    }

    class AreaTargeting implements ITargetingMethod
    {
        private final double radius;

        public AreaTargeting(double radius)
        {
            this.radius = radius;
        }

        @Override
        public Optional<RitualTarget> acquireTarget(Level level, BlockPos ritualPos, Player performer)
        {
            return Optional.of(RitualTarget.area(ritualPos, radius));
        }

        @Override
        public String getDescription()
        {
            return "Target area with radius " + radius + " blocks";
        }
    }
}