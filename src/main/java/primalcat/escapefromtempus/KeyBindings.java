package primalcat.escapefromtempus;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.util.Random;

public class KeyBindings {
    public static KeyBinding reloadKey;
    public static KeyBinding inspectKey;
    private static final Random RANDOM = new Random();
    private static final String[] ANIMATIONS = {
            "reload_empty",
            "reload_tactical",
            "draw",
            "put_away",
            "inspect_empty",
            "inspect",
//            "shoot"
    };

    public static void registerKeyBindings() {
        reloadKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.escapefromtempus.reload",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                "category.escapefromtempus"
        ));

        inspectKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.escapefromtempus.inspect",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_L,
                "category.escapefromtempus"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null && client.player.getMainHandStack().getItem() instanceof AnimatableCrossbow) {
                AnimatableCrossbow crossbow = (AnimatableCrossbow) client.player.getMainHandStack().getItem();
                if (reloadKey.wasPressed()) {
                    crossbow.triggerAnimation(client.player, client.player.getMainHandStack(), "reload_empty");
                }
                if (inspectKey.wasPressed()) {
                    int randomIndex = RANDOM.nextInt(ANIMATIONS.length);
                    String randomAnimation = ANIMATIONS[randomIndex];
                    crossbow.triggerAnimation(client.player, client.player.getMainHandStack(), randomAnimation);
                }
            }
        });
    }
}
