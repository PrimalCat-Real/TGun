package primalcat.escapefromtempus;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.cache.object.GeoCube;
import software.bernie.geckolib.cache.object.GeoQuad;
import software.bernie.geckolib.cache.object.GeoVertex;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import software.bernie.geckolib.util.RenderUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("LanguageDetectionInspection")
public class DBLongModel extends GeoModel {

    public GeoBone rightHandBone;
    public GeoBone leftHandBone;
    public GeoBone rightHandPosBone;
    public GeoBone leftHandPosBone;

    public DBLongModel() {
        super();
        initializeHandBones();
    }
    private void initializeHandBones() {
        this.rightHandBone = getBoneByName("rightHand");
        this.leftHandBone = getBoneByName("leftHand");
        rightHandPosBone = getBoneByName("righthand_pos");
        leftHandPosBone = getBoneByName("lefthand_pos");
    }

    private GeoBone getBoneByName(String boneName) {
        return (GeoBone) this.getBone(boneName).orElse(null);
    }

    @Override
    public Identifier getModelResource(GeoAnimatable animatable) {
        return Identifier.of(TGuns.MOD_ID, "geo/item/scar_h_geo.geo.json");
    }

    @Override
    public Identifier getTextureResource(GeoAnimatable animatable) {
        return Identifier.of(TGuns.MOD_ID, "textures/gun/uv/scar_h.png");
    }


    private boolean textureSaved = false;

    @Override
    public Identifier getAnimationResource(GeoAnimatable animatable) {
        return Identifier.of(TGuns.MOD_ID, "animations/scar_h.animation.json");
    }


    public void syncHandBonesWithPlayer() {
//        MinecraftClient client = MinecraftClient.getInstance();
//        if (client != null) {
//            AbstractClientPlayerEntity player = client.player;
//            if (player != null) {
//                PlayerEntityRenderer playerRenderer = (PlayerEntityRenderer) client.getEntityRenderDispatcher().getRenderer(player);
//                PlayerEntityModel<AbstractClientPlayerEntity> playerModel = playerRenderer.getModel();
//
//                rightHandBone = getBoneByName("rightHand");
//                leftHandBone = getBoneByName("leftHand");
//
//
//                Identifier playerSkinTexture = player.getSkinTextures().texture();
//
//                GeoBone bone = null;
////                if (rightHandBone != null) {
//////                    setBoneTexture(rightHandBone, playerSkinTexture, player);
//////                    bone = playerModel.rightArm;
//////                    ModelPart rightArmPart = baseModel.rightArm;
//////
//////                    RenderUtil.matchModelPartRot(rightArmPart, playerModel.rightArm);
//////                    playerModel.rightArm.updatePosition(rightArmPart.x + 5, 2 - rightArmPart.y, rightArmPart.z);
////                    syncBoneWithModelPart(rightHandBone, playerModel.rightArm);
////                }
////
////                if (leftHandBone != null) {
//////                    setBoneTexture(leftHandBone, playerSkinTexture, player);
////                    syncBoneWithModelPart(leftHandBone, playerModel.leftArm);
////                }
//            }
//        }
    }

    @Override
    public void setCustomAnimations(GeoAnimatable animatable, long instanceId, AnimationState animationState) {
        syncHandBonesWithPlayer();
        super.setCustomAnimations(animatable, instanceId, animationState);

    }
}

