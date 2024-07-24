package primalcat.escapefromtempus.test;

import net.minecraft.util.Identifier;
import primalcat.escapefromtempus.TGuns;
import software.bernie.geckolib.model.GeoModel;

public class TestModel extends GeoModel<TestItem> {

    @Override
    public Identifier getModelResource(TestItem animatable) {
        return Identifier.of(TGuns.MOD_ID, "geo/item/db_long.geo.json");
    }

    @Override
    public Identifier getTextureResource(TestItem animatable) {
        return Identifier.of(TGuns.MOD_ID, "textures/gun/uv/db_long.png");
    }

    @Override
    public Identifier getAnimationResource(TestItem animatable) {
        return Identifier.of(TGuns.MOD_ID, "animations/db_long.animation.json");
    }
}
