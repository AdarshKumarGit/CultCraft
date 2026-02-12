package org.chubby.github.cultcraft.content.ritual.compoents;

import java.util.ArrayList;
import java.util.List;

public class RitualBuilder
{
    private BloodType bloodType;
    private int bloodCost;
    private final List<ICatalyst> catalysts = new ArrayList<>();
    private RitualStructure requiredStructure;
    private final List<EnvironmentalCondition> environmentalConditions = new ArrayList<>();
    private final List<IRitualFailEffect> failEffects = new ArrayList<>();
    private RitualCategory ritualCategory;

    public RitualBuilder bloodType(BloodType type)
    {
        this.bloodType = type;
        return this;
    }

    public RitualBuilder bloodCost(int cost)
    {
        this.bloodCost = cost;
        return this;
    }

    public RitualBuilder catalyst(ICatalyst catalyst)
    {
        this.catalysts.add(catalyst);
        return this;
    }

    public RitualBuilder structure(RitualStructure structure)
    {
        this.requiredStructure = structure;
        return this;
    }

    public RitualBuilder environmentalCondition(EnvironmentalCondition condition)
    {
        this.environmentalConditions.add(condition);
        return this;
    }

    public RitualBuilder failEffect(IRitualFailEffect effect)
    {
        this.failEffects.add(effect);
        return this;
    }

    public RitualBuilder category(RitualCategory category)
    {
        this.ritualCategory = category;
        return this;
    }

    public RitualBuilder requireMoonPhase(int phase)
    {
        return environmentalCondition(new EnvironmentalCondition.MoonPhaseCondition(phase));
    }

    public RitualBuilder requireWeather(EnvironmentalCondition.WeatherType weather)
    {
        return environmentalCondition(new EnvironmentalCondition.WeatherCondition(weather));
    }

    public RitualBuilder requireTime(long min, long max)
    {
        return environmentalCondition(new EnvironmentalCondition.TimeCondition(min, max));
    }

    public RitualBuilder requireWitnesses(int min, int max, double radius)
    {
        return environmentalCondition(new EnvironmentalCondition.WitnessCountCondition(min, max, radius));
    }

    public RitualBuilder onFailDamage(float damage)
    {
        return failEffect(new IRitualFailEffect.DamageEffect(damage));
    }

    public RitualBuilder onFailExplosion(float strength, boolean fire)
    {
        return failEffect(new IRitualFailEffect.ExplosionEffect(strength, fire));
    }

    public RitualBuilder onFailCorruption(int radius, int intensity)
    {
        return failEffect(new IRitualFailEffect.CorruptionEffect(radius, intensity));
    }

    protected BloodType getBloodType()
    {
        return bloodType;
    }

    protected int getBloodCost()
    {
        return bloodCost;
    }

    protected List<ICatalyst> getCatalysts()
    {
        return catalysts;
    }

    protected RitualStructure getRequiredStructure()
    {
        return requiredStructure;
    }

    protected List<EnvironmentalCondition> getEnvironmentalConditions()
    {
        return environmentalConditions;
    }

    protected List<IRitualFailEffect> getFailEffects()
    {
        return failEffects;
    }

    protected RitualCategory getRitualCategory()
    {
        return ritualCategory;
    }

    public void validate()
    {
        if (bloodType == null)
        {
            throw new IllegalStateException("Blood type must be set");
        }
        if (bloodCost <= 0)
        {
            throw new IllegalStateException("Blood cost must be positive");
        }
        if (requiredStructure == null)
        {
            throw new IllegalStateException("Ritual structure must be set");
        }
        if (ritualCategory == null)
        {
            throw new IllegalStateException("Ritual category must be set");
        }
    }
}