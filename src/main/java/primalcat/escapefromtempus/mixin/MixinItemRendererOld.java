package primalcat.escapefromtempus.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import primalcat.escapefromtempus.AnimatableCrossbow;
import primalcat.escapefromtempus.CrossbowAnimationCache;
import primalcat.escapefromtempus.DBLongModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoItemRenderer;

@Mixin(ItemRenderer.class)
public class MixinItemRendererOld {
    @Unique
    private final CrossbowAnimationCache crossbowAnimationCache = new CrossbowAnimationCache((AnimatableCrossbow) Items.CROSSBOW);

    private final GeoItemRenderer customRenderer = new GeoItemRenderer(new DBLongModel());
    @Inject(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V", at = @At("HEAD"), cancellable = true)
    public void render(ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo ci) {
//        && renderMode.isFirstPerson()
        if (stack.getItem() == Items.CROSSBOW ) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;

//        printMatrix();

            Matrix4f pose = matrices.peek().getPositionMatrix();
            Matrix3f normal = matrices.peek().getNormalMatrix();
            MatrixStack poseStack2 = new MatrixStack();
            poseStack2.peek().getNormalMatrix().mul(normal);

            matrices.scale(2.0f, 2.0f, 2.0f);

            // Примените трансляцию
            matrices.translate(-2.81438, 12.165, 12.072);

            // Примените вращение (маленький угол)
            matrices.multiply(RotationAxis.POSITIVE_Z.rotation((float) Math.toRadians(1.332E-8)));
            poseStack2.peek().getPositionMatrix().mul(pose);
//            System.out.println("Test:");
//            printMatrix(poseStack2.peek().getPositionMatrix());

            PlayerEntityRenderer playerRenderer = (PlayerEntityRenderer) MinecraftClient.getInstance().getEntityRenderDispatcher().getRenderer(player);
            playerRenderer.renderLeftArm(matrices, vertexConsumers, light, player);

//            matrices.translate(-0.2, -0.73, -0.4);
//            matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(10));

            customRenderer.render(stack, ModelTransformationMode.NONE, matrices, MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers(), 0xF000F0, 0);

            //            matrices.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(30));
//            if(renderMode.isFirstPerson()){
//                customRenderer.render(stack, ModelTransformationMode.NONE, matrices, MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers(), 0xF000F0, 0);
//            }else {
//                // Render the item using the original method if not in first person
//                MinecraftClient.getInstance().getItemRenderer().renderItem(stack, renderMode, leftHanded, matrices, vertexConsumers, light, overlay, model);
//            }

            if (player != null) {
//                System.out.println(player.getSkinTextures().texture().getPath());

//                RenderLayer.getEntitySolid(player.getSkinTextures().texture());
                // Sync and render player hands
                ((DBLongModel) customRenderer.getGeoModel()).syncHandBonesWithPlayer();
//
////                // Render player hands
//                PlayerEntityRenderer playerRenderer = (PlayerEntityRenderer) MinecraftClient.getInstance().getEntityRenderDispatcher().getRenderer(player);
                PlayerEntityModel<AbstractClientPlayerEntity> playerModel = playerRenderer.getModel();


                renderRightHand(player, matrices, vertexConsumers, light, renderMode);
                renderLeftHand(player, matrices, vertexConsumers, light, renderMode);
            }
            ci.cancel();
        }
    }


