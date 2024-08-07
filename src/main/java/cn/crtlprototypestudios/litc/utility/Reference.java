package cn.crtlprototypestudios.litc.utility;

import net.minecraft.util.Identifier;

public class Reference {
    public static String MOD_ID = "litc";

    public static Identifier id(String path){
        return Identifier.of(MOD_ID, path);
    }
}
