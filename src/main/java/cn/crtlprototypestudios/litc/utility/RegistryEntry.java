package cn.crtlprototypestudios.litc.utility;

import net.minecraft.potion.Potions;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.function.Supplier;

public class RegistryEntry<T> implements Supplier<T> {
    private final Registry<? super T> registry;
    private final String name;
    private final Supplier<T> supplier;
    private T value;
    private boolean hasModel = false;

    protected RegistryEntry(Registry<? super T> registry, String name, Supplier<T> supplier) {
        this(registry, name, supplier, false);
    }

    protected RegistryEntry(Registry<? super T> registry, String name, Supplier<T> supplier, boolean hasModel) {
        this.registry = registry;
        this.name = name;
        this.supplier = supplier;
        this.hasModel = hasModel;
    }

    /**
     * Retrieves the stored value for this registry entry.
     *
     * If the value is null, this method registers a new value with the registry using the supplied supplier function.
     * The registered value is then stored and returned.
     *
     * @return the stored value for this registry entry
     */
    @Override
    public T get() {
        if (value == null) {
            value = Registry.register(registry, RegistryHelper.id(name), supplier.get());
        }
        return value;
    }

    /**
     * Retrieves the identifier of this RegistryEntry.
     *
     * @return the identifier of this RegistryEntry
     */
    public Identifier getId() {
        return Reference.id(name);
    }

    public boolean hasModel() {
        return hasModel;
    }
}
