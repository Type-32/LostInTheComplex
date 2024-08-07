package cn.crtlprototypestudios.litc.utility;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.enchantment.Enchantment;
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
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class RegisterHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterHelper.class);
    private static final List<Item> REGISTERED_ITEMS = new ArrayList<>();
    private static Item.Settings DEFAULT_ITEM_SETTINGS = new Item.Settings();

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

        public Item build() {
            return registerItem(name, new Item(settings));
        }

        public <T extends Item> T build(Function<Item.Settings, T> factory) {
            return registerItem(name, factory.apply(settings));
        }
    }

    public static ItemBuilder item(String name) {
        return new ItemBuilder(name);
    }

    private static <T extends Item> T registerItem(String name, T item) {
        T registeredItem = Registry.register(Registries.ITEM, Reference.id(name), item);
        REGISTERED_ITEMS.add(registeredItem);
        LOGGER.info("Registered item: {}", Reference.id(name));
        return registeredItem;
    }

    // Block registration
    public static class BlockBuilder {
        private final String name;
        private final Block block;
        private AbstractBlock.Settings blockSettings;
        private Item.Settings itemSettings;
        private boolean registerItem = false;

        private BlockBuilder(String name, Block block) {
            this.name = name;
            this.block = block;
            this.itemSettings = DEFAULT_ITEM_SETTINGS;
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

        public BlockBuilder item(){
            registerItem = true;
            this.itemSettings = DEFAULT_ITEM_SETTINGS;
            return this;
        }

        public BlockBuilder settings(Consumer<AbstractBlock.Settings> settingsModifier) {
            settingsModifier.accept(this.blockSettings);
            return this;
        }

        public BlockBuilder settings(Block copyBlock){
            this.blockSettings = AbstractBlock.Settings.copy(copyBlock);
            return this;
        }

        public Block build() {
            return registerBlock(name, new Block(blockSettings), itemSettings, registerItem);
        }

        public <T extends Block> T build(Function<AbstractBlock.Settings, T> factory) {
            return registerBlock(name, factory.apply(blockSettings), itemSettings, registerItem);
        }
    }

    public static BlockBuilder block(String name, Block block) {
        return new BlockBuilder(name, block);
    }

    public static BlockBuilder block(String name){
        return block(name, new Block(AbstractBlock.Settings.create()));
    }

    private static <T extends Block> T registerBlock(String name, T block, Item.Settings itemSettings, boolean registerItem) {
        T registeredBlock = Registry.register(Registries.BLOCK, Reference.id(name), block);
        if(registerItem) registerItem(name, new BlockItem(registeredBlock, itemSettings));
        LOGGER.info("Registered block: {}", Reference.id(name));
        return registeredBlock;
    }

    // Block Entity registration
    public static <T extends BlockEntity> BlockEntityType<T> blockEntity(String name, BlockEntityType.Builder<T> builder) {
        BlockEntityType<T> type = Registry.register(Registries.BLOCK_ENTITY_TYPE, Reference.id(name), builder.build(null));
        LOGGER.info("Registered block entity: {}", Reference.id(name));
        return type;
    }

    // Entity registration
    public static <T extends Entity> EntityType<T> entity(String name, EntityType.Builder<T> builder) {
        EntityType<T> type = Registry.register(Registries.ENTITY_TYPE, Reference.id(name), builder.build(name));
        LOGGER.info("Registered entity: {}", Reference.id(name));
        return type;
    }

    // Fluid registration
    public static <T extends FlowableFluid> T fluid(String name, T fluid) {
        T registeredFluid = Registry.register(Registries.FLUID, Reference.id(name), fluid);
        LOGGER.info("Registered fluid: {}", Reference.id(name));
        return registeredFluid;
    }

    // Screen Handler (Menu) registration
    public static <T extends ScreenHandler> ScreenHandlerType<T> screenHandler(String name, ScreenHandlerType.Factory<T> factory, FeatureSet featureSet) {
        ScreenHandlerType<T> type = Registry.register(Registries.SCREEN_HANDLER, Reference.id(name), new ScreenHandlerType<>(factory, featureSet));
        LOGGER.info("Registered screen handler: {}", Reference.id(name));
        return type;
    }

    // Enchantment registration
//    public static <T extends Enchantment> T enchantment(String name, T enchantment) {
//        T registeredEnchantment = Registry.register(Registries.ENCHANTMENT, Reference.id(name), enchantment);
//        LOGGER.info("Registered enchantment: {}", Reference.id(name));
//        return registeredEnchantment;
//    }

    // Get all registered items
    public static List<Item> getRegisteredItems() {
        return new ArrayList<>(REGISTERED_ITEMS);
    }
}