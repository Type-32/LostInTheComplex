package cn.crtlprototypestudios.litc;

import cn.crtlprototypestudios.litc.foundation.datagen.ModBlockTagProvider;
import cn.crtlprototypestudios.litc.foundation.datagen.ModFluidTagProvider;
import cn.crtlprototypestudios.litc.foundation.datagen.ModLootTableGenerator;
import cn.crtlprototypestudios.litc.foundation.datagen.ModModelProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class LostInTheComplexDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(ModLootTableGenerator::new);
        pack.addProvider(ModModelProvider::new);
        pack.addProvider(ModFluidTagProvider::new);
        pack.addProvider(ModBlockTagProvider::new);
    }
}
