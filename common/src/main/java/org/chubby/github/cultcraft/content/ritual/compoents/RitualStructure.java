package org.chubby.github.cultcraft.content.ritual.compoents;

import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import java.util.List;

public class RitualStructure
{
    private final StructureTier tier;
    private final List<StructureComponent> requiredComponents;
    private final int minimumRadius;
    private final int maximumRadius;

    public RitualStructure(StructureTier tier, List<StructureComponent> components, int minRadius, int maxRadius)
    {
        this.tier = tier;
        this.requiredComponents = components;
        this.minimumRadius = minRadius;
        this.maximumRadius = maxRadius;
    }

    public boolean isValid(Level level, BlockPos centerPos)
    {
        for (StructureComponent component : requiredComponents)
        {
            if (!component.isPresent(level, centerPos))
            {
                return false;
            }
        }
        return true;
    }

    public StructureTier getTier()
    {
        return tier;
    }

    public List<StructureComponent> getRequiredComponents()
    {
        return requiredComponents;
    }

    public int getMinimumRadius()
    {
        return minimumRadius;
    }

    public int getMaximumRadius()
    {
        return maximumRadius;
    }

    public enum StructureTier
    {
        TIER_I(1),
        TIER_II(2),
        TIER_III(3);

        private final int level;

        StructureTier(int level)
        {
            this.level = level;
        }

        public int getLevel()
        {
            return level;
        }
    }

    public static class StructureComponent
    {
        private final Block block;
        private final BlockPos relativePos;
        private final boolean optional;

        public StructureComponent(Block block, BlockPos relativePos, boolean optional)
        {
            this.block = block;
            this.relativePos = relativePos;
            this.optional = optional;
        }

        public StructureComponent(Block block, BlockPos relativePos)
        {
            this(block, relativePos, false);
        }

        public boolean isPresent(Level level, BlockPos centerPos)
        {
            BlockPos checkPos = centerPos.offset(relativePos);
            boolean matches = level.getBlockState(checkPos).getBlock().equals(block);
            return optional || matches;
        }

        public Block getBlock()
        {
            return block;
        }

        public BlockPos getRelativePos()
        {
            return relativePos;
        }

        public boolean isOptional()
        {
            return optional;
        }
    }

    public static class Builder
    {
        private StructureTier tier;
        private final List<StructureComponent> components = new java.util.ArrayList<>();
        private int minRadius = 3;
        private int maxRadius = 10;

        public Builder tier(StructureTier tier)
        {
            this.tier = tier;
            return this;
        }

        public Builder component(Block block, BlockPos pos)
        {
            components.add(new StructureComponent(block, pos));
            return this;
        }

        public Builder optionalComponent(Block block, BlockPos pos)
        {
            components.add(new StructureComponent(block, pos, true));
            return this;
        }

        public Builder radius(int min, int max)
        {
            this.minRadius = min;
            this.maxRadius = max;
            return this;
        }

        public RitualStructure build()
        {
            if (tier == null)
            {
                throw new IllegalStateException("Structure tier must be set");
            }
            return new RitualStructure(tier, components, minRadius, maxRadius);
        }
    }
}