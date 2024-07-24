package primalcat.escapefromtempus;

import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.InstancedAnimatableInstanceCache;

public class CrossbowAnimationCache {
    private final AnimatableInstanceCache cache;

    public CrossbowAnimationCache(AnimatableCrossbow crossbow) {
        this.cache = new InstancedAnimatableInstanceCache(crossbow);
    }

    public AnimatableInstanceCache getCache() {
        return this.cache;
    }
}
