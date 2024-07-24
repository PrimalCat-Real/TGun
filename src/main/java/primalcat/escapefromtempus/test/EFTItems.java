package primalcat.escapefromtempus.test;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import primalcat.escapefromtempus.TGuns;

public class EFTItems {
    public static final Item testItem = registerItem("db_long",
            new TestItem(new Item.Settings()));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(TGuns.MOD_ID, name), item);
    }

    public static void registerModItems() {
    }
}