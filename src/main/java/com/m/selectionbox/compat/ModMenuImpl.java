package com.m.selectionbox.compat;

import com.m.selectionbox.config.SelectionBoxConfig;
import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;

public class ModMenuImpl implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> SelectionBoxConfig.getInstance().getConfigScreen(parent);
    }
}

