package com.theonlytazz.unpluggedafk;

import com.mojang.logging.LogUtils;
import com.theonlytazz.unpluggedafk.config.ConfigManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;
import org.slf4j.Logger;

@Mod(UnpluggedAfk.MOD_ID)
public final class UnpluggedAfk {
    public static final String MOD_ID = "unplugged_afk";
    public static final Logger LOGGER = LogUtils.getLogger();

    public UnpluggedAfk() {
        ConfigManager.initialize(FMLPaths.CONFIGDIR.get().resolve(MOD_ID + ".json"));
        MinecraftForge.EVENT_BUS.register(new UnpluggedEvents());
        LOGGER.info("Unplugged AFK configuration loaded");
    }
}
