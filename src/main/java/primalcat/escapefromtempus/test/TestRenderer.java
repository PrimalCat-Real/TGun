package primalcat.escapefromtempus.test;

import software.bernie.geckolib.renderer.GeoItemRenderer;

public class TestRenderer extends GeoItemRenderer<TestItem> {
    public TestRenderer() {
        super(new TestModel());
    }
}
//