package org.chubby.github.cultcraft.content.ritual;

import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class RitualRegistry<T extends AbstractRitual> implements Supplier<T>
{

    public static final Map<ResourceLocation,RitualRegistry<? extends AbstractRitual>> REGISTRY = new HashMap<>();
    private final ResourceLocation name;
    private final Supplier<T> supplier;

    public RitualRegistry(ResourceLocation name, Supplier<T> supplier) {
        this.name = name;
        this.supplier = supplier;
        REGISTRY.put(name,this);
    }

    @Override
    public T get() {
        return supplier.get();
    }

    public ResourceLocation getId() {
        return name;
    }

    public static Optional<RitualRegistry<? extends AbstractRitual>> get(ResourceLocation id) {
        return Optional.ofNullable(REGISTRY.get(id));
    }

    public static Collection<RitualRegistry<? extends AbstractRitual>> values() {
        return REGISTRY.values();
    }
}
