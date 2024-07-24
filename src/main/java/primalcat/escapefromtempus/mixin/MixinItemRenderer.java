package primalcat.escapefromtempus.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.RotationAxis;
import org.joml.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import primalcat.escapefromtempus.*;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.cache.object.GeoCube;
import software.bernie.geckolib.renderer.GeoItemRenderer;

import java.util.Iterator;

@Mixin(ItemRenderer.class)
public class MixinItemRenderer {
    @Unique
    private final CrossbowAnimationCache crossbowAnimationCache = new CrossbowAnimationCache((AnimatableCrossbow) Items.CROSSBOW);

    private final GeoItemRenderer customRenderer = new GeoItemRenderer(new DBLongModel());

    @Inject(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V", at = @At("HEAD"), cancellable = true)
    public void render(ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo ci) {

        if (stack.getItem() == Items.CROSSBOW && renderMode.isFirstPerson() && renderMode != ModelTransformationMode.GUI) {
            matrices.push();
            // base translate
            matrices.translate(-0.5, -0.5, -0.5);
            // make it on screen center
            matrices.translate(0.3, -0.3, 0);
            matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(10));
            // crossbow offset
//            matrices.translate(0.3, -0.3, 0);
            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            customRenderer.render(stack, ModelTransformationMode.NONE, matrices, MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers(), light, overlay);
            matrices.pop();

            ci.cancel();
        }
    }


}
