package cn.crtlprototypestudios.litc.foundation.item;

import cn.crtlprototypestudios.litc.LostInTheComplex;
import cn.crtlprototypestudios.litc.foundation.ModComponents;
import cn.crtlprototypestudios.litc.foundation.ModFluids;
import cn.crtlprototypestudios.litc.foundation.ModPotions;
import cn.crtlprototypestudios.litc.foundation.component.LiquidContainerDataComponent;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.*;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.potion.Potions;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class LiquidContainerItem extends PotionItem {
    private static final int ITEM_BAR_COLOR = MathHelper.packRgb(0.4F, 0.4F, 1.0F);

    public LiquidContainerItem(Settings settings) {
        super(settings.rarity(Rarity.COMMON).maxCount(1));
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        LiquidContainerDataComponent comp = stack.getComponents().get(ModComponents.LIQUID_CONTAINER_DATA_COMPONENT.get());
        if(comp == null)
            return 0;
        return MathHelper.clamp(Math.round(13 * ((float) comp.amount() / comp.max())), 0, 13);
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        return ITEM_BAR_COLOR;
    }

    @Override
    public int getMaxCount() {
        return 1;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        if (!itemStack.contains(ModComponents.LIQUID_CONTAINER_DATA_COMPONENT.get())) {
            itemStack.set(ModComponents.LIQUID_CONTAINER_DATA_COMPONENT.get(), LiquidContainerDataComponent.DEFAULT_EMPTY);
            if(!itemStack.contains(ModComponents.LIQUID_CONTAINER_DATA_COMPONENT.get()))
                return TypedActionResult.fail(itemStack);
        }

        LiquidContainerDataComponent comp = itemStack.get(ModComponents.LIQUID_CONTAINER_DATA_COMPONENT.get());
        assert comp != null;
        Identifier liquid = comp.liquid();

        BlockHitResult raytrace = Item.raycast(world, user, RaycastContext.FluidHandling.SOURCE_ONLY);

        if(raytrace.getType() == HitResult.Type.BLOCK) {
            BlockPos pos = raytrace.getBlockPos();
            if(world.canPlayerModifyAt(user, pos)) {
                BlockState state = world.getBlockState(pos);
                FluidState fluidState = world.getFluidState(pos);

                LostInTheComplex.LOGGER.info("Detected {} and {} from raycast", state, fluidState);

                if (comp.amount() >= comp.max() || !comp.replenishable()){
                    return TypedActionResult.fail(itemStack);
                }

                if(fluidState.getFluid() == Fluids.EMPTY) {
                    if (comp.amount() <= 0 || liquid == null){
                        return TypedActionResult.fail(itemStack);
                    }

                    return ItemUsage.consumeHeldItem(world, user, hand);
                }

                if(liquid == null || comp.amount() <= 0) { // If the liquid container item is empty.
                    if (state.getBlock() == Blocks.WATER_CAULDRON) { // Water from cauldron
                        int level = state.get(LeveledCauldronBlock.LEVEL);

                        playFillSound(world, user);
                        if (level > 0 && !world.isClient) {
                            LeveledCauldronBlock.decrementFluidLevel(state, world, pos);
                            itemStack.set(ModComponents.LIQUID_CONTAINER_DATA_COMPONENT.get(),
                                    new LiquidContainerDataComponent(Math.clamp(comp.amount() + 1, 0, comp.max()), comp.max(), comp.replenishable(), ModFluids.findId(fluidState.getFluid())));
                            itemStack.set(DataComponentTypes.POTION_CONTENTS, new PotionContentsComponent(Potions.WATER));

                            return TypedActionResult.success(itemStack);
                        }
                    } else if (fluidState.getFluid() != null || fluidState.isIn(FluidTags.WATER)) { // Fluid in a Water tag.
                        playFillSound(world, user);
                        if (!world.isClient) {
                            itemStack.set(ModComponents.LIQUID_CONTAINER_DATA_COMPONENT.get(),
                                    new LiquidContainerDataComponent(Math.clamp(comp.amount() + 1, 0, comp.max()), comp.max(), comp.replenishable(), ModFluids.findId(fluidState.getFluid().getDefaultState().getFluid())));

                            if (fluidState.getFluid().equals(ModFluids.ALMOND_WATER.getFluid()) || fluidState.getFluid().equals(ModFluids.ALMOND_WATER.getFluid()) || fluidState.getFluid().getDefaultState().getFluid().equals(ModFluids.ALMOND_WATER.getFluid())) {
                                itemStack.set(DataComponentTypes.POTION_CONTENTS, new PotionContentsComponent(ModPotions.SLIGHT_REJUVENATION.get()));
                            } else {
                                itemStack.set(DataComponentTypes.POTION_CONTENTS, new PotionContentsComponent(Potions.WATER));
                            }

                            LostInTheComplex.LOGGER.info("DEBUG Fluid not null, there is fluid for block");
                            return TypedActionResult.success(itemStack);
                        }
                    }
                }
                else {
                    if (state.getBlock() == Blocks.WATER_CAULDRON) {
                        int level = state.get(LeveledCauldronBlock.LEVEL);

                        playFillSound(world, user);
                        if (level > 0 && !world.isClient) {
                            LeveledCauldronBlock.decrementFluidLevel(state, world, pos);
                            itemStack.set(ModComponents.LIQUID_CONTAINER_DATA_COMPONENT.get(),
                                    new LiquidContainerDataComponent(Math.clamp(comp.amount() + 1, 0, comp.max()), comp.max(), comp.replenishable(), ModFluids.findId(fluidState.getFluid())));

                            return TypedActionResult.success(itemStack);
                        }
                    } else if (Registries.FLUID.get(liquid).equals(fluidState.getFluid())) {
                        playFillSound(world, user);
                        if (!world.isClient) {

                            itemStack.set(ModComponents.LIQUID_CONTAINER_DATA_COMPONENT.get(),
                                    new LiquidContainerDataComponent(Math.clamp(comp.amount() + 1, 0, comp.max()), comp.max(), comp.replenishable(), ModFluids.findId(fluidState.getFluid())));

                            return TypedActionResult.success(itemStack);
                        }
                    }
                }
            }
        }

        if (comp.amount() <= 0 || liquid == null){
            return TypedActionResult.fail(itemStack);
        }

        return ItemUsage.consumeHeldItem(world, user, hand);
    }

    private void playFillSound(World world, PlayerEntity user) {
        world.playSound(user, user.getX(), user.getY(), user.getZ(), SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return super.getTranslationKey(stack);
    }

    @Override
    public ItemStack getDefaultStack() {
        ItemStack stack = super.getDefaultStack();
        stack.set(ModComponents.LIQUID_CONTAINER_DATA_COMPONENT.get(), LiquidContainerDataComponent.DEFAULT_EMPTY);
        return stack;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        return ActionResult.PASS;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (!stack.contains(ModComponents.LIQUID_CONTAINER_DATA_COMPONENT.get()))
            return stack;

        LiquidContainerDataComponent comp = stack.get(ModComponents.LIQUID_CONTAINER_DATA_COMPONENT.get());
        int currentAmount = 0;
        assert comp != null;
        currentAmount = comp.amount();
        Identifier liquid = comp.liquid();

        PlayerEntity playerEntity = user instanceof PlayerEntity ? (PlayerEntity)user : null;

        if (liquid != null){
            if (playerEntity instanceof ServerPlayerEntity) {
                Criteria.CONSUME_ITEM.trigger((ServerPlayerEntity)playerEntity, stack);
            }

            if (!world.isClient) {
                if (stack.getComponents().contains(DataComponentTypes.POTION_CONTENTS)) {
                    PotionContentsComponent potionContentsComponent = stack.getOrDefault(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT);
                    potionContentsComponent.forEachEffect((effect) -> {
                        if (effect.getEffectType().value().isInstant()) {
                            effect.getEffectType().value().applyInstantEffect(playerEntity, playerEntity, user, effect.getAmplifier(), 1.0);
                        } else {
                            user.addStatusEffect(effect);
                        }
                    });
                }
            }

            if (playerEntity != null) {
                playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));

                if(!playerEntity.isInCreativeMode()) {
                    currentAmount = Math.clamp(currentAmount - 1, 0, comp.max());
                    if (currentAmount <= 0) {
                        liquid = null;
                        if(stack.getComponents().contains(DataComponentTypes.POTION_CONTENTS))
                            stack.set(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT);
                    }
                }
            }

            stack.set(ModComponents.LIQUID_CONTAINER_DATA_COMPONENT.get(), new LiquidContainerDataComponent(currentAmount, comp.max(), comp.replenishable(), liquid));
        }

        user.emitGameEvent(GameEvent.DRINK);

        return stack;
    }
}
