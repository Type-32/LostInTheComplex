package cn.crtlprototypestudios.litc.foundation.block;

import cn.crtlprototypestudios.litc.foundation.ModStats;
import cn.crtlprototypestudios.litc.foundation.block.entity.LootCrateBlockEntity;
import cn.crtlprototypestudios.litc.foundation.custom.ModProperties;
import cn.crtlprototypestudios.litc.foundation.custom.impl.properties.IdentifierProperty;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class LootCrateBlock extends BlockWithEntity implements BlockEntityProvider {
    public static final MapCodec<LootCrateBlock> CODEC = createCodec(LootCrateBlock::new);
    public static final BooleanProperty OPEN = Properties.OPEN;
    public static final IdentifierProperty LOOT_TABLE = ModProperties.LOOT_TABLE;
    public static final int CRATE_INVENTORY_SIZE = 18;

    public LootCrateBlock(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(OPEN, Boolean.FALSE));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(OPEN);
    }

    @Override
    protected MapCodec<LootCrateBlock> getCodec() {
        return CODEC;
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        } else {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof LootCrateBlockEntity) {
                player.openHandledScreen((LootCrateBlockEntity)blockEntity);
                player.incrementStat(ModStats.OPEN_LOOT_CRATE.get());
            }

            return ActionResult.CONSUME;
        }
    }

    @Override
    protected void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        ItemScatterer.onStateReplaced(state, newState, world, pos);
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Override
    protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof LootCrateBlockEntity) {
            ((LootCrateBlockEntity)blockEntity).tick();
        }
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new LootCrateBlockEntity(pos, state);
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    protected int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos));
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof LootCrateBlockEntity) {
            ((LootCrateBlockEntity) blockEntity).setShouldRegenerate(false);
        }
    }
}
