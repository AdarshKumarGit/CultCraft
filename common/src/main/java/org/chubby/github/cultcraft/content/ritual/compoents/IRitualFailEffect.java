package org.chubby.github.cultcraft.content.ritual.compoents;

import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffect;

public interface IRitualFailEffect
{
    void apply(Level level, BlockPos pos, Player performer);

    String getDescription();

    class DamageEffect implements IRitualFailEffect
    {
        private final float damageAmount;

        public DamageEffect(float damage)
        {
            this.damageAmount = damage;
        }

        @Override
        public void apply(Level level, BlockPos pos, Player performer)
        {
            performer.hurt(level.damageSources().magic(), damageAmount);
        }

        @Override
        public String getDescription()
        {
            return "Inflicts " + damageAmount + " damage";
        }
    }

    class ExplosionEffect implements IRitualFailEffect
    {
        private final float strength;
        private final boolean causeFire;

        public ExplosionEffect(float strength, boolean fire)
        {
            this.strength = strength;
            this.causeFire = fire;
        }

        @Override
        public void apply(Level level, BlockPos pos, Player performer)
        {
            level.explode(
                    performer,
                    pos.getX(),
                    pos.getY(),
                    pos.getZ(),
                    strength,
                    causeFire,
                    Level.ExplosionInteraction.BLOCK
            );
        }

        @Override
        public String getDescription()
        {
            return "Causes explosion (strength: " + strength + ")";
        }
    }

    class StatusEffect implements IRitualFailEffect
    {
        private final MobEffect effect;
        private final int duration;
        private final int amplifier;

        public StatusEffect(MobEffect effect, int duration, int amplifier)
        {
            this.effect = effect;
            this.duration = duration;
            this.amplifier = amplifier;
        }

        @Override
        public void apply(Level level, BlockPos pos, Player performer)
        {
            performer.addEffect(new MobEffectInstance(effect, duration, amplifier));
        }

        @Override
        public String getDescription()
        {
            return "Applies status effect";
        }
    }

    class CorruptionEffect implements IRitualFailEffect
    {
        private final int radius;
        private final int intensity;

        public CorruptionEffect(int radius, int intensity)
        {
            this.radius = radius;
            this.intensity = intensity;
        }

        @Override
        public void apply(Level level, BlockPos pos, Player performer)
        {
            for (BlockPos affectedPos : BlockPos.betweenClosed(
                    pos.offset(-radius, -radius, -radius),
                    pos.offset(radius, radius, radius)))
            {
                if (level.random.nextInt(100) < intensity)
                {

                }
            }
        }

        @Override
        public String getDescription()
        {
            return "Corrupts nearby area";
        }
    }

    class BloodLossEffect implements IRitualFailEffect
    {
        private final int bloodAmount;

        public BloodLossEffect(int amount)
        {
            this.bloodAmount = amount;
        }

        @Override
        public void apply(Level level, BlockPos pos, Player performer)
        {

        }

        @Override
        public String getDescription()
        {
            return "Drains " + bloodAmount + " blood";
        }
    }

    class AffinityShiftEffect implements IRitualFailEffect
    {
        private final int affinityChange;

        public AffinityShiftEffect(int change)
        {
            this.affinityChange = change;
        }

        @Override
        public void apply(Level level, BlockPos pos, Player performer)
        {

        }

        @Override
        public String getDescription()
        {
            return (affinityChange > 0 ? "Increases" : "Decreases") + " affinity";
        }
    }

    class SummonHostileEffect implements IRitualFailEffect
    {
        private final String entityType;
        private final int count;

        public SummonHostileEffect(String entityType, int count)
        {
            this.entityType = entityType;
            this.count = count;
        }

        @Override
        public void apply(Level level, BlockPos pos, Player performer)
        {

        }

        @Override
        public String getDescription()
        {
            return "Summons " + count + " hostile entities";
        }
    }

    class DelayedEffect implements IRitualFailEffect
    {
        private final IRitualFailEffect delayedEffect;
        private final int ticksDelay;

        public DelayedEffect(IRitualFailEffect effect, int delay)
        {
            this.delayedEffect = effect;
            this.ticksDelay = delay;
        }

        @Override
        public void apply(Level level, BlockPos pos, Player performer)
        {

        }

        @Override
        public String getDescription()
        {
            return "Delayed: " + delayedEffect.getDescription();
        }
    }
}