    private void renderRightHand(ClientPlayerEntity player, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, ModelTransformationMode renderMode) {
//        ((DBLongModel) customRenderer.getGeoModel()).syncHandBonesWithPlayer();
        PlayerEntityRenderer playerRenderer = (PlayerEntityRenderer) MinecraftClient.getInstance().getEntityRenderDispatcher().getRenderer(player);
        PlayerEntityModel<AbstractClientPlayerEntity> playerModel = playerRenderer.getModel();
        ModelPart rightHand = playerModel.rightArm;

        matrices.push();

        GeoBone geoRightHand = ((DBLongModel) customRenderer.getGeoModel()).rightHandBone;
//        MatrixStack test = new MatrixStack();
//        if(geoRightHand != null){
//            test.multiplyPositionMatrix(geoRightHand.getModelSpaceMatrix());
//        }
//        matrices.translate(0.6, -0.5, 1.5);
        if (geoRightHand != null) {
//            matrices.peek().getPositionMatrix().mul(geoRightHand.getLocalSpaceMatrix());
//            matrices.peek().getPositionMatrix().mul(geoRightHand.getLocalSpaceMatrix());
//            matrices.multiplyPositionMatrix(geoRightHand.getLocalSpaceMatrix());

//            matrices.translate(1.2, -0.6, 1.0);
            matrices.peek().getPositionMatrix().translate(geoRightHand.getPosX(), geoRightHand.getPosY(), geoRightHand.getPivotZ());
//            matrices.multiplyPositionMatrix(geoRightHand.getModelSpaceMatrix());
//            matrices.translate(1.2, -0.6, 1.0);
//            matrices.multiplyPositionMatrix(geoRightHand.getModelRotationMatrix());


        }
//        if(renderMode.isFirstPerson()) {
//            rightHand.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntitySolid(player.getSkinTextures().texture())), light, OverlayTexture.DEFAULT_UV);
//        }
        rightHand.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntitySolid(player.getSkinTextures().texture())), light, OverlayTexture.DEFAULT_UV);
        // Adjust the position and rotation for the right hand
//        matrices.translate(-0.3, -0.3, -0.3); // Adjust these values as needed
//        vertexConsumers.getBuffer(RenderLayer.getEntitySolid(player.getSkinTextures().texture()));
//        rightHand.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntitySolid(player.getSkinTextures().texture())), light, OverlayTexture.DEFAULT_UV);


        matrices.pop();
    }

    private void renderLeftHand(ClientPlayerEntity player, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, ModelTransformationMode renderMode) {
        PlayerEntityRenderer playerRenderer = (PlayerEntityRenderer) MinecraftClient.getInstance().getEntityRenderDispatcher().getRenderer(player);
        GeoBone geoLeftHand = ((DBLongModel) customRenderer.getGeoModel()).leftHandBone;

        matrices.push();

        if (geoLeftHand != null) {
//            matrices.translate(0, -0.3, 0.8);
//            syncMatrixWithBone(matrices, geoLeftHand);
//            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));
//
//            // Примените масштабирование
//            matrices.scale(-0.8244803f, -0.34062225f, -0.44440603f);
            matrices.multiplyPositionMatrix(geoLeftHand.getModelSpaceMatrix());
//            System.out.println("Position matrix before rendering left hand:");
//            printMatrix(geoLeftHand.getModelSpaceMatrix());

//            matrices.peek().getPositionMatrix().rotate(RotationAxis.NEGATIVE_X.rotation(180));
//            matrices.peek().getPositionMatrix().rotate(RotationAxis.NEGATIVE_Y.rotation(180));
        }

//        Identifier identifier = player.getSkinTextures().texture();
//        playerRenderer.getModel().leftArm.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntitySolid(identifier)), light, OverlayTexture.DEFAULT_UV);
//        playerRenderer.renderLeftArm(matrices, vertexConsumers, light, player);
        matrices.pop();
    }
    private static void printMatrix(Matrix4f matrix) {
        for (int i = 0; i < 4; i++) {
            System.out.println(matrix.m00() + " " + matrix.m01() + " " + matrix.m02() + " " + matrix.m03());
        }
    }

    private void syncMatrixWithBone(MatrixStack matrices, GeoBone bone) {
        matrices.multiplyPositionMatrix(bone.getModelSpaceMatrix());
//        matrices.translate(bone.getPosX() / 16.0F, bone.getPosY() / 16.0F, bone.getPosZ() / 16.0F);
//
//        if (bone.getRotX() != 0.0F || bone.getRotY() != 0.0F || bone.getRotZ() != 0.0F) {
//            matrices.multiply(new Quaternionf().rotationXYZ(bone.getRotX(), bone.getRotY(), bone.getRotZ()));
//        }
//
//        if (bone.getScaleX() != 1.0F || bone.getScaleY() != 1.0F || bone.getScaleZ() != 1.0F) {
//            matrices.scale(bone.getScaleX(), bone.getScaleY(), bone.getScaleZ());
//        }
    }

