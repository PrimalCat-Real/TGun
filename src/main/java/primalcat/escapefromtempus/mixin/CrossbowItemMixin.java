package primalcat.escapefromtempus.mixin;

import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import primalcat.escapefromtempus.*;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;
import software.bernie.geckolib.util.RenderUtil;

import java.util.function.Consumer;

@Mixin(CrossbowItem.class)
public class CrossbowItemMixin extends Item implements GeoAnimatable, AnimatableCrossbow, GeoItem {
    private static final String CONTROLLER_NAME = "controller";
    private static final String RELOAD_ANIMATION = "reload_empty";
    private static final String IDLE_ANIMATION = "static_idle";
    private static final String INSPECT_ANIMATION = "inspect";
    private static final String DRAW_ANIMATION = "draw";
    private static final String PUT_AWAY_ANIMATION = "put_away";
    private static final String INSPECT_EMPTY_ANIMATION = "inspect_empty";
    private static final String RELOAD_TACTICAL_ANIMATION = "reload_tactical";
    private static final String SHOOT_ANIMATION = "shoot";

    @Unique
    private final AnimatableInstanceCache animatableInstanceCache = GeckoLibUtil.createInstanceCache(this);

    public CrossbowItemMixin(Settings settings) {
        super(settings);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return animatableInstanceCache;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, CONTROLLER_NAME, 0, this::predicate)
                .triggerableAnim(RELOAD_ANIMATION, RawAnimation.begin().thenPlay(RELOAD_ANIMATION).thenLoop(IDLE_ANIMATION))
                .triggerableAnim(IDLE_ANIMATION, RawAnimation.begin().thenPlay(IDLE_ANIMATION).thenLoop(IDLE_ANIMATION))
                .triggerableAnim(INSPECT_ANIMATION, RawAnimation.begin().thenPlay(INSPECT_ANIMATION).thenLoop(IDLE_ANIMATION))
                .triggerableAnim(DRAW_ANIMATION, RawAnimation.begin().thenPlay(DRAW_ANIMATION).thenLoop(IDLE_ANIMATION))
                .triggerableAnim(PUT_AWAY_ANIMATION, RawAnimation.begin().thenPlay(PUT_AWAY_ANIMATION).thenLoop(IDLE_ANIMATION))
                .triggerableAnim(INSPECT_EMPTY_ANIMATION, RawAnimation.begin().thenPlay(INSPECT_EMPTY_ANIMATION).thenLoop(IDLE_ANIMATION))
                .triggerableAnim(SHOOT_ANIMATION, RawAnimation.begin().thenPlay(SHOOT_ANIMATION).thenLoop(IDLE_ANIMATION))
                .triggerableAnim(RELOAD_TACTICAL_ANIMATION, RawAnimation.begin().thenPlay(RELOAD_TACTICAL_ANIMATION).thenLoop(IDLE_ANIMATION)));
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> state) {
        if (state.getController().getCurrentAnimation() == null) {
            state.getController().setAnimation(RawAnimation.begin().thenPlay(IDLE_ANIMATION).thenLoop(IDLE_ANIMATION));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void triggerAnimation(PlayerEntity player, ItemStack stack, String animationName) {
        triggerAnim(player, GeoItem.getId(stack), CONTROLLER_NAME, animationName);
    }

    @Override
    public double getTick(Object object) {
        return RenderUtil.getCurrentTick();
    }
}
