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
    private boolean hasModel = false, registerReference = false;
    private net.minecraft.registry.entry.RegistryEntry.Reference<? super T> entry;

    protected RegistryEntry(Registry<? super T> registry, String name, Supplier<T> supplier) {
        this(registry, name, supplier, false, false);
    }

    protected RegistryEntry(Registry<? super T> registry, String name, Supplier<T> supplier, boolean hasModel, boolean registerReference) {
        this.registry = registry;
        this.name = name;
        this.supplier = supplier;
        this.hasModel = hasModel;
        this.registerReference = registerReference;
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
            if(!registerReference) value = Registry.register(registry, RegistryHelper.id(name), supplier.get());
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

    public net.minecraft.registry.entry.RegistryEntry<T> asRegistryEntry() {
        if (entry == null && value == null)
            entry = Registry.registerReference(registry, RegistryHelper.id(name), supplier.get());

        return (net.minecraft.registry.entry.RegistryEntry<T>) entry;
    }

    public boolean hasModel() {
        return hasModel;
    }

    public boolean registerReference() {
        return registerReference;
    }
}