//    private void renderLeftHand(ClientPlayerEntity player, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, ModelTransformationMode renderMode) {
//        PlayerEntityRenderer playerRenderer = (PlayerEntityRenderer) MinecraftClient.getInstance().getEntityRenderDispatcher().getRenderer(player);
//        PlayerEntityModel<AbstractClientPlayerEntity> playerModel = playerRenderer.getModel();
//        ModelPart leftHand = playerModel.leftArm;
//
//        matrices.push();
//        GeoBone geoLeftHand = ((DBLongModel) customRenderer.getGeoModel()).leftHandBone;
//
//        if (geoLeftHand != null) {
////            syncBoneWithModelPart(geoLeftHand, leftHand);
////            matrices.multiply(Quaternionf.,0, 0, 0.65);
//
//            MinecraftClient mc = MinecraftClient.getInstance();
//            EntityRenderDispatcher renderManager = mc.getEntityRenderDispatcher();
////            MultiBufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
//            PlayerEntityRenderer renderer = (PlayerEntityRenderer) renderManager.getRenderer(player);
//            matrices.multiplyPositionMatrix(geoLeftHand.getModelSpaceMatrix());
////            syncMatrixWithBone(matrices, geoLeftHand);
//            renderer.renderLeftArm(matrices, vertexConsumers, light, player);
//
//
////            leftHand.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntitySolid(player.getSkinTextures().texture())), light, OverlayTexture.DEFAULT_UV);
//
//        }
//
//        matrices.pop();
//    }

    private void syncBoneWithModelPart(GeoBone bone, ModelPart modelPart) {
        modelPart.resetTransform();
//        RenderUtil.matchModelPartRot(modelPart, bone);
//        final GeoCube firstCube = bone.getCubes().getFirst();
//        final ModelPart.Cuboid armorCube = modelPart.cuboids.getFirst();
        modelPart.xScale = bone.getScaleX();
        modelPart.yScale = bone.getScaleY();
        modelPart.zScale = bone.getScaleZ();
        modelPart.pivotX = bone.getPivotX();
        modelPart.pivotY = bone.getPivotY();
        modelPart.pivotZ = bone.getPivotZ();
        modelPart.pitch = bone.getRotX();
        modelPart.yaw = bone.getRotY();
        modelPart.roll = bone.getRotZ();
        modelPart.xScale = bone.getScaleX();
        modelPart.yScale = bone.getScaleY();
        modelPart.zScale = bone.getScaleZ();
//        modelPart.xRot = 0.0f;
//        modelPart.yRot = 0.0f;
//        modelPart.zRot = 0.0f;
//        bone.setHidden(true);
    }

    @Unique
    private void checkSynchronization(GeoBone bone, ModelPart modelPart) {
        boolean synch = true;
        StringBuilder mismatch = new StringBuilder("Desynchronization detected in: ");

        if (modelPart.pivotX != bone.getPivotX()) {
            synch = false;
            mismatch.append("pivotX ");
        }
        if (modelPart.pivotY != bone.getPivotY()) {
            synch = false;
            mismatch.append("pivotY ");
        }
        if (modelPart.pivotZ != bone.getPivotZ()) {
            synch = false;
            mismatch.append("pivotZ ");
        }
        if (modelPart.pitch != bone.getRotX()) {
            synch = false;
            mismatch.append("pitch ");
        }
        if (modelPart.yaw != bone.getRotY()) {
            synch = false;
            mismatch.append("yaw ");
        }
        if (modelPart.roll != bone.getRotZ()) {
            synch = false;
            mismatch.append("roll ");
        }
        if (modelPart.xScale != bone.getScaleX()) {
            synch = false;
            mismatch.append("xScale ");
        }
        if (modelPart.yScale != bone.getScaleY()) {
            synch = false;
            mismatch.append("yScale ");
        }
        if (modelPart.zScale != bone.getScaleZ()) {
            synch = false;
            mismatch.append("zScale ");
        }

        if (!synch) {
            System.out.println(mismatch.toString());
        } else {
            System.out.println("Synchronization successful.");
        }
    }

}
