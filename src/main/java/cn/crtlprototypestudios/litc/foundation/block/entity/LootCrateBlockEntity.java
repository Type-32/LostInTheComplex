package cn.crtlprototypestudios.litc.foundation.block.entity;

import cn.crtlprototypestudios.litc.foundation.ModBlockEntities;
import cn.crtlprototypestudios.litc.foundation.ModComponents;
import cn.crtlprototypestudios.litc.foundation.ModLootTables;
import cn.crtlprototypestudios.litc.foundation.ModSoundEvents;
import cn.crtlprototypestudios.litc.foundation.block.LootCrateBlock;
import cn.crtlprototypestudios.litc.foundation.component.LootCrateDataComponent;
import cn.crtlprototypestudios.litc.foundation.datagen.ModLootTableGenerator;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.BarrelBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.block.entity.ViewerCountManager;
import net.minecraft.component.ComponentMap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.context.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Objects;

public class LootCrateBlockEntity extends LootableContainerBlockEntity {
    private DefaultedList<ItemStack> inventory = DefaultedList.ofSize(LootCrateBlock.CRATE_INVENTORY_SIZE, ItemStack.EMPTY);
    private boolean shouldRegenerate = true;
    private final ViewerCountManager stateManager = new ViewerCountManager() {
        @Override
        protected void onContainerOpen(World world, BlockPos pos, BlockState state) {
            LootCrateBlockEntity.this.playSound(state, ModSoundEvents.BLOCK_LOOT_CRATE_OPEN.get());
            LootCrateBlockEntity.this.setOpen(state, true);
        }

        @Override
        protected void onContainerClose(World world, BlockPos pos, BlockState state) {
            LootCrateBlockEntity.this.playSound(state, ModSoundEvents.BLOCK_LOOT_CRATE_CLOSE.get());
            LootCrateBlockEntity.this.setOpen(state, false);
        }

        @Override
        protected void onViewerCountUpdate(World world, BlockPos pos, BlockState state, int oldViewerCount, int newViewerCount) {
        }

        @Override
        protected boolean isPlayerViewing(PlayerEntity player) {
            if (player.currentScreenHandler instanceof GenericContainerScreenHandler) {
                Inventory inventory = ((GenericContainerScreenHandler)player.currentScreenHandler).getInventory();
                return inventory == LootCrateBlockEntity.this;
            } else {
                return false;
            }
        }
    };

    public LootCrateBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.LOOT_CRATE_BE.get(), pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        if (!this.writeLootTable(nbt)) {
            Inventories.writeNbt(nbt, this.inventory, registryLookup);
        }
        nbt.putBoolean("ShouldRegenerate", this.shouldRegenerate);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        if (!this.readLootTable(nbt)) {
            Inventories.readNbt(nbt, this.inventory, registryLookup);
        }
        this.shouldRegenerate = nbt.getBoolean("ShouldRegenerate");
    }

    @Override
    public int size() {
        return LootCrateBlock.CRATE_INVENTORY_SIZE;
    }

    @Override
    protected DefaultedList<ItemStack> getHeldStacks() {
        return this.inventory;
    }

    @Override
    protected void setHeldStacks(DefaultedList<ItemStack> inventory) {
        this.inventory = inventory;
    }

    @Override
    protected Text getContainerName() {
        return Text.translatable("container.barrel");
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return GenericContainerScreenHandler.createGeneric9x2(syncId, playerInventory);
    }

    @Override
    public void onOpen(PlayerEntity player) {
        if (!this.removed && !player.isSpectator()) {
            this.stateManager.openContainer(player, this.getWorld(), this.getPos(), this.getCachedState());
        }
    }

    @Override
    public void onClose(PlayerEntity player) {
        if (!this.removed && !player.isSpectator()) {
            this.stateManager.closeContainer(player, this.getWorld(), this.getPos(), this.getCachedState());
        }
    }

    public void tick() {
        if (!this.removed) {
            this.stateManager.updateViewerCount(this.getWorld(), this.getPos(), this.getCachedState());
        }
        if (this.shouldRegenerate && this.world instanceof ServerWorld && this.world.getTime() % 5000 == 0) {
            this.regenerateInventory((ServerWorld) this.world);
        }
    }

    private void regenerateInventory(ServerWorld world) {
        LootTable lootTable = ModLootTables.CRATE_BLOCK_LOOT.build();

        DefaultedList<ItemStack> newInventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        newInventory.addAll(lootTable.generateLoot(new LootContextParameterSet.Builder(world).build(LootContextTypes.CHEST)));

        this.setHeldStacks(newInventory);
        this.markDirty();
    }

    public void setShouldRegenerate(boolean shouldRegenerate) {
        this.shouldRegenerate = shouldRegenerate;
        this.markDirty();
    }

    void setOpen(BlockState state, boolean open) {
        this.world.setBlockState(this.getPos(), state.with(LootCrateBlock.OPEN, Boolean.valueOf(open)), Block.NOTIFY_ALL);
    }

    void playSound(BlockState state, SoundEvent soundEvent) {
        this.world.playSound(null, this.pos.getX(), this.pos.getY(), this.pos.getZ(), soundEvent, SoundCategory.BLOCKS, 0.5F, this.world.random.nextFloat() * 0.1F + 0.9F);
    }
}
