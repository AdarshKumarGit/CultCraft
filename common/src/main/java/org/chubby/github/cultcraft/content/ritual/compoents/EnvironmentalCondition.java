package org.chubby.github.cultcraft.content.ritual.compoents;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;

public abstract class EnvironmentalCondition
{
    public abstract boolean isMet(Level level, BlockPos pos);

    public abstract String getDescription();

    public static class BiomeCondition extends EnvironmentalCondition
    {
        private final Biome requiredBiome;

        public BiomeCondition(Biome biome)
        {
            this.requiredBiome = biome;
        }

        @Override
        public boolean isMet(Level level, BlockPos pos)
        {
            return level.getBiome(pos).value().equals(requiredBiome);
        }

        @Override
        public String getDescription()
        {
            return "Requires specific biome";
        }
    }

    public static class MoonPhaseCondition extends EnvironmentalCondition
    {
        private final int requiredPhase;

        public MoonPhaseCondition(int phase)
        {
            this.requiredPhase = phase;
        }

        @Override
        public boolean isMet(Level level, BlockPos pos)
        {
            return level.getMoonPhase() == requiredPhase;
        }

        @Override
        public String getDescription()
        {
            return "Requires moon phase: " + requiredPhase;
        }
    }

    public static class WeatherCondition extends EnvironmentalCondition
    {
        private final WeatherType requiredWeather;

        public WeatherCondition(WeatherType weather)
        {
            this.requiredWeather = weather;
        }

        @Override
        public boolean isMet(Level level, BlockPos pos)
        {
            return switch (requiredWeather)
            {
                case CLEAR -> !level.isRaining() && !level.isThundering();
                case RAIN -> level.isRaining() && !level.isThundering();
                case THUNDER -> level.isThundering();
            };
        }

        @Override
        public String getDescription()
        {
            return "Requires weather: " + requiredWeather;
        }
    }

    public static class WitnessCountCondition extends EnvironmentalCondition
    {
        private final int minWitnesses;
        private final int maxWitnesses;
        private final double radius;

        public WitnessCountCondition(int min, int max, double radius)
        {
            this.minWitnesses = min;
            this.maxWitnesses = max;
            this.radius = radius;
        }

        @Override
        public boolean isMet(Level level, BlockPos pos)
        {
            int witnessCount = level.getEntitiesOfClass(
                    LivingEntity.class,
                    new AABB(pos).inflate(radius)
            ).size();

            return witnessCount >= minWitnesses && witnessCount <= maxWitnesses;
        }

        @Override
        public String getDescription()
        {
            return "Requires " + minWitnesses + "-" + maxWitnesses + " witnesses";
        }
    }

    public static class NearbyBlockCondition extends EnvironmentalCondition
    {
        private final Block requiredBlock;
        private final int minCount;
        private final int searchRadius;

        public NearbyBlockCondition(Block block, int count, int radius)
        {
            this.requiredBlock = block;
            this.minCount = count;
            this.searchRadius = radius;
        }

        @Override
        public boolean isMet(Level level, BlockPos pos)
        {
            int count = 0;
            for (BlockPos checkPos : BlockPos.betweenClosed(
                    pos.offset(-searchRadius, -searchRadius, -searchRadius),
                    pos.offset(searchRadius, searchRadius, searchRadius)))
            {
                if (level.getBlockState(checkPos).getBlock().equals(requiredBlock))
                {
                    count++;
                    if (count >= minCount) return true;
                }
            }
            return false;
        }

        @Override
        public String getDescription()
        {
            return "Requires " + minCount + " nearby blocks";
        }
    }

    public static class TimeCondition extends EnvironmentalCondition
    {
        private final long minTime;
        private final long maxTime;

        public TimeCondition(long min, long max)
        {
            this.minTime = min;
            this.maxTime = max;
        }

        @Override
        public boolean isMet(Level level, BlockPos pos)
        {
            long dayTime = level.getDayTime() % 24000;
            return dayTime >= minTime && dayTime <= maxTime;
        }

        @Override
        public String getDescription()
        {
            return "Requires specific time of day";
        }
    }

    public enum WeatherType
    {
        CLEAR,
        RAIN,
        THUNDER
    }
}