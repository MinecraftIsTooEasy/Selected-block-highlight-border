package com.m.selectionbox.mixin;

import com.m.selectionbox.render.SelectionBoxStyle;
import net.minecraft.*;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Replaces entity hitbox rendering (F3+B) with a configurable gradient.
 */
@Mixin(RenderManager.class)
public class RenderManagerMixin {

    @Inject(method = "func_85094_b", at = @At("HEAD"), cancellable = true)
    private void renderEntityHitbox(Entity par1Entity, double par2, double par4, double par6, float par8, float par9, CallbackInfo ci) {
        // Fully take over entity debug hitbox rendering to avoid overwrite conflicts.
        ci.cancel();

        if (!SelectionBoxStyle.renderEntityOutline()) {
            return;
        }

        if (par1Entity instanceof EntityClientPlayerMP) {
            par4 -= par1Entity.yOffset;
        }

        GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        float time = SelectionBoxStyle.getAnimationProgress();
        double minX = -par1Entity.width / 2.0F;
        double maxX = par1Entity.width / 2.0F;
        double minY = 0.0F;
        double maxY = par1Entity.height;

        GL11.glLineWidth(SelectionBoxStyle.getLineWidth());
        Tessellator tessellator = Tessellator.instance;

        tessellator.startDrawing(3);
        addColorVertex(tessellator, par2 + minX, par4 + minY, par6 + minX, time, 0.0F);
        addColorVertex(tessellator, par2 + maxX, par4 + minY, par6 + minX, time, 0.25F);
        addColorVertex(tessellator, par2 + maxX, par4 + minY, par6 + maxX, time, 0.5F);
        addColorVertex(tessellator, par2 + minX, par4 + minY, par6 + maxX, time, 0.75F);
        addColorVertex(tessellator, par2 + minX, par4 + minY, par6 + minX, time, 1.0F);
        tessellator.draw();

        tessellator.startDrawing(3);
        addColorVertex(tessellator, par2 + minX, par4 + maxY, par6 + minX, time, 0.125F);
        addColorVertex(tessellator, par2 + maxX, par4 + maxY, par6 + minX, time, 0.375F);
        addColorVertex(tessellator, par2 + maxX, par4 + maxY, par6 + maxX, time, 0.625F);
        addColorVertex(tessellator, par2 + minX, par4 + maxY, par6 + maxX, time, 0.875F);
        addColorVertex(tessellator, par2 + minX, par4 + maxY, par6 + minX, time, 0.125F);
        tessellator.draw();

        tessellator.startDrawing(1);
        addColorVertex(tessellator, par2 + minX, par4 + minY, par6 + minX, time, 0.0F);
        addColorVertex(tessellator, par2 + minX, par4 + maxY, par6 + minX, time, 0.0F);
        addColorVertex(tessellator, par2 + maxX, par4 + minY, par6 + minX, time, 0.25F);
        addColorVertex(tessellator, par2 + maxX, par4 + maxY, par6 + minX, time, 0.25F);
        addColorVertex(tessellator, par2 + maxX, par4 + minY, par6 + maxX, time, 0.5F);
        addColorVertex(tessellator, par2 + maxX, par4 + maxY, par6 + maxX, time, 0.5F);
        addColorVertex(tessellator, par2 + minX, par4 + minY, par6 + maxX, time, 0.75F);
        addColorVertex(tessellator, par2 + minX, par4 + maxY, par6 + maxX, time, 0.75F);
        tessellator.draw();

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDepthMask(true);
    }

    private static void addColorVertex(Tessellator tessellator, double x, double y, double z, float timeBase, float offset) {
        SelectionBoxStyle.addGradientVertex(tessellator, x, y, z, timeBase, offset);
    }
}
