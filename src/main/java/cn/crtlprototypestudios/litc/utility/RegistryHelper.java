package cn.crtlprototypestudios.litc.utility;

import cn.crtlprototypestudios.litc.LostInTheComplex;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.component.ComponentType;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.StatFormatter;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class RegistryHelper {
    public static final Logger LOGGER = LoggerFactory.getLogger(RegistryHelper.class);
    private static final Map<Registry<?>, List<RegistryEntry<?>>> DEFERRED_REGISTERS = new HashMap<>();
    private static Item.Settings DEFAULT_ITEM_SETTINGS = new Item.Settings();
    private static AbstractBlock.Settings DEFAULT_ABSTRACT_BLOCK_SETTINGS = AbstractBlock.Settings.create();
    private static String MOD_ID = "litc";

    // Item registration
    public static class ItemBuilder {
        private final String name;
        protected boolean hasModel = false;
        private final Item.Settings settings;
        protected ItemGroup group;

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

        public <T> ItemBuilder component(ComponentType<T> componentType, T value) {
            this.settings.component(componentType, value);
            return this;
        }

        public ItemBuilder group(ItemGroup group) {
            this.group = group;
            return this;
        }

        public ItemBuilder hasModel(){
            hasModel = true;
            return this;
        }

        /**
         * Builds the item with the specified settings.
         *
         * @return the registry entry for the item
         */
        public RegistryEntry<Item> build() {
            return register(Registries.ITEM, name, () -> new Item(settings), hasModel);
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
        protected final String name;
        protected AbstractBlock.Settings blockSettings;
        protected Item.Settings itemSettings;
        protected boolean registerItem = false;

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

    public static class ItemGroupBuilder {
        protected final String name;
        protected ItemStack stack;
        protected List<ItemStack> entries;

        private ItemGroupBuilder(String name, Item icon) {
            this.name = name;
            icon(icon);
            entries = new ArrayList<>();
        }

        public ItemGroupBuilder icon(Item icon) {
            this.stack = new ItemStack(icon);
            return this;
        }

        public ItemGroupBuilder entry(Item item) {
            entries.add(new ItemStack(item));
            return this;
        }

        public ItemGroupBuilder entries(Item... items){
            for(Item item : items){
                entry(item);
            }
            return this;
        }

        public ItemGroupBuilder entries(List<Item> items){
            entries(items.toArray(new Item[0]));
            return this;
        }

        public ItemGroupBuilder entries(RegistryEntry<Item>... items) {
            for(RegistryEntry<Item> entry : items){
                entry(entry.get());
            }
            return this;
        }

        public RegistryEntry<ItemGroup> build() {
            return register(Registries.ITEM_GROUP, name, () -> {
                return FabricItemGroup.builder().displayName(Text.translatable(String.format("itemGroup.%s", name)))
                        .icon(() -> stack).entries(((displayContext, entries) -> {
                            entries.addAll(this.entries);
                        })).build();
            });
        }
    }

    // Fluid Block registration
    @Deprecated(forRemoval = true)
    public static class FluidBlockBuilder {
        protected final String name;
        protected final FlowableFluid stillFluid;
        protected boolean hasModel = false;
        protected AbstractBlock.Settings blockSettings;

        private FluidBlockBuilder(String name, FlowableFluid stillFluid) {
            this.name = name;
            this.stillFluid = stillFluid;
            this.blockSettings = DEFAULT_ABSTRACT_BLOCK_SETTINGS;
        }

        /**
         * Sets the block settings using a consumer function.
         *
         * @param settingsModifier the consumer function that modifies the block settings
         * @return the block builder with the block settings applied
         */
        public FluidBlockBuilder settings(Consumer<AbstractBlock.Settings> settingsModifier) {
            settingsModifier.accept(this.blockSettings);
            return this;
        }

        /**
         * Sets the block settings by copying another block's settings.
         *
         * @param copyBlock the block to copy settings from
         * @return the block builder with the copied block settings applied
         */
        public FluidBlockBuilder settings(Block copyBlock) {
            this.blockSettings = AbstractBlock.Settings.copy(copyBlock);
            return this;
        }

        /**
         * Builds the block with the specified settings.
         *
         * @return the registry entry for the block
         */
        public RegistryEntry<FluidBlock> build() {
            return register(Registries.BLOCK, name, () -> new FluidBlock(stillFluid, blockSettings));
        }

        public FluidBlockBuilder hasModel(){
            hasModel = true;
            return this;
        }
    }

    // Fluid registration
    public static class FluidBuilder<T extends Fluid> {
        private final String name;
        private final Supplier<T> stillSupplier;
        private final Supplier<T> flowingSupplier;
        private Supplier<? extends Item> bucketSupplier;
        private BiFunction<T, AbstractBlock.Settings, FluidBlock> blockSupplier;
        private boolean isVirtual = false;
        protected AbstractBlock.Settings blockSettings;

        private FluidBuilder(String name, Supplier<T> still, Supplier<T> flowing) {
            this.name = name;
            this.stillSupplier = still;
            this.flowingSupplier = flowing;

        }

//        public FluidBuilder<T> bucket(Supplier<? extends Item> supplier) {
//            this.bucketSupplier = supplier;
//            return this;
//        }

        public FluidBuilder<T> block(BiFunction<T, AbstractBlock.Settings, FluidBlock> supplier) {
            this.blockSupplier = supplier;
            return this;
        }

        /**
         * Sets the block settings using a consumer function.
         *
         * @param settingsModifier the consumer function that modifies the block settings
         * @return the block builder with the block settings applied
         */
        public FluidBuilder<T> blockSettings(Consumer<AbstractBlock.Settings> settingsModifier) {
            settingsModifier.accept(this.blockSettings);
            return this;
        }

        /**
         * Sets the block settings by copying another block's settings.
         *
         * @param copyBlock the block to copy settings from
         * @return the block builder with the copied block settings applied
         */
        public FluidBuilder<T> blockSettings(Block copyBlock) {
            this.blockSettings = AbstractBlock.Settings.copy(copyBlock);
            return this;
        }

        public FluidBuilder<T> virtual() {
            this.isVirtual = true;
            return this;
        }

        public FluidEntry<T> build(){
            if (!isVirtual && (stillSupplier == null || flowingSupplier == null || blockSupplier == null)) {
//                throw new RuntimeException("Cannot initialize non-virtual fluid without still/flowing/block suppliers.");
            }

            RegistryEntry<T> stillFluidEntry = register(Registries.FLUID, name, stillSupplier);
            RegistryEntry<T> flowingFluidEntry = null;
            RegistryEntry<? extends Block> blockEntry = null;
            RegistryEntry<? extends Item> bucketEntry = null;

            if (!isVirtual) {
                if (flowingSupplier != null) {
                    flowingFluidEntry = register(Registries.FLUID, "flowing_" + name, flowingSupplier);
                }
                if (blockSupplier != null) {
                    blockEntry = register(Registries.BLOCK, name + "_block", () -> blockSupplier.apply(stillFluidEntry.get(), blockSettings));
                }
            }

            if (bucketSupplier != null) {
                bucketEntry = register(Registries.ITEM, name + "_bucket", bucketSupplier);
            }

            return new FluidEntry<>(stillFluidEntry, flowingFluidEntry, bucketEntry, blockEntry);
        }
    }

    public static class FoodComponentBuilder {
        private final FoodComponent.Builder builder = new FoodComponent.Builder();

        public FoodComponentBuilder settings(Consumer<FoodComponent.Builder> settingsModifier) {
            settingsModifier.accept(this.builder);
            return this;
        }

        public FoodComponent build() {
            return builder.build();
        }
    }

    public static class SimpleComponentBuilder {
        private final String name;

        public SimpleComponentBuilder(String name) {
            this.name = name;
        }

        public <T> RegistryEntry<ComponentType<T>> build(Codec<T> codec){
            return register(Registries.DATA_COMPONENT_TYPE, name, () -> ComponentType.<T>builder().codec(codec).build());
        }
    }

    public static class PotionBuilder {
        private final String baseName;
        private final String entryName;
        private List<StatusEffectInstance> effects;

        private PotionBuilder(String baseName, String entryName) {
            this.baseName = baseName;
            this.entryName = entryName;
            effects = new ArrayList<>();
        }

        private PotionBuilder(String baseName){
            this(baseName, baseName);
        }

        /**
         * @param effect The status effect of the liquid.
         * @param duration The duration in terms of ticks.
         * @return Returns the PotionBuilder.
         */
        public PotionBuilder effect(StatusEffect effect, int duration) {
            effects.add(new StatusEffectInstance(Registries.STATUS_EFFECT.getEntry(effect), duration));
            return this;
        }

        /**
         * @param effect The status effect of the liquid.
         * @param duration The duration in terms of ticks.
         * @param amplifier The amplifier of the effect.
         * @return Returns the PotionBuilder.
         */
        public PotionBuilder effect(StatusEffect effect, int duration, int amplifier) {
            effects.add(new StatusEffectInstance(Registries.STATUS_EFFECT.getEntry(effect), duration, amplifier));
            return this;
        }

        public RegistryEntry<Potion> build() {
            return register(Registries.POTION, entryName, () -> {
                return entryName.equals(baseName) ? new Potion(effects.toArray(new StatusEffectInstance[0])) : new Potion(baseName, effects.toArray(new StatusEffectInstance[0]));
            }, false, true);
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

    /**
     * Creates a new block builder with the specified name.
     *
     * @param name the name of the block
     * @param stillFluidVariant The Still Fluid Variant of the Fluid block. Preferably.
     * @return the new block builder
     */
    @Deprecated(forRemoval = true)
    public static FluidBlockBuilder fluidBlock(String name, FlowableFluid stillFluidVariant) {
        return new FluidBlockBuilder(name, stillFluidVariant);
    }

    // Block Entity registration
    /**
     * Registers a new block entity type with the specified name and builder.
     *
     * @param <T> the type of the block entity
     * @param name the name of the block entity type
     * @param factory the factory for the block entity type
     * @param referenceBlock the block of the block entity
     * @return the registry entry for the block entity type
     */
    public static <T extends BlockEntity> RegistryEntry<BlockEntityType<T>> blockEntity(String name, BlockEntityType.BlockEntityFactory<T> factory, Block referenceBlock) {
        return register(Registries.BLOCK_ENTITY_TYPE, name, () -> BlockEntityType.Builder.create(factory, referenceBlock).build(null));
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
     * @param still a supplier for creating still instances of the fluid
     * @param flowing a supplier for creating flowing instances of the fluid
     * @return Fluid Builder for the fluid
     */
    // Fluid registration
    public static <T extends Fluid> FluidBuilder<T> fluid(String name, Supplier<T> still, Supplier<T> flowing) {
        return new FluidBuilder<>(name, still, flowing);
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

    public static RegistryEntry<SoundEvent> soundEvent(String name){
        return register(Registries.SOUND_EVENT, name, () -> SoundEvent.of(id(name)));
    }

    public static RegistryEntry<Identifier> stat(String name, StatFormatter formatter) {
        return register(Registries.CUSTOM_STAT, name, () -> {
            Identifier i = id(name);
//            Stats.CUSTOM.getOrCreateStat(i, formatter);
            return i;
        });
    }

    public static ItemGroupBuilder itemGroup(String name, Item icon) {
        return new ItemGroupBuilder(name, icon);
    }

    public static PotionBuilder potion(String name){
        return new PotionBuilder(name);
    }

    public static PotionBuilder potion(String baseName, String entryName){
        return new PotionBuilder(baseName, entryName);
    }

    public static <T extends StatusEffect> RegistryEntry<T> statusEffect(String name, Supplier<T> supplier){
        return register(Registries.STATUS_EFFECT, name, supplier, false, true);
    }

    public static RegistryEntry<Identifier> stat(String name) {
        return stat(name, StatFormatter.DEFAULT);
    }

    public static SimpleComponentBuilder simpleComponent(String name){
        return new SimpleComponentBuilder(name);
    }

    public static FoodComponentBuilder foodComponent(){
        return new FoodComponentBuilder();
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
        return register(registry, name, supplier, false, false);
    }

    /**
     * Registers a new entry in the given registry with the specified name and supplier.
     * The supplier is used to create an instance of the entry in the registry.
     *
     * @param <T>      the type of the entry
     * @param registry the registry to register the entry in
     * @param name     the name of the entry
     * @param supplier the supplier to create an instance of the entry
     * @param hasModel whether the entry has a pre-existing model
     * @return the registry entry for the entry that was registered
     */
    private static <T> RegistryEntry<T> register(Registry<? super T> registry, String name, Supplier<T> supplier, boolean hasModel) {
        return register(registry, name, supplier, hasModel, false);
    }

    /**
     * Registers a new entry in the given registry with the specified name and supplier.
     * The supplier is used to create an instance of the entry in the registry.
     *
     * @param <T>      the type of the entry
     * @param registry the registry to register the entry in
     * @param name     the name of the entry
     * @param supplier the supplier to create an instance of the entry
     * @param hasModel whether the entry has a pre-existing model
     * @param registerReference register a reference of the object instead and access it with <code>asRegistryEntry()</code> instead.
     * @return the registry entry for the entry that was registered
     */
    private static <T> RegistryEntry<T> register(Registry<? super T> registry, String name, Supplier<T> supplier, boolean hasModel, boolean registerReference) {
        RegistryEntry<T> entry = new RegistryEntry<>(registry, name, supplier, hasModel, registerReference);
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
    public static void registerAll(boolean clearRegisters) {
        for (List<RegistryEntry<?>> entries : DEFERRED_REGISTERS.values()) {
            for (RegistryEntry<?> entry : entries) {
                if(!entry.registerReference()) entry.get(); // This triggers the actual registration
                else entry.asRegistryEntry();
                LostInTheComplex.LOGGER.info("Registered {}", entry.getId());
            }
        }
        if(clearRegisters) DEFERRED_REGISTERS.clear();
    }

    public static List<RegistryEntry<?>> getDeferredRegisters(Registry<?> registry){
        return DEFERRED_REGISTERS.get(registry);
    }

    public static String getModId(){
        return MOD_ID;
    }

    public static Identifier id(String name) {
        return Identifier.of(MOD_ID, name);
    }
}