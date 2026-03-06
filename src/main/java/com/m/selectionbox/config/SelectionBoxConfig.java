package com.m.selectionbox.config;

import fi.dy.masa.malilib.config.SimpleConfigs;
import fi.dy.masa.malilib.config.options.ConfigBase;
import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.config.options.ConfigColor;
import fi.dy.masa.malilib.config.options.ConfigDouble;
import fi.dy.masa.malilib.config.options.ConfigEnum;
import fi.dy.masa.malilib.config.options.ConfigHotkey;
import fi.dy.masa.malilib.config.options.ConfigInteger;
import fi.dy.masa.malilib.hotkeys.KeybindMulti;
import fi.dy.masa.malilib.hotkeys.KeybindSettings;

import java.util.List;

public final class SelectionBoxConfig extends SimpleConfigs {
    private static final SelectionBoxConfig INSTANCE;
    public static final List<ConfigHotkey> HOTKEYS;
    public static final List<ConfigBase<?>> VALUES;

    public static final ConfigHotkey OPEN_CONFIG_SCREEN =
            new ConfigHotkey("selectionBox.openConfig", KeybindMulti.fromStorageString("M,B", KeybindSettings.RELEASE), "Open this config screen.");

    public static final ConfigBoolean ENABLE_BLOCK_OUTLINE =
            new ConfigBoolean("selectionBox.enableBlockOutline", true, "Render colored block selection outlines.");
    public static final ConfigBoolean ENABLE_ENTITY_OUTLINE =
            new ConfigBoolean("selectionBox.enableEntityOutline", true, "Render colored F3+B entity outlines.");

    public static final ConfigColor START_COLOR =
            new ConfigColor("selectionBox.gradientStartColor", "#FFFF4D4D", "Gradient start color.");
    public static final ConfigColor END_COLOR =
            new ConfigColor("selectionBox.gradientEndColor", "#FF4DD2FF", "Gradient end color.");
    public static final ConfigEnum<GradientInterpolationMode> INTERPOLATION_MODE =
            new ConfigEnum<>("selectionBox.gradientMode", GradientInterpolationMode.HSV, "RGB or HSV interpolation.");

    public static final ConfigDouble CYCLE_SECONDS =
            new ConfigDouble("selectionBox.cycleSeconds", 3.0D, 0.2D, 12.0D, true, "Time for one full color cycle.");
    public static final ConfigDouble LINE_WIDTH =
            new ConfigDouble("selectionBox.lineWidth", 3.0D, 2.0D, 6.0D, true, "Outline line width.");
    public static final ConfigInteger ALPHA =
            new ConfigInteger("selectionBox.alpha", 204, 16, 255, true, "Outline alpha (opacity).");

    static {
        HOTKEYS = List.of(OPEN_CONFIG_SCREEN);
        VALUES = List.of(
                ENABLE_BLOCK_OUTLINE,
                ENABLE_ENTITY_OUTLINE,
                START_COLOR,
                END_COLOR,
                INTERPOLATION_MODE,
                CYCLE_SECONDS,
                LINE_WIDTH,
                ALPHA
        );
        INSTANCE = new SelectionBoxConfig("selection_box", HOTKEYS, VALUES);
    }

    private SelectionBoxConfig(String name, List<ConfigHotkey> hotkeys, List<?> values) {
        super(name, hotkeys, values, "Configure outline gradient colors, animation and width.");
    }

    public static SelectionBoxConfig getInstance() {
        return INSTANCE;
    }
}
