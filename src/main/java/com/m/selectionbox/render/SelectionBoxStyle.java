package com.m.selectionbox.render;

import com.m.selectionbox.config.GradientInterpolationMode;
import com.m.selectionbox.config.SelectionBoxConfig;
import net.minecraft.Tessellator;

import java.awt.Color;

public final class SelectionBoxStyle {
    private SelectionBoxStyle() {
    }

    public static boolean renderBlockOutline() {
        return SelectionBoxConfig.ENABLE_BLOCK_OUTLINE.getBooleanValue();
    }

    public static boolean renderEntityOutline() {
        return SelectionBoxConfig.ENABLE_ENTITY_OUTLINE.getBooleanValue();
    }

    public static float getLineWidth() {
        return (float) Math.max(1.0D, SelectionBoxConfig.LINE_WIDTH.getDoubleValue());
    }

    public static float getAnimationProgress() {
        double cycleSeconds = Math.max(0.2D, SelectionBoxConfig.CYCLE_SECONDS.getDoubleValue());
        double cycleMillis = cycleSeconds * 1000.0D;
        double progress = (System.currentTimeMillis() / cycleMillis) % 1.0D;
        return (float) progress;
    }

    public static void addGradientVertex(Tessellator tessellator, double x, double y, double z, float timeBase, float gradientOffset) {
        float position = wrap01(timeBase + gradientOffset);
        int rgb = interpolateColor(position);
        float alpha = clamp01(SelectionBoxConfig.ALPHA.getIntegerValue() / 255.0F);

        float red = ((rgb >> 16) & 0xFF) / 255.0F;
        float green = ((rgb >> 8) & 0xFF) / 255.0F;
        float blue = (rgb & 0xFF) / 255.0F;
        tessellator.setColorRGBA_F(red, green, blue, alpha);
        tessellator.addVertex(x, y, z);
    }

    private static int interpolateColor(float t) {
        int start = SelectionBoxConfig.START_COLOR.getColorInteger();
        int end = SelectionBoxConfig.END_COLOR.getColorInteger();
        GradientInterpolationMode mode = SelectionBoxConfig.INTERPOLATION_MODE.getEnumValue();
        if (mode == GradientInterpolationMode.HSV) {
            return interpolateHsv(start, end, t);
        }
        return interpolateRgb(start, end, t);
    }

    private static int interpolateRgb(int start, int end, float t) {
        int sr = (start >> 16) & 0xFF;
        int sg = (start >> 8) & 0xFF;
        int sb = start & 0xFF;

        int er = (end >> 16) & 0xFF;
        int eg = (end >> 8) & 0xFF;
        int eb = end & 0xFF;

        int r = lerpInt(sr, er, t);
        int g = lerpInt(sg, eg, t);
        int b = lerpInt(sb, eb, t);
        return (r << 16) | (g << 8) | b;
    }

    private static int interpolateHsv(int start, int end, float t) {
        float[] a = Color.RGBtoHSB((start >> 16) & 0xFF, (start >> 8) & 0xFF, start & 0xFF, null);
        float[] b = Color.RGBtoHSB((end >> 16) & 0xFF, (end >> 8) & 0xFF, end & 0xFF, null);

        float hue = lerpHue(a[0], b[0], t);
        float saturation = lerpFloat(a[1], b[1], t);
        float brightness = lerpFloat(a[2], b[2], t);
        return Color.HSBtoRGB(hue, saturation, brightness) & 0x00FFFFFF;
    }

    private static int lerpInt(int a, int b, float t) {
        return Math.round(a + (b - a) * t);
    }

    private static float lerpFloat(float a, float b, float t) {
        return a + (b - a) * t;
    }

    private static float lerpHue(float startHue, float endHue, float t) {
        float delta = endHue - startHue;
        if (delta > 0.5F) {
            delta -= 1.0F;
        } else if (delta < -0.5F) {
            delta += 1.0F;
        }
        return wrap01(startHue + delta * t);
    }

    private static float wrap01(float value) {
        float wrapped = value % 1.0F;
        if (wrapped < 0.0F) {
            wrapped += 1.0F;
        }
        return wrapped;
    }

    private static float clamp01(float value) {
        if (value < 0.0F) {
            return 0.0F;
        }
        if (value > 1.0F) {
            return 1.0F;
        }
        return value;
    }
}

