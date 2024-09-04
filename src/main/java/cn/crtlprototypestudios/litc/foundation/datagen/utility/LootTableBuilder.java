package cn.crtlprototypestudios.litc.foundation.datagen.utility;

import net.minecraft.component.ComponentType;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LeafEntry;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.function.SetComponentsLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.LootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

public class LootTableBuilder {
    protected LootTable.Builder base;

    public LootTableBuilder() {
        base = LootTable.builder();
    }

    public LootTable.Builder get(){
        return base;
    }

    public LootTableBuilder base(LootTable.Builder base) {
        this.base = base;
        return this;
    }

    public PoolBuilder pool() {
        return new PoolBuilder(this);
    }

    public LootTable build() {
        return base.build();
    }

    protected LootTableBuilder addPool(LootPool pool) {
        base.pool(pool);
        return this;
    }

    public static class PoolBuilder {
        private final LootTableBuilder parent;
        protected LootPool.Builder base;
        private final List<LootPoolEntry.Builder<?>> entries = new ArrayList<>();
        private final List<LootCondition.Builder> conditions = new ArrayList<>();
        private final List<LootFunction.Builder> functions = new ArrayList<>();

        public PoolBuilder(LootTableBuilder parent) {
            this.parent = parent;
            this.base = LootPool.builder();
        }

        public PoolBuilder rolls(int min, int max) {
            base.rolls(UniformLootNumberProvider.create(min, max));
            return this;
        }

        public PoolBuilder rolls(int fixed) {
            base.rolls(ConstantLootNumberProvider.create(fixed));
            return this;
        }

        public PoolBuilder bonusRolls(float min, float max) {
            base.bonusRolls(UniformLootNumberProvider.create(min, max));
            return this;
        }

        public PoolBuilder addEntry(LootPoolEntry.Builder<?> entry) {
            entries.add(entry);
            return this;
        }

        public PoolBuilder addCondition(LootCondition.Builder condition) {
            conditions.add(condition);
            return this;
        }

        public PoolBuilder addFunction(LootFunction.Builder function) {
            functions.add(function);
            return this;
        }

        public ItemEntryBuilder addItem(Item item) {
            ItemEntryBuilder itemBuilder = new ItemEntryBuilder(this, item);
            entries.add(itemBuilder.getBase());
            return itemBuilder;
        }

        public LootTableBuilder endPool() {
            for (LootPoolEntry.Builder<?> entry : entries) {
                base.with(entry);
            }
            for (LootCondition.Builder condition : conditions) {
                base.conditionally(condition);
            }
            for (LootFunction.Builder function : functions) {
                base.apply(function);
            }
            return parent.addPool(base.build());
        }
    }

    public static class ItemEntryBuilder {
        private final PoolBuilder parent;
        private final LeafEntry.Builder<?> base;

        public ItemEntryBuilder(PoolBuilder parent, Item item) {
            this.parent = parent;
            this.base = ItemEntry.builder(item);
        }

        public ItemEntryBuilder weight(int weight) {
            base.weight(weight);
            return this;
        }

        public ItemEntryBuilder quality(int quality) {
            base.quality(quality);
            return this;
        }

        public ItemEntryBuilder addFunction(LootFunction.Builder function) {
            base.apply(function);
            return this;
        }

        public ItemEntryBuilder addCondition(LootCondition.Builder condition) {
            base.conditionally(condition);
            return this;
        }

        public ItemEntryBuilder setCount(int min, int max) {
            return addFunction(SetCountLootFunction.builder(UniformLootNumberProvider.create(min, max)));
        }

        public ItemEntryBuilder setCount(int fixed) {
            return addFunction(SetCountLootFunction.builder(ConstantLootNumberProvider.create(fixed)));
        }

        public <T> ItemEntryBuilder setComponent(ComponentType<T> type, T component){
            return addFunction(SetComponentsLootFunction.builder(type, component));
        }

        public ItemEntryBuilder setRandomChance(float chance) {
            return addCondition(RandomChanceLootCondition.builder(chance));
        }

        public PoolBuilder endItem() {
//            parent.base.with(base);
            return parent;
        }

        LeafEntry.Builder<?> getBase() {
            return base;
        }
    }
}