package cn.crtlprototypestudios.litc.utility;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class RegistryHelper {
    public static final Logger LOGGER = LoggerFactory.getLogger(RegistryHelper.class);
    private static final Map<Registry<?>, List<RegistryEntry<?>>> DEFERRED_REGISTERS = new HashMap<>();
    private static Item.Settings DEFAULT_ITEM_SETTINGS = new Item.Settings();
    private static AbstractBlock.Settings DEFAULT_ABSTRACT_BLOCK_SETTINGS = AbstractBlock.Settings.create();

    // Item registration
    public static class ItemBuilder {
        private final String name;
        private final Item.Settings settings;

        private ItemBuilder(String name) {
            this.name = name;
            this.settings = new Item.Settings();
        }

        /**
         * Sets the settings for an item.
         *
         * This method takes a consumer function as a parameter that accepts an {@code Item.Settings} object. The consumer
         * function allows modifying the settings object to customize the behavior of the item. The modified settings object
         * is then applied to the item builder.
         *
         * @param settingsModifier the consumer function that modifies the item settings
         * @return the item builder with the modified settings applied
         */
        public ItemBuilder settings(Consumer<Item.Settings> settingsModifier) {
            settingsModifier.accept(this.settings);
            return this;
        }

        /**
         * Builds the item with the specified settings.
         *
         * @return the registry entry for the item
         */
        public RegistryEntry<Item> build() {
            return register(Registries.ITEM, name, () -> new Item(settings));
        }

        /**
         * Builds the item using a custom factory function.
         *
         * @param <T> the type of the item
         * @param factory the factory function to create the item
         * @return the registry entry for the item
         */
        public <T extends Item> RegistryEntry<T> build(Function<Item.Settings, T> factory) {
            return register(Registries.ITEM, name, () -> factory.apply(settings));
        }
    }

    // Block registration
    public static class BlockBuilder {
        private final String name;
        private AbstractBlock.Settings blockSettings;
        private Item.Settings itemSettings;
        private boolean registerItem = false;

        private BlockBuilder(String name) {
            this.name = name;
            this.itemSettings = DEFAULT_ITEM_SETTINGS;
            this.blockSettings = DEFAULT_ABSTRACT_BLOCK_SETTINGS;
        }

        /**
         * Sets the item settings for the block.
         *
         * @param settings the settings for the item
         * @return the block builder with the item settings applied
         */
        public BlockBuilder item(Item.Settings settings) {
            registerItem = true;
            this.itemSettings = settings;
            return this;
        }

        /**
         * Sets the item settings using a consumer function.
         *
         * @param settingsModifier the consumer function that modifies the item settings
         * @return the block builder with the item settings applied
         */
        public BlockBuilder item(Consumer<Item.Settings> settingsModifier) {
            registerItem = true;
            settingsModifier.accept(this.itemSettings);
            return this;
        }

        /**
         * Enables item registration for the block.
         *
         * @return the block builder with item registration enabled
         */
        public BlockBuilder item() {
            registerItem = true;
            this.itemSettings = DEFAULT_ITEM_SETTINGS;
            return this;
        }

        /**
         * Sets the block settings using a consumer function.
         *
         * @param settingsModifier the consumer function that modifies the block settings
         * @return the block builder with the block settings applied
         */
        public BlockBuilder settings(Consumer<AbstractBlock.Settings> settingsModifier) {
            settingsModifier.accept(this.blockSettings);
            return this;
        }

        /**
         * Sets the block settings by copying another block's settings.
         *
         * @param copyBlock the block to copy settings from
         * @return the block builder with the copied block settings applied
         */
        public BlockBuilder settings(Block copyBlock) {
            this.blockSettings = AbstractBlock.Settings.copy(copyBlock);
            return this;
        }

        /**
         * Builds the block with the specified settings.
         *
         * @return the registry entry for the block
         */
        public RegistryEntry<Block> build() {
            RegistryEntry<Block> blockEntry = register(Registries.BLOCK, name, () -> new Block(blockSettings));
            if (registerItem) {
                register(Registries.ITEM, name, () -> new BlockItem(blockEntry.get(), itemSettings));
            }
            return blockEntry;
        }

        /**
         * Builds the block using a custom factory function.
         *
         * @param <T> the type of the block
         * @param factory the factory function to create the block
         * @return the registry entry for the block
         */
        public <T extends Block> RegistryEntry<T> build(Function<AbstractBlock.Settings, T> factory) {
            RegistryEntry<T> blockEntry = register(Registries.BLOCK, name, () -> factory.apply(blockSettings));
            if (registerItem) {
                register(Registries.ITEM, name, () -> new BlockItem(blockEntry.get(), itemSettings));
            }
            return blockEntry;
        }
    }

    /**
     * Creates a new item builder with the specified name.
     *
     * @param name the name of the item
     * @return the new item builder
     */
    public static ItemBuilder item(String name) {
        return new ItemBuilder(name);
    }

    /**
     * Creates a new block builder with the specified name.
     *
     * @param name the name of the block
     * @return the new block builder
     */
    public static BlockBuilder block(String name) {
        return new BlockBuilder(name);
    }

    // Block Entity registration
    /**
     * Registers a new block entity type with the specified name and builder.
     *
     * @param <T> the type of the block entity
     * @param name the name of the block entity type
     * @param builder the builder for the block entity type
     * @return the registry entry for the block entity type
     */
    public static <T extends BlockEntity> RegistryEntry<BlockEntityType<T>> blockEntity(String name, BlockEntityType.Builder<T> builder) {
        return register(Registries.BLOCK_ENTITY_TYPE, name, () -> builder.build(null));
    }

    // Entity registration
    /**
     * Registers a new entity type with the specified name.
     *
     * @param <T> the type of the entity
     * @param name the name of the entity type
     * @param builder the builder for the entity type
     * @return the registry entry for the entity type
     */
    public static <T extends Entity> RegistryEntry<EntityType<T>> entity(String name, EntityType.Builder<T> builder) {
        return register(Registries.ENTITY_TYPE, name, () -> builder.build(name));
    }

    /**
     * Registers a fluid with the specified name and supplier.
     *
     * @param <T> the type of the fluid (must extend FlowableFluid)
     * @param name the name of the fluid
     * @param fluidSupplier a supplier for creating instances of the fluid
     * @return the registry entry for the fluid
     */
    // Fluid registration
    public static <T extends FlowableFluid> RegistryEntry<T> fluid(String name, Supplier<T> fluidSupplier) {
        return register(Registries.FLUID, name, fluidSupplier);
    }

    /**
     * Registers a new screen handler (menu) type with the specified name, factory, and feature set.
     *
     * @param <T> the type of the screen handler
     * @param name the name of the screen handler type
     * @param factory the factory for the screen handler type
     * @param featureSet the feature set for the screen handler type
     * @return the registry entry for the screen handler type
     */
    // Screen Handler (Menu) registration
    public static <T extends ScreenHandler> RegistryEntry<ScreenHandlerType<T>> screenHandler(String name, ScreenHandlerType.Factory<T> factory, FeatureSet featureSet) {
        return register(Registries.SCREEN_HANDLER, name, () -> new ScreenHandlerType<>(factory, featureSet));
    }

    /**
     * Registers a new entry in the given registry with the specified name and supplier.
     * The supplier is used to create an instance of the entry in the registry.
     *
     * @param <T>      the type of the entry
     * @param registry the registry to register the entry in
     * @param name     the name of the entry
     * @param supplier the supplier to create an instance of the entry
     * @return the registry entry for the entry that was registered
     */
    private static <T> RegistryEntry<T> register(Registry<? super T> registry, String name, Supplier<T> supplier) {
        RegistryEntry<T> entry = new RegistryEntry<>(registry, name, supplier);
        DEFERRED_REGISTERS.computeIfAbsent(registry, k -> new ArrayList<>()).add(entry);
        return entry;
    }

    /**
     * Method `registerAll` is responsible for triggering the registration of all deferred entries in the registry.
     * It iterates over the values of the `DEFERRED_REGISTERS` map, which stores a list of deferred entries for each registry.
     * For each list of entries, it calls the `get` method on each entry, which triggers the actual registration by calling the `Registry.register` method.
     * After all registrations are complete, the `DEFERRED_REGISTERS` map is cleared.
     *
     * The method does not return anything.
     */
    public static void registerAll() {
        for (List<RegistryEntry<?>> entries : DEFERRED_REGISTERS.values()) {
            for (RegistryEntry<?> entry : entries) {
                entry.get(); // This triggers the actual registration
            }
        }
        DEFERRED_REGISTERS.clear();
    }
}