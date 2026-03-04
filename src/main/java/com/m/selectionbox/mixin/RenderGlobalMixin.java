package com.m.selectionbox.mixin;

import com.m.selectionbox.render.SelectionBoxStyle;
import net.minecraft.AxisAlignedBB;
import net.minecraft.Block;
import net.minecraft.EntityPlayer;
import net.minecraft.RaycastCollision;
import net.minecraft.RenderGlobal;
import net.minecraft.Tessellator;
import net.minecraft.WorldClient;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Replaces selected block outline rendering with a configurable gradient.
 */
@Mixin(RenderGlobal.class)
public class RenderGlobalMixin {

    @Shadow
    private WorldClient theWorld;

    @Inject(method = "drawSelectionBox", at = @At("HEAD"), cancellable = true)
    private void renderSelectionBox(EntityPlayer player, RaycastCollision rc, int pass, float partialTicks, CallbackInfo ci) {
        // Fully take over selection box rendering to avoid method overwrite conflicts.
        ci.cancel();

        if (!SelectionBoxStyle.renderBlockOutline() || pass != 0 || !rc.isBlock()) {
            return;
        }

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glLineWidth(SelectionBoxStyle.getLineWidth());
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDepthMask(false);

        float expand = 0.002F;
        Block block = rc.getBlockHit();
        double px = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
        double py = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
        double pz = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;

        AxisAlignedBB box = block
                .getSelectedBoundingBoxFromPool(this.theWorld, rc.block_hit_x, rc.block_hit_y, rc.block_hit_z)
                .expand(expand, expand, expand)
                .getOffsetBoundingBox(-px, -py, -pz);
        drawGradientOutlinedBoundingBox(box);

        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    private static void drawGradientOutlinedBoundingBox(AxisAlignedBB box) {
        Tessellator var2 = Tessellator.instance;
        float time = SelectionBoxStyle.getAnimationProgress();

        var2.startDrawing(3);
        addColorVertex(var2, box.minX, box.minY, box.minZ, time, 0.0F);
        addColorVertex(var2, box.maxX, box.minY, box.minZ, time, 0.25F);
        addColorVertex(var2, box.maxX, box.minY, box.maxZ, time, 0.5F);
        addColorVertex(var2, box.minX, box.minY, box.maxZ, time, 0.75F);
        addColorVertex(var2, box.minX, box.minY, box.minZ, time, 1.0F);
        var2.draw();

        var2.startDrawing(3);
        addColorVertex(var2, box.minX, box.maxY, box.minZ, time, 0.125F);
        addColorVertex(var2, box.maxX, box.maxY, box.minZ, time, 0.375F);
        addColorVertex(var2, box.maxX, box.maxY, box.maxZ, time, 0.625F);
        addColorVertex(var2, box.minX, box.maxY, box.maxZ, time, 0.875F);
        addColorVertex(var2, box.minX, box.maxY, box.minZ, time, 0.125F);
        var2.draw();

        var2.startDrawing(1);
        addColorVertex(var2, box.minX, box.minY, box.minZ, time, 0.0F);
        addColorVertex(var2, box.minX, box.maxY, box.minZ, time, 0.0F);
        addColorVertex(var2, box.maxX, box.minY, box.minZ, time, 0.25F);
        addColorVertex(var2, box.maxX, box.maxY, box.minZ, time, 0.25F);
        addColorVertex(var2, box.maxX, box.minY, box.maxZ, time, 0.5F);
        addColorVertex(var2, box.maxX, box.maxY, box.maxZ, time, 0.5F);
        addColorVertex(var2, box.minX, box.minY, box.maxZ, time, 0.75F);
        addColorVertex(var2, box.minX, box.maxY, box.maxZ, time, 0.75F);
        var2.draw();
    }

    private static void addColorVertex(Tessellator tessellator, double x, double y, double z, float timeBase, float offset) {
        SelectionBoxStyle.addGradientVertex(tessellator, x, y, z, timeBase, offset);
    }
}
