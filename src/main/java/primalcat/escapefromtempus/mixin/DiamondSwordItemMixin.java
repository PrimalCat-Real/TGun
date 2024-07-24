package primalcat.escapefromtempus.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import primalcat.escapefromtempus.AnimatableCrossbow;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;
import software.bernie.geckolib.util.RenderUtil;

@Mixin(SwordItem.class)
public class DiamondSwordItemMixin extends Item implements GeoAnimatable, AnimatableCrossbow, GeoItem {
    private static String CONTROLLER_NAME = "controller";
    private static final String RELOAD_ANIMATION = "reload_empty";
    private static final String IDLE_ANIMATION = "static_idle";
    private static final String INSPECT_ANIMATION = "reload_tactical";
    private static final RawAnimation RELOAD_ANIM = RawAnimation.begin().thenPlay(RELOAD_ANIMATION);
    private static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenPlay(IDLE_ANIMATION);
    private static final RawAnimation INSPECT_ANIM = RawAnimation.begin().thenPlay(INSPECT_ANIMATION);
    public DiamondSwordItemMixin(Settings settings) {
        super(settings);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }


    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return animatableInstanceCache;
    }

    @Override
    public void triggerAnimation(PlayerEntity player, ItemStack stack, String animationName) {

    }

    @Unique
    private final AnimatableInstanceCache animatableInstanceCache = GeckoLibUtil.createInstanceCache(this);
//    private AnimationController<GeoAnimatable> controller = new AnimationController<>(this, CONTROLLER_NAME, 0, this::predicate);
//    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, CONTROLLER_NAME, 0, this::predicate)
                .triggerableAnim(RELOAD_ANIMATION, RELOAD_ANIM));
//        controllers.add(new AnimationController<>(this, CONTROLLER_NAME, 0, this::predicate)
//                .triggerableAnim(INSPECT_ANIMATION, INSPECT_ANIM));
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> state) {
        state.getController().setAnimation(RawAnimation.begin().then("static_idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }
//    @Override
//    public void tguns_1_21$triggerReloadAnimation(PlayerEntity player, ItemStack stack) {
//        System.out.println("Trigger anim");
//        triggerAnim(player, GeoItem.getId(stack), CONTROLLER_NAME, RELOAD_ANIMATION);
//    }
//    @Override
//    public void tguns_1_21$triggerInspectAnimation(PlayerEntity player, ItemStack stack) {
//        System.out.println("Trigger anim");
//        triggerAnim(player, GeoItem.getId(stack), CONTROLLER_NAME, RELOAD_ANIMATION);
//    }




    @Override
    public double getTick(Object object) {
        return RenderUtil.getCurrentTick();
    }
}
