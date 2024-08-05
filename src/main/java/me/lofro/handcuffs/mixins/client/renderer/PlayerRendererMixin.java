package me.lofro.handcuffs.mixins.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import me.lofro.handcuffs.Main;
import me.lofro.handcuffs.accessors.entity.EntityRendererAccessor;
import me.lofro.handcuffs.accessors.entity.PlayerEntityAccessor;
import me.lofro.handcuffs.link.ClientLinkManager;
import me.lofro.handcuffs.link.LinkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.LightType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin extends LivingRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> {

    public PlayerRendererMixin(EntityRendererManager p_i50965_1_, PlayerModel<AbstractClientPlayerEntity> p_i50965_2_, float p_i50965_3_) {
        super(p_i50965_1_, p_i50965_2_, p_i50965_3_);
    }

    @Inject(method = "render(Lnet/minecraft/client/entity/player/AbstractClientPlayerEntity;FFLcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;I)V", at = @At("HEAD"))
    private void modifyRender(AbstractClientPlayerEntity p_225623_1_, float p_225623_2_, float p_225623_3_, MatrixStack p_225623_4_, IRenderTypeBuffer p_225623_5_, int p_225623_6_, CallbackInfo ci) {
        UUID handcuffedUUID = ClientLinkManager.linkedPlayers.get(p_225623_1_.getUniqueID());

        if (handcuffedUUID != null) {
            PlayerEntity handcuffedPlayer = p_225623_1_.world.getPlayerByUuid(handcuffedUUID);
            PlayerEntity clientPlayer = Minecraft.getInstance().player;

            if (clientPlayer != null && p_225623_1_.getUniqueID().equals(clientPlayer.getUniqueID())) return;

            if (handcuffedPlayer != null && (((PlayerEntityAccessor)handcuffedPlayer).shouldRenderRope$0() || (clientPlayer != null && (/*(clientPlayer.getUniqueID().equals(p_225623_1_.getUniqueID())) ||*/ clientPlayer.getUniqueID().equals(handcuffedUUID))))) {
                handCuffs$renderLeash(p_225623_1_, p_225623_3_, p_225623_4_, p_225623_5_, handcuffedPlayer);
            }
        }
    }

    @Unique
    private <E extends Entity> void handCuffs$renderLeash(AbstractClientPlayerEntity player, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, E leashHolder) {
        matrixStack.push();

        Vector3d leashHolderPosition = leashHolder.getLeashPosition(partialTicks);
        double bodyRotation = (double)(MathHelper.lerp(partialTicks, player.renderYawOffset, player.prevRenderYawOffset) * ((float)Math.PI / 180F)) + (Math.PI / 2D);
        Vector3d leashOffset = player.getLeashPosition(partialTicks).subtract(player.positionOffset());
        double offsetX = Math.cos(bodyRotation) * leashOffset.z + Math.sin(bodyRotation) * leashOffset.x;
        double offsetZ = Math.sin(bodyRotation) * leashOffset.z - Math.cos(bodyRotation) * leashOffset.x;
        double interpolatedPosX = MathHelper.lerp(partialTicks, player.prevPosX, player.getPosX()) + offsetX;
        double interpolatedPosY = MathHelper.lerp(partialTicks, player.prevPosY, player.getPosY()) + leashOffset.y;
        double interpolatedPosZ = MathHelper.lerp(partialTicks, player.prevPosZ, player.getPosZ()) + offsetZ;

        matrixStack.translate(offsetX, leashOffset.y, offsetZ);

        float deltaX = (float)(leashHolderPosition.x - interpolatedPosX);
        float deltaY = (float)(leashHolderPosition.y - interpolatedPosY);
        float deltaZ = (float)(leashHolderPosition.z - interpolatedPosZ);
        float leashWidth = 0.025F;

        IVertexBuilder vertexBuilder = buffer.getBuffer(RenderType.getLeash());
        Matrix4f matrix = matrixStack.getLast().getMatrix();

        float distanceSqrt = MathHelper.fastInvSqrt(deltaX * deltaX + deltaZ * deltaZ) * leashWidth / 2.0F;
        float f5 = deltaZ * distanceSqrt;
        float f6 = deltaX * distanceSqrt;

        BlockPos playerEyePos = new BlockPos(player.getEyePosition(partialTicks));
        BlockPos leashHolderEyePos = new BlockPos(leashHolder.getEyePosition(partialTicks));

        int playerBlockLight = this.getBlockLight(player, playerEyePos);
        int leashHolderBlockLight = ((EntityRendererAccessor<E>) this.renderManager.getRenderer(leashHolder)).getBlockLight$0(leashHolder, leashHolderEyePos);
        int playerSkyLight = player.world.getLightFor(LightType.SKY, playerEyePos);
        int leashHolderSkyLight = player.world.getLightFor(LightType.SKY, leashHolderEyePos);

        handCuffs$renderSide(vertexBuilder, matrix, deltaX, deltaY, deltaZ, playerBlockLight, leashHolderBlockLight, playerSkyLight, leashHolderSkyLight, leashWidth, leashWidth, f5, f6);
        handCuffs$renderSide(vertexBuilder, matrix, deltaX, deltaY, deltaZ, playerBlockLight, leashHolderBlockLight, playerSkyLight, leashHolderSkyLight, leashWidth, 0.0F, f5, f6);

        matrixStack.pop();
    }

    @Unique
    private static void handCuffs$renderSide(IVertexBuilder vertexBuilder, Matrix4f matrix, float deltaX, float deltaY, float deltaZ, int playerBlockLight, int leashHolderBlockLight, int playerSkyLight, int leashHolderSkyLight, float leashWidth1, float leashWidth2, float f5, float f6) {
        int i = 24;

        for (int j = 0; j < 24; ++j) {
            float f = (float)j / 23.0F;
            int k = (int)MathHelper.lerp(f, (float)playerBlockLight, (float)leashHolderBlockLight);
            int l = (int)MathHelper.lerp(f, (float)playerSkyLight, (float)leashHolderSkyLight);
            int i1 = LightTexture.packLight(k, l);
            handCuffs$addVertexPair(vertexBuilder, matrix, i1, deltaX, deltaY, deltaZ, leashWidth1, leashWidth2, 24, j, false, f5, f6);
            handCuffs$addVertexPair(vertexBuilder, matrix, i1, deltaX, deltaY, deltaZ, leashWidth1, leashWidth2, 24, j + 1, true, f5, f6);
        }
    }

    @Unique
    private static void handCuffs$addVertexPair(IVertexBuilder vertexBuilder, Matrix4f matrix, int light, float deltaX, float deltaY, float deltaZ, float leashWidth1, float leashWidth2, int segments, int segment, boolean swap, float f5, float f6) {
        float r = 0.5F;
        float g = 0.5F;
        float b = 0.5F;
        if (segment % 2 == 0) {
            r *= 0.7F;
            g *= 0.7F;
            b *= 0.7F;
        }

        float f3 = (float)segment / (float)segments;
        float f4 = deltaX * f3;
        float f5_adjusted = deltaY > 0.0F ? deltaY * f3 * f3 : deltaY - deltaY * (1.0F - f3) * (1.0F - f3);
        float f6_adjusted = deltaZ * f3;
        if (!swap) {
            vertexBuilder.pos(matrix, f4 + f5, f5_adjusted + leashWidth1 - leashWidth2, f6_adjusted - f6).color(r, g, b, 1.0F).lightmap(light).endVertex();
        }

        vertexBuilder.pos(matrix, f4 - f5, f5_adjusted + leashWidth2, f6_adjusted + f6).color(r, g, b, 1.0F).lightmap(light).endVertex();
        if (swap) {
            vertexBuilder.pos(matrix, f4 + f5, f5_adjusted + leashWidth1 - leashWidth2, f6_adjusted - f6).color(r, g, b, 1.0F).lightmap(light).endVertex();
        }
    }
}
