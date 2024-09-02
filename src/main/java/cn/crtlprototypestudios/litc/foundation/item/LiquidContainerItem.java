package cn.crtlprototypestudios.litc.foundation.item;

import cn.crtlprototypestudios.litc.foundation.ModComponents;
import cn.crtlprototypestudios.litc.foundation.component.LiquidContainerDataComponent;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Blocks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.*;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
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
        return MathHelper.clamp(Math.round(comp.max() * ((float) comp.amount() / comp.max())), 0, comp.max());
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
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (context.getWorld().getFluidState(context.getBlockPos()).isIn(FluidTags.WATER)){

        } else if (context.getWorld().getBlockState(context.getBlockPos()).getBlock().equals(Blocks.WATER_CAULDRON)){

        }

        return ActionResult.FAIL;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!user.getStackInHand(hand).contains(ModComponents.LIQUID_CONTAINER_DATA_COMPONENT.get()))
            return TypedActionResult.pass(user.getStackInHand(hand));

        LiquidContainerDataComponent comp = user.getStackInHand(hand).get(ModComponents.LIQUID_CONTAINER_DATA_COMPONENT.get());
        int currentAmount = 0;
        currentAmount = comp.amount();
        Identifier liquid = comp.liquid();

        if (currentAmount <= 0 || liquid == null){
            return TypedActionResult.pass(user.getStackInHand(hand));
        } else{
            return TypedActionResult.success(user.getStackInHand(hand));
        }
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (!stack.contains(ModComponents.LIQUID_CONTAINER_DATA_COMPONENT.get()))
            return stack;

        LiquidContainerDataComponent comp = stack.get(ModComponents.LIQUID_CONTAINER_DATA_COMPONENT.get());
        int currentAmount = 0, maxAmount = 0;
        currentAmount = comp.amount();
        maxAmount = comp.max();
        boolean replenishable = comp.replenishable();
        Identifier liquid = comp.liquid();

        PlayerEntity playerEntity = user instanceof PlayerEntity ? (PlayerEntity)user : null;

        if (liquid != null){
            if(liquid.equals(Registries.FLUID.getId(Fluids.WATER))) { // If the liquid is "minecraft:water", which is water
                if (playerEntity instanceof ServerPlayerEntity) {
                    Criteria.CONSUME_ITEM.trigger((ServerPlayerEntity)playerEntity, stack);
                }

                if (playerEntity != null) {
                    playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));

                    if(!playerEntity.isInCreativeMode()) {
                        currentAmount -= 1;
                        if (currentAmount <= 0) {
                            currentAmount = 0;
                            liquid = null;
                        }
                    }
                }

                stack.set(ModComponents.LIQUID_CONTAINER_DATA_COMPONENT.get(), new LiquidContainerDataComponent(currentAmount, maxAmount, replenishable, liquid));
            } else if (liquid.equals(Registries.ITEM.getId(Items.POTION))) { // This is intentional. To check if the liquid is a potion, thus "minecraft:potion" in general.
                if (playerEntity instanceof ServerPlayerEntity) {
                    Criteria.CONSUME_ITEM.trigger((ServerPlayerEntity)playerEntity, stack);
                }

                if (!world.isClient) {
                    PotionContentsComponent potionContentsComponent = (PotionContentsComponent)stack.getOrDefault(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT);
                    potionContentsComponent.forEachEffect((effect) -> {
                        if (((StatusEffect)effect.getEffectType().value()).isInstant()) {
                            ((StatusEffect)effect.getEffectType().value()).applyInstantEffect(playerEntity, playerEntity, user, effect.getAmplifier(), 1.0);
                        } else {
                            user.addStatusEffect(effect);
                        }

                    });
                }

                if (playerEntity != null) {
                    playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));

                    if(!playerEntity.isInCreativeMode()) {
                        currentAmount -= 1;
                        if (currentAmount <= 0) {
                            currentAmount = 0;
                            liquid = null;
                        }
                    }
                }

//                if (playerEntity == null || !playerEntity.isInCreativeMode()) {
//                    if (playerEntity != null) {
//                        playerEntity.getInventory().insertStack(new ItemStack(Items.GLASS_BOTTLE));
//                    }
//                }
            } else {
                stack.set(ModComponents.LIQUID_CONTAINER_DATA_COMPONENT.get(), new LiquidContainerDataComponent(0, maxAmount, replenishable, null));
            }
        }

        user.emitGameEvent(GameEvent.DRINK);

        return stack;
    }
}
