package dev.snowclient;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class SnowClient implements ClientModInitializer {
    private static final int ICE = 0xFF8BE9FF;
    private static final int WHITE = 0xFFF4FAFF;
    private static final int PANEL = 0xB9162332;

    private static boolean espEnabled = true;
    private static boolean hudEnabled = true;
    private static KeyBinding toggleEsp;
    private static KeyBinding toggleHud;
    private static final Set<UUID> highlightedByUs = new HashSet<>();

    @Override
    public void onInitializeClient() {
        toggleEsp = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.snowclient.toggle_esp", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_O,
                KeyBinding.Category.MISC));
        toggleHud = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.snowclient.toggle_hud", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_P,
                KeyBinding.Category.MISC));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleEsp.wasPressed()) espEnabled = !espEnabled;
            while (toggleHud.wasPressed()) hudEnabled = !hudEnabled;
            updatePlayerHighlights(client);
        });

        HudElementRegistry.addLast(Identifier.of("snowclient", "main_hud"),
                (context, tickCounter) -> renderHud(context));
    }

    private static void updatePlayerHighlights(MinecraftClient client) {
        if (client.world == null || client.player == null) return;

        for (UUID id : Set.copyOf(highlightedByUs)) {
            PlayerEntity old = client.world.getPlayerByUuid(id);
            if (old != null) old.setGlowing(false);
        }
        highlightedByUs.clear();

        if (!espEnabled) return;
        for (PlayerEntity target : client.world.getPlayers()) {
            if (target == client.player || target.isRemoved() || target.isSpectator()) continue;
            target.setGlowing(true);
            highlightedByUs.add(target.getUuid());
        }
    }

    private static void renderHud(net.minecraft.client.gui.DrawContext context) {
        if (!hudEnabled) return;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.world == null) return;

        int x = 8;
        int y = 8;
        int width = 154;
        int height = 62;
        context.fill(x, y, x + width, y + height, PANEL);
        context.fill(x, y, x + 2, y + height, ICE);
        context.drawTextWithShadow(client.textRenderer, Text.literal("SNOWCLIENT"),
                x + 9, y + 6, ICE);
        context.drawTextWithShadow(client.textRenderer,
                Text.literal("FPS  " + client.getCurrentFps()), x + 9, y + 22, WHITE);
        context.drawTextWithShadow(client.textRenderer,
                Text.literal("XYZ  " + (int) client.player.getX() + " "
                        + (int) client.player.getY() + " " + (int) client.player.getZ()),
                x + 9, y + 36, WHITE);
        context.drawTextWithShadow(client.textRenderer,
                Text.literal("ESP  " + (espEnabled ? "ON" : "OFF")),
                x + 9, y + 49, espEnabled ? ICE : 0xFFFF7777);
    }
}
