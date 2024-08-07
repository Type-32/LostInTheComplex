package cn.crtlprototypestudios.litc.utility;

import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.function.Supplier;

public class RegistryEntry<T> implements Supplier<T> {
    private final Registry<? super T> registry;
    private final String name;
    private final Supplier<T> supplier;
    private T value;

    RegistryEntry(Registry<? super T> registry, String name, Supplier<T> supplier) {
        this.registry = registry;
        this.name = name;
        this.supplier = supplier;
    }

    @Override
    public T get() {
        if (value == null) {
            value = Registry.register(registry, Reference.id(name), supplier.get());
            RegisterHelper.LOGGER.info("Registered {} to {}: {}", value.getClass().getSimpleName(), registry.getKey().getValue(), Reference.id(name));
        }
        return value;
    }

    public Identifier getId() {
        return Reference.id(name);
    }
}
