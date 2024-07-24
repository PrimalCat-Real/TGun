package primalcat.escapefromtempus.test;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import primalcat.escapefromtempus.DBLongModel;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;
import software.bernie.geckolib.util.RenderUtil;

import java.util.function.Consumer;

public class TestItem extends Item implements GeoItem {
    public static final String CONTROLLER_NAME = "controller";
    public static final String TRIGGER_ANIMATION = "reload_tactical";
    private static final RawAnimation RELOAD_ANIM = RawAnimation.begin().thenPlay(TRIGGER_ANIMATION);
    public final AnimatableInstanceCache instanceCache = GeckoLibUtil.createInstanceCache(this);

//    private final Supplier<Object> renderProvider = GeoItem.makeRenderer(this);

    public TestItem(Settings settings) {
        super(settings);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }


    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        // Accepts a consumer to create a new renderer

        consumer.accept(new GeoRenderProvider() {
            GeoItemRenderer<TestItem> renderer = null;

            @Override
            public @Nullable BuiltinModelItemRenderer getGeoItemRenderer() {
                if (this.renderer == null)
                    this.renderer = new TestRenderer();
//                ((DBLongModel) customRenderer.getGeoModel()).syncHandBonesWithPlayer();


                return this.renderer;
            }
        });
    }

    @Override
    public double getTick(Object itemStack) {
        return RenderUtil.getCurrentTick();
//        return GeoItem.super.getTick(itemStack);
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {
        tAnimationState.getController().setAnimation(RawAnimation.begin().then("static_idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

//    private PlayState predicate(AnimationState<TestItem> state) {
//
//        return PlayState.STOP;
//    }
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, CONTROLLER_NAME, 0, this::predicate)
                .triggerableAnim(TRIGGER_ANIMATION, RELOAD_ANIM));
    }


    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world instanceof ServerWorld serverLevel)
            triggerAnim(user, GeoItem.getOrAssignId(user.getMainHandStack(), serverLevel), CONTROLLER_NAME, TRIGGER_ANIMATION);

        return super.use(world, user, hand);
    }

    public void triggerCustomAnimation(PlayerEntity player, ItemStack stack) {

        triggerAnim(player, GeoItem.getId(stack), CONTROLLER_NAME, TRIGGER_ANIMATION);

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return instanceCache;
    }
}

