package com.m.selectionbox;

import com.m.selectionbox.config.SelectionBoxManyLibInit;
import fi.dy.masa.malilib.event.InitializationHandler;
import net.fabricmc.api.ModInitializer;
import net.xiaoyu233.fml.ModResourceManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SelectionBoxMod implements ModInitializer {
    public static final String MOD_ID = "selectionbox";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("SelectionBox Mod initialized!");
        InitializationHandler.getInstance().registerInitializationHandler(new SelectionBoxManyLibInit());
        ModResourceManager.addResourcePackDomain(MOD_ID);
    }
}
