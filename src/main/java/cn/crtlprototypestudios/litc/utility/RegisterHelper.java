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

public class RegisterHelper {
    public static final Logger LOGGER = LoggerFactory.getLogger(RegisterHelper.class);
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

        public ItemBuilder settings(Consumer<Item.Settings> settingsModifier) {
            settingsModifier.accept(this.settings);
            return this;
        }

        public RegistryEntry<Item> build() {
            return register(Registries.ITEM, name, () -> new Item(settings));
        }

        public <T extends Item> RegistryEntry<T> build(Function<Item.Settings, T> factory) {
            return register(Registries.ITEM, name, () -> factory.apply(settings));
        }
    }

    public static ItemBuilder item(String name) {
        return new ItemBuilder(name);
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

        public BlockBuilder item(Item.Settings settings) {
            registerItem = true;
            this.itemSettings = settings;
            return this;
        }

        public BlockBuilder item(Consumer<Item.Settings> settingsModifier) {
            registerItem = true;
            settingsModifier.accept(this.itemSettings);
            return this;
        }

        public BlockBuilder item() {
            registerItem = true;
            this.itemSettings = DEFAULT_ITEM_SETTINGS;
            return this;
        }

        public BlockBuilder settings(Consumer<AbstractBlock.Settings> settingsModifier) {
            settingsModifier.accept(this.blockSettings);
            return this;
        }

        public BlockBuilder settings(Block copyBlock) {
            this.blockSettings = AbstractBlock.Settings.copy(copyBlock);
            return this;
        }

        public RegistryEntry<Block> build() {
            RegistryEntry<Block> blockEntry = register(Registries.BLOCK, name, () -> new Block(blockSettings));
            if (registerItem) {
                register(Registries.ITEM, name, () -> new BlockItem(blockEntry.get(), itemSettings));
            }
            return blockEntry;
        }

        public <T extends Block> RegistryEntry<T> build(Function<AbstractBlock.Settings, T> factory) {
            RegistryEntry<T> blockEntry = register(Registries.BLOCK, name, () -> factory.apply(blockSettings));
            if (registerItem) {
                register(Registries.ITEM, name, () -> new BlockItem(blockEntry.get(), itemSettings));
            }
            return blockEntry;
        }
    }

    public static BlockBuilder block(String name) {
        return new BlockBuilder(name);
    }

    // Block Entity registration
    public static <T extends BlockEntity> RegistryEntry<BlockEntityType<T>> blockEntity(String name, BlockEntityType.Builder<T> builder) {
        return register(Registries.BLOCK_ENTITY_TYPE, name, () -> builder.build(null));
    }

    // Entity registration
    public static <T extends Entity> RegistryEntry<EntityType<T>> entity(String name, EntityType.Builder<T> builder) {
        return register(Registries.ENTITY_TYPE, name, () -> builder.build(name));
    }

    // Fluid registration
    public static <T extends FlowableFluid> RegistryEntry<T> fluid(String name, Supplier<T> fluidSupplier) {
        return register(Registries.FLUID, name, fluidSupplier);
    }

    // Screen Handler (Menu) registration
    public static <T extends ScreenHandler> RegistryEntry<ScreenHandlerType<T>> screenHandler(String name, ScreenHandlerType.Factory<T> factory, FeatureSet featureSet) {
        return register(Registries.SCREEN_HANDLER, name, () -> new ScreenHandlerType<>(factory, featureSet));
    }

    private static <T> RegistryEntry<T> register(Registry<? super T> registry, String name, Supplier<T> supplier) {
        RegistryEntry<T> entry = new RegistryEntry<>(registry, name, supplier);
        DEFERRED_REGISTERS.computeIfAbsent(registry, k -> new ArrayList<>()).add(entry);
        return entry;
    }

    public static void registerAll() {
        for (List<RegistryEntry<?>> entries : DEFERRED_REGISTERS.values()) {
            for (RegistryEntry<?> entry : entries) {
                entry.get(); // This triggers the actual registration
            }
        }
        DEFERRED_REGISTERS.clear();
    }
}