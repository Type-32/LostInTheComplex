package cn.crtlprototypestudios.litc.utility;

import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.function.Supplier;

public class ReferenceRegistryEntry<T> implements Supplier<RegistryEntry.Reference<T>> {
    private final Registry<T> registry;
    private final String name;
    private final Supplier<T> supplier;
    private RegistryEntry.Reference<T> value;

    public ReferenceRegistryEntry(Registry<T> registry, String name, Supplier<T> supplier) {
        this.registry = registry;
        this.name = name;
        this.supplier = supplier;
    }

    @Override
    public RegistryEntry.Reference<T> get() {
        if (value == null) {
            value = Registry.registerReference(registry, RegistryHelper.id(name), supplier.get());
        }
        return value;
    }

    public T getValue() {
        return get().value();
    }
}