package cn.crtlprototypestudios.litc.client;

import cn.crtlprototypestudios.litc.client.rendering.FluidRendering;
import net.fabricmc.api.ClientModInitializer;

public class LostInTheComplexClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        FluidRendering.register();
    }
}
