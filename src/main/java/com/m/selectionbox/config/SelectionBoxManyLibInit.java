package com.m.selectionbox.config;

import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.hotkeys.IHotkeyCallback;
import fi.dy.masa.malilib.hotkeys.IKeybind;
import fi.dy.masa.malilib.hotkeys.KeyAction;
import fi.dy.masa.malilib.interfaces.IInitializationHandler;
import net.minecraft.Minecraft;

public class SelectionBoxManyLibInit implements IInitializationHandler {
    @Override
    public void registerModHandlers() {
        SelectionBoxConfig.getInstance().load();
        ConfigManager.getInstance().registerConfig(SelectionBoxConfig.getInstance());
        SelectionBoxConfig.OPEN_CONFIG_SCREEN.getKeybind().setCallback(new OpenConfigScreenCallback());
    }

    private static class OpenConfigScreenCallback implements IHotkeyCallback {
        @Override
        public boolean onKeyAction(KeyAction action, IKeybind key) {
            Minecraft minecraft = Minecraft.getMinecraft();
            minecraft.displayGuiScreen(SelectionBoxConfig.getInstance().getConfigScreen(minecraft.currentScreen));
            return true;
        }
    }
}

