package primalcat.escapefromtempus;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;

public interface AnimatableCrossbow extends GeoAnimatable {
    AnimatableInstanceCache getAnimatableInstanceCache();
    void triggerAnimation(PlayerEntity player, ItemStack stack, String animationName);
}
