/*
 * Copyright 2022 FrozenBlock
 * This file is part of FrozenLib.
 *
 * FrozenLib is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * FrozenLib is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with FrozenLib. If not, see <https://www.gnu.org/licenses/>.
 */

package net.frozenblock.lib.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import net.frozenblock.lib.entity.render.FrozenRenderType;
import net.frozenblock.lib.registry.FrozenRegistry;
import net.frozenblock.lib.spotting_icons.SpottingIconManager;
import net.frozenblock.lib.spotting_icons.impl.EntitySpottingIconInterface;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> {

    @Shadow
	protected M model;

	protected LivingEntityRendererMixin(EntityRendererProvider.Context context) {
		super(context);
	}

	@Inject(method = "render", at = @At(value = "TAIL"))
	public void render(T entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight, CallbackInfo info) {
		this.renderSpottingIcon(entity, matrixStack, buffer, packedLight);
	}

	@Inject(method = "getRenderType", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/EntityModel;renderType(Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/client/renderer/RenderType;"), cancellable = true)
    private void getEasterEgg(T livingEntity, boolean bodyVisible, boolean translucent, boolean glowing, CallbackInfoReturnable<RenderType> cir) {
        FrozenRegistry.ENTITY_TEXTURE_OVERRIDE.forEach(override -> {
            if (override.getType() == livingEntity.getType()) {
                var texture = override.getTexture();
                if (texture != null) {
                    if (override.getCondition().condition(livingEntity)) {
                        cir.setReturnValue(this.model.renderType(texture));
                    }
                }
            }
        });
    }

    @Inject(method = "getRenderType", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderType;itemEntityTranslucentCull(Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/client/renderer/RenderType;"), cancellable = true)
    private void getItemEasterEgg(T livingEntity, boolean bodyVisible, boolean translucent, boolean glowing, CallbackInfoReturnable<RenderType> cir) {
        FrozenRegistry.ENTITY_TEXTURE_OVERRIDE.forEach(override -> {
            if (override.getType() == livingEntity.getType()) {
                var texture = override.getTexture();
                if (texture != null) {
                    if (override.getCondition().condition(livingEntity)) {
                        cir.setReturnValue(RenderType.itemEntityTranslucentCull(texture));
                    }
                }
            }
        });
    }

    @Inject(method = "getRenderType", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderType;outline(Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/client/renderer/RenderType;", shift = At.Shift.BEFORE), cancellable = true)
    private void getOutlineEasterEgg(T livingEntity, boolean bodyVisible, boolean translucent, boolean glowing, CallbackInfoReturnable<RenderType> cir) {
        if (glowing) {
            FrozenRegistry.ENTITY_TEXTURE_OVERRIDE.forEach(override -> {
                if (override.getType() == livingEntity.getType()) {
                    var texture = override.getTexture();
                    if (texture != null) {
                        if (override.getCondition().condition(livingEntity)) {
                            cir.setReturnValue(RenderType.outline(texture));
                        }
                    }
                }
            });
        }
    }

	//TODO: Render above blocks
	@Unique
	public void renderSpottingIcon(T entity, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
		if (entity instanceof EntitySpottingIconInterface iconInterface) {
			SpottingIconManager.SpottingIcon icon = iconInterface.getSpottingIconManager().icon;
			if (icon != null) {
				double dist = Mth.sqrt((float) this.entityRenderDispatcher.distanceToSqr(entity));
				if (dist > icon.startFadeDist) {
					float endDist = icon.endFadeDist - icon.startFadeDist;
					dist -= icon.startFadeDist;
					float alpha = dist > endDist ? 1F : (float) Math.min(1F, dist / endDist);
					float f = entity.getBbHeight() + 1F;
					matrixStack.pushPose();
					matrixStack.translate(0.0D, f, 0.0D);
					matrixStack.translate(this.entityRenderDispatcher.camera.getEntity().getX() - (entity.getX() * 0.7F), 0, this.entityRenderDispatcher.camera.getEntity().getZ() -entity.getZ() * 0.7F);
					matrixStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
					matrixStack.scale(-1, 1, 1);
					Matrix4f matrix4f = matrixStack.last().pose();
					Matrix3f matrix3f = matrixStack.last().normal();
					int overlay = OverlayTexture.pack(OverlayTexture.u(0F), OverlayTexture.v(false));
					VertexConsumer vertexConsumer = buffer.getBuffer(FrozenRenderType.entityTranslucentEmissiveFixedNoOutline(iconInterface.getSpottingIconManager().icon.getTexture()));
					vertexConsumer
							.vertex(matrix4f, -0.5F, -0.5F, 0.0F)
							.color(1, 1, 1, alpha)
							.uv(0, 1)
							.overlayCoords(overlay)
							.uv2(packedLight)
							.normal(matrix3f, 0.0F, 1.0F, 0.0F)
							.endVertex();
					vertexConsumer
							.vertex(matrix4f, 0.5F, -0.5F, 0.0F)
							.color(1, 1, 1, alpha)
							.uv(1, 1)
							.overlayCoords(overlay)
							.uv2(packedLight)
							.normal(matrix3f, 0.0F, 1.0F, 0.0F)
							.endVertex();
					vertexConsumer
							.vertex(matrix4f, 0.5F, 0.5F, 0.0F)
							.color(1, 1, 1, alpha)
							.uv(1, 0)
							.overlayCoords(overlay)
							.uv2(packedLight)
							.normal(matrix3f, 0.0F, 1.0F, 0.0F)
							.endVertex();
					vertexConsumer
							.vertex(matrix4f, -0.5F, 0.5F, 0.0F)
							.color(1, 1, 1, alpha)
							.uv(0, 0)
							.overlayCoords(overlay)
							.uv2(packedLight)
							.normal(matrix3f, 0.0F, 1.0F, 0.0F)
							.endVertex();

					matrixStack.popPose();
				}
			}
		}
	}
}